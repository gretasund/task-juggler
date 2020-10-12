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
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseTask implements Serializable {

    // fields
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    public Long id;
    public String name;
    public String description;
    public int estimation;
    protected LocalDate creationDate;

    //private Enum<Status> status;
    public String status;

    @ManyToOne(optional = false)
    public User creator;

    @ManyToOne(optional = false)
    public Project project;


    // enum
    public enum Status {
        IDEA("Idea"),
        PLANNED("Planned"),
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
            return displayValue;
        }

        public static List<Status> getAllStatus() {
            List<Status> allStatus = new ArrayList<>();
            allStatus.add(IDEA);
            allStatus.add(PLANNED);
            allStatus.add(WORK_IN_PROGRESS);
            allStatus.add(TESTING);
            allStatus.add(DONE);
            return allStatus;
        }

    }

}
