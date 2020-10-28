package de.hsba.bi.projectwork.userTest;


import de.hsba.bi.projectwork.user.UserService;
import de.hsba.bi.projectwork.web.exception.UserAlreadyExistException;
import de.hsba.bi.projectwork.web.user.RegisterUserForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class RegisterTest {

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mvc;


    @Test
    void registerUser() throws UserAlreadyExistException {
        RegisterUserForm registerUserForm = new RegisterUserForm("nadine", "passwortpasswort", "passwortpasswort");
        userService.createUser(registerUserForm, "DEVELOPER");
        assertThat(userService.findByName("nadine")).isNotNull();
    }


/*

    //Spring Security issues
    @Test
    void ChangePassword() {

        //TO-DO Log-In definieren

        ChangePasswordForm changePasswordForm = new ChangePasswordForm("oldpassword12", "password1234", "password1234");
        // Set a password for ChangePasswordForm
        //changePasswordForm.setoldpassword()= "oldpasswort";
        //Excecute changePassword
        userService.changePassword(changePasswordForm);
        //Get user information from userRepository
        User user = userService.findByName("ina");
        //check if password was changed to the new set password
        assertThat(userService.oldPasswordIsCorrect("passwort1234",user.getPassword());
    }


    @Test
    void registerUserController() throws Exception {
        JSONObject user = new JSONObject();
        user.put("name", "james");
        user.put("password", "testtesttesttest");
        user.put("matchingPassword", "testtesttesttest");
        String json = user.toString();

        mvc.perform(post("/register")
                .contentType(APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk());*/
}


