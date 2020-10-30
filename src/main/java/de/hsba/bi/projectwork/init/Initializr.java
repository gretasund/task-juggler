package de.hsba.bi.projectwork.init;

import de.hsba.bi.projectwork.project.ProjectService;
import de.hsba.bi.projectwork.user.UserService;
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

        // prevent initialisation if DB is not empty
        if (!userService.findAll().isEmpty() && !projectService.findAll().isEmpty()) {
            return;
        }

        onlyUsers.createUsers();
        applicationInUse.createUsers();
        applicationInUse.firstProject();
        applicationInUse.secondProject();
        applicationInUse.emptyProjects();

    }

}
