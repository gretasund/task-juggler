package de.hsba.bi.projectwork.task.acceptedtask;

import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.project.ProjectRepository;
import de.hsba.bi.projectwork.project.ProjectService;
import de.hsba.bi.projectwork.user.User;
import de.hsba.bi.projectwork.user.UserService;
import de.hsba.bi.projectwork.web.task.acceptedtask.AcceptedTaskForm;
import de.hsba.bi.projectwork.web.task.acceptedtask.AcceptedTaskFormConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class AcceptedTaskService {

    private final AcceptedTaskRepository acceptedTaskRepository;
    private final UserService userService;
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;
    private final AcceptedTaskFormConverter acceptedTaskFormConverter;


    // find tasks
    public List<AcceptedTask> findAll() {
        // load tasks
        List<AcceptedTask> acceptedTasks = acceptedTaskRepository.findAll();

        // calc daysLeft
        for (AcceptedTask acceptedTask : acceptedTasks) {
            acceptedTask.calcDaysLeft();
        }

        return acceptedTasks;
    }

    public AcceptedTask findById(Long id) {
        Optional<AcceptedTask> optionalTask = acceptedTaskRepository.findById(id);

        if (optionalTask.isPresent()) {
            AcceptedTask acceptedTask = optionalTask.get();
            acceptedTaskRepository.save(acceptedTask);
            return acceptedTask;
        }
        return null;
    }

    public List<AcceptedTask> findByStatus(Enum<AcceptedTask.Status> status) {
        if(status != null) {
            return acceptedTaskRepository.findTaskByStatus(status);
        }
        return this.findAll();
    }

    public List<AcceptedTask> findUnassignedAndUnscheduled() {
        List<AcceptedTask> usersAcceptedTasks = this.findAll();
        List<AcceptedTask> unassignedUnscheduledAcceptedTasks = new ArrayList<>();

        for (AcceptedTask acceptedTask : usersAcceptedTasks){
            if ((acceptedTask.getDueDate()==null || acceptedTask.getAssignee()==null) && acceptedTask.getStatus() != AcceptedTask.Status.DONE)
                unassignedUnscheduledAcceptedTasks.add(acceptedTask);
        }

        return unassignedUnscheduledAcceptedTasks;
    }

    public List<AcceptedTask> findOpenTasks(List<AcceptedTask> acceptedTasks) {
        // remove tasks with status done
        acceptedTasks.removeIf(task -> (task.getStatus().equals(AcceptedTask.Status.DONE) || task.getDueDate() == null));
        return acceptedTasks;
    }


    // find task by user
    public List<AcceptedTask> findUsersTasks(List<AcceptedTask> acceptedTasks) {
        // load entities
        User currentUser = userService.findCurrentUser();

        // iterate over tasks
        acceptedTasks.removeIf(task -> !task.getProject().getMembers().contains(currentUser));

        // sort tasks
        Collections.sort(acceptedTasks);

        return acceptedTasks;
    }

    public List<AcceptedTask> findTaskByAssignee(User assignee) {
        return acceptedTaskRepository.findTaskByAssignee(assignee);
    }


    // add task methods
    public AcceptedTask save(AcceptedTask acceptedTask) {
        return acceptedTaskRepository.save(acceptedTask);
    }

    public AcceptedTask addTask(AcceptedTask acceptedTask, Long projectId) {
        // TODO Als Entwickler in einem Projekt kann ich eine Aufgabe zu diesem Projekt hinzufügen, diese beinhaltet wenigstens einen Titel und eine Beschreibung
        // load entities
        Project project = projectService.findById(projectId);

        if (project != null) {
            // link task in project
            project.getAcceptedTasks().add(acceptedTask);

            // persist entities
            acceptedTaskRepository.save(acceptedTask);
            projectRepository.save(project);

        }

        return acceptedTask;
    }


    // edit task methods
    public void editTask(AcceptedTaskForm acceptedTaskForm) {
        // TODO Als Entwickler in einem Projekt kann ich eine Zeitschätzung (grob in Stunden) in einer Aufgabe speichern (diese Schätzung soll eine Eigenschaft der Aufgabe sein - verschiedene Entwickler würden diese Schätzung sehen und ändern dürfen)
        // TODO Als Entwickler in einem Projekt kann ich den Status einer Aufgabe ändern (Idee, Geplant, in Bearbeitung, im Test, Fertig)
        AcceptedTask acceptedTask = this.findById(acceptedTaskForm.getTaskId());
        this.save(acceptedTaskFormConverter.update(acceptedTask, acceptedTaskForm));
    }

    public void setAssignee(Long taskId, User assignee) {
        // load entity
        AcceptedTask acceptedTask = this.findById(taskId);

        // link user in task
        acceptedTask.setAssignee(assignee);

        // persist entity
        this.save(acceptedTask);
    }

}
