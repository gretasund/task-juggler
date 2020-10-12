package de.hsba.bi.projectwork.taskTest;


import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.project.ProjectService;
import de.hsba.bi.projectwork.task.Task;
import de.hsba.bi.projectwork.task.TaskRepository;
import de.hsba.bi.projectwork.task.TaskService;
import de.hsba.bi.projectwork.user.User;
import de.hsba.bi.projectwork.user.UserService;
import de.hsba.bi.projectwork.web.exception.UserAlreadyExistException;
import de.hsba.bi.projectwork.web.user.RegisterUserForm;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@MockitoSettings
class TaskSortingTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;
    @Mock
    private ProjectService projectService;
    @Mock
    private TaskService taskService;



    //@BeforeEach
    @Test
    public void setup() {
        // create manager (head of the department)
        userService.createUser(new RegisterUserForm("petra", "1234567890", "1234567890"), "MANAGER");
        User petra = userService.findByName("petra");
        List<User> users = new ArrayList<>(Arrays.asList(petra));

        // create project
        Project project = projectService.createNewProject(new Project("Amazing HR Strategy"));
        //projectService.addUserToProject(new UpdateProjectForm(users), project.getId());

        // create tasks
        /*Task task1 = taskService.addNewTask(new Task(petra, "Create a Company Page on Facebook", "In order to attract possible applicants our company has to be introduced on social media. A facebook page has to be created and filled with content.", 3, "Testing", "2020-08-28"), project.getId());
        Task task2 = taskService.addNewTask(new Task(petra, "Create a Company Page on Instagram", "In order to attract possible applicants our company has to be introduced on social media. An instagram page has to be created an filled with content.", 3, "Done", null), project.getId());
        Task task3 = taskService.addNewTask(new Task(petra,"List All Job Fairs Taking Place in the Region", "We want to meet people looking for a job! This is why we want to visit some job fairs that are visited by possible applicants. Please create a list of all job fairs within a radius of 30km.", 2, "Work in progress", null), project.getId()); //"2020-09-03"
        Task task4 = taskService.addNewTask(new Task(petra, "Design an Employee-Survey", "We want to know how satisfied our current employees are and what we can do to make them even happier. To gain the necessary insights please plan a survey that will later be send to all employees.", 6, "Planned", "2020-08-28"), project.getId());
        Task task5 = taskService.addNewTask(new Task(petra, "Organize a Summer Party", "For staff retention reasons we will be holding a summer party again this year!", 6, "Idea", "2020-10-20"), project.getId());
        Task task6 = taskService.addNewTask(new Task(petra, "Promote the Employee Referral Program", "95% of all applicants that apply for one of our jobs because of a referral are successful but only <1% of all employees know about the program. Please create a page in the intranet and promote it!", 3, "Idea", "2020-09-10"), project.getId());*/
    }


    @Test
    void findAll() throws UserAlreadyExistException {

        List<Task> tasks = taskRepository.findAll();


    }

}
