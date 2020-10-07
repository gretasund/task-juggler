package de.hsba.bi.projectwork.task;

import de.hsba.bi.projectwork.user.User;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
@IdClass(BaseTask.class)
public class Task extends BaseTask implements Comparable<Task>, Serializable {

    private LocalDate dueDate;
    private int totalTime;

    @ManyToOne
    private User assignee;

    @Transient
    private int daysLeft;


    public Task(User creator, String name, String description, int estimation, String status, String dueDate) {
        // set due date if present
        if(dueDate != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            this.dueDate = LocalDate.parse(dueDate, dateTimeFormatter);
        }
        else {
            this.dueDate = null;
        }

        this.creator = creator;
        this.creationDate = LocalDate.now();
        this.name = name;
        this.description = description;
        this.estimation = estimation;
        this.status = status;

    }

    public Task(SuggestedTask suggestedTask) {
        this.creationDate = LocalDate.now();
        this.creator = suggestedTask.creator;
        this.name = suggestedTask.name;
        this.description = suggestedTask.description;
        this.estimation = suggestedTask.estimation;
        this.status = "Idea";
        this.project = suggestedTask.project;
    }

    public void calcDaysLeft() {
        // if due date is not set
        if(this.dueDate == null) {
            return;
        }
        LocalDate dueDate = this.dueDate;
        LocalDate todaysDate = LocalDate.now();
        this.daysLeft = (int) ChronoUnit.DAYS.between(todaysDate, dueDate);
    }

    @Override
    public int compareTo(Task task) {
        return getDueDate().compareTo(task.getDueDate());
    }

}
