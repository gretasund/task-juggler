package de.hsba.bi.projectwork.task;

import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.user.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


@NoArgsConstructor
@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseTask implements Serializable {

    // fields
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    protected Long id;
    protected String name;
    protected String description;
    protected int estimation;
    protected LocalDate creationDate;
    @ManyToOne(optional = false)
    protected User creator;
    @ManyToOne(optional = false)
    protected Project project;

}
