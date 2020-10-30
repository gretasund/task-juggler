package de.hsba.bi.projectwork.init;

import de.hsba.bi.projectwork.user.User;
import de.hsba.bi.projectwork.user.UserService;
import de.hsba.bi.projectwork.web.user.RegisterUserForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OnlyUsers {

    private final UserService userService;

    public void createUsers() {
        userService.createUser(new RegisterUserForm("admin", "1234567890", "1234567890"), User.Role.ADMIN);
        userService.createUser(new RegisterUserForm("manager", "1234567890", "1234567890"), User.Role.MANAGER);
        userService.createUser(new RegisterUserForm("developer", "1234567890", "1234567890"), User.Role.DEVELOPER);

    }

}