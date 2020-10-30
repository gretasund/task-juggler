package de.hsba.bi.projectwork.web.project;

import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
public class ProjectForm {

    long id;

    @Size(min = 5, message = "The project name has to be at least 5 characters long.")
    @Size(max = 35, message = "The project name cannot be longer than 35 characters long.")
    private String name;

    List<User> members = new ArrayList<>();


    public ProjectForm(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.members = project.getMembers();
    }

    public ProjectForm(String name, List<User> users) {
        this.name = name;

        // set due date if present
        if(users != null) {
            this.members = users;
        }
    }

    public ProjectForm(List<User> users) {
        // set due date if present
        if(users != null) {
            this.members = users;
        }
    }

}
