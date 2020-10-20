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
        return "userDeveloper/dashboard";
    }


    // Als Entwickler in einem Projekt kann ich eine Aufgabe zu diesem Projekt hinzufügen, diese beinhaltet wenigstens einen Titel und eine Beschreibung
    @GetMapping("/projects")
    public String viewProjects(Model model) {
        // TODO Als Entwickler in einem Projekt kann ich sehen, wieviel Zeit ich insgesamt (aufgabenübergreifend) gebucht habe (allerdings nicht die Zeiten anderer Entwickler)
        model.addAttribute("projects", projectService.findProjectsByUser());
        return "userDeveloper/projects";
    }

    @GetMapping("/addTask/{projectId}")
    public String addTask(Model model, @PathVariable("projectId") Long projectId) {
        model.addAttribute("suggestedTaskForm", new SuggestedTaskForm(projectService.findById(projectId), userService.findCurrentUser()));
        return "userDeveloper/addTask";
    }

    @PostMapping("/addTask")
    public String addTask(@ModelAttribute("suggestedTaskForm") @Valid SuggestedTaskForm suggestedTaskForm, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            SuggestedTask suggestedTask = suggestedTaskService.createNewSuggestedTask(suggestedTaskForm);
            return "redirect:/userDeveloper/viewSuggestedTask/" + suggestedTask.getId() + "?created=true";
        }
        model.addAttribute("suggestedTaskForm", suggestedTaskForm);
        return "userDeveloper/addTask";
    }


    // Als Entwickler oder Manager kann ich pro Aufgabe sehen, wieviel Zeit insgesamt schon darauf gebucht wurde (allerdings nicht von wem)
    @GetMapping("/viewTask/{taskId}")
    public String viewTask(@PathVariable("taskId") Long taskId, @RequestParam(value="edited", required=false) boolean edited, Model model) {
        if(edited) {
            model.addAttribute("message", "Task successfully edited.");
        }
        Task task = taskService.findById(taskId);
        task.calcTotalTime();
        model.addAttribute("task", task);
        model.addAttribute("bookingForm", new BookingForm());
        model.addAttribute("userObject", userService.findCurrentUser());
        model.addAttribute("usersBookings", bookingService.findUsersBookings(task.getTimes()));
        model.addAttribute("project", projectService.findById(task.getProject().getId()));
        model.addAttribute("allStatus", SuggestedTask.Status.getAllStatus());
        return "userDeveloper/viewTask";
    }

    @GetMapping("/viewSuggestedTask/{taskId}")
    public String viewSuggestedTask(@PathVariable("taskId") Long taskId, @RequestParam(value="created", required=false) boolean edited, Model model) {
        if(edited) {
            model.addAttribute("message", "Task successfully suggested.");
        }
        SuggestedTask suggestedTask = suggestedTaskService.findById(taskId);
        model.addAttribute("suggestedTask", suggestedTask);
        return "userDeveloper/viewSuggestedTask";
    }


    // edit task
    @GetMapping("/editTask/{taskId}")
    public String editTask(@PathVariable("taskId") Long taskId, Model model) {
        Task task = taskService.findById(taskId);
        task.calcTotalTime();
        model.addAttribute("task", task);
        model.addAttribute("taskForm", new TaskForm(taskId));
        model.addAttribute("project", projectService.findById(task.getProject().getId()));
        model.addAttribute("allStatus", Task.Status.getAllStatus());
        return "userDeveloper/editTask";
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
        return "userDeveloper/editTask";
    }


    //  Als Entwickler in einem Projekt kann ich aufgewendete Zeiten für eine Aufgabe buchen (diese Buchung gilt pro Aufgabe und Entwickler, außerdem kann ein Entwickler mehrmals Zeiten buchen, wenn die Bearbeitung beispielsweise mehrere Tage dauert)
    /*@PostMapping("/bookTime")
    public String bookTime(@RequestParam("taskId") Long taskId, @RequestParam("projectId") Long projectId, @RequestParam("date") String date, @RequestParam("time") int time) {
        bookingService.bookTime(taskId, projectId, time, date, userService.findCurrentUser());
        return "redirect:/userDeveloper/viewTask/" + taskId;
    }
    */
    @PostMapping("/bookTime")
    public String bookTime(@RequestParam("taskId") Long taskId, @RequestParam("projectId") Long projectId, @ModelAttribute("bookingForm") @Valid BookingForm bookingForm, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            bookingService.bookTime(taskId, projectId, bookingForm, userService.findCurrentUser());
            return "redirect:/userDeveloper/viewTask/" + taskId;
        }
        Task task = taskService.findById(taskId);
        task.calcTotalTime();
        model.addAttribute("task", task);
        model.addAttribute("bookingForm", bookingForm);
        model.addAttribute("userObject", userService.findCurrentUser());
        model.addAttribute("usersBookings", bookingService.findUsersBookings(task.getTimes()));
        model.addAttribute("project", projectService.findById(task.getProject().getId()));
        model.addAttribute("allStatus", SuggestedTask.Status.getAllStatus());
        return "userDeveloper/viewTask";
    }


    // Als Entwickler oder Manager kann ich mir eine Liste der Aufgaben gefiltert nach Status anzeigen lassen.
    @GetMapping("/tasks")
    public String viewTasks(@RequestParam(value = "status", required = false) String status, Model model) {
        model.addAttribute("tasks", taskService.findUsersTasks(taskService.findByStatus(Task.Status.getEnumByDisplayValue(status))));
        return "userDeveloper/tasks";
    }

}