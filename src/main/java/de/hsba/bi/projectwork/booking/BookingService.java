package de.hsba.bi.projectwork.booking;

import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.project.ProjectRepository;
import de.hsba.bi.projectwork.project.ProjectService;
import de.hsba.bi.projectwork.task.Task;
import de.hsba.bi.projectwork.task.TaskRepository;
import de.hsba.bi.projectwork.task.TaskService;
import de.hsba.bi.projectwork.user.User;
import de.hsba.bi.projectwork.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class BookingService {

    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final ProjectService projectService;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TaskService taskService;


    // find tasks
    public Booking findById(Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        return booking.orElse(null);
    }


    // find bookings by user
    public List<Booking> findUsersBookings(List<Booking> bookings) {
        // load entities
        User currentUser = userService.findCurrentUser();

        // init new list of usersTasks
        List<Booking> usersBookings = new ArrayList<>();

        // iterate over tasks
        for (Booking booking: bookings) {
            // alternative:
            // tasks.removeIf(task -> !task.getProject().getMembers().contains(user));
            if(booking.getUser()==currentUser && !usersBookings.contains(booking)){
                usersBookings.add(booking);
            }
        }

        return usersBookings;
    }


    // Add / delete bookings
    public void bookTime(Long taskId, Long projectId, int timeSpent, String date, User user) {
        // TODO Als Entwickler in einem Projekt kann ich aufgewendete Zeiten für eine Aufgabe buchen (diese Buchung gilt pro Aufgabe und Entwickler, außerdem kann ein Entwickler mehrmals Zeiten buchen, wenn die Bearbeitung beispielsweise mehrere Tage dauert)
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);

        // get all entities
        Project project = projectService.findById(projectId);
        Task task = taskService.findById(taskId);
        Booking booking = new Booking(user, localDate, timeSpent);

        // link booking to task
        booking.setTask(task);
        booking.setProject(project);
        bookingRepository.save(booking);

        // link task to booking
        task.getTimes().add(booking);
        task.setTotalTime(task.calcTotalTime());
        taskRepository.save(task);

        //link project and booking
        project.getBookedTimes().add(booking);
        projectRepository.save(project);

    }

    public void deleteBookedTime(Task task, Booking booking) {
        task.getTimes().remove(booking);
        taskRepository.save(task);
        bookingRepository.delete(booking);
    }


    // statistic
    public HashMap<User, Task> compareEstimatedAndBookedTimes() {
        // TODO Als Manager kann ich eine Statistik einsehen, bei der die geschätzten und gebuchten Zeiten gegenübergestellt werden
        HashMap<User, Task> comparison = null;
        return comparison;
    }

}