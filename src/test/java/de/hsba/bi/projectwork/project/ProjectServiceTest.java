package de.hsba.bi.projectwork.project;

import de.hsba.bi.projectwork.ProjectWorkApplication;
import de.hsba.bi.projectwork.task.acceptedtask.AcceptedTaskService;
import de.hsba.bi.projectwork.task.suggestedtask.SuggestedTaskService;
import de.hsba.bi.projectwork.user.User;
import de.hsba.bi.projectwork.user.UserService;
import de.hsba.bi.projectwork.web.project.ProjectForm;
import de.hsba.bi.projectwork.web.user.RegisterUserForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//Newly added feature that forces reset
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(classes = ProjectWorkApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProjectServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    @Autowired
    AcceptedTaskService acceptedTaskService;

    @Autowired
    SuggestedTaskService suggestedTaskService;

    @Autowired
    ProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
        userService.createUser(new RegisterUserForm("greta", "1234567890", "1234567890"), User.Role.DEVELOPER);
        userService.createUser(new RegisterUserForm("timo", "1234567890", "1234567890"), User.Role.MANAGER);
        userService.createUser(new RegisterUserForm("ina", "1234567890", "1234567890"), User.Role.DEVELOPER);
        userService.createUser(new RegisterUserForm("testUser", "1234567890", "1234567890"), User.Role.DEVELOPER);
    }

    @Test
    void ShouldNotAddProject() {
        //Name of Project is too short
        projectService.addProject(new ProjectForm("Test", null));
        assertThat(projectService.findByName("Test") == null);
    }
    @Test
    void ShouldBeAbleToAddProjects() {
        projectService.addProject(new ProjectForm("Test Project3", null));
        assertThat(projectService.findByName("Test Project3")).isNotNull();
    }

    @Test
    void ShouldBeAbleToFindByUser() {
        User greta = userService.findByName("greta");
        User timo = userService.findByName("timo");
        User ina = userService.findByName("ina");

        Project testProject1= projectService.addProject(new ProjectForm("Test Project1", null));
        Project testProject2= projectService.addProject(new ProjectForm("Test Project1", null));

        //add Users to Projects
        List<User> users1 = new ArrayList<>(Arrays.asList(ina, greta, timo));
        List<User> users2 = new ArrayList<>(Arrays.asList(ina, timo));

        ProjectForm testProject1Form= new ProjectForm(testProject1);
        testProject1Form.setMembers(users1);
        projectService.editProject(testProject1Form);

        ProjectForm testProject2Form= new ProjectForm(testProject2);
        testProject2Form.setMembers(users2);
        projectService.editProject(testProject2Form);

        //findProjectByUser()
        List<Project> allProjects = projectService.findAll();
        //list with ina's Projects
        List<Project> inaProjects = new ArrayList<>();
        //list with greta's Projects
        List<Project> gretaProjects = new ArrayList<>();

        for (Project project : allProjects) {
            if (project.getMembers().contains(ina)) {
                inaProjects.add(project);
            }
            if(project.getMembers().contains(greta)) {
                gretaProjects.add(project);
            }
        }
        assertThat(inaProjects.size()==2);
        assertThat(gretaProjects.size()==1);
    }

    @Test
    void ShouldBeAbleToAddAndRemoveFromProject() {
        // Create a list of users and add these to a Project
        User greta = userService.findByName("greta");
        User timo = userService.findByName("timo");
        List<User> users = new ArrayList<>(Arrays.asList(timo, greta));
        //Create new Project
        Project testProject1= projectService.addProject(new ProjectForm("Test Project1", null));
        //Add users to the new project form and edit project
        ProjectForm testProject1Form= new ProjectForm(testProject1);
        testProject1Form.setMembers(users);
        projectService.editProject(testProject1Form);

        //Check that the project now has 2 members
        assertThat(testProject1.getMembers().size()).isEqualTo(2);
    }
}

