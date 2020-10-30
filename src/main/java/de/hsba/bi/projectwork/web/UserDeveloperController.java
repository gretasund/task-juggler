package de.hsba.bi.projectwork.web;

import de.hsba.bi.projectwork.booking.BookingService;
import de.hsba.bi.projectwork.project.ProjectService;
import de.hsba.bi.projectwork.task.SuggestedTask;
import de.hsba.bi.projectwork.task.SuggestedTaskService;
import de.hsba.bi.projectwork.task.Task;
import de.hsba.bi.projectwork.task.TaskService;
import de.hsba.bi.projectwork.user.UserService;
import de.hsba.bi.projectwork.web.booking.BookingForm;
import de.hsba.bi.projectwork.web.task.SuggestedTaskForm;
import de.hsba.bi.projectwork.web.task.TaskForm;
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
    private final TaskService taskService;
    private final SuggestedTaskService suggestedTaskService;
    private final BookingService bookingService;


    // dashboard
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("projects", projectService.findProjectsByUser());
        model.addAttribute("myTasks", taskService.findTaskByAssignee(userService.findCurrentUser()));
        model.addAttribute("mySuggestedTasks", suggestedTaskService.findSuggestedTaskByCreator(userService.findCurrentUser()));
        model.addAttribute("openTasks", taskService.findUsersTasks(taskService.findTaskByAssignee(null)));
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
        Task task = taskService.findById(taskId);
        model.addAttribute("task", task);
        model.addAttribute("bookingForm", new BookingForm());
        model.addAttribute("userObject", userService.findCurrentUser());
        model.addAttribute("project", projectService.findById(task.getProject().getId()));
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
        Task task = taskService.findById(taskId);
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
            return "redirect:/userDeveloper/viewTask/" + taskForm.getTaskId() + "?edited=true";
        }
        Task task = taskService.findById(taskForm.getTaskId());
        model.addAttribute("task", task);
        model.addAttribute("taskForm", taskForm);
        model.addAttribute("project", projectService.findById(task.getProject().getId()));
        return "user/managerDeveloper/editTask";
    }


    @PostMapping("/bookTime")
    public String bookTime(@RequestParam("taskId") Long taskId, @RequestParam("projectId") Long projectId, @ModelAttribute("bookingForm") @Valid BookingForm bookingForm, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            bookingService.addBooking(taskId, projectId, bookingForm, userService.findCurrentUser());
            return "redirect:/userDeveloper/viewTask/" + taskId;
        }
        Task task = taskService.findById(taskId);
        model.addAttribute("task", task);
        model.addAttribute("bookingForm", bookingForm);
        model.addAttribute("userObject", userService.findCurrentUser());
        model.addAttribute("usersBookings", bookingService.findUsersBookings(task.getTimes()));
        model.addAttribute("project", projectService.findById(task.getProject().getId()));
        model.addAttribute("allStatus", SuggestedTask.Status.getAllStatus());
        return "user/managerDeveloper/viewTask";
    }


    // view all tasks
    @GetMapping("/tasks")
    public String viewTasks(@RequestParam(value = "status", required = false) String status, Model model) {
        model.addAttribute("tasks", taskService.findUsersTasks(taskService.findByStatus(Task.Status.getEnumByDisplayValue(status))));
        return "user/managerDeveloper/tasks";
    }

}