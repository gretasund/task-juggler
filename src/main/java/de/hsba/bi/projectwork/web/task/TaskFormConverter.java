package de.hsba.bi.projectwork.web.task;

import de.hsba.bi.projectwork.task.Task;
import org.springframework.stereotype.Component;


@Component
public class TaskFormConverter {

    public Task update(Task task, TaskForm form) {
        task.setName(form.getName());
        task.setDescription(form.getDescription());
        task.setEstimation(form.getEstimation());
        task.setStatus(form.getStatus());
        task.setDueDate(form.getDueDate());

        if(form.getAssignee() != null) {
            task.setAssignee(form.getAssignee());
        }

        return task;

    }

}
