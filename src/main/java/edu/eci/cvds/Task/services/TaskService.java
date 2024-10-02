package edu.eci.cvds.Task.services;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Difficulty;
import edu.eci.cvds.Task.models.Task;
import edu.eci.cvds.Task.models.TaskDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {
    Task addTask(TaskDTO dto) throws TaskManagerException;
    void deleteTask(String id) throws TaskManagerException;
    void changeStateTask(String id)throws TaskManagerException;
    void updateTask(TaskDTO dto) throws TaskManagerException ;
    List<Task> getAllTasks()throws TaskManagerException;
    List<Task> getTasksByState(boolean state)throws TaskManagerException;
    List<Task> getTasksByDeadline(LocalDateTime deadline)throws TaskManagerException;
    List<Task> getTaskByPriority(int priority) throws TaskManagerException;
    List<Task> getTaskByDifficulty(Difficulty difficulty) throws TaskManagerException;
    List<Task> getTaskByEstimatedTime(int estimatedTime) throws TaskManagerException;
}