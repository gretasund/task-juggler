package de.hsba.bi.projectwork.web.task;

import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.user.User;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Data
public class SuggestedTaskForm {

    @NotNull(message = "Please enter a name.")
    private String name;

    @NotEmpty(message = "Please enter a description.")
    private String description;

    @NotNull(message = "Please enter an estimation.")
    private int estimation;

    private Project project;

    private String status;

    private User creator;

    private LocalDate creationDate;


    public SuggestedTaskForm(Project project, User creator) {
        this.project = project;
        this.status = "Idea";
        this.creator = creator;
        this.creationDate = LocalDate.now();
    }

}
