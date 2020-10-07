package de.hsba.bi.projectwork.project;

import de.hsba.bi.projectwork.user.User;
import de.hsba.bi.projectwork.user.UserService;
import de.hsba.bi.projectwork.web.project.UpdateProjectForm;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;

    public ProjectService(ProjectRepository projectRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
    }

    public Project createNewProject(Project project) {
        // TODO Als Admin kann ich ein neues Projekt anlegen
        if (project.getMembers() == null) {
            project.setMembers(new ArrayList<>());
        }
        projectRepository.save(project);
        return project;
    }

    public void save(Project project) {
        projectRepository.save(project);
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project findById(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        return project.orElse(null);
    }

    public List<Project> findUsersProjects() {
        User user = userService.findCurrentUser();
        List<Project> allProjects = projectRepository.findAll();
        List<Project> usersProjects = new ArrayList<>();

        // find projects belonging to the logged in user
        for (Project project : allProjects) {
            if (project.getMembers().contains(user)) {
                usersProjects.add(project);
            }
        }
        return usersProjects;
    }

    public List<User> findUsersNotInProject(Long projectId) {
        Project project = this.findById(projectId);
        List<User> usersNotInProject = new ArrayList<>(userService.findAll());
        usersNotInProject.removeAll(project.getMembers());
        return usersNotInProject;
    }

    public void addUserToProject(UpdateProjectForm updateProjectForm, Long projectId) {
        // TODO Als Admin kann ich andere Nutzer (jeder Rolle) zu meinem Projekt hinzufügen
        List<User> newUsers = updateProjectForm.getNewUsers();
        List<User> projectMembers = this.findById(projectId).getMembers();
        Project project = this.findById(projectId);

        for (User newUser : newUsers) {
            if (!projectMembers.contains(newUser)) {
                project.getMembers().add(newUser);
                newUser.getProjects().add(project);
                userService.save(newUser);
            }
            this.createNewProject(project);
        }
    }

    public void addUserToProject(List<User> newUsers, Long projectId) {
        // TODO Als Admin kann ich andere Nutzer (jeder Rolle) zu meinem Projekt hinzufügen
        List<User> projectMembers = this.findById(projectId).getMembers();
        Project project = this.findById(projectId);

        for (User newUser : newUsers) {
            if (!projectMembers.contains(newUser) && !newUser.getProjects().contains(project)) {
                // link user in project
                project.getMembers().add(newUser);

                // link project in user
                newUser.getProjects().add(project);

                // persist entities I
                userService.save(newUser);
            }
        }
        // persist entities II
        projectRepository.save(project);
    }

    public void removeUserFromProject(UpdateProjectForm updateProjectForm, Long projectId) {
        // TODO Als Admin kann ich andere Nutzer (jeder Rolle) zu meinem Projekt hinzufügen
        List<User> removeUsers = updateProjectForm.getNewUsers();
        List<User> projectMembers = this.findById(projectId).getMembers();
        Project project = this.findById(projectId);

        for (User newUser : removeUsers) {
            if (projectMembers.contains(newUser)) {
                project.getMembers().remove(newUser);
                newUser.getProjects().remove(project);
                userService.save(newUser);
            }
        }
    }

}
