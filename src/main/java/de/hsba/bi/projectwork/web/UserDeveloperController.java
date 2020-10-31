package de.hsba.bi.projectwork.web;

import de.hsba.bi.projectwork.booking.BookingService;
import de.hsba.bi.projectwork.project.ProjectService;
import de.hsba.bi.projectwork.task.acceptedtask.AcceptedTask;
import de.hsba.bi.projectwork.task.acceptedtask.AcceptedTaskService;
import de.hsba.bi.projectwork.task.suggestedtask.SuggestedTask;
import de.hsba.bi.projectwork.task.suggestedtask.SuggestedTaskService;
import de.hsba.bi.projectwork.user.UserService;
import de.hsba.bi.projectwork.web.booking.BookingForm;
import de.hsba.bi.projectwork.web.task.acceptedtask.AcceptedTaskForm;
import de.hsba.bi.projectwork.web.task.suggestedtask.SuggestedTaskForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping("/userDeveloper")
@PreAuthorize("hasRole('ROLE_DEVELOPER')")
@RequiredArgsConstructor
public class UserDeveloperController {

    private final UserService userService;
    private final ProjectService projectService;
    private final AcceptedTaskService acceptedTaskService;
    private final SuggestedTaskService suggestedTaskService;
    private final BookingService bookingService;


    // dashboard
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("projects", projectService.findProjectsByUser());
        model.addAttribute("myTasks", acceptedTaskService.findTaskByAssignee(userService.findCurrentUser()));
        model.addAttribute("mySuggestedTasks", suggestedTaskService.findSuggestedTaskByCreator(userService.findCurrentUser()));
        model.addAttribute("openTasks", acceptedTaskService.findUsersTasks(acceptedTaskService.findTaskByAssignee(null)));
        return "user/dashboard";
    }


    // projects
    @GetMapping("/projects")
    public String viewProjects(Model model) {
        // TODO Als Entwickler in einem Projekt kann ich sehen, wieviel Zeit ich insgesamt (aufgabenübergreifend) gebucht habe (allerdings nicht die Zeiten anderer Entwickler)
        model.addAttribute("projects", projectService.findProjectsByUser());
        model.addAttribute("usersBookings", userService.findCurrentUser().getBookedTimes());
        return "user/projects";
    }


    // add tasks
    @GetMapping("/addTask/{projectId}")
    public String addTask(Model model, @PathVariable("projectId") Long projectId) {
        model.addAttribute("suggestedTaskForm", new SuggestedTaskForm(projectService.findById(projectId), userService.findCurrentUser()));
        return "user/developer/addTask";
    }

    @PostMapping("/addTask")
    public String addTask(@ModelAttribute("suggestedTaskForm") @Valid SuggestedTaskForm suggestedTaskForm, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            SuggestedTask suggestedTask = suggestedTaskService.addSuggestedTask(suggestedTaskForm);
            return "redirect:/userDeveloper/viewSuggestedTask/" + suggestedTask.getId() + "?created=true";
        }
        model.addAttribute("suggestedTaskForm", suggestedTaskForm);
        return "user/developer/addTask";
    }


    // view task
    @GetMapping("/viewTask/{taskId}")
    public String viewTask(@PathVariable("taskId") Long taskId, @RequestParam(value="edited", required=false) boolean edited, Model model) {
        if(edited) {
            model.addAttribute("message", "Task successfully edited.");
        }
        AcceptedTask acceptedTask = acceptedTaskService.findById(taskId);
        model.addAttribute("acceptedTask", acceptedTask);
        model.addAttribute("bookingForm", new BookingForm());
        model.addAttribute("userObject", userService.findCurrentUser());
        model.addAttribute("project", projectService.findById(acceptedTask.getProject().getId()));
        model.addAttribute("allStatus", SuggestedTask.Status.getAllStatus());
        return "user/managerDeveloper/viewTask";
    }

    @GetMapping("/viewSuggestedTask/{taskId}")
    public String viewSuggestedTask(@PathVariable("taskId") Long taskId, @RequestParam(value="created", required=false) boolean edited, Model model) {
        if(edited) {
            model.addAttribute("message", "Task successfully suggested.");
        }
        SuggestedTask suggestedTask = suggestedTaskService.findById(taskId);
        model.addAttribute("suggestedTask", suggestedTask);
        return "user/managerDeveloper/viewSuggestedTask";
    }


    // edit task
    @GetMapping("/editTask/{taskId}")
    public String editTask(@PathVariable("taskId") Long taskId, Model model) {
        AcceptedTask acceptedTask = acceptedTaskService.findById(taskId);
        model.addAttribute("acceptedTask", acceptedTask);
        model.addAttribute("taskForm", new AcceptedTaskForm(taskId));
        model.addAttribute("project", projectService.findById(acceptedTask.getProject().getId()));
        model.addAttribute("allStatus", AcceptedTask.Status.getAllStatus());
        return "user/managerDeveloper/editTask";
    }

    @PostMapping("/editTask")
    public String editTask(@ModelAttribute("taskForm") @Valid AcceptedTaskForm acceptedTaskForm, BindingResult bindingResult, Model model) {
       if (!bindingResult.hasErrors()) {
            acceptedTaskService.editTask(acceptedTaskForm);
            return "redirect:/userDeveloper/viewTask/" + acceptedTaskForm.getTaskId() + "?edited=true";
        }
        AcceptedTask acceptedTask = acceptedTaskService.findById(acceptedTaskForm.getTaskId());
        model.addAttribute("acceptedTask", acceptedTask);
        model.addAttribute("taskForm", acceptedTaskForm);
        model.addAttribute("project", projectService.findById(acceptedTask.getProject().getId()));
        return "user/managerDeveloper/editTask";
    }


    @PostMapping("/bookTime")
    public String bookTime(@RequestParam("taskId") Long taskId, @RequestParam("projectId") Long projectId, @ModelAttribute("bookingForm") @Valid BookingForm bookingForm, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            bookingService.addBooking(taskId, projectId, bookingForm, userService.findCurrentUser());
            return "redirect:/userDeveloper/viewTask/" + taskId;
        }
        AcceptedTask acceptedTask = acceptedTaskService.findById(taskId);
        model.addAttribute("acceptedTask", acceptedTask);
        model.addAttribute("bookingForm", bookingForm);
        model.addAttribute("userObject", userService.findCurrentUser());
        model.addAttribute("usersBookings", bookingService.findUsersBookings(acceptedTask.getTimes()));
        model.addAttribute("project", projectService.findById(acceptedTask.getProject().getId()));
        model.addAttribute("allStatus", SuggestedTask.Status.getAllStatus());
        return "user/managerDeveloper/viewTask";
    }


    // view all tasks
    @GetMapping("/tasks")
    public String viewTasks(@RequestParam(value = "status", required = false) String status, Model model) {
        model.addAttribute("tasks", acceptedTaskService.findUsersTasks(acceptedTaskService.findByStatus(AcceptedTask.Status.getEnumByDisplayValue(status))));
        return "user/managerDeveloper/tasks";
    }

}