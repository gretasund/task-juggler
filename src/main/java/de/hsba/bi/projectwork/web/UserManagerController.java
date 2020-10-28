package de.hsba.bi.projectwork.web;

import de.hsba.bi.projectwork.booking.Booking;
import de.hsba.bi.projectwork.booking.BookingService;
import de.hsba.bi.projectwork.project.ProjectService;
import de.hsba.bi.projectwork.task.SuggestedTask;
import de.hsba.bi.projectwork.task.SuggestedTaskService;
import de.hsba.bi.projectwork.task.Task;
import de.hsba.bi.projectwork.task.TaskService;
import de.hsba.bi.projectwork.user.User;
import de.hsba.bi.projectwork.user.UserService;
import de.hsba.bi.projectwork.web.task.TaskForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping("/userManager")
@PreAuthorize("hasRole('ROLE_MANAGER')")
@RequiredArgsConstructor
public class UserManagerController {

    private final UserService userService;
    private final TaskService taskService;
    private final BookingService bookingService;
    private final SuggestedTaskService suggestedTaskService;
    private final ProjectService projectService;


    // dashboard
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("projects", projectService.findProjectsByUser());
        model.addAttribute("tasksAlmostDue", taskService.findUsersTasks(taskService.findOpenTasks(taskService.findAll())));
        model.addAttribute("suggestedTasks", suggestedTaskService.findSuggestedTaskByUser(suggestedTaskService.findByStatus(SuggestedTask.Status.IDEA)));
        model.addAttribute("unassignedAndUnscheduled", taskService.findUsersTasks(taskService.findUnassignedAndUnscheduled()));
        return "user/dashboard";
    }


    // Als Entwickler oder Manager kann ich pro Aufgabe sehen, wieviel Zeit insgesamt schon darauf gebucht wurde (allerdings nicht von wem)
    @GetMapping("/projects")
    public String viewProjects(Model model) {
        model.addAttribute("dueDate", "");
        model.addAttribute("projects", projectService.findAll());
        return "user/managerDeveloper/projects";
    }


    // view a task
    @GetMapping("/viewTask/{taskId}")
    public String viewTask(@PathVariable("taskId") Long taskId, @RequestParam(value="edited", required=false) boolean edited, Model model) {
        if(edited) {
            model.addAttribute("message", "Task successfully edited.");
        }
        Task task = taskService.findById(taskId);
        task.calcDaysLeft();
        model.addAttribute("task", task);
        model.addAttribute("dueDate", "");
        model.addAttribute("assignee", new User());
        model.addAttribute("project", projectService.findById(task.getProject().getId()));
        model.addAttribute("allStatus", Task.Status.getAllStatus());
        return "user/managerDeveloper/viewTask";
    }

    // view a suggestedTask
    @GetMapping("/viewSuggestedTask/{taskId}")
    public String viewSuggestedTask(@PathVariable("taskId") Long taskId, Model model) {
        SuggestedTask suggestedTask = suggestedTaskService.findById(taskId);
        model.addAttribute("suggestedTask", suggestedTask);
        return "user/managerDeveloper/viewSuggestedTask";
    }


    // edit a task
    @GetMapping("/editTask/{taskId}")
    public String editTask(@PathVariable("taskId") Long taskId, Model model) {
        Task task = taskService.findById(taskId);
        task.calcTotalTime();
        model.addAttribute("task", task);
        model.addAttribute("taskForm", new TaskForm(taskId));
        model.addAttribute("project", projectService.findById(task.getProject().getId()));
        model.addAttribute("allStatus", Task.Status.getAllStatus());
        return "user/managerDeveloper/editTask";
    }

    @PostMapping("/editTask")
    public String editTask(@ModelAttribute("taskForm") @Valid TaskForm taskForm, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            taskService.editTask(taskForm);
            return "redirect:/userManager/viewTask/" + taskForm.getTaskId() + "?edited=true";
        }
        Task task = taskService.findById(taskForm.getTaskId());
        model.addAttribute("task", task);
        model.addAttribute("taskForm", taskForm);
        model.addAttribute("project", projectService.findById(task.getProject().getId()));
        return "user/managerDeveloper/editTask";
    }

    @PostMapping("/deleteBookedTime")
    public String deleteBookedTime(@RequestParam("taskId") Long taskId, @RequestParam("bookingId") Long bookingId) {
        Task task = taskService.findById(taskId);
        Booking booking = bookingService.findById(bookingId);
        bookingService.deleteBookedTime(task, booking);
        return "redirect:/userManager/viewTask/" + taskId;
    }

    @PostMapping("/setAssignee/{taskId}")
    public String setAssignee(@PathVariable("taskId") Long taskId, @ModelAttribute("assignee") User assignee) {
        taskService.setAssignee(taskId, assignee);
        return "redirect:/userManager/viewTask/" + taskId;
    }

    @PostMapping("/setDueDate/{taskId}")
    public String setDueDate(@PathVariable("taskId") Long taskId, @ModelAttribute("dueDate") String dueDate) {
        taskService.setDueDate(taskId, dueDate);
        return "redirect:/userManager/viewTask/" + taskId;
    }


    // Als Entwickler oder Manager kann ich mir eine Liste der Aufgaben gefiltert nach Status anzeigen lassen.
    @GetMapping("/tasks")
    public String viewTasks(@RequestParam(value = "status", required = false) String status, Model model) {
        model.addAttribute("tasks", taskService.findUsersTasks(taskService.findByStatus(Task.Status.getEnumByDisplayValue(status))));
        return "user/managerDeveloper/tasks";
    }


    // accept a suggestedTask
    @PostMapping("/evaluateSuggestedTask/{evaluation}/{taskId}")
    public String acceptTask(@PathVariable("evaluation") String evaluation, @PathVariable("taskId") Long taskId) {
        suggestedTaskService.evaluateSuggestedTask(taskId, evaluation);
        return "redirect:/userManager";
    }


    // Als Manager kann ich die gebuchten Zeiten aller Entwickler einsehen
    @GetMapping("/users")
    public String viewUsers(Model model) {
        model.addAttribute("users", userService.findUsers());
        return "user/manager/users";
    }

}