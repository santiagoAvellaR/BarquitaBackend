package edu.eci.cvds.Task.services.persistence;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Priority;
import edu.eci.cvds.Task.models.Task;
import edu.eci.cvds.Task.models.TaskDTO;
import edu.eci.cvds.Task.services.TaskPersistence;
import edu.eci.cvds.Task.services.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class TaskPersistenceMongo implements TaskPersistence {
    private final TaskRepository taskRepository;

    @Override
    public Task save(Task task) throws TaskManagerException {
        taskRepository.save(task);
        return task;
    }

    @Override
    public void deleteById(String id) {
            taskRepository.deleteById(id);

    }

    @Override
    public List<Task> findAll() throws TaskManagerException {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> findByState(boolean state) throws TaskManagerException {
        return taskRepository.findByState(state);
    }

    @Override
    public List<Task> findByDeadline(LocalDateTime deadline) throws TaskManagerException {
        return taskRepository.findByDeadline(deadline);
    }

    @Override
    public List<Task> findByPriority(Priority priority) throws TaskManagerException {
        return taskRepository.findByPriority(priority);
    }

    @Override
    public Optional<Task> findById(String id) throws TaskManagerException {
        return taskRepository.findById(id);
    }


}
