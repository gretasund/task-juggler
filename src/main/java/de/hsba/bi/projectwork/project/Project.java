package de.hsba.bi.projectwork.project;

import de.hsba.bi.projectwork.booking.Booking;
import de.hsba.bi.projectwork.task.acceptedtask.AcceptedTask;
import de.hsba.bi.projectwork.task.suggestedtask.SuggestedTask;
import de.hsba.bi.projectwork.user.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class Project {

    // FIELDS
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private Long id;
    @ManyToMany(cascade = CascadeType.MERGE)
    @OrderBy
    private List<User> members = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "project")
    @OrderBy
    private List<AcceptedTask> acceptedTasks = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "project")
    @OrderBy
    private List<SuggestedTask> suggestedTasks = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "project")
    @OrderBy
    private List<Booking> bookedTimes = new ArrayList<>();
    private String name;


    // CONSTRUCTOR
    public Project(String name) {
        this.name = name;
    }


    // METHODS
    public int findTasks(String status) {
        List<AcceptedTask> allAcceptedTasks = this.getAcceptedTasks();
        List<AcceptedTask> tasksWithStatuses = new ArrayList<>();
        Enum<AcceptedTask.Status> statusEnum = AcceptedTask.Status.getEnumByDisplayValue(status);

        for (AcceptedTask acceptedTask : allAcceptedTasks) {
            if (acceptedTask.getStatus() == statusEnum) {
                tasksWithStatuses.add(acceptedTask);
            }

        }
        return tasksWithStatuses.size();
    }

}
