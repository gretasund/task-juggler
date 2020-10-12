package de.hsba.bi.projectwork.user;

import de.hsba.bi.projectwork.booking.Booking;
import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.task.BaseTask;
import de.hsba.bi.projectwork.task.Task;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@RequiredArgsConstructor
@Data
public class User implements Comparable<User> {

    public static String ADMIN_ROLE = "ADMIN";
    public static String DEVELOPER_ROLE = "DEVELOPER";
    public static String MANAGER_ROLE = "MANAGER";


    // FIELDS
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private Long id;
    @Basic(optional = false)
    private String name;
    @Basic(optional = false)
    private String password;
    @Basic(optional = false)
    private String role;
    @ManyToMany(mappedBy = "members")
    @OrderBy
    private List<Project> projects = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    @OrderBy
    private List<Booking> bookedTimes;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "creator")
    @OrderBy
    private List<BaseTask> createdTasks = new ArrayList<>();
    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REMOVE}, orphanRemoval = true, mappedBy = "assignee")
    @OrderBy
    private List<Task> assignedTasks = new ArrayList<>();


    // METHODS
    @Override
    public int compareTo(User other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return name;
    }

}
