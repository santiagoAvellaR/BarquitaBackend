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
@CrossOrigin
public class TaskController {
    private final ServiceUser userService;

    public TaskController(ServiceUser userService){
        this.userService = userService;
    }

    @PostMapping("/createUser")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO)throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.createUser(userDTO));
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestParam String userId) throws TaskManagerException{
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body("OK");
    }

    @GetMapping("/getUser")
    public ResponseEntity<UserDTO> getUser(@RequestParam String userId) throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUser(userId));
    }
    @PostMapping("/{userId}/addTask")
    public ResponseEntity<Task> addTask(@PathVariable String userId, @RequestBody TaskDTO task) throws TaskManagerException{
        Task task1 = userService.addTask(userId, task);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(task1);
    }

    @GetMapping("/login")
    public ResponseEntity<Boolean> login(@RequestParam String userId, @RequestParam String password) throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.login(userId,password));
    }

    @GetMapping("/{userId}/getAllTasks")
    public ResponseEntity<List<Task>> getAllTasks(@PathVariable String userId) throws TaskManagerException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getAllTasks(userId));
    }


    @GetMapping("/{userId}/getTasksByState")
    public ResponseEntity<List<Task>> getTasksByState(@PathVariable String userId, @RequestParam boolean state) throws TaskManagerException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getTasksByState(userId, state));
    }

    @DeleteMapping("/{userId}/deleteTask")
    public ResponseEntity<String> deleteTask(@PathVariable String userId, @RequestParam String id) throws TaskManagerException{
        userService.deleteTask(userId, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body("OK");
    }

    @PutMapping("/{userId}/changeStateTask")
    public ResponseEntity<String> changeStateTask(@PathVariable String userId, @RequestParam String id) throws TaskManagerException {
        userService.changeStateTask(userId, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body("OK");
    }

    @PutMapping("/{userId}/updateTask")
    public ResponseEntity<String> updateTask(@PathVariable String userId, @RequestBody TaskDTO taskDTO) throws TaskManagerException{
        userService.updateTask(userId, taskDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body("OK");
    }

    @GetMapping("/{userId}/getTasksByDeadline")
    public ResponseEntity<List<Task>> getTasksByDeadline(@PathVariable String userId, @RequestParam LocalDateTime deadline) throws TaskManagerException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getTasksByDeadline(userId, deadline));
    }

    @GetMapping("/{userId}/getTaskByPriority")
    public ResponseEntity<List<Task>> getTaskByPriority(@PathVariable String userId, @RequestParam int priority) throws TaskManagerException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getTaskByPriority(userId, priority));
    }

    @GetMapping("/{userId}/getTaskByDifficulty")
    public ResponseEntity<List<Task>> getTaskByDifficulty(@PathVariable String userId, @RequestParam Difficulty difficulty) throws TaskManagerException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getTaskByDifficulty(userId, difficulty));
    }

    @GetMapping("/{userId}/getTaskByEstimatedTime")
    public ResponseEntity<List<Task>> getTaskByEstimatedTime(@PathVariable String userId, @RequestParam int estimatedTime) throws TaskManagerException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getTaskByEstimatedTime(userId, estimatedTime));
    }
}
