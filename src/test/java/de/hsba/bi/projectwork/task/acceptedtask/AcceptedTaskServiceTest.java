package de.hsba.bi.projectwork.task.acceptedtask;

import de.hsba.bi.projectwork.ProjectWorkApplication;
import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.project.ProjectService;
import de.hsba.bi.projectwork.task.suggestedtask.SuggestedTaskService;
import de.hsba.bi.projectwork.user.User;
import de.hsba.bi.projectwork.user.UserService;
import de.hsba.bi.projectwork.web.project.ProjectForm;
import de.hsba.bi.projectwork.web.user.RegisterUserForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

//Newly added feature that forces reset
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(classes = ProjectWorkApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    @Autowired
    AcceptedTaskService acceptedTaskService;

    @Autowired
    SuggestedTaskService suggestedTaskService;

    @Test
    void ShouldBeAbleToAssignTasks() {
        //create User Ina
        userService.createUser(new RegisterUserForm("ina", "1234567890", "1234567890"), User.Role.MANAGER);
        User ina = userService.findByName("ina");
        //create User Greta
        userService.createUser(new RegisterUserForm("greta", "1234567890", "1234567890"), User.Role.MANAGER);
        User greta = userService.findByName("greta");
        //create Project
        Project project1= projectService.addProject(new ProjectForm("Test Project1", null));
        //Create 2 additional tasks
        AcceptedTask task4 = acceptedTaskService.addTask(new AcceptedTask( ina, "Testing103", "content", 3, AcceptedTask.Status.TESTING, null, project1), project1.getId());
        AcceptedTask task5 = acceptedTaskService.addTask(new AcceptedTask( ina, "Testing103", "content", 3, AcceptedTask.Status.TESTING, null, project1), project1.getId());
        //2 previous tasks + 3 tasks from the setup
        assertThat(acceptedTaskService.findAll().size()).isEqualTo(3);
        //2 previous tasks + 3 tasks from the setup==> All Unassigned
        assertThat(acceptedTaskService.findUnassignedAndUnscheduled().size()==5);
        //Assign task4 to Greta
        Long Task4ID= task4.getId();
        acceptedTaskService.setAssignee(Task4ID, greta);
        //Only 4 unassigned Tasks left
        assertThat(acceptedTaskService.findUnassignedAndUnscheduled().size()==4);
        //Assign another task to greta
        Long Task5ID= task5.getId();
        acceptedTaskService.setAssignee(Task5ID, greta);
        //Greta should be assigned to 2 tasks
        assertThat(acceptedTaskService.findTaskByAssignee(greta).size()==1);
    }
}
