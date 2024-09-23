package edu.eci.cvds.Task;
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
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskServiceImpl taskService){
        this.taskService = taskService;
    }

    /**
     * This method gets a TaskDTO in a JSON format from the client to be added with the interface TaskService;
     * @param task the task to add in JSON format
     * @return Confirmation of success
     * @throws TaskManagerException
     */
    @PostMapping("/addTask")
    public ResponseEntity<String> addTask(@RequestBody TaskDTO task) throws TaskManagerException {
        // Aqui creo que hay un problema porque estamos recibiendo las 'id' de las tareas
        //    Por lo que no se si deberiamos hacer que en la clase de TaskServiceImpl se
        //   asigne la 'id' (para garantizar no repetidas)

        taskService.addTask(task);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("OK");
        }
    /**
     * This method returns all the tasks without a specific order
     * @return The list of task in JSON format
     * @throws TaskManagerException
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
     * @throws TaskManagerException
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
    public ResponseEntity<String> deleteTask(@RequestParam String id){
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

    // Debo validar que este toda la informacion de la tarea.

    /**
     * This method updates a task by the given updated task
     * @param taskDTO The updated task
     * @return Confirmation of success
     * @throws TaskManagerException
     */
    @PutMapping("/updateTask")
    public ResponseEntity<String> updateTask(@RequestBody TaskDTO taskDTO) throws TaskManagerException {
        taskService.updateTask(taskDTO);
        return ResponseEntity.status(HttpStatus.FOUND)
                .body("OK");
    }

    /**
     * This method returns the tasks with the given deadline
     * @param deadline The deadline to filter the tasks
     * @return the list of tasks with the given deadline
     * @throws TaskManagerException
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
     * @throws TaskManagerException
     */
    @GetMapping("/getTaskByPriority")
    public ResponseEntity<List<Task>> getTaskByPriority(@RequestParam Priority priority) throws TaskManagerException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(taskService.getTaskByPriority(priority));
    }

}
