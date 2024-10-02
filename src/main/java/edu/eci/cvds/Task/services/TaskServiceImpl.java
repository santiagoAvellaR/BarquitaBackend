package edu.eci.cvds.Task.services;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Difficulty;
import edu.eci.cvds.Task.models.Task;
import edu.eci.cvds.Task.models.TaskDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This class is the implementation of the Task Service Interface.
 * The responsibility of this class is the administration and use
 * of the databases that are available to return the requests made by the client.
 * @Version 1.0
 * @Since 26-09-2024
 */
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskPersistence taskRepository;
    private int id = 1; // Este metodo se usara para crear una String

    public TaskServiceImpl(@Qualifier("filePersistenceImpl") TaskPersistence taskRepository) {
        this.taskRepository = taskRepository;
    }
    /**
     * This method adds a Task by the given DTO Task and stores it in thd Date Base.
     * @param dto The given DTO Object, in this case, TaskDTO
     * @return the created Task
     * @throws TaskManagerException In the case the information given to create the Task is not correct.
     */
    @Override
    public Task addTask(TaskDTO dto) throws TaskManagerException {
        String taskId =generateId();
        Task task = new Task(taskId,
                dto.getName(), dto.getDescription(), dto.getState(),
                dto.getPriority(), dto.getDifficulty(), dto.getDeadline());
        taskRepository.save(task);
        return task;
    }

    /**
     * This method deletes a task by the given id, if it exists
     * @param id The id of the task to delete
     * @throws TaskManagerException Throws an exception in case the task is not found
     */
    @Override
    public void deleteTask(String id) throws TaskManagerException{
        taskRepository.deleteById(id);
    }

    /**
     * This method changes the state of a task by the given id.
     * @param id The task of the id to change state
     * @throws TaskManagerException Throws an exception if there is a problem
     * with the database or with the given id is not correct.
     */
    @Override
    public void changeStateTask(String id) throws TaskManagerException{
        Optional<Task> possibleTask = taskRepository.findById(id);
        if(possibleTask.isEmpty())throw new TaskManagerException(TaskManagerException.TASK_NOT_FOUND);
        Task task = possibleTask.get();
        task.changeState();
        taskRepository.save(task);
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
        Optional<Task> possibleTask = taskRepository.findById(dto.getId());
        if(possibleTask.isEmpty())throw new TaskManagerException(TaskManagerException.TASK_NOT_FOUND);
        Task task = possibleTask.get();
        task.changeName(dto.getName());
        task.changeDescription(dto.getDescription());
        task.setState(dto.getState());
        task.changeDeadline(dto.getDeadline());
        task.changePriority(dto.getPriority());
        task.changeDifficulty(dto.getDifficulty());
        taskRepository.save(task);
    }

    /**
     * This method returns all the tasks of the database.
     * @return A List of the task from the database.
     * @throws TaskManagerException Throws an exception if there is a problem with the database.
     */
    @Override
    public List<Task> getAllTasks() throws TaskManagerException {
        return taskRepository.findAll();
    }

    /**
     * This method return a List of Tasks with the state given.
     * @param state The state to filter the list of tasks
     * @return The list of Tasks with the same state that is given.
     * @throws TaskManagerException Throws an exception if there is a problem with the database.
     */
    @Override
    public List<Task> getTasksByState(boolean state) throws TaskManagerException{
        return taskRepository.findByState(state);
    }

    /**
     * This method returns List of Tasks that have a deadline equal or before the given date.
     * @param deadline The deadline to filter the List of Task
     * @return The list of tasks that satisfies the condition.
     * @throws TaskManagerException Throws an exception if there is a problem with the database.
     */
    @Override
    public List<Task> getTasksByDeadline(LocalDateTime deadline) throws TaskManagerException {
        return taskRepository.findByDeadline(deadline);
    }


    /**
     * This method returns List of Tasks that have the given priority.
     * @param priority The priority to filter the List of Task.
     * @return The list of tasks that satisfies the condition.
     * @throws TaskManagerException Throws an exception if there is a problem with the database.
     */
    @Override
    public List<Task> getTaskByPriority(int priority) throws TaskManagerException{
        return taskRepository.findByPriority(priority);
    }

    /**
     * This method returns List of Tasks that have the given difficulty
     * @param difficulty The difficulty to filter the List of Taks.
     * @return THe list of tasks that satisfies the condition.
     * @throws TaskManagerException Throws an exception if there is a problem with the database.
     */
    @Override
    public List<Task> getTaskByDifficulty(Difficulty difficulty) throws TaskManagerException{
        return taskRepository.findByDifficulty(difficulty);
    }


    // Este metodo genra la clave de 14 caracteres, mas el valor del contador (imposible que se repita)
    private String generateId(){
        return UUID.randomUUID().toString().replace("-", "").substring(0, 9) + this.id++;
    }
}
