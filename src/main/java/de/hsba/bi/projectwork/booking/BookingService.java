package de.hsba.bi.projectwork.booking;

import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.project.ProjectRepository;
import de.hsba.bi.projectwork.project.ProjectService;
import de.hsba.bi.projectwork.task.Task;
import de.hsba.bi.projectwork.task.TaskRepository;
import de.hsba.bi.projectwork.task.TaskService;
import de.hsba.bi.projectwork.user.User;
import de.hsba.bi.projectwork.user.UserService;
import de.hsba.bi.projectwork.web.booking.BookingForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
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
        bookings.removeIf( booking -> !(booking.getUser()==currentUser && usersBookings.contains(booking)) );

        return usersBookings;
    }


    // Add / delete bookings
    public void addBooking(Long taskId, Long projectId, int timeSpent, String date, User user) {
        // TODO Als Entwickler in einem Projekt kann ich aufgewendete Zeiten für eine Aufgabe buchen (diese Buchung gilt pro Aufgabe und Entwickler, außerdem kann ein Entwickler mehrmals Zeiten buchen, wenn die Bearbeitung beispielsweise mehrere Tage dauert)
        LocalDate localDate = LocalDate.parse(date);

        // get all entities
        Project project = projectService.findById(projectId);
        Task task = taskService.findById(taskId);
        Booking booking = new Booking(user, localDate, timeSpent);

        // link booking to task
        booking.setTask(task);
        booking.setProject(project);
        bookingRepository.save(booking);

        // refresh task
        task.setTotalTime(task.calcTotalTime());

    }

    public void addBooking(Long taskId, Long projectId, BookingForm bookingForm, User user) {
        // TODO Als Entwickler in einem Projekt kann ich aufgewendete Zeiten für eine Aufgabe buchen (diese Buchung gilt pro Aufgabe und Entwickler, außerdem kann ein Entwickler mehrmals Zeiten buchen, wenn die Bearbeitung beispielsweise mehrere Tage dauert)
        LocalDate localDate = LocalDate.parse(bookingForm.getDate());

        // get all entities
        Project project = projectService.findById(projectId);
        Task task = taskService.findById(taskId);
        Booking booking = new Booking(user, localDate, bookingForm.getTimeSpent());

        // link booking to task
        booking.setTask(task);
        booking.setProject(project);
        bookingRepository.save(booking);

        // refresh task
        task.setTotalTime(task.calcTotalTime());

    }

    public void deleteBooking(Task task, Booking booking) {
        // delete booking in task and refresh task
        task.getTimes().remove(booking);
        task.setTotalTime(task.calcTotalTime());
        taskRepository.save(task);

        // delete booking
        bookingRepository.delete(booking);
    }

}