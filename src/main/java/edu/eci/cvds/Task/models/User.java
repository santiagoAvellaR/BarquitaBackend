package edu.eci.cvds.Task.models;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.services.TaskService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Document(collection = "Users")
public class User implements TaskService {
    @Id
    private String usernameId;
    private HashMap<String,Task> tasks;
    private String name;
    private String password;
    private int idTask=1;
    public User(String usernameId, String name, String password) throws TaskManagerException {
        validate(usernameId,name, password);
        this.usernameId = usernameId;
        this.name = name;
        this.password = password;
        this.tasks = new HashMap<>();
    }
    @Override
    public Task addTask(TaskDTO taskDTO) throws TaskManagerException {
        if(tasks.containsKey(taskDTO.getId())) throw new TaskManagerException(TaskManagerException.TASK_ALREADY_EXIST);
        Task task = new Task(
                generateId(),
                taskDTO.getName(),
                taskDTO.getDescription(),
                taskDTO.getState(),
                taskDTO.getPriority(),
                taskDTO.getEstimatedTime(),
                taskDTO.getDifficulty(),
                taskDTO.getDeadline());
        tasks.put(task.getId(), task);
        return task;
    }
    @Override
    public void deleteTask(String id) throws TaskManagerException {

    }
    @Override
    public void changeStateTask(String id) throws TaskManagerException {

    }

    @Override
    public void updateTask(TaskDTO dto) throws TaskManagerException {

    }

    @Override
    public List<Task> getAllTasks() throws TaskManagerException {
        return List.of();
    }

    @Override
    public List<Task> getTasksByState(boolean state) throws TaskManagerException {
        return List.of();
    }

    @Override
    public List<Task> getTasksByDeadline(LocalDateTime deadline) throws TaskManagerException {
        return List.of();
    }

    @Override
    public List<Task> getTaskByPriority(int priority) throws TaskManagerException {
        return List.of();
    }

    @Override
    public List<Task> getTaskByDifficulty(Difficulty difficulty) throws TaskManagerException {
        return List.of();
    }

    @Override
    public List<Task> getTaskByEstimatedTime(int estimatedTime) throws TaskManagerException {
        return List.of();
    }

    private String generateId(){
        return UUID.randomUUID().toString().replace("-", "").substring(0, 9) + this.idTask++;
    }

    private void validate(String usernameId, String name, String password) throws TaskManagerException {
        if(usernameId == null || usernameId.isEmpty() ) throw new TaskManagerException(TaskManagerException.INVALID_USER_ID);
        if(name == null || name.isEmpty()) throw new TaskManagerException(TaskManagerException.INVALID_USER_NAME);
        if(password == null || password.isEmpty()) throw new TaskManagerException(TaskManagerException.INVALID_USER_PASSWD);
    }
}
