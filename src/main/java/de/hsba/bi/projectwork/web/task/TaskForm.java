package de.hsba.bi.projectwork.web.task;

import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.user.User;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
public class TaskForm {

    @NotNull(message = "Please enter a name.")
    private String name;

    private User assignee;

    @NotEmpty(message = "Please enter a description.")
    private String description;

    @NotNull(message = "Please choose a status.")
    private String status;

    @NotNull(message = "Please enter an estimation.")
    private int estimation;

    private Project project;

}