package edu.eci.cvds.Task.services;

import edu.eci.cvds.Task.models.Priority;
import edu.eci.cvds.Task.models.Task;
import edu.eci.cvds.Task.models.TaskDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByState(boolean state);
    List<Task> findByDeadline(LocalDateTime deadline);
    List<Task> findByPriority(Priority priority);
}
