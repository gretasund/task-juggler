package de.hsba.bi.projectwork.task.acceptedtask;

import de.hsba.bi.projectwork.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AcceptedTaskRepository extends JpaRepository<AcceptedTask, Long> {

    List<AcceptedTask> findAll();

    Optional<AcceptedTask> findById(Long id);

    List<AcceptedTask> findTaskByAssignee(User assignee);

    List<AcceptedTask> findTaskByStatus(Enum<AcceptedTask.Status> status);

}
