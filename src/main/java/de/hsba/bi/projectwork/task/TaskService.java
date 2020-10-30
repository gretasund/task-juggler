package de.hsba.bi.projectwork.task;

import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.project.ProjectRepository;
import de.hsba.bi.projectwork.project.ProjectService;
import de.hsba.bi.projectwork.user.User;
import de.hsba.bi.projectwork.user.UserService;
import de.hsba.bi.projectwork.web.task.TaskForm;
import de.hsba.bi.projectwork.web.task.TaskFormConverter;
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
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;
    private final TaskFormConverter taskFormConverter;


    // find tasks
    public List<Task> findAll() {
        // load tasks
        List<Task> tasks = taskRepository.findAll();

        // calc daysLeft
        for (Task task: tasks) {
            task.calcDaysLeft();
        }

        return tasks;
    }

    public Task findById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            taskRepository.save(task);
            return task;
        }
        return null;
    }

    public List<Task> findByStatus(Enum<Task.Status> status) {
        if(status != null) {
            return taskRepository.findTaskByStatus(status);
        }
        return this.findAll();
    }

    public List<Task> findUnassignedAndUnscheduled() {
        List<Task> usersTasks = this.findAll();
        List<Task> unassignedUnscheduledTasks = new ArrayList<>();

        for (Task task : usersTasks){
            if ((task.getDueDate()==null || task.getAssignee()==null) && task.getStatus() != Task.Status.DONE)
                unassignedUnscheduledTasks.add(task);
        }

        return unassignedUnscheduledTasks;
    }

    public List<Task> findOpenTasks(List<Task> tasks) {
        // remove tasks with status done
        tasks.removeIf(task -> (task.getStatus().equals(Task.Status.DONE) || task.getDueDate() == null));
        return tasks;
    }


    // find task by user
    public List<Task> findUsersTasks(List<Task> tasks) {
        // load entities
        User currentUser = userService.findCurrentUser();

        // iterate over tasks
        tasks.removeIf(task -> !task.getProject().getMembers().contains(currentUser));

        // sort tasks
        Collections.sort(tasks);

        return tasks;
    }

    public List<Task> findTaskByAssignee(User assignee) {
        return taskRepository.findTaskByAssignee(assignee);
    }


    // add task methods
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public Task addTask(Task task, Long projectId) {
        // TODO Als Entwickler in einem Projekt kann ich eine Aufgabe zu diesem Projekt hinzufügen, diese beinhaltet wenigstens einen Titel und eine Beschreibung
        // load entities
        Project project = projectService.findById(projectId);

        if (project != null) {
            // link task in project
            project.getTasks().add(task);

            // persist entities
            taskRepository.save(task);
            projectRepository.save(project);

        }

        return task;
    }


    // edit task methods
    public void editTask(TaskForm taskForm) {
        // TODO Als Entwickler in einem Projekt kann ich eine Zeitschätzung (grob in Stunden) in einer Aufgabe speichern (diese Schätzung soll eine Eigenschaft der Aufgabe sein - verschiedene Entwickler würden diese Schätzung sehen und ändern dürfen)
        // TODO Als Entwickler in einem Projekt kann ich den Status einer Aufgabe ändern (Idee, Geplant, in Bearbeitung, im Test, Fertig)
        Task task = this.findById(taskForm.getTaskId());
        this.save(taskFormConverter.update(task, taskForm));
    }

    public void setAssignee(Long taskId, User assignee) {
        // load entity
        Task task = this.findById(taskId);

        // link user in task
        task.setAssignee(assignee);

        // persist entity
        this.save(task);
    }

}
