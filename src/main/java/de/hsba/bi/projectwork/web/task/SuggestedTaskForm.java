package de.hsba.bi.projectwork.web.task;

import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.task.SuggestedTask;
import de.hsba.bi.projectwork.user.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@Data
@RequiredArgsConstructor
public class SuggestedTaskForm {

    @Size(min = 5, message = "The task name has to be at least 5 characters long.")
    @Size(max = 35, message = "The task name cannot be longer than 35 characters long.")
    private String name;

    @Size(min = 5, message = "The description has to be at least 15 characters long.")
    @Size(max = 2500, message = "The description cannot be longer than 2,500 characters long.")
    private String description;

    @Min(value = 1, message = "Please enter a whole number > 0.")
    @Max(value = 10000, message = "Please enter a whole number <= 10,000.")
    private int estimation;

    @NotNull(message = "The task does not belong to a project.")
    private Project project;

    private Enum<SuggestedTask.Status> status;

    private User creator;

    private LocalDate creationDate;


    public SuggestedTaskForm(Project project, User creator) {
        this.project = project;
        this.status = SuggestedTask.Status.IDEA;
        this.creator = creator;
        this.creationDate = LocalDate.now();
    }

}