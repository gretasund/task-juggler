package de.hsba.bi.projectwork.init;

import de.hsba.bi.projectwork.booking.BookingService;
import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.project.ProjectService;
import de.hsba.bi.projectwork.task.acceptedtask.AcceptedTask;
import de.hsba.bi.projectwork.task.acceptedtask.AcceptedTaskService;
import de.hsba.bi.projectwork.task.suggestedtask.SuggestedTask;
import de.hsba.bi.projectwork.task.suggestedtask.SuggestedTaskService;
import de.hsba.bi.projectwork.user.User;
import de.hsba.bi.projectwork.user.UserService;
import de.hsba.bi.projectwork.web.project.ProjectForm;
import de.hsba.bi.projectwork.web.task.suggestedtask.SuggestedTaskForm;
import de.hsba.bi.projectwork.web.user.RegisterUserForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ApplicationInUse {

    private final UserService userService;
    private final ProjectService projectService;
    private final AcceptedTaskService acceptedTaskService;
    private final BookingService bookingService;
    private final SuggestedTaskService suggestedTaskService;


    public void createUsers() {
        // create manager (head of the department)
        userService.createUser(new RegisterUserForm("petra", "1234567890", "1234567890"), User.Role.MANAGER);

        // create project members
        userService.createUser(new RegisterUserForm("sandra", "1234567890", "1234567890"), User.Role.DEVELOPER);
        userService.createUser(new RegisterUserForm("brigitte", "1234567890", "1234567890"), User.Role.DEVELOPER);
        userService.createUser(new RegisterUserForm("conny", "1234567890", "1234567890"), User.Role.DEVELOPER);
        userService.createUser(new RegisterUserForm("student", "1234567890", "1234567890"), User.Role.DEVELOPER);
    }

    public void firstProject() {
        // create list of users
        User petra = userService.findByName("petra");
        User sandra = userService.findByName("sandra");
        User brigitte = userService.findByName("brigitte");
        User conny = userService.findByName("conny");
        List<User> users = new ArrayList<>(Arrays.asList(petra, sandra, brigitte, conny));


        // create project
        Project project = projectService.addProject(new ProjectForm("Amazing HR Strategy", users));


        // create tasks
        AcceptedTask acceptedTask1 = acceptedTaskService.addTask(new AcceptedTask(sandra, "Create a Company Page on Facebook", "In order to attract possible applicants our company has to be introduced on social media. A facebook page has to be created and filled with content.", 3, AcceptedTask.Status.TESTING, LocalDate.now().plusDays(55), project), project.getId());
        acceptedTaskService.setAssignee(acceptedTask1.getId(), sandra);
        bookingService.addBooking(acceptedTask1.getId(), project.getId(), 1, "2020-06-28", conny);
        bookingService.addBooking(acceptedTask1.getId(), project.getId(), 4, "2020-06-30", brigitte);
        bookingService.addBooking(acceptedTask1.getId(), project.getId(), 1, "2020-06-28", sandra);
        bookingService.addBooking(acceptedTask1.getId(), project.getId(), 1, "2020-06-30", sandra);

        AcceptedTask acceptedTask2 = acceptedTaskService.addTask(new AcceptedTask(brigitte, "Create a Company Page on Instagram", "In order to attract possible applicants our company has to be introduced on social media. An instagram page has to be created an filled with content.", 3, AcceptedTask.Status.DONE, null, project), project.getId());
        acceptedTaskService.setAssignee(acceptedTask2.getId(), brigitte);
        bookingService.addBooking(acceptedTask2.getId(), project.getId(), 1, "2020-07-09", brigitte);
        bookingService.addBooking(acceptedTask2.getId(), project.getId(), 1, "2020-07-11", brigitte);

        AcceptedTask acceptedTask3 = acceptedTaskService.addTask(new AcceptedTask(conny,"List All Job Fairs in the Region", "We want to meet people looking for a job! This is why we want to visit some job fairs that are visited by possible applicants. Please create a list of all job fairs within a radius of 30km.", 2, AcceptedTask.Status.WORK_IN_PROGRESS, null, project), project.getId());
        acceptedTaskService.setAssignee(acceptedTask2.getId(), conny);
        bookingService.addBooking(acceptedTask3.getId(), project.getId(), 3, "2020-07-09", conny);

        AcceptedTask acceptedTask4 = acceptedTaskService.addTask(new AcceptedTask(sandra, "Design an Employee-Survey", "We want to know how satisfied our current employees are and what we can do to make them even happier. To gain the necessary insights please plan a survey that will later be send to all employees.", 6, AcceptedTask.Status.ACCEPTED, LocalDate.now().plusDays(5), project), project.getId());
        acceptedTaskService.setAssignee(acceptedTask4.getId(), sandra);

        AcceptedTask acceptedTask5 = acceptedTaskService.addTask(new AcceptedTask(sandra, "Organize a Summer Party", "For staff retention reasons we will be holding a summer party again this year!", 6, AcceptedTask.Status.ACCEPTED, LocalDate.now().plusDays(2), project), project.getId());
        acceptedTaskService.setAssignee(acceptedTask5.getId(), sandra);

        AcceptedTask acceptedTask6 = acceptedTaskService.addTask(new AcceptedTask(conny, "Promote the Employee Referral Program", "95% of all applicants that apply for one of our jobs because of a referral are successful but only <1% of all employees know about the program. Please create a page in the intranet and promote it!", 3, AcceptedTask.Status.ACCEPTED, LocalDate.now().plusDays(4), project), project.getId());

        // create task suggestions
        SuggestedTask suggestedTask1 = suggestedTaskService.addSuggestedTask(new SuggestedTaskForm(project, "This is a suggestion", "I suggest to accept this task.", 6, sandra));
        SuggestedTask suggestedTask2 = suggestedTaskService.addSuggestedTask(new SuggestedTaskForm(project, "This is another suggestion", "I suggest to accept this task.", 2, brigitte));

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
        Project project = projectService.addProject(new ProjectForm("Launch a health program", users));


        // create tasks
        AcceptedTask acceptedTask1 = acceptedTaskService.addTask(new AcceptedTask(sandra, "Find a good fitness club to cooperate with", "We want our employees to become member of a fitness club. In order to make this more attractive find a club we can cooperate with.", 3, AcceptedTask.Status.ACCEPTED, LocalDate.now().plusDays(18), project), project.getId());
        acceptedTaskService.setAssignee(acceptedTask1.getId(), sandra);
        bookingService.addBooking(acceptedTask1.getId(), project.getId(), 1, "2020-06-28", sandra);
        bookingService.addBooking(acceptedTask1.getId(), project.getId(), 4, "2020-06-30", sandra);

        AcceptedTask acceptedTask2 = acceptedTaskService.addTask(new AcceptedTask(brigitte, "Organize a company sports event", "Sport brings people together and is lots of fun. Organize a sports event", 3, AcceptedTask.Status.DONE, null, project), project.getId());
        acceptedTaskService.setAssignee(acceptedTask2.getId(), brigitte);
        bookingService.addBooking(acceptedTask2.getId(), project.getId(), 1, "2020-07-09", brigitte);
        bookingService.addBooking(acceptedTask2.getId(), project.getId(), 1, "2020-07-11", brigitte);

        AcceptedTask acceptedTask3 = acceptedTaskService.addTask(new AcceptedTask(conny, "Find a good yoga teacher", "The latest employee survey found that 47% of our employees would like to do yoga at work. Let´s find a good yoga teacher.", 2, AcceptedTask.Status.WORK_IN_PROGRESS, LocalDate.now().plusDays(-11), project), project.getId());
        acceptedTaskService.setAssignee(acceptedTask3.getId(), conny);
        bookingService.addBooking(acceptedTask3.getId(), project.getId(), 3, "2020-07-09", student);

        AcceptedTask acceptedTask4 = acceptedTaskService.addTask(new AcceptedTask(sandra, "Department breakfasts", "Each department has the possibility to have breakfast together (once a month). Promote this.", 6, AcceptedTask.Status.ACCEPTED, LocalDate.now().plusDays(67), project), project.getId());
        acceptedTaskService.setAssignee(acceptedTask4.getId(), sandra);

        AcceptedTask acceptedTask5 = acceptedTaskService.addTask(new AcceptedTask(student, "Partner health insurance", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.", 6, AcceptedTask.Status.ACCEPTED, LocalDate.now().plusDays(81), project), project.getId());
        acceptedTaskService.setAssignee(acceptedTask5.getId(), student);

        AcceptedTask acceptedTask6 = acceptedTaskService.addTask(new AcceptedTask(brigitte, "Company sports group program", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.", 3, AcceptedTask.Status.ACCEPTED, LocalDate.now().plusDays(0), project), project.getId());


        // create task suggestions
        SuggestedTask suggestedTask1 = suggestedTaskService.addSuggestedTask(new SuggestedTaskForm(project, "Bonus program for sportive activities", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.", 6, sandra));
        SuggestedTask suggestedTask2 = suggestedTaskService.addSuggestedTask(new SuggestedTaskForm(project, "Write a sports blog", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.", 2, brigitte));

    }

    public void emptyProjects() {
        // create projects
        Project project1 = projectService.addProject(new ProjectForm("Project without users 1", null));
        Project project2 = projectService.addProject(new ProjectForm("Project without users 2", null));
        Project project3 = projectService.addProject(new ProjectForm("Project without users 3", null));
        Project project4 = projectService.addProject(new ProjectForm("Project without users 4", null));

    }

    public void usersWithoutProjects() {
        userService.createUser(new RegisterUserForm("sean", "1234567890", "1234567890"), User.Role.DEVELOPER);
        userService.createUser(new RegisterUserForm("hannah", "1234567890", "1234567890"), User.Role.DEVELOPER);
        userService.createUser(new RegisterUserForm("michelle", "1234567890", "1234567890"), User.Role.DEVELOPER);
        userService.createUser(new RegisterUserForm("nico", "1234567890", "1234567890"), User.Role.DEVELOPER);
        userService.createUser(new RegisterUserForm("birgit", "1234567890", "1234567890"), User.Role.MANAGER);
        userService.createUser(new RegisterUserForm("franz", "1234567890", "1234567890"), User.Role.MANAGER);
        userService.createUser(new RegisterUserForm("thomas", "1234567890", "1234567890"), User.Role.MANAGER);
        userService.createUser(new RegisterUserForm("bernd", "1234567890", "1234567890"), User.Role.MANAGER);
    }


}