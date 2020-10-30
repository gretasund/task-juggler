package de.hsba.bi.projectwork.web;

import de.hsba.bi.projectwork.project.ProjectService;
import de.hsba.bi.projectwork.user.UserService;
import de.hsba.bi.projectwork.web.project.ProjectForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping("/userAdmin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;
    private final ProjectService projectService;

    // dashboard
    @GetMapping
    public String index(Model model) {
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("users", userService.findAll());
        return "user/dashboard";
    }


    // view users
    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/admin/users";
    }

    @PostMapping("/changeRole")
    public String changeRole(@RequestParam("id") Long id, @RequestParam(value = "role", required = false) String role) {
        userService.changeRole(id, role);
        return "redirect:/userAdmin/users";
    }


    // projects
    @GetMapping("/projects")
    public String projects(@RequestParam(value="edited", required=false) boolean edited, @RequestParam(value="edited", required=false) boolean added, Model model) {
        if(added) {
            model.addAttribute("message", "Project successfully added.");
        }
        if(edited) {
            model.addAttribute("message", "Project successfully edited.");
        }
        model.addAttribute("projects", projectService.findAll());
        return "user/projects";
    }

    @GetMapping("/editProject/{projectId}")
    public String editProject(@PathVariable("projectId") Long projectId, Model model) {
        model.addAttribute("projectForm", new ProjectForm(projectService.findById(projectId)));
        model.addAttribute("users", userService.findAll());
        return "user/admin/editProject";
    }

    @PostMapping("/editProject")
    public String editProject(@ModelAttribute("projectForm") @Valid ProjectForm projectForm, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            projectService.editProject(projectForm);
            return "redirect:/userAdmin/projects?edited=true";
        }
        model.addAttribute("projectForm", projectForm);
        model.addAttribute("users", userService.findAll());
        return "user/admin/addProject";
    }

    @GetMapping("/addProject")
    public String addTask(Model model) {
        model.addAttribute("projectForm", new ProjectForm());
        model.addAttribute("users", userService.findAll());
        return "user/admin/addProject";
    }

    @PostMapping("/addProject")
    public String createNewProject(@ModelAttribute("projectForm") @Valid ProjectForm projectForm, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            projectService.addProject(projectForm);
            return "redirect:/userAdmin/projects?created=true";
        }
        model.addAttribute("ProjectForm", projectForm);
        model.addAttribute("users", userService.findAll());
        return "user/admin/addProject";
    }

}