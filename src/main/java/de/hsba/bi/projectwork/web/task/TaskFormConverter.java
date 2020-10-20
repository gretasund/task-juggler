package de.hsba.bi.projectwork.web.task;

import de.hsba.bi.projectwork.task.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
public class TaskFormConverter {

    public Task update(Task task, TaskForm taskForm) {
        task.setName(taskForm.getName());
        task.setDescription(taskForm.getDescription());
        task.setEstimation(taskForm.getEstimation());
        task.setStatus(Task.Status.getEnumByDisplayValue(taskForm.getStatus()));
        task.setAssignee(taskForm.getAssignee());

        if(taskForm.getDueDate()!= null) {
            task.setDueDate(LocalDate.parse(taskForm.getDueDate()));
        }

        return task;

    }

}
