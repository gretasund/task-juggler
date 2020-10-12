package de.hsba.bi.projectwork.init;

import de.hsba.bi.projectwork.booking.BookingService;
import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.project.ProjectService;
import de.hsba.bi.projectwork.task.SuggestedTask;
import de.hsba.bi.projectwork.task.SuggestedTaskService;
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
public class FirstDepartmentInitializr {

    private final UserService userService;
    private final ProjectService projectService;
    private final TaskService taskService;
    private final BookingService bookingService;
    private final SuggestedTaskService suggestedTaskService;


    @EventListener(ApplicationStartedEvent.class)
    public void init() {
        this.createUsers();
        this.firstProject();
        this.secondProject();
    }

    public void createUsers() {
        // create manager (head of the department)
        userService.createUser(new RegisterUserForm("petra", "1234567890", "1234567890"), "MANAGER");

        // create project members
        userService.createUser(new RegisterUserForm("sandra", "1234567890", "1234567890"), "DEVELOPER");
        userService.createUser(new RegisterUserForm("brigitte", "1234567890", "1234567890"), "DEVELOPER");
        userService.createUser(new RegisterUserForm("conny", "1234567890", "1234567890"), "DEVELOPER");
        userService.createUser(new RegisterUserForm("student", "1234567890", "1234567890"), "DEVELOPER");
    }

    public void firstProject() {
        // create list of users
        User petra = userService.findByName("petra");
        User sandra = userService.findByName("sandra");
        User brigitte = userService.findByName("brigitte");
        User conny = userService.findByName("conny");
        List<User> users = new ArrayList<>(Arrays.asList(petra, sandra, brigitte, conny));


        // create project
        Project project = projectService.createNewProject(new Project("Amazing HR Strategy"));
        projectService.addUserToProject(new UpdateProjectForm(users), project.getId());


        // create tasks
        Task task1 = taskService.createNewTask(new Task(sandra, "Create a Company Page on Facebook", "In order to attract possible applicants our company has to be introduced on social media. A facebook page has to be created and filled with content.", 3, "Testing", "2020-08-28", project), project.getId());
        taskService.setAssignee(task1.getId(), sandra);
        bookingService.bookTime(task1.getId(), project.getId(), 1, "2020-06-28", conny);
        bookingService.bookTime(task1.getId(), project.getId(), 4, "2020-06-30", brigitte);
        bookingService.bookTime(task1.getId(), project.getId(), 1, "2020-06-28", sandra);
        bookingService.bookTime(task1.getId(), project.getId(), 1, "2020-06-30", sandra);

        Task task2 = taskService.createNewTask(new Task(brigitte, "Create a Company Page on Instagram", "In order to attract possible applicants our company has to be introduced on social media. An instagram page has to be created an filled with content.", 3, "Done", null, project), project.getId());
        taskService.setAssignee(task2.getId(), brigitte);
        bookingService.bookTime(task2.getId(), project.getId(), 1, "2020-07-09", brigitte);
        bookingService.bookTime(task2.getId(), project.getId(), 1, "2020-07-11", brigitte);

        Task task3 = taskService.createNewTask(new Task(conny,"List All Job Fairs Taking Place in the Region", "We want to meet people looking for a job! This is why we want to visit some job fairs that are visited by possible applicants. Please create a list of all job fairs within a radius of 30km.", 2, "Work in progress", null, project), project.getId());
        taskService.setAssignee(task2.getId(), conny);
        bookingService.bookTime(task3.getId(), project.getId(), 3, "2020-07-09", conny);

        Task task4 = taskService.createNewTask(new Task(sandra, "Design an Employee-Survey", "We want to know how satisfied our current employees are and what we can do to make them even happier. To gain the necessary insights please plan a survey that will later be send to all employees.", 6, "Planned", "2020-08-28", project), project.getId());
        taskService.setAssignee(task4.getId(), sandra);

        Task task5 = taskService.createNewTask(new Task(sandra, "Organize a Summer Party", "For staff retention reasons we will be holding a summer party again this year!", 6, "Idea", "2020-10-20", project), project.getId());
        taskService.setAssignee(task5.getId(), sandra);

        Task task6 = taskService.createNewTask(new Task(conny, "Promote the Employee Referral Program", "95% of all applicants that apply for one of our jobs because of a referral are successful but only <1% of all employees know about the program. Please create a page in the intranet and promote it!", 3, "Idea", "2020-09-10", project), project.getId());

        // create task suggestions
        SuggestedTask suggestedTask1 = suggestedTaskService.createNewSuggestedTask(new SuggestedTask(project, "This is a suggestion", "I suggest to accept this task.", 6, sandra));
        SuggestedTask suggestedTask2 = suggestedTaskService.createNewSuggestedTask(new SuggestedTask(project, "This is another suggestion", "I suggest to accept this task.", 2, brigitte));

    }

    public void secondProject() {
        // create list of users
        User petra = userService.findByName("petra");
        User sandra = userService.findByName("sandra");
        User brigitte = userService.findByName("brigitte");
        User conny = userService.findByName("conny");
        User student = userService.findByName("student");
        List<User> users = new ArrayList<>(Arrays.asList(petra, sandra, brigitte, conny));


        // create project
        Project project = projectService.createNewProject(new Project("Launch a health program"));
        projectService.addUserToProject(new UpdateProjectForm(users), project.getId());


        // create tasks
        Task task1 = taskService.createNewTask(new Task(sandra, "Find a good fitness club to cooperate with", "We want our employees to become member of a fitness club. In order to make this more attractive find a club we can cooperate with.", 3, "Planned", "2020-12-28", project), project.getId());
        taskService.setAssignee(task1.getId(), sandra);
        bookingService.bookTime(task1.getId(), project.getId(), 1, "2020-06-28", sandra);
        bookingService.bookTime(task1.getId(), project.getId(), 4, "2020-06-30", sandra);

        Task task2 = taskService.createNewTask(new Task(brigitte, "Organize a company sports event", "Sport brings people together and is lots of fun. Organize a sports event", 3, "Done", null, project), project.getId());
        taskService.setAssignee(task2.getId(), brigitte);
        bookingService.bookTime(task2.getId(), project.getId(), 1, "2020-07-09", brigitte);
        bookingService.bookTime(task2.getId(), project.getId(), 1, "2020-07-11", brigitte);

        Task task3 = taskService.createNewTask(new Task(conny, "Find a good yoga teacher", "The latest employee survey found that 47% of our employees would like to do yoga at work. Let´s find a good yoga teacher.", 2, "Work in progress", "2020-11-03", project), project.getId());
        taskService.setAssignee(task3.getId(), conny);
        bookingService.bookTime(task3.getId(), project.getId(), 3, "2020-07-09", student);

        Task task4 = taskService.createNewTask(new Task(sandra, "Department breakfasts", "Each department has the possibility to have breakfast together (once a month). Promote this.", 6, "Planned", "2021-02-28", project), project.getId());
        taskService.setAssignee(task4.getId(), sandra);

        Task task5 = taskService.createNewTask(new Task(student, "Partner health insurance", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.", 6, "Idea", "2020-10-20", project), project.getId());
        taskService.setAssignee(task5.getId(), student);

        Task task6 = taskService.createNewTask(new Task(brigitte, "Company sports group program", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.", 3, "Idea", "2020-09-10", project), project.getId());


        // create task suggestions
        SuggestedTask suggestedTask1 = suggestedTaskService.createNewSuggestedTask(new SuggestedTask(project, "Bonus program for sportive activities", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.", 6, sandra));
        SuggestedTask suggestedTask2 = suggestedTaskService.createNewSuggestedTask(new SuggestedTask(project, "Write a sports blog", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.", 2, brigitte));
    }

}