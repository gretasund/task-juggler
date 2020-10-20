package de.hsba.bi.projectwork.task;

import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.project.ProjectService;
import de.hsba.bi.projectwork.user.User;
import de.hsba.bi.projectwork.user.UserService;
import de.hsba.bi.projectwork.web.task.SuggestedTaskForm;
import de.hsba.bi.projectwork.web.task.SuggestedTaskFormConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Transactional
public class SuggestedTaskService {

    private final SuggestedTaskRepository suggestedTaskRepository;

    private final UserService userService;
    private final TaskService taskService;
    private final ProjectService projectService;
    private final SuggestedTaskFormConverter suggestedTaskFormConverter;


    // find suggestedTask
    public SuggestedTask findById(long suggestedTaskId) {
        Optional<SuggestedTask> suggestedTask = suggestedTaskRepository.findById(suggestedTaskId);
        return suggestedTask.orElse(null);
    }

    public List<SuggestedTask> findByStatus(Enum<SuggestedTask.Status> status) {
        return suggestedTaskRepository.findSuggestedTaskByStatus(status);
    }


    // find suggestedTask by user
    public List<SuggestedTask> findSuggestedTaskByUser(List<SuggestedTask> allSuggestedTasks) {
        // load current user
        User user = userService.findCurrentUser();

        // get all projects and users
        List<SuggestedTask> usersSuggestedTasks = new ArrayList<>();

        // find projects belonging to the logged in user
        for (SuggestedTask suggestedTask : allSuggestedTasks) {
            if (suggestedTask.getProject().getMembers().contains(user)) {
                usersSuggestedTasks.add(suggestedTask);
            }
        }
        return usersSuggestedTasks;
    }

    public List<SuggestedTask> findSuggestedTaskByCreator(User creator) {
        return suggestedTaskRepository.findSuggestedTaskByCreator(creator);
    }


    // add suggestedTask
    public SuggestedTask save(SuggestedTask suggestedTask) {
        return suggestedTaskRepository.save(suggestedTask);
    }

    public SuggestedTask createNewSuggestedTask(SuggestedTask suggestedTask) {
        // load entities
        Project project = suggestedTask.getProject();
        User creator = suggestedTask.getCreator();

        // link project and user in suggestedTask
        suggestedTask.setProject(project);
        suggestedTask.setCreator(creator);

        // link suggestedTask in project
        List<SuggestedTask> suggestedTasksProject = project.getSuggestedTasks();
        suggestedTasksProject.add(suggestedTask);

        // link suggestedTask in user/creator
        List<BaseTask> suggestedTasksUser = creator.getCreatedTasks();
        suggestedTasksUser.add(suggestedTask);

        // persist entities
        suggestedTaskRepository.save(suggestedTask);
        projectService.save(project);
        //userService.save(creator);

        return suggestedTask;

    }

    public SuggestedTask createNewSuggestedTask(SuggestedTaskForm suggestedTaskForm) {
        // TODO Als Entwickler in einem Projekt kann ich eine Aufgabe zu diesem Projekt hinzufügen, diese beinhaltet wenigstens einen Titel und eine Beschreibung
        SuggestedTask suggestedTask = suggestedTaskFormConverter.convert(suggestedTaskForm);
        return this.save(suggestedTask);
    }

    public void evaluateSuggestedTask(long taskId, String evaluation) {
        SuggestedTask suggestedTask = this.findById(taskId);
        switch (evaluation) {
            case "accept":
                suggestedTask.setStatus(SuggestedTask.Status.ACCEPTED);
                taskService.save(new Task(suggestedTask));
            case "decline":
                suggestedTask.setStatus(SuggestedTask.Status.DECLINED);
                this.save(suggestedTask);
            default:
                // TODO
        }
    }

}
