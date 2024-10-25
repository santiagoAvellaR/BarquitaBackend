package edu.eci.cvds.Task.models;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.services.TaskService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Setter
@Getter
@Document(collection = "Users")
public class User implements TaskService, UserDetails {
    @Id
    private String usernameId;
    private HashMap<String,Task> tasks;
    private String name;
    private String password;
    private int idTask=1;
    private String email;
    private Role role = Role.USER;


    /**
     * This method is the constructor of User, and it validates the field of password, name and usernameId.
     * @param usernameId The given username id
     * @param name The Given name
     * @param password The given password
     * @throws TaskManagerException If the information of the user is not correct.
     */
    public User(String usernameId, String name, String password, String email) throws TaskManagerException {
        validate(usernameId,name, password, email);
        this.usernameId = usernameId;
        this.name = name;
        this.password = password;
        this.tasks = new HashMap<>();
        this.email = email;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
    public boolean isAdmin() {
        return role.equals(Role.ADMIN);
    }

    /**
     * This method adds a Task by the given DTO Task.
     * @param taskDTO The given DTO Object, in this case, TaskDTO
     * @return the created Task
     * @throws TaskManagerException In the case the information given to create the Task is not correct.
     */
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

    /**
     * This method deletes a task by the given id, if it exists
     * @param id The id of the task to delete
     * @throws TaskManagerException Throws an exception in case the task is not found
     */
    @Override
    public void deleteTask(String id) throws TaskManagerException {
        if(!tasks.containsKey(id)) throw new TaskManagerException(TaskManagerException.TASK_NOT_FOUND);
        tasks.remove(id);
    }

    /**
     * This method changes the state of a task by the given id.
     * @param id The task of the id to change state
     * @throws TaskManagerException Throws an exception if there is a problem
     * with the database or with the given id is not correct.
     */
    @Override
    public void changeStateTask(String id) throws TaskManagerException {
        if(!tasks.containsKey(id)) throw new TaskManagerException(TaskManagerException.TASK_NOT_FOUND);
        Task task = tasks.get(id);
        task.changeState();
        tasks.put(id, task);
    }

    /**
     * This method updates a Task from the database by the given TaskDTO.
     * this updates the information of the task, except, if there is a problem with the given TaskDTO information
     * of if there is a problem with the Database.
     * @param dto The given TaskDTO to update the Task in the database
     * @throws TaskManagerException If the information of the DTO is incorrect, or if there is a problem
     * with the database.
     */
    @Override
    public void updateTask(TaskDTO dto) throws TaskManagerException {
        if(!tasks.containsKey(dto.getId())) throw new TaskManagerException(TaskManagerException.TASK_NOT_FOUND);
        Task task = tasks.get(dto.getId());
        task.changeName(dto.getName());
        task.changeDescription(dto.getDescription());
        task.setState(dto.getState());
        task.changePriority(dto.getPriority());
        task.changeEstimatedTime(dto.getEstimatedTime());
        task.setDifficulty(dto.getDifficulty());
        task.setDeadline(dto.getDeadline());
        tasks.put(task.getId(), task);
    }

    /**
     * This method returns all the tasks of the database.
     * @return A List of the task from the database.
     * @throws TaskManagerException Throws an exception if there is a problem with the database.
     */
    @Override
    public List<Task> getAllTasks() throws TaskManagerException {
        return new ArrayList<>(tasks.values());
    }

    /**
     * This method return a List of Tasks with the state given.
     * @param state The state to filter the list of tasks
     * @return The list of Tasks with the same state that is given.
     * @throws TaskManagerException Throws an exception if there is a problem with the database.
     */
    @Override
    public List<Task> getTasksByState(boolean state) throws TaskManagerException {
        return getAllTasks().stream().filter(task -> state==task.getState()).toList();
    }

    /**
     * This method returns List of Tasks that have a deadline equal or before the given date.
     * @param deadline The deadline to filter the List of Task
     * @return The list of tasks that satisfies the condition.
     * @throws TaskManagerException Throws an exception if there is a problem with the database.
     */
    @Override
    public List<Task> getTasksByDeadline(LocalDateTime deadline) throws TaskManagerException {
        return getAllTasks().stream().filter(task -> task.getDeadline().isBefore(deadline)).toList();
    }

    /**
     * This method returns List of Tasks that have the given priority.
     * @param priority The priority to filter the List of Task.
     * @return The list of tasks that satisfies the condition.
     * @throws TaskManagerException Throws an exception if there is a problem with the database.
     */
    @Override
    public List<Task> getTaskByPriority(int priority) throws TaskManagerException {
        return List.of();
    }

    /**
     * This method returns List of Tasks that have the given difficulty
     * @param difficulty The difficulty to filter the List of Taks.
     * @return The list of tasks that satisfies the condition.
     * @throws TaskManagerException Throws an exception if there is a problem with the database.
     */
    @Override
    public List<Task> getTaskByDifficulty(Difficulty difficulty) throws TaskManagerException {
        return getAllTasks().stream().filter(task -> task.getDifficulty().equals(difficulty)).toList();
    }
    /**
     * This method returns List of Tasks that have the given estimated time.
     * @param estimatedTime The estimated time to complete the task
     * @return The list of tasks that satisfies the condition.
     * @throws TaskManagerException Throws an exception if there is a problem with the database.
     */
    @Override
    public List<Task> getTaskByEstimatedTime(int estimatedTime) throws TaskManagerException {
        return getAllTasks().stream().filter(task -> task.getEstimatedTime() == estimatedTime).toList();
    }

    /**
     * This method returns a User DTO with the user information instead of returning the User Object
     * @return The User DTO of this user
     * @throws TaskManagerException If there is a problem tasks of the user.
     */
    public UserDTO toDTO() throws TaskManagerException {
        return new UserDTO(usernameId,getAllTasks(),name, email);
    }

    private String generateId(){
        return UUID.randomUUID().toString().replace("-", "").substring(0, 9) + this.idTask++;
    }

    private void validate(String usernameId, String name, String password, String email) throws TaskManagerException {
        if(usernameId == null || usernameId.isEmpty() ) throw new TaskManagerException(TaskManagerException.INVALID_USER_ID);
        if(name == null || name.isEmpty()) throw new TaskManagerException(TaskManagerException.INVALID_USER_NAME);
        if(password == null || password.isEmpty()) throw new TaskManagerException(TaskManagerException.INVALID_USER_PASSWD);
        if(email == null || email.isEmpty()) throw  new TaskManagerException(TaskManagerException.INVALID_USER_EMAIL);
    }

    /*
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
     */

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
