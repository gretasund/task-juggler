package de.hsba.bi.projectwork.task;

import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


@Entity
@NoArgsConstructor
@Data
public class Task implements Comparable<Task>{

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private Long id;
    private String name;
    private String description;
    private int estimation;
    private int totalTime;
    private String status;
    private LocalDate dueDate;

    @Transient
    private int daysLeft;
    //private Enum<Status> status;

    @ManyToOne(optional = false)
    private Project project;

    @ManyToOne
    private User assignee;

    public enum Status {
        IDEA("Idea"),
        PLANNED("Planned"),
        WORK_IN_PROGRESS("Work in progress"),
        TESTING("Testing"),
        DONE("Done");

        private final String displayValue;

        Status(String displayValue) {
            this.displayValue = displayValue;
        }

        public String getDisplayValue() {
            return displayValue;
        }
    }

    public Task(String name, String description, int estimation, String status, String dueDate) {
        // set due date if present
        if(dueDate != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            this.dueDate = LocalDate.parse(dueDate, dateTimeFormatter);
        }
        else {
            this.dueDate = null;
        }

        this.name = name;
        this.description = description;
        this.estimation = estimation;
        this.status = status;

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
