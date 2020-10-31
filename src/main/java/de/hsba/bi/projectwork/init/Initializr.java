package de.hsba.bi.projectwork.init;

import de.hsba.bi.projectwork.project.ProjectService;
import de.hsba.bi.projectwork.user.User;
import de.hsba.bi.projectwork.user.UserService;
import de.hsba.bi.projectwork.web.user.RegisterUserForm;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class Initializr {

    private final UserService userService;
    private final ProjectService projectService;
    private final OnlyUsers onlyUsers;
    private final OnlyProjects onlyProjects;
    private final ApplicationInUse applicationInUse;

    @EventListener(ApplicationStartedEvent.class)
    public void init() {

        // creates admin user
        if(userService.findByName("admin1") == null) {
            userService.createUser(new RegisterUserForm("admin1", "1234567890", "1234567890"), User.Role.ADMIN);
        }

        // prevent initialisation if DB is not empty
        if (userService.findAll().size()<=1 && !projectService.findAll().isEmpty()) {
            return;
        }

        // choose where which data should be initialized
        applicationInUse.init();
        onlyProjects.createProjects();
        onlyUsers.createUsers();

    }

}
