package de.hsba.bi.projectwork.booking;

import de.hsba.bi.projectwork.ProjectWorkApplication;
import de.hsba.bi.projectwork.project.Project;
import de.hsba.bi.projectwork.project.ProjectService;
import de.hsba.bi.projectwork.task.acceptedtask.AcceptedTask;
import de.hsba.bi.projectwork.task.acceptedtask.AcceptedTaskService;
import de.hsba.bi.projectwork.task.suggestedtask.SuggestedTaskService;
import de.hsba.bi.projectwork.user.User;
import de.hsba.bi.projectwork.user.UserService;
import de.hsba.bi.projectwork.web.project.ProjectForm;
import de.hsba.bi.projectwork.web.user.RegisterUserForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

//Newly added feature that forces reset
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(classes = ProjectWorkApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookingServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    @Autowired
    AcceptedTaskService acceptedTaskService;

    @Autowired
    SuggestedTaskService suggestedAcceptedTaskService;

    @Autowired
    BookingService bookingService;

    @Test
    void ShouldBeAbleToBookTime() {
        userService.createUser(new RegisterUserForm("TestUser", "1234567890", "1234567890"), User.Role.DEVELOPER);
        User TestUser = userService.findByName("TestUser");
        Project project = projectService.addProject(new ProjectForm("Test Project", null));
        AcceptedTask task = acceptedTaskService.addTask(new AcceptedTask( TestUser, "Testing101", "Testing content", 3, AcceptedTask.Status.TESTING, null, project), project.getId());
        Long projectID = project.getId();
        Long taskID = task.getId();
        Booking booking = bookingService.addBooking(taskID, projectID, 1, "2020-07-28", TestUser);
        Long BookingID= booking.getId();
        assertThat(bookingService.findById(BookingID)).isNotNull();
        bookingService.deleteBooking(task, booking);
        assertThat(bookingService.findById(BookingID)).isNull();
    }
}
