package de.hsba.bi.projectwork.project;

import de.hsba.bi.projectwork.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Project {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private Long id;

    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    @OrderBy
    private List<User> members;

    @Transient
    private transient double usersTimeSpentInProject;

    public Project(String name) {
        this.name = name;
    }

}
