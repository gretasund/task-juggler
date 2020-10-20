package de.hsba.bi.projectwork.project;

import de.hsba.bi.projectwork.booking.Booking;
import de.hsba.bi.projectwork.task.SuggestedTask;
import de.hsba.bi.projectwork.task.Task;
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
    private List<Task> tasks = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "project")
    @OrderBy
    private List<SuggestedTask> suggestedTasks = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "project")
    @OrderBy
    private List<Booking> bookedTimes = new ArrayList<>();
    @Transient
    private transient double usersTimeSpentInProject;
    private String name;


    // CONSTRUCTOR
    public Project(String name) {
        this.name = name;
    }


    // METHODS
    public int findTasks(String status) {
        List<Task> allTasks = this.getTasks();
        List<Task> tasksWithStatus = new ArrayList<>();
        Enum<Task.Status> statusEnum = Task.Status.getEnumByDisplayValue(status);

        for (Task task : allTasks) {
            if (task.getStatus() == statusEnum) {
                tasksWithStatus.add(task);
            }

        }
        return tasksWithStatus.size();
    }

}
