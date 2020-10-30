package de.hsba.bi.projectwork.task;

import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
@IdClass(BaseTask.class)
public class SuggestedTask extends BaseTask {

    // Field
    protected Enum<Status> status = SuggestedTask.Status.IDEA;


    // Constructor
    // TODO delete
    public SuggestedTask(Project project, String name, String description, int estimation, User creator) {
        this.project = project;
        this.name = name;
        this.description = description;
        this.estimation = estimation;
        this.status = SuggestedTask.Status.IDEA;
        this.creator = creator;
    }


    // Enum
    public enum Status {
        IDEA("Idea"),
        ACCEPTED("Accepted"),
        DECLINED("Declined");

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

        public static List<SuggestedTask.Status> getAllStatus() {
            List<Status> allStatus = new ArrayList<>();
            allStatus.add(IDEA);
            allStatus.add(ACCEPTED);
            allStatus.add(DECLINED);
            return allStatus;
        }

    }

}
