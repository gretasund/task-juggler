package de.hsba.bi.projectwork.web.task.acceptedtask;

import de.hsba.bi.projectwork.task.acceptedtask.AcceptedTask;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;


@Component
public class AcceptedTaskFormConverter {

    public AcceptedTask update(AcceptedTask acceptedTask, AcceptedTaskForm acceptedTaskForm) {
        acceptedTask.setName(acceptedTaskForm.getName());
        acceptedTask.setDescription(acceptedTaskForm.getDescription());
        acceptedTask.setEstimation(acceptedTaskForm.getEstimation());
        acceptedTask.setStatus(AcceptedTask.Status.getEnumByDisplayValue(acceptedTaskForm.getStatus()));
        acceptedTask.setAssignee(acceptedTaskForm.getAssignee());

        if(acceptedTaskForm.getDueDate().equals("") || acceptedTaskForm.getDueDate() ==  null) {
            acceptedTask.setDueDate(null);
        } else {
            try{
                acceptedTask.setDueDate(LocalDate.parse(acceptedTaskForm.getDueDate()));
            } catch (DateTimeParseException dateTimeParseException) {
                System.out.println("Could not set dueDate");
            }
        }

        return acceptedTask;

    }

}
