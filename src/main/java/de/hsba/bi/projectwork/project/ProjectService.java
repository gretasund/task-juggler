package de.hsba.bi.projectwork.project;

import de.hsba.bi.projectwork.user.User;
import de.hsba.bi.projectwork.user.UserService;
import de.hsba.bi.projectwork.web.project.ProjectForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;


    // find projects
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project findById(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        return project.orElse(null);
    }
    public Project findByName(String name) {
        Optional<Project> project = projectRepository.findByName(name);
        return project.orElse(null);
    }


    // find projects by user
    public List<Project> findProjectsByUser() {
        // load current user
        User user = userService.findCurrentUser();

        // get all projects and users
        List<Project> allProjects = this.findAll();
        List<Project> usersProjects = new ArrayList<>();

        // find projects belonging to the logged in user
        for (Project project : allProjects) {
            if (project.getMembers().contains(user)) {
                usersProjects.add(project);
            }
        }
        return usersProjects;
    }


    // add project methods
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    public Project addProject(ProjectForm projectForm) {
        // TODO Als Admin kann ich ein neues Projekt anlegen
        Project project = new Project();

        if (projectForm.getMembers() == null) {
            project.setMembers(new ArrayList<>());
        }
        else{
            project.setMembers(projectForm.getMembers());
        }
        project.setName(projectForm.getName());

        return this.save(project);
    }


    // edit methods
    public void editProject(ProjectForm projectForm) {
        // TODO Als Admin kann ich andere Nutzer (jeder Rolle) zu einem Projekt hinzufügen
        // TODO Als Admin kann ich andere Nutzer (jeder Rolle) aus einem Projekt entfernen
        Project project = this.findById(projectForm.getId());

        project.setName(projectForm.getName());
        project.setMembers(projectForm.getMembers());

        this.save(project);
    }

}
