package edu.eci.cvds.Task.services.user;

import edu.eci.cvds.Task.LoginDTO;
import edu.eci.cvds.Task.RegisterDTO;
import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.TokenDTO;
import edu.eci.cvds.Task.models.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ServiceUser {
    TokenDTO createUser(RegisterDTO registerDTO)throws TaskManagerException;
    UserDTO getUser(String id) throws TaskManagerException;
    TokenDTO login(LoginDTO loginDTO) throws TaskManagerException;
    // Delete account
    void deleteUser(String userId) throws TaskManagerException;
    //
    Task addTask(String userId, TaskDTO dto) throws TaskManagerException;
    void deleteTask(String userId, String id) throws TaskManagerException;
    void changeStateTask(String userId, String id)throws TaskManagerException;
    void updateTask(String userId, TaskDTO dto) throws TaskManagerException ;
    List<Task> getAllTasks(String userId)throws TaskManagerException;
    List<Task> getTasksByState(String userId, boolean state)throws TaskManagerException;
    List<Task> getTasksByDeadline(String userId, LocalDateTime deadline)throws TaskManagerException;
    List<Task> getTaskByPriority(String userId, int priority) throws TaskManagerException;

    List<Task> getTaskByDifficulty(String userId, Difficulty difficulty) throws TaskManagerException;
    // Analytics
    List<Task> getTaskByEstimatedTime(String userId, int estimatedTime) throws TaskManagerException;
    void deleteAll();
}
