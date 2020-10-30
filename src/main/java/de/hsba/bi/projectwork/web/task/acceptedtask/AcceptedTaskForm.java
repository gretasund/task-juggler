package de.hsba.bi.projectwork.web.task.acceptedtask;

import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.user.User;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
public class AcceptedTaskForm {

    @NotNull
    private long taskId;

    @Size(min = 5, message = "The task name has to be at least 5 characters long.")
    @Size(max = 50, message = "The task name cannot be longer than 50 characters.")
    private String name;

    @Size(min = 5, message = "The description has to be at least 15 characters long.")
    @Size(max = 2500, message = "The description cannot be longer than 2,500 characters long.")
    private String description;

    @Min(value = 1, message = "Please enter a whole number > 0.")
    @Max(value = 10000, message = "Please enter a whole number <= 10,000.")
    private int estimation;

    @NotNull(message = "Please choose a status.")
    private String status;

    @NotNull(message = "The task does not belong to a project.")
    private Project project;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private String dueDate;

    private User assignee;


    public AcceptedTaskForm(long taskId) {
        this.taskId = taskId;
    }

}