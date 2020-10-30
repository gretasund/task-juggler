package de.hsba.bi.projectwork.task.suggestedtask;

import de.hsba.bi.projectwork.task.AbstractTask;
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
@IdClass(AbstractTask.class)
public class SuggestedTask extends AbstractTask {

    // field
    protected Enum<Status> status = SuggestedTask.Status.IDEA;


    // enum
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
