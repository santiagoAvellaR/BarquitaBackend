package edu.eci.cvds.Task.services;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Priority;
import edu.eci.cvds.Task.models.Task;
import edu.eci.cvds.Task.models.TaskDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {
    Task addTask(TaskDTO dto) throws TaskManagerException, FilePersistenceException;
    void deleteTask(String id) throws TaskManagerException, FilePersistenceException;
    void changeStateTask(String id);
    void updateTask(TaskDTO dto) throws FilePersistenceException;
    List<Task> getAllTasks();
    List<Task> getTasksByState(boolean state);
    List<Task> getTasksByDeadline(LocalDateTime deadline);
    List<Task> getTaskByPriority(Priority priority);
}
