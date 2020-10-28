package de.hsba.bi.projectwork.init;

import de.hsba.bi.projectwork.user.User;
import de.hsba.bi.projectwork.user.UserService;
import de.hsba.bi.projectwork.web.user.RegisterUserForm;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AdminInitializr {

    private final UserService userService;


    @EventListener(ApplicationStartedEvent.class)
    public void init() {
        if (!userService.findAll().isEmpty()) {
            // prevent initialisation if DB is not empty
            return;
        }
        this.initAdmins();
    }

    public void initAdmins() {
        // create some admins
        userService.createUser(new RegisterUserForm("admin1", "1234567890", "1234567890"), User.Role.ADMIN);
        userService.createUser(new RegisterUserForm("admin2", "1234567890", "1234567890"), User.Role.ADMIN);
    }

}