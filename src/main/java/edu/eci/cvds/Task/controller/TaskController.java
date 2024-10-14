package edu.eci.cvds.Task.controller;
import edu.eci.cvds.Task.*;
import edu.eci.cvds.Task.models.*;
import edu.eci.cvds.Task.services.user.ServiceUser;
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
@CrossOrigin(origins = "https://agreeable-field-0b472e70f.5.azurestaticapps.net/")
public class TaskController {
    private final ServiceUser userService;

    public TaskController(ServiceUser userService){
        this.userService = userService;
    }

    /**
     * This method creates a user by the given Data transfer User and returns the created
     * user (with the generated id) if the event was executed successfully. Throws an exception otherwise.
     * @param userDTO The given Data Transfer User
     * @return the created User
     * @throws TaskManagerException If there is a problem with the user information or the database.
     */
    @PostMapping("/createUser")
    public ResponseEntity<TokenDTO> createUser(@RequestBody RegisterDTO userDTO)throws TaskManagerException{
        System.out.printf("%s %s %s %s", userDTO.getUsernameId(), userDTO.getName(), userDTO.getPassword(), userDTO.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.createUser(userDTO));
    }

    /**
     * This method deletes the user from the database by the given id.
     * @param userId The user id to be deleted from the DB.
     * @return The String OK if it was executed successfully. Throws an exception otherwise.
     * @throws TaskManagerException If there is a problem with the user id or the database.
     */
    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestParam String userId) throws TaskManagerException{
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body("OK");
    }

    /**
     * This method returns a user by searching it in the database by the given id.
     * @param userId The id of the user.
     * @return The user with the given id.
     * @throws TaskManagerException If there is a problem with the user id or the database.
     */
    @GetMapping("/getUser")
    public ResponseEntity<UserDTO> getUser(@RequestParam String userId) throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUser(userId));
    }

    /**
     * This method adds a task to the given id user in the database.
     * @param userId The user id to search in the database.
     * @param task The given task Transfer Object to create and assign to the user
     * @return The task created. Throws an exception otherwise
     * @throws TaskManagerException If there is a problem with the user id, the task information or the database.
     */
    @PostMapping("/{userId}/addTask")
    public ResponseEntity<Task> addTask(@PathVariable String userId, @RequestBody TaskDTO task) throws TaskManagerException{
        Task task1 = userService.addTask(userId, task);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(task1);
    }

    /**
     * This method checks if the user is correctly logged with the given email and password.
     * @param loginDTO The given data email and password
     * @return True if the id and password are in the database and assigned to the same user. False otherwise
     * @throws TaskManagerException If there is a problem with the user information or the database.
     */
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO) throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.login(loginDTO));
    }

    /**
     * This method returns the tasks of the user by the given id from the database.
     * @param userId The given user id.
     * @return The tasks as a list.
     * @throws TaskManagerException If there is a problem with the user information or the database.
     */
    @GetMapping("/{userId}/getAllTasks")
    public ResponseEntity<List<Task>> getAllTasks(@PathVariable String userId) throws TaskManagerException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getAllTasks(userId));
    }

    /**
     * Retrieves the user ID associated with the given email.
     *
     * @param email The user's email address.
     * @return A response containing the user's ID if found, with an HTTP 200 status.
     * @throws TaskManagerException If there is an issue retrieving the user ID or with the database.
     */
    @GetMapping("/getUserId")
    public ResponseEntity<UserIDTO> getUserId(@RequestParam String email) throws TaskManagerException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUserId(email));
    }

    /**
     * This method returns the tasks filtered by state.
     * @param userId The given user id.
     * @param state The given state.
     * @return The tasks with the given state from the user by the given id.
     * @throws TaskManagerException If there is a problem with the user information or the database.
     */
    @GetMapping("/{userId}/getTasksByState")
    public ResponseEntity<List<Task>> getTasksByState(@PathVariable String userId, @RequestParam boolean state) throws TaskManagerException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getTasksByState(userId, state));
    }

    /**
     * This method removes a task from the user by the given user id and the task id.
     * @param userId The given user id
     * @param id The given task id
     * @return OK if it was successfully removed, throws an exception otherwise.
     * @throws TaskManagerException If there is a problem with the user information or the database.
     */
    @DeleteMapping("/{userId}/deleteTask")
    public ResponseEntity<String> deleteTask(@PathVariable String userId, @RequestParam String id) throws TaskManagerException{
        userService.deleteTask(userId, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body("OK");
    }

    /**
     * This method changes the state of a task by the given user and task id.
     * @param userId The given user id
     * @param id The given task id
     * @return OK if it was successfully changed, throws an exception otherwise.
     * @throws TaskManagerException If there is a problem with the user information or the database.
     */
    @PutMapping("/{userId}/changeStateTask")
    public ResponseEntity<String> changeStateTask(@PathVariable String userId, @RequestParam String id) throws TaskManagerException {
        userService.changeStateTask(userId, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body("OK");
    }

    /***
     * This method updates a task by the given user id and task id
     * @param userId The given user id.
     * @param taskDTO The new updated task
     * @return OK if it was successfully changed, throws an exception otherwise.
     * @throws TaskManagerException If there is a problem with the user information or the database.
     */
    @PutMapping("/{userId}/updateTask")
    public ResponseEntity<String> updateTask(@PathVariable String userId, @RequestBody TaskDTO taskDTO) throws TaskManagerException{
        userService.updateTask(userId, taskDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body("OK");
    }

    /**
     * This method returns a list of tasks filtered by the given deadline, if the deadline is before the given one.
     * @param userId The given user id.
     * @param deadline The given deadline to filter the user tasks
     * @return The list of tasks with the given deadline condition.
     * @throws TaskManagerException If there is a problem with the user information or the database.
     */
    @GetMapping("/{userId}/getTasksByDeadline")
    public ResponseEntity<List<Task>> getTasksByDeadline(@PathVariable String userId, @RequestParam LocalDateTime deadline) throws TaskManagerException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getTasksByDeadline(userId, deadline));
    }

    /**
     * This method returns a list of tasks filtered by the given priority.
     * @param userId The given user id.
     * @param priority The given priority.
     * @return The list of tasks with the given priority.
     * @throws TaskManagerException If there is a problem with the user information or the database.
     */
    @GetMapping("/{userId}/getTaskByPriority")
    public ResponseEntity<List<Task>> getTaskByPriority(@PathVariable String userId, @RequestParam int priority) throws TaskManagerException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getTaskByPriority(userId, priority));
    }

    /**
     * This method returns the tasks by the given user id and the given difficulty.
     * @param userId The given user id.
     * @param difficulty The given task difficulty
     * @return The list of tasks with the given difficulty
     * @throws TaskManagerException If there is a problem with the user information or the database.
     */
    @GetMapping("/{userId}/getTaskByDifficulty")
    public ResponseEntity<List<Task>> getTaskByDifficulty(@PathVariable String userId, @RequestParam Difficulty difficulty) throws TaskManagerException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getTaskByDifficulty(userId, difficulty));
    }

    /**
     * This method returns a list of tasks with the given estimated time of the user with the given id.
     * @param userId THe given user id
     * @param estimatedTime The given task estimated time
     * @return The list of tasks with the given estimated time.
     * @throws TaskManagerException If there is a problem with the user information or the database.
     */
    @GetMapping("/{userId}/getTaskByEstimatedTime")
    public ResponseEntity<List<Task>> getTaskByEstimatedTime(@PathVariable String userId, @RequestParam int estimatedTime) throws TaskManagerException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getTaskByEstimatedTime(userId, estimatedTime));
    }

    @RequestMapping(value="/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        return ResponseEntity.ok().build();  // Responder 200 OK
    }
}
