package de.hsba.bi.projectwork.task.acceptedtask;

import de.hsba.bi.projectwork.booking.Booking;
import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.task.AbstractTask;
import de.hsba.bi.projectwork.task.suggestedtask.SuggestedTask;
import de.hsba.bi.projectwork.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
@IdClass(AbstractTask.class)
public class AcceptedTask extends AbstractTask implements Comparable<AcceptedTask>, Serializable {

    // fields
    @ManyToOne
    private User assignee;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "task")
    private List<Booking> times = new ArrayList<>();
    private Enum<Status> status;
    private LocalDate dueDate;
    private int daysLeft;
    private int totalTime;


    // constructors
    public AcceptedTask(User creator, String name, String description, int estimation, Enum<Status> status, LocalDate dueDate, Project project) {
        // set due date if present
        if(dueDate != null) {
            this.dueDate = dueDate;
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

    public AcceptedTask(SuggestedTask suggestedTask) {
        this.creator = suggestedTask.getCreator();
        this.name = suggestedTask.getName();
        this.description = suggestedTask.getDescription();
        this.estimation = suggestedTask.getEstimation();
        this.status = Status.ACCEPTED;
        this.creationDate = suggestedTask.getCreationDate();
        this.project = suggestedTask.getProject();
    }


    // methods
    @Override
    public int compareTo(AcceptedTask acceptedTask) {
        if (this.getDueDate() == null || acceptedTask.getDueDate() == null) {
            return 0;
        }
        return this.getDueDate().compareTo(acceptedTask.getDueDate());
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

    public int calcTotalTime() {
        int sum = 0;
        for (Booking booking : this.times) {
            int time = booking.getTimeSpent();
            sum = sum + time;
        }
        this.setTotalTime(sum);
        return sum;
    }


    // enum
    public enum Status {
        ACCEPTED("Accepted"),
        WORK_IN_PROGRESS("Work in progress"),
        TESTING("Testing"),
        DONE("Done");

        // fields
        private final String displayValue;

        // constructor
        Status(String displayValue) {
            this.displayValue = displayValue;
        }

        // methods
        public String getDisplayValue() {
            return this.displayValue;
        }

        public static List<Status> getAllStatus() {
            List<Status> allStatus = new ArrayList<>();
            allStatus.add(ACCEPTED);
            allStatus.add(WORK_IN_PROGRESS);
            allStatus.add(TESTING);
            allStatus.add(DONE);
            return allStatus;
        }

        public static Enum<Status> getEnumByDisplayValue(String displayValue){
            for(Status status : Status.values()){
                if(status.displayValue.equals(displayValue)) {
                    return status;
                }
            }
            return null;
        }

    }


}
