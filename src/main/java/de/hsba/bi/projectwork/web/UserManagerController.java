package de.hsba.bi.projectwork.web;

import de.hsba.bi.projectwork.booking.Booking;
import de.hsba.bi.projectwork.booking.BookingService;
import de.hsba.bi.projectwork.project.ProjectService;
import de.hsba.bi.projectwork.task.acceptedtask.AcceptedTask;
import de.hsba.bi.projectwork.task.acceptedtask.AcceptedTaskService;
import de.hsba.bi.projectwork.task.suggestedtask.SuggestedTask;
import de.hsba.bi.projectwork.task.suggestedtask.SuggestedTaskService;
import de.hsba.bi.projectwork.web.task.acceptedtask.AcceptedTaskForm;
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

    private final AcceptedTaskService acceptedTaskService;
    private final BookingService bookingService;
    private final SuggestedTaskService suggestedTaskService;
    private final ProjectService projectService;


    // dashboard
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("projects", projectService.findProjectsByUser());
        model.addAttribute("tasksAlmostDue", acceptedTaskService.findUsersTasks(acceptedTaskService.findOpenTasks(acceptedTaskService.findAll())));
        model.addAttribute("suggestedTasks", suggestedTaskService.findSuggestedTaskByUser(suggestedTaskService.findByStatus(SuggestedTask.Status.IDEA)));
        model.addAttribute("unassignedAndUnscheduled", acceptedTaskService.findUsersTasks(acceptedTaskService.findUnassignedAndUnscheduled()));
        return "user/dashboard";
    }


    // view projects
    @GetMapping("/projects")
    public String viewProjects(Model model) {
        model.addAttribute("projects", projectService.findAll());
        return "user/projects";
    }


    // view a task
    @GetMapping("/viewTask/{taskId}")
    public String viewTask(@PathVariable("taskId") Long taskId, @RequestParam(value="edited", required=false) boolean edited, Model model) {
        if(edited) {
            model.addAttribute("message", "Task successfully edited.");
        }
        AcceptedTask acceptedTask = acceptedTaskService.findById(taskId);
        acceptedTask.calcDaysLeft();
        model.addAttribute("acceptedTask", acceptedTask);
        model.addAttribute("project", projectService.findById(acceptedTask.getProject().getId()));
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
        AcceptedTask acceptedTask = acceptedTaskService.findById(taskId);
        model.addAttribute("acceptedTask", acceptedTask);
        model.addAttribute("acceptedTaskForm", new AcceptedTaskForm(taskId));
        model.addAttribute("project", projectService.findById(acceptedTask.getProject().getId()));
        model.addAttribute("allStatus", AcceptedTask.Status.getAllStatus());
        return "user/managerDeveloper/editTask";
    }

    @PostMapping("/editTask")
    public String editTask(@ModelAttribute("acceptedTaskForm") @Valid AcceptedTaskForm acceptedTaskForm, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            acceptedTaskService.editTask(acceptedTaskForm);
            return "redirect:/userManager/viewTask/" + acceptedTaskForm.getTaskId() + "?edited=true";
        }
        AcceptedTask acceptedTask = acceptedTaskService.findById(acceptedTaskForm.getTaskId());
        model.addAttribute("acceptedTask", acceptedTask);
        model.addAttribute("acceptedTaskForm", acceptedTaskForm);
        model.addAttribute("project", projectService.findById(acceptedTask.getProject().getId()));
        model.addAttribute("allStatus", AcceptedTask.Status.getAllStatus());
        return "user/managerDeveloper/editTask";
    }

    @PostMapping("/deleteBookedTime")
    public String deleteBookedTime(@RequestParam("taskId") Long taskId, @RequestParam("bookingId") Long bookingId) {
        AcceptedTask acceptedTask = acceptedTaskService.findById(taskId);
        Booking booking = bookingService.findById(bookingId);
        bookingService.deleteBooking(acceptedTask, booking);
        return "redirect:/userManager/viewTask/" + taskId;
    }


    // view tasks and filter them by status
    @GetMapping("/tasks")
    public String viewTasks(@RequestParam(value = "status", required = false) String status, Model model) {
        model.addAttribute("tasks", acceptedTaskService.findUsersTasks(acceptedTaskService.findByStatus(AcceptedTask.Status.getEnumByDisplayValue(status))));
        return "user/managerDeveloper/tasks";
    }


    // accept a suggestedTask
    @PostMapping("/evaluateSuggestedTask/{evaluation}/{taskId}")
    public String acceptTask(@PathVariable("evaluation") String evaluation, @PathVariable("taskId") Long taskId) {
        suggestedTaskService.evaluateSuggestedTask(taskId, evaluation);
        return "redirect:/userManager";
    }

}