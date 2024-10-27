package edu.eci.cvds.Task.controller;

import edu.eci.cvds.Task.RoleDTO;
import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Difficulty;
import edu.eci.cvds.Task.services.persistence.AdminService;
import edu.eci.cvds.Task.services.user.ServiceUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "https://agreeable-field-0b472e70f.5.azurestaticapps.net/")
public class AdminController {
    private final AdminService adminService;
    private final ServiceUser serviceUser;

    public AdminController(AdminService adminService, ServiceUser serviceUser) {
        this.adminService = adminService;
        this.serviceUser = serviceUser;
    }

    @GetMapping("/eachUserHistogram")
    public ResponseEntity<Map<String, Map<Difficulty, Long>>> getEachUserHistogram(@RequestParam String userId) throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.getEachUserHistogram());
    }

    @GetMapping("/eachUserFinishedTasks")
    public ResponseEntity<Map<String, Map<Integer, Long>>> getEachUserFinishedTask()throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.getEachUserFinishedTask());
    }

    @GetMapping("/eachUserConsolidatedPriority")
    public ResponseEntity<Map<String, Map<Integer, Double>>> getEachUserConsolidatedPriority()throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.getEachUserConsolidatedPriority());
    }

    @GetMapping("/eachUserTotalTimeSpentByDifficulty")
    public ResponseEntity<Map<String, Map<Difficulty, Double>>> getAllUsers()throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.getEachUserTotalTimeSpentByDifficulty());
    }



    // For all users General

    @GetMapping("/usersHistogram")
    public ResponseEntity<Map<Difficulty, Long>> getUsersHistogram() throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.getUsersHistogram());
    }

    @GetMapping("/usersFinishedTasks")
    public ResponseEntity<Map<Integer, Long>> getUsersFinishedTask() throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.getUsersFinishedTasks());
    }

    @GetMapping("/usersConsolidatedPriority")
    public ResponseEntity<Map<Integer, Double>> getUsersConsolidatedPriority() throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.getUsersConsolidatedPriority());
    }

    @GetMapping("/usersTimeSpentByDifficulty")
    public ResponseEntity<Map<Difficulty, Double>> getUsersTimeSpentByDifficulty() throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.getUsersTotalTimeSpentByDifficulty());
    }

    // For a specific user

    @GetMapping("/userHistogram")
    public ResponseEntity<Map<Difficulty, Long>> getHistogram(@RequestParam String userId) throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.getHistogram(userId));
    }

    @GetMapping("/userFinishedTasks")
    public ResponseEntity<Map<Integer, Long>> getUserFinishedTask(@RequestParam String userId) throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.getFinishedTasks(userId));
    }

    @GetMapping("/userConsolidatedPriority")
    public ResponseEntity<Map<Integer, Double>> getUserConsolidatedPriority(@RequestParam String userId) throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.getConsolidatedPriority(userId));
    }

    @GetMapping("/userTotalTimeSpentByDifficulty")
    public ResponseEntity<Map<Difficulty, Double>> getUserTotalTimeSpentByDifficulty(@RequestParam String userId) throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.getTotalTimeSpentByDifficulty(userId));
    }

    @DeleteMapping("/delUser")
    public ResponseEntity<String> delUser(@RequestParam String userId) throws TaskManagerException{
        adminService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
    @GetMapping("/getUsers")
    public ResponseEntity<List<RoleDTO>> getUsers(){
        return  ResponseEntity.status(HttpStatus.OK).body(adminService.getUsersDTO());
    }
}
