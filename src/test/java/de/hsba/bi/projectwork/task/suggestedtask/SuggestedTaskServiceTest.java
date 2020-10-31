package de.hsba.bi.projectwork.task.suggestedtask;

import de.hsba.bi.projectwork.ProjectWorkApplication;
import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.project.ProjectService;
import de.hsba.bi.projectwork.task.AbstractTask;
import de.hsba.bi.projectwork.task.acceptedtask.AcceptedTask;
import de.hsba.bi.projectwork.task.acceptedtask.AcceptedTaskService;
import de.hsba.bi.projectwork.user.User;
import de.hsba.bi.projectwork.user.UserService;
import de.hsba.bi.projectwork.web.project.ProjectForm;
import de.hsba.bi.projectwork.web.task.suggestedtask.SuggestedTaskForm;
import de.hsba.bi.projectwork.web.user.RegisterUserForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
    void ShouldBeAbleToFindUnassignedUnscheduledTasks() {
        //create User
        userService.createUser(new RegisterUserForm("testUser", "1234567890", "1234567890"), User.Role.DEVELOPER);
        User testUser = userService.findByName("testUser");
        //create Project
        Project project= projectService.addProject(new ProjectForm("Test Project1", null));
        //Create Suggested Task1
        SuggestedTask suggestedTask1 = suggestedTaskService.addSuggestedTask(new SuggestedTaskForm(project, "Suggested Task Nr1", "This is a Suggested Task", 6, testUser));
        assertThat(suggestedTaskService.findSuggestedTaskByCreator(testUser).size()==1);
        //Get Task ID
        Long TaskID = suggestedTask1.getId();
        //Accept Task
        AbstractTask task= suggestedTaskService.evaluateSuggestedTask(TaskID,"accept");
        assertThat(task).isNotNull();
        assertThat(task.getClass().isInstance(AcceptedTask.class));
        assertFalse(task.getClass().isInstance(SuggestedTask.class));
    }
}