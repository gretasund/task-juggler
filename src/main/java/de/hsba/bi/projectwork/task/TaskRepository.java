package de.hsba.bi.projectwork.task;

import de.hsba.bi.projectwork.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAll();

    Optional<Task> findById(Long id);

    List<Task> findTaskByAssignee(User assignee);

    List<Task> findTaskByDueDate(String dueDate);

}
