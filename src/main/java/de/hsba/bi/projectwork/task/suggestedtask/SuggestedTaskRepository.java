package de.hsba.bi.projectwork.task.suggestedtask;

import de.hsba.bi.projectwork.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuggestedTaskRepository extends JpaRepository<SuggestedTask, Long> {

    List<SuggestedTask> findSuggestedTaskByStatus(Enum<SuggestedTask.Status> status);

    List<SuggestedTask> findSuggestedTaskByCreator(User user);

}
