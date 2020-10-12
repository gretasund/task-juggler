package de.hsba.bi.projectwork.task;

import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.IdClass;


@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
@IdClass(BaseTask.class)
public class SuggestedTask extends BaseTask {

    public SuggestedTask(Project project, String name, String description, int estimation, User creator) {
        this.project = project;
        this.name = name;
        this.description = description;
        this.estimation = estimation;
        this.status = "Idea";
        this.creator = creator;
    }

}
