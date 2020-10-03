package de.hsba.bi.projectwork.project;

import de.hsba.bi.projectwork.task.Task;
import de.hsba.bi.projectwork.user.User;
import lombok.*;

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

    @ManyToMany(cascade = CascadeType.ALL)
    @OrderBy
    private List<User> members;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "project")
    @OrderBy
    private List<Task> tasks = new ArrayList<>();

    @Transient
    private transient double usersTimeSpentInProject;

    public Project(String name) {
        this.name = name;
    }

}
