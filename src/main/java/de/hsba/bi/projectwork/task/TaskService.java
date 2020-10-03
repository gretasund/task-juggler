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

@RequiredArgsConstructor
@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;
    private final TaskFormConverter taskFormConverter;


    // find tasks
    public Task findById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            taskRepository.save(task);
            return task;
        } else {
            // TODO throw an exception
            return null;
        }
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public List<Task> findTasksByStatus(String status) {
        if (status != null) {
            if (status.equals("Idea") || status.equals("Planned") || status.equals("wip") || status.equals("Testing") || status.equals("Done")) {
                List<Task> allTasks = taskRepository.findAll();
                List<Task> tasks = new ArrayList<>();

                if (status.equals("wip")) {
                    status = "Work in progress";
                }

                for (Task task : allTasks) {
                    if (task.getStatus().equals(status)) {
                        tasks.add(task);
                    }
                }
                return tasks;
            }
        }
        return this.findAll();
    }

    public List<Task> findTasksByStatusAndUser(String status) {
        User user = userService.findCurrentUser();
        List<Task> tasks = this.findTasksByStatus(status);
        if (tasks.size() > 0) {
            tasks.removeIf(task -> !task.getProject().getMembers().contains(user));
        }
        return tasks;
    }

    public List<Task> findAllManagersTasks() {
        User user = userService.findCurrentUser();
        List<Task> tasks = this.findAll();
        if (tasks.size() > 0) {
            tasks.removeIf(task -> !task.getProject().getMembers().contains(user));
        }
        return tasks;
    }

    public List<Task> findTaskByAssignee(User assignee) {
        return taskRepository.findTaskByAssignee(assignee);
    }

    public List<Task> findTaskByDueDate(String dueDate) {
        return taskRepository.findTaskByDueDate(dueDate);
    }

    public List<String> findRemainingStatuses(Long taskId) {
        Task task = this.findById(taskId);
        List<String> remainingStatuses = new ArrayList<>();
        remainingStatuses.add("Idea");
        remainingStatuses.add("Planned");
        remainingStatuses.add("Work in progress");
        remainingStatuses.add("Testing");
        remainingStatuses.add("Done");
        remainingStatuses.remove(task.getStatus());
        return remainingStatuses;
    }

    public List<Task> findAlmostDue() {
        List<Task> allManagersTasks = this.findAllManagersTasks();
        List<Task> allManagersTasksWithDueDate = new ArrayList<>();

        if (allManagersTasks.size() > 0) {

            // remove Tasks with unset dueDate
            for(int i=0; i<allManagersTasks.size(); i++){
                if(allManagersTasks.get(i).getDueDate() != null){
                    allManagersTasksWithDueDate.add(allManagersTasks.get(i));
                }
            }

            // calc daysLeft
            for (Task task: allManagersTasksWithDueDate) {
                task.calcDaysLeft();
            }

            // sort allManagersTasks by date
            Collections.sort(allManagersTasksWithDueDate);

        }
        return allManagersTasksWithDueDate;
    }

    public List<Task> findUnassignedAndUnscheduled() {
        List<Task> unassignedTasks = this.findTaskByAssignee(null);
        List<Task> unscheduledTasks = this.findTaskByDueDate(null);

        for (Task task : unscheduledTasks){
            if (!unassignedTasks.contains(task))
                unassignedTasks.add(task);
        }

        return unassignedTasks;
    }


    // add task methods
    public void save(Task task) {
        taskRepository.save(task);
    }

    public Task addNewTask(Task task, Long projectId) {
        // TODO Als Entwickler in einem Projekt kann ich eine Aufgabe zu diesem Projekt hinzufügen, diese beinhaltet wenigstens einen Titel und eine Beschreibung
        Project project = projectService.findById(projectId);
        if (checkStatusValidity(task) && project != null) {
            task.setProject(project);
            List<Task> tasks = project.getTasks();
            tasks.add(task);
            taskRepository.save(task);
            projectRepository.save(project);
        } else {
            // TODO throw an expection
        }
        return task;
    }


    // edit task methods
    public void editTask(Long taskId, TaskForm form) {
        // TODO Als Entwickler in einem Projekt kann ich eine Zeitschätzung (grob in Stunden) in einer Aufgabe speichern (diese Schätzung soll eine Eigenschaft der Aufgabe sein - verschiedene Entwickler würden diese Schätzung sehen und ändern dürfen)
        // TODO Als Entwickler in einem Projekt kann ich den Status einer Aufgabe ändern (Idee, Geplant, in Bearbeitung, im Test, Fertig)
        Task task = this.findById(taskId);
        this.save(taskFormConverter.update(task, form));
    }

    public void setAssignee(Long taskId, User assignee) {
        Task task = this.findById(taskId);
        task.setAssignee(assignee);
        this.save(task);
    }

    public boolean checkStatusValidity(Task task) {
        switch (task.getStatus()) {
            case "Idea":
                return true;
            case "Planned":
                return true;
            case "Work in progress":
                return true;
            case "Testing":
                return true;
            case "Done":
                return true;
            default:
                return false;
        }
    }

}
