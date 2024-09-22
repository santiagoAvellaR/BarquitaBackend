package edu.eci.cvds.Task;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {
    Task addTask(TaskDTO dto) throws TaskManagerExceptions;
    void deleteTask(String id);
    void changeStateTask(String id);
    void updateTask(TaskDTO dto);
    List<Task> getAllTasks();
    List<Task> getTasksByState(boolean state);
    List<Task> getTasksByDeadline(LocalDateTime deadline);
    List<Task> getTaskByPriority(Priority priority);
}
