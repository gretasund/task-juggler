package de.hsba.bi.projectwork.user;

import de.hsba.bi.projectwork.ProjectWorkApplication;
import de.hsba.bi.projectwork.project.ProjectService;
import de.hsba.bi.projectwork.task.acceptedtask.AcceptedTaskService;
import de.hsba.bi.projectwork.task.suggestedtask.SuggestedTaskService;
import de.hsba.bi.projectwork.web.exception.UserAlreadyExistException;
import de.hsba.bi.projectwork.web.user.RegisterUserForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

//Newly added feature that forces reset
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(classes = ProjectWorkApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    private javax.validation.Validator validator;

    @Autowired
    ProjectService projectService;

    @Autowired
    AcceptedTaskService acceptedTaskService;

    @Autowired
    SuggestedTaskService suggestedTaskService;

    @BeforeEach
    void setUp() {
        //Create Admin
        userService.createUser(new RegisterUserForm("dora", "1234567890", "1234567890"), User.Role.ADMIN);
        //Create developer1
        userService.createUser(new RegisterUserForm("ben", "1234567890", "1234567890"), User.Role.DEVELOPER);
        //Create Manager
        userService.createUser(new RegisterUserForm("max", "1234567890", "1234567890"), User.Role.MANAGER);
    }

    @Test
    void PasswordIsTooShort(){
        //Create user with a shorter Password than allowed
        RegisterUserForm registerUserForm = new RegisterUserForm("ShortPasswordUser", "123", "123");
        //validate registerUserForm
        Set<ConstraintViolation<RegisterUserForm>> violations = validator.validate(registerUserForm);
        // if registerUserForm doesn´t have errors, create the user
        if(violations.isEmpty()) {
            userService.createUser(registerUserForm, User.Role.DEVELOPER);
        }
        assertFalse(violations.isEmpty());
        assertThat(userService.findByName("ShortPasswordUser")).isNull();
    }

    @Test
    void UsernameIsTooShort(){
        //Create user with a shorter Password than allowed
        RegisterUserForm registerUserForm = new RegisterUserForm("SU", "1234567890", "1234567890");
        //validate registerUserForm
        Set<ConstraintViolation<RegisterUserForm>> violations = validator.validate(registerUserForm);
        // if registerUserForm doesn´t have errors, create the user
        if(violations.isEmpty()) {
            userService.createUser(registerUserForm, User.Role.DEVELOPER);
        }
        assertFalse(violations.isEmpty());
        assertThat(userService.findByName("SU")).isNull();
    }

    @Test
    void UserExists() throws UserAlreadyExistException {
        //creates user with an existing name
        RegisterUserForm registerUserForm = new RegisterUserForm("dora", "passwortpasswort", "passwortpasswort");
        userService.createUser(registerUserForm, User.Role.DEVELOPER);

    }

    @Test
    void ShouldDetectFalsePassword(){
        User ina = userService.findByName("dora");
        assertThat(userService.oldPasswordIsCorrect("FalsePassword", ina)).isFalse();
    }

    @Test
    void ShouldBeAbleToChangeRole() {
        userService.createUser(new RegisterUserForm("ChangeRoleUser", "1234567890", "1234567890"), User.Role.DEVELOPER);;
        // Find user and userId
        User changeRoleUser = userService.findByName("ChangeRoleUser");
        long UserID = changeRoleUser.getId();
        // Check if dora is a developer
        assertThat(changeRoleUser.getRole()).isEqualTo(User.Role.DEVELOPER);
        System.out.println(changeRoleUser.getRole());
        //Change Role
        userService.changeRole(UserID, "Manager");
        System.out.println(changeRoleUser.getRole());
        // Check dora´s role
        assertThat(changeRoleUser.getRole()).isEqualTo(User.Role.MANAGER);
    }

    /*@Test
    public void UserExists() {
        RegisterUserForm registerUserForm = new RegisterUserForm("dora", "passwortpasswort", "passwortpasswort");
        UserAlreadyExistException e = assertThrows(
                UserAlreadyExistException.class, userService.createUser(registerUserForm, User.Role.DEVELOPER));

    }*/
}