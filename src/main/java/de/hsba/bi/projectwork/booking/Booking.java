package de.hsba.bi.projectwork.booking;

import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.task.Task;
import de.hsba.bi.projectwork.user.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Data
public class Booking {

    // FIELDS
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private Long id;
    private LocalDate date;
    private int timeSpent;
    @ManyToOne(optional = false)
    private User user;
    @ManyToOne(optional = false)
    private Task task;
    @ManyToOne(optional = false)
    private Project project;


    // CONSTRUCTOR
    public Booking(User user, LocalDate date, int timeSpent) {
        this.user = user;
        this.date = date;
        this.timeSpent = timeSpent;
    }

}