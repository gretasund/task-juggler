package de.hsba.bi.projectwork.init;

import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.project.ProjectService;
import de.hsba.bi.projectwork.task.Task;
import de.hsba.bi.projectwork.task.TaskService;
import de.hsba.bi.projectwork.user.User;
import de.hsba.bi.projectwork.user.UserService;
import de.hsba.bi.projectwork.web.project.UpdateProjectForm;
import de.hsba.bi.projectwork.web.user.RegisterUserForm;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserInitializr {

    private final UserService userService;
    private final ProjectService projectService;
    private final TaskService taskService;


    @EventListener(ApplicationStartedEvent.class)
    public void init() {
        if (!userService.findAll().isEmpty()) {
            // prevent initialisation if DB is not empty
            return;
        }
        this.initAdmins();
        this.initFirstDepartment();
    }


    public void initAdmins() {
        // create some admins
        userService.createUser(new RegisterUserForm("admin1", "1234567890", "1234567890"), "ADMIN");
        userService.createUser(new RegisterUserForm("admin2", "1234567890", "1234567890"), "ADMIN");
    }


    public void initFirstDepartment() {
        // petra, head of HR
        User petra = userService.createUser(new RegisterUserForm("petra", "1234567890", "1234567890"), "MANAGER");

        // CREATE PROJECT MEMBERS
        User sandra = userService.createUser(new RegisterUserForm("sandra", "1234567890", "1234567890"), "DEVELOPER");
        User brigitte = userService.createUser(new RegisterUserForm("brigitte", "1234567890", "1234567890"), "DEVELOPER");
        User conny = userService.createUser(new RegisterUserForm("conny", "1234567890", "1234567890"), "DEVELOPER");
        User student = userService.createUser(new RegisterUserForm("student", "1234567890", "1234567890"), "DEVELOPER");
        List<User> usersFirstProject = new ArrayList<>(Arrays.asList(petra, sandra, brigitte, conny));
        List<User> usersSecondProject = new ArrayList<>(Arrays.asList(petra, sandra, brigitte, student));


        // CREATE FIRST PROJECT
        Project project = projectService.createNewProject(new Project("Amazing HR Strategy"));
        projectService.addUserToProject(usersFirstProject, project.getId());

        // create tasks
        Task task1 = taskService.addNewTask(new Task("Create a Company Page on Facebook", "In order to attract possible applicants our company has to be introduced on social media. A facebook page has to be created an filled with content.", 3, "Testing", "2020-08-28"), project.getId());
        taskService.setAssignee(task1.getId(), sandra);
        Task task2 = taskService.addNewTask(new Task("Create a Company Page on Instagram", "In order to attract possible applicants our company has to be introduced on social media. An instagram page has to be created an filled with content.", 3, "Done", null), project.getId());
        taskService.setAssignee(task2.getId(), brigitte);
        Task task3 = taskService.addNewTask(new Task("List All Job Fairs Taking Place in the Region", "We want to meet people looking for a job! This is why we want to visit some job fairs that are visited by possible applicants. Please create a list of all job fairs within a radius of 30km.", 2, "Work in progress", null), project.getId()); //"2020-09-03"
        taskService.setAssignee(task2.getId(), conny);
        Task task4 = taskService.addNewTask(new Task("Design an Employee-Survey", "We want to know how satisfied our current employees are and what we can do to make them even happier. To gain the necessary insights please plan a survey that will later be send to all employees.", 6, "Planned", "2020-08-28"), project.getId());
        taskService.setAssignee(task4.getId(), sandra);
        Task task5 = taskService.addNewTask(new Task("Organize a Summer Party", "For staff retention reasons we will be holding a summer party again this year!", 6, "Idea", "2020-10-20"), project.getId());
        taskService.setAssignee(task5.getId(), sandra);
        Task task6 = taskService.addNewTask(new Task("Promote the Employee Referral Program", "95% of all applicants that apply for one of our jobs because of a referral are successful but only <1% of all employees know about the program. Please create a page in the intranet and promote it!", 3, "Idea", "2020-09-10"), project.getId());

    }

}