package de.hsba.bi.projectwork.web;

import de.hsba.bi.projectwork.user.User;
import de.hsba.bi.projectwork.user.UserService;
import de.hsba.bi.projectwork.web.exception.IncorrectPasswordException;
import de.hsba.bi.projectwork.web.exception.UserAlreadyExistException;
import de.hsba.bi.projectwork.web.user.ChangePasswordForm;
import de.hsba.bi.projectwork.web.user.RegisterUserForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexController {

    private final UserService userService;


    @GetMapping("/dashboard")
    public String index(Model model) {
        model.addAttribute("authenticatedUser", userService.findCurrentUser());
        model.addAttribute("user", new User());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        switch (auth.getAuthorities().toString()) {
            case "[ROLE_ADMIN]":
                return "redirect:/userAdmin";
            case "[ROLE_DEVELOPER]":
                return "redirect:/userDeveloper";
            case "[ROLE_MANAGER]":
                return "redirect:/userManager";
        }
        return "index";
    }

    @GetMapping("/home")
    public String dashboard() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth instanceof AnonymousAuthenticationToken ? "login" : "redirect:/dashboard";
    }


    // Als Nutzer kann ich mich registrieren und bekomme standardmäßig die Rolle "Entwickler"
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new RegisterUserForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") @Valid RegisterUserForm registerUserForm, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            try {
                userService.createUser(registerUserForm, "DEVELOPER");
                model.addAttribute("user", registerUserForm);
                model.addAttribute("message", "You've successfully registered.");
                return "login";
            } catch (UserAlreadyExistException uaeEx) {
                model.addAttribute("user", registerUserForm);
                model.addAttribute("message", uaeEx.getMessage());
                return "register";
            }
        }
        model.addAttribute("user", registerUserForm);
        return "register";
    }


    // Als Nutzer kann ich mein Passwort ändern
    @GetMapping("/account")
    public String account(Model model) {
        model.addAttribute("user", userService.findCurrentUser());
        model.addAttribute("changePasswordForm", new ChangePasswordForm());
        return "account";
    }


    @PreAuthorize("hasRole(authenticated)")
    @PostMapping("/changePassword")
    public String changePassword(@ModelAttribute("changePasswordForm") @Valid ChangePasswordForm changePasswordForm, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            try {
                ChangePasswordForm changedPassword = userService.changePassword(changePasswordForm);
                model.addAttribute("user", userService.findCurrentUser());
                model.addAttribute("changePasswordForm", changePasswordForm);
                model.addAttribute("successMessage", "You've successfully changed your password.");
                return "account";
            } catch (IncorrectPasswordException ipEx) {
                model.addAttribute("user", userService.findCurrentUser());
                model.addAttribute("changePasswordForm", changePasswordForm);
                model.addAttribute("errorMessage", "The old password you entered is incorrect.");
                return "account";
            }
        }
        model.addAttribute("user", userService.findCurrentUser());
        model.addAttribute("changePasswordForm", changePasswordForm);
        return "account";
    }

}
