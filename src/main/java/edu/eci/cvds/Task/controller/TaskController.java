package edu.eci.cvds.Task.controller;
import edu.eci.cvds.Task.*;
import edu.eci.cvds.Task.models.Priority;
import edu.eci.cvds.Task.models.Task;
import edu.eci.cvds.Task.models.TaskDTO;
import edu.eci.cvds.Task.services.FilePersistenceException;
import edu.eci.cvds.Task.services.TaskService;
import edu.eci.cvds.Task.services.TaskServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This class is the controller, the connection between the frontend and the APIRest.
 * {@code @Version} 1.0
 * {@code @Since} 22-09-2024
 */
@RestController
@CrossOrigin
public class TaskController {

    private final TaskService taskService;

    /**
     * This method is the constructor and receives the taskService by Injection.
     * @param taskService The taskService by Injection
     */
    public TaskController(TaskServiceImpl taskService){
        this.taskService = taskService;
    }

    /**
     * This method gets a TaskDTO in a JSON format from the client to be added with the interface TaskService;
     * @param task the task to add in JSON format
     * @return Confirmation of success
     * @throws TaskManagerException Throws an exception if the given information is incorrect, if the
     * given id of the TaskDTO does not exist in the database or if there is a problem with the database.
     */
    @PostMapping("/addTask")
    public ResponseEntity<Task> addTask(@RequestBody TaskDTO task) throws TaskManagerException, FilePersistenceException {
        // La id de la tarea TaskDTO debe existir, de lo contrario lanza una excepcion.
        Task task1 = taskService.addTask(task);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(task1);
        }
    /**
     * This method returns all the tasks without a specific order
     * @return The list of task in JSON format
     * @throws TaskManagerException If there is a problem with the database.
     */
    @GetMapping("/getAllTasks")
    public ResponseEntity<List<Task>> getAllTasks() throws TaskManagerException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.getAllTasks());
    }

    /**
     * This method returns all the tasks filtered by the given state
     * @param state The state of the tasks
     * @return The tasks with the given state
     * @throws TaskManagerException Throws an exception if there is a problem the database.
     */
    @GetMapping("/getTasksByState")
    public ResponseEntity<List<Task>> getTasksByState(@RequestParam boolean state) throws TaskManagerException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.getTasksByState(state));
    }

    /**
     * This method deletes a task if it exists
     * @param id The id of the task
     * @return Confirmation of success
     */
    @DeleteMapping("/deleteTask")
    public ResponseEntity<String> deleteTask(@RequestParam String id) throws TaskManagerException, FilePersistenceException {
        taskService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body("OK");
    }

    /**
     * THis method changes the state of a Task if it is found
     * @param id The id of the task
     * @return Confirmation of success
     * @throws TaskManagerException
     */
    @PutMapping("/changeStateTask")
    public ResponseEntity<String> changeStateTask(@RequestParam String id) throws TaskManagerException {
        taskService.changeStateTask(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body("OK");
    }

    /**
     * This method updates a task by the given updated task
     * @param taskDTO The updated task
     * @return Confirmation of success
     * @throws TaskManagerException Throws an exception if the given information is incorrect,
     * or if there is a problem with the database.
     */
    @PutMapping("/updateTask")
    public ResponseEntity<String> updateTask(@RequestBody TaskDTO taskDTO) throws TaskManagerException, FilePersistenceException {
        taskService.updateTask(taskDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body("OK");
    }

    /**
     * This method returns the tasks with the given deadline
     * @param deadline The deadline to filter the tasks
     * @return the list of tasks with the given deadline
     * @throws TaskManagerException If there is a problem with the database or the given deadline.
     */
    @GetMapping("/getTasksByDeadline")
    public ResponseEntity<List<Task>> getTasksByDeadline(@RequestParam LocalDateTime deadline) throws TaskManagerException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.getTasksByDeadline(deadline));
    }

    /**
     * This method returns the tasks filtered by a given priority
     * @param priority The priority to filter the tasks
     * @return The list of tasks with the given priority
     * @throws TaskManagerException If there is a problem with the database.
     */
    @GetMapping("/getTaskByPriority")
    public ResponseEntity<List<Task>> getTaskByPriority(@RequestParam Priority priority) throws TaskManagerException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.getTaskByPriority(priority));
    }

}
