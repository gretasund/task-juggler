package de.hsba.bi.projectwork.web;

import de.hsba.bi.projectwork.booking.Booking;
import de.hsba.bi.projectwork.booking.BookingService;
import de.hsba.bi.projectwork.project.ProjectService;
import de.hsba.bi.projectwork.task.*;
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
import java.util.HashMap;


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
    public String index(Model model) {
        model.addAttribute("projects", projectService.findProjectsByUser());
        model.addAttribute("tasksAlmostDue", taskService.findUsersTasks(taskService.findAll()));
        model.addAttribute("suggestedTasks", suggestedTaskService.findSuggestedTaskByUser(suggestedTaskService.findOpenSuggestions()));
        model.addAttribute("unassignedAndUnscheduled", taskService.findUsersTasks(taskService.findUnassignedAndUnscheduled()));
        return "userManager/index";
    }


    // Als Entwickler oder Manager kann ich pro Aufgabe sehen, wieviel Zeit insgesamt schon darauf gebucht wurde (allerdings nicht von wem)
    @GetMapping("/projects")
    public String viewProjects(Model model) {
        model.addAttribute("dueDate", "");
        model.addAttribute("projects", projectService.findAll());
        return "userManager/projects";
    }


    // view a task
    @GetMapping("/viewTask/{taskId}")
    public String viewTask(@PathVariable("taskId") Long taskId, Model model) {
        Task task = taskService.findById(taskId);
        task.calcDaysLeft();
        model.addAttribute("task", task);
        model.addAttribute("dueDate", "");
        model.addAttribute("assignee", new User());
        model.addAttribute("project", projectService.findById(task.getProject().getId()));
        model.addAttribute("allStatus", BaseTask.Status.getAllStatus());
        return "userManager/viewTask";
    }

    // view a suggestedTask
    @GetMapping("/viewSuggestedTask/{taskId}")
    public String viewSuggestedTask(@PathVariable("taskId") Long taskId, Model model) {
        SuggestedTask suggestedTask = suggestedTaskService.findById(taskId);
        model.addAttribute("suggestedTask", suggestedTask);
        return "userManager/viewSuggestedTask";
    }


    // edit a task
    @PostMapping("/editTask/{taskId}")
    public String update(@PathVariable("taskId") Long taskId, @ModelAttribute("taskForm") @Valid TaskForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/userManager/viewTask/" + taskId;
        }
        taskService.editTask(taskId, form);
        return "redirect:/userManager/viewTask/" + taskId;
    }

    @PostMapping("/deleteBookedTime")
    public String deleteBookedTime(@RequestParam("taskId") Long taskId, @RequestParam("bookingId") Long bookingId) {
        Task task = taskService.findById(taskId);
        Booking booking = bookingService.findById(bookingId);
        bookingService.deleteBookedTime(task, booking);
        return "redirect:/userManager/viewTask/" + taskId;
    }

    @PostMapping("/setAssignee/{taskId}")
    public String setAssignee(@PathVariable("taskId") Long taskId, @ModelAttribute("assignee") User assignee,  BindingResult bindingResult) {
        taskService.setAssignee(taskId, assignee);
        return "redirect:/userManager/viewTask/" + taskId;
    }

    @PostMapping("/setDueDate/{taskId}")
    public String setDueDate(@PathVariable("taskId") Long taskId, @ModelAttribute("dueDate") String dueDate, BindingResult bindingResult) {
        taskService.setDueDate(taskId, dueDate);
        return "redirect:/userManager/viewTask/" + taskId;
    }


    // Als Entwickler oder Manager kann ich mir eine Liste der Aufgaben gefiltert nach Status anzeigen lassen.
    @GetMapping("/tasks")
    public String viewTasks(@RequestParam(value = "status", required = false) String status, Model model) {
        if (status != null) {
            if (status.equals("Idea") || status.equals("Planned") || status.equals("wip") || status.equals("Testing") || status.equals("Done")) {
                model.addAttribute("tasks", taskService.findUsersTasks(taskService.findByStatus(status)));
            }
        } else {
            model.addAttribute("tasks", taskService.findAll());
        }
        return "userManager/tasks";
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
        return "userManager/users";
    }


    // Als Manager kann ich eine Statistik einsehen, bei der die geschätzten und gebuchten Zeiten gegenübergestellt werden
    @PostMapping("/compareEstimatedAndBookedTimes")
    public String compareEstimatedAndBookedTimes() {
        HashMap<User, Task> comparison = bookingService.compareEstimatedAndBookedTimes();
        return "redirect:/userManager";
    }

}