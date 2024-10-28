package edu.eci.cvds.Task.controller;

import edu.eci.cvds.Task.RegisterDTO;
import edu.eci.cvds.Task.RoleDTO;
import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.TokenDTO;
import edu.eci.cvds.Task.models.Difficulty;
import edu.eci.cvds.Task.services.persistence.AdminService;
import edu.eci.cvds.Task.services.user.ServiceUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * This class is the admin controller, providing the features of the admin by the given endpoints.
 * @version 1.0
 * @since 28-10-2024
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "https://agreeable-field-0b472e70f.5.azurestaticapps.net/")
public class AdminController {
    private final AdminService adminService;
    private final ServiceUser serviceUser;

    /**
     * The Admin controller builder, the admin service and the service user are injected.
     * @param adminService The admin service, that provides the facilities of an admin user.
     * @param serviceUser The service of a user that provides the facilities of a normal user.
     */
    public AdminController(AdminService adminService, ServiceUser serviceUser) {
        this.adminService = adminService;
        this.serviceUser = serviceUser;
    }

    /**
     * This method returns a histogram for each user by the respective user_id and providing for each one
     * the score for the histogram.
     * @return A Map with the histogram results for each user.
     * @throws TaskManagerException If there is any error with DB.
     */
    @GetMapping("/eachUserHistogram")
    public ResponseEntity<Map<String, Map<Difficulty, Long>>> getEachUserHistogram() throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.getEachUserHistogram());
    }

    /**
     * This method returns the finished tasks grouping by the estimated time for each user.
     * @return The Map with the given user's finished tasks grouped by estimated time and user id.
     * @throws TaskManagerException If there is any problem with the DB.
     */
    @GetMapping("/eachUserFinishedTasks")
    public ResponseEntity<Map<String, Map<Integer, Long>>> getEachUserFinishedTask()throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.getEachUserFinishedTask());
    }

    /**
     * This method returns the tasks sorted by priority for each user.
     * @return The Map with the respective user id, the priorities and the respective scores for each one.
     * @throws TaskManagerException If there is any error with the DB.
     */
    @GetMapping("/eachUserConsolidatedPriority")
    public ResponseEntity<Map<String, Map<Integer, Double>>> getEachUserConsolidatedPriority()throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.getEachUserConsolidatedPriority());
    }

    /**
     * This method returns the total time spend by difficulty for each user.
     * @return The Map with the given total time by grouping by difficulty for each user.
     * @throws TaskManagerException If there is any problem with the DB.
     */
    @GetMapping("/eachUserTotalTimeSpentByDifficulty")
    public ResponseEntity<Map<String, Map<Difficulty, Double>>> getEachUserTotalTimeSpentByDifficulty()throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.getEachUserTotalTimeSpentByDifficulty());
    }



    // For all users General

    /**
     * This method returns the total histogram of all the users
     * @return The Map with the histogram with number of all the tasks of the users sorted by difficulty.
     * @throws TaskManagerException If there is a problem with the DB.
     */
    @GetMapping("/usersHistogram")
    public ResponseEntity<Map<Difficulty, Long>> getUsersHistogram() throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.getUsersHistogram());
    }

    /**
     * This method returns the finished tasks grouped by priority of all the users.
     * @return The Map with the given tasks grouped by priority.
     * @throws TaskManagerException If there is any error with the DB.
     */
    @GetMapping("/usersFinishedTasks")
    public ResponseEntity<Map<Integer, Long>> getUsersFinishedTask() throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.getUsersFinishedTasks());
    }

    /**
     * This method returns all the users consolidated as a Map.
     * @return The consolidated priority of all the users as a Map.
     * @throws TaskManagerException If there is an error with the DB.
     */
    @GetMapping("/usersConsolidatedPriority")
    public ResponseEntity<Map<Integer, Double>> getUsersConsolidatedPriority() throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.getUsersConsolidatedPriority());
    }

    /**
     * This method returns the total time spend by difficulty of all users.
     * @return The Map with the values of the difficulty and the total spend time of all the users.
     * @throws TaskManagerException If there is any error with the DB.
     */
    @GetMapping("/usersTimeSpentByDifficulty")
    public ResponseEntity<Map<Difficulty, Double>> getUsersTimeSpentByDifficulty() throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.getUsersTotalTimeSpentByDifficulty());
    }

    // For a specific user

    /**
     * This method returns the histogram of a specific user.
     * @param userId The given id of the user.
     * @return The histogram of the given user.
     * @throws TaskManagerException If there is any error with the given id or the DB.
     */
    @GetMapping("/userHistogram")
    public ResponseEntity<Map<Difficulty, Long>> getHistogram(@RequestParam String userId) throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.getHistogram(userId));
    }

    /**
     * This method returns the finished tasks grouped by priority of the given user.
     * @param userId The given user id to generate the analysis.
     * @return The Map of priorities and it's respective scores.
     * @throws TaskManagerException If there is any error with the given id or the DB.
     */
    @GetMapping("/userFinishedTasks")
    public ResponseEntity<Map<Integer, Long>> getUserFinishedTask(@RequestParam String userId) throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.getFinishedTasks(userId));
    }

    /**
     * This method returns the consolidated priorities of a user
     * @param userId the given user id to generate the respective analysis.
     * @return The Map of consolidated priorities of a given user.
     * @throws TaskManagerException If there is any error with the given id or the DB.
     */
    @GetMapping("/userConsolidatedPriority")
    public ResponseEntity<Map<Integer, Double>> getUserConsolidatedPriority(@RequestParam String userId) throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.getConsolidatedPriority(userId));
    }

    /**
     * This method returns the total time spend of tasks grouped by difficulty of a given user.
     * @param userId The given user id to generate the analysis.
     * @return The Map of The total time spend grouped by difficulty.
     * @throws TaskManagerException If there is any error with the given id or the DB.
     */
    @GetMapping("/userTotalTimeSpentByDifficulty")
    public ResponseEntity<Map<Difficulty, Double>> getUserTotalTimeSpentByDifficulty(@RequestParam String userId) throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.getTotalTimeSpentByDifficulty(userId));
    }

    /**
     * This method deletes a user by the given id.
     * @param userId the user id to delete.
     * @return Status OK if the assignment was completed successfully.
     * @throws TaskManagerException If there is any error with the given id or the DB.
     */
    @DeleteMapping("/delUser")
    public ResponseEntity<String> delUser(@RequestParam String userId) throws TaskManagerException{
        adminService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    /**
     * This method returns all the users in the DB as RoleDTO.
     * @return The List of Users as RoleDTO.
     * @throws TaskManagerException If there is any error with the given id or the DB.
     */
    @GetMapping("/getUsers")
    public ResponseEntity<List<RoleDTO>> getUsers()throws TaskManagerException{
        return  ResponseEntity.status(HttpStatus.OK).body(adminService.getUsersDTO());
    }

    /**
     * This method creates an admin by the given UserDTO and the admin id, to verify is actually admin.
     * @param userDTO The user DTO
     * @param creatorUserId The admin user id that is creating the admin.
     * @return The TokenDTO of the new user, but the created user is Admin.
     * @throws TaskManagerException If there is any error with the given User DTO, the admin id or the DB.
     */
    @PostMapping("/createAdmin")
    public ResponseEntity<TokenDTO> createUser(@RequestBody RegisterDTO userDTO, @RequestParam String creatorUserId)throws TaskManagerException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(serviceUser.createAdmin(userDTO, creatorUserId));
    }
}
