package de.hsba.bi.projectwork;


import de.hsba.bi.projectwork.user.UserService;
import de.hsba.bi.projectwork.web.exception.UserAlreadyExistException;
import de.hsba.bi.projectwork.web.user.RegisterUserForm;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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


    @Test
    void registerUserController() throws Exception {
    /*    JSONObject user = new JSONObject();
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

}