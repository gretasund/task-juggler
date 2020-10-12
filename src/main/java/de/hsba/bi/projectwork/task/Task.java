package de.hsba.bi.projectwork.task;

import de.hsba.bi.projectwork.booking.Booking;
import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
@IdClass(BaseTask.class)
public class Task extends BaseTask implements Comparable<Task>, Serializable {

    // FIELDS
    @ManyToOne
    private User assignee;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "task")
    private List<Booking> times = new ArrayList<>();
    private LocalDate dueDate;
    private int daysLeft;
    private int totalTime;


    // CONSTRUCTORS
    // TODO delete
    public Task(User creator, String name, String description, int estimation, String status, String dueDate, Project project) {
        // set due date if present
        if(dueDate != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            this.dueDate = LocalDate.parse(dueDate, dateTimeFormatter);
            this.calcDaysLeft();
        }

        this.creator = creator;
        this.creationDate = LocalDate.now();
        this.name = name;
        this.description = description;
        this.estimation = estimation;
        this.status = status;
        this.calcDaysLeft();
        this.project = project;

    }

    public Task(User creator, String name, String description, int estimation, String status, String dueDate) {
        // set due date if present
        if(dueDate != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            this.dueDate = LocalDate.parse(dueDate, dateTimeFormatter);
            this.calcDaysLeft();
        }

        this.creator = creator;
        this.creationDate = LocalDate.now();
        this.name = name;
        this.description = description;
        this.estimation = estimation;
        this.status = status;
        this.calcDaysLeft();

    }

    public Task(SuggestedTask suggestedTask) {
        this.creator = suggestedTask.getCreator();
        this.name = suggestedTask.getName();
        this.description = suggestedTask.getDescription();
        this.estimation = suggestedTask.getEstimation();
        this.status = suggestedTask.getStatus();
        this.creationDate = suggestedTask.getCreationDate();
        this.project = suggestedTask.getProject();
        this.calcDaysLeft();
    }


    // METHODS
    @Override
    public int compareTo(Task task) {
            if (this.getDueDate() == null) {
                return (task.getDueDate() == null) ? 0 : -1;
            }
            if (task.getDueDate() == null) {
                return 1;
            }
            return task.getDueDate().compareTo(this.getDueDate());
    }

    @Scheduled(cron = "0 0 * * * ?") // refreshes daysLeft everyday at midnight
    public void calcDaysLeft() {
        // if due date is not set
        if(this.dueDate == null) {
            return;
        }
        LocalDate dueDate = this.dueDate;
        LocalDate todaysDate = LocalDate.now();
        this.daysLeft = (int) ChronoUnit.DAYS.between(todaysDate, dueDate);
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        this.calcDaysLeft();
    }

    public int calcTotalTime() {
        int sum = 0;
        for (Booking booking : this.times) {
            int time = booking.getTimeSpent();
            sum = sum + time;
        }
        this.setTotalTime(sum);
        return sum;
    }


}
