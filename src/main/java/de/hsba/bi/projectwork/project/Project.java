package de.hsba.bi.projectwork.project;

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

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private Long id;

    private String name;

    @ManyToMany(cascade = CascadeType.MERGE)
    @OrderBy
    private List<User> members = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "project")
    @OrderBy
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "project")
    @OrderBy
    private List<SuggestedTask> suggestedTasks = new ArrayList<>();

    @Transient
    private transient double usersTimeSpentInProject;

    public Project(String name) {
        this.name = name;
    }

    public int findTasks(String status) {
        List<Task> allTasks = this.getTasks();
        List<Task> tasks = new ArrayList<>();
        for (Task task : allTasks) {
            if (task.getStatus().equals(status)) {
                tasks.add(task);
            }
        }
        return tasks.size();
    }

}
