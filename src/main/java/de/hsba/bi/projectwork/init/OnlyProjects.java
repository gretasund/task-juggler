package de.hsba.bi.projectwork.init;

import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.project.ProjectService;
import de.hsba.bi.projectwork.web.project.ProjectForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OnlyProjects {

    private final ProjectService projectService;

    public void createProjects() {
        Project project1 = projectService.addProject(new ProjectForm("Project1", null));
        Project project2 = projectService.addProject(new ProjectForm("Project2", null));
    }

}
