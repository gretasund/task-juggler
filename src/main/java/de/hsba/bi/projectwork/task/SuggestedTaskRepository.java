package de.hsba.bi.projectwork.task;

import de.hsba.bi.projectwork.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuggestedTaskRepository extends JpaRepository<SuggestedTask, Long> {

    SuggestedTask save(SuggestedTask suggestedTask);

    List<SuggestedTask> findSuggestedTaskByStatus(String status);

    List<SuggestedTask> findSuggestedTaskByCreator(User user);

}
