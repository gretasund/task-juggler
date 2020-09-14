package de.hsba.bi.projectwork.web;

import de.hsba.bi.projectwork.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/userDeveloper")
@PreAuthorize("hasRole('ROLE_DEVELOPER')")
@RequiredArgsConstructor
public class UserDeveloperController {

    private final UserService userService;


    // dashboard
    @GetMapping
    public String index() {
        return "userDeveloper/index";
    }

}