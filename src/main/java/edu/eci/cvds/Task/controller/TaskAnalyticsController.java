package edu.eci.cvds.Task.controller;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Difficulty;
import edu.eci.cvds.Task.services.TaskAnalyticsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * The TaskAnalyticsController class is responsible for handling HTTP requests related to task analytics.
 * It serves as the intermediary between the frontend and the backend service layer for operations involving
 * analytics and metrics of tasks, such as creating tasks, generating histograms, and calculating various
 * statistics.
 *
 * @version 1.0
 * @since 04-10-2024
 */
@RestController
@CrossOrigin
@RequestMapping("/Analytics")
public class TaskAnalyticsController {

    private final TaskAnalyticsService taskAnalyticsService;

    /**
     * Constructs a new TaskAnalyticsController with the provided TaskAnalyticsService.
     *
     * @param taskAnalyticsService The TaskAnalyticsService that will handle business logic related to task analytics.
     */
    public TaskAnalyticsController(TaskAnalyticsService taskAnalyticsService) {
        this.taskAnalyticsService = taskAnalyticsService;
    }

    /**
     * Creates a specified number of tasks using the TaskAnalyticsService.
     *
     * @param numberTasks The number of tasks to create.
     * @throws TaskManagerException If there is an error while creating tasks.
     */
    @PostMapping("/createTasks")
    public void createTasks(@RequestParam String userId, @RequestParam int numberTasks) throws TaskManagerException {
        taskAnalyticsService.createTasks(userId, numberTasks);
    }

    /**
     * Retrieves a histogram that shows the number of tasks grouped by difficulty.
     *
     * @return A map where the keys are task difficulties and the values are the count of tasks for each difficulty.
     * @throws TaskManagerException If there is an error while retrieving the histogram data.
     */
    @GetMapping("/getHistogram")
    public Map<Difficulty, Long> getHistogram(@RequestParam String userId) throws TaskManagerException {
        return taskAnalyticsService.getHistogram(userId);
    }

    /**
     * Retrieves the number of finished tasks grouped by priority.
     *
     * @return A map where the keys are task priorities and the values are the count of finished tasks for each priority.
     * @throws TaskManagerException If there is an error while retrieving the data.
     */
    @GetMapping("/getFinishedTasks")
    public Map<Integer, Long> getFinishedTasks(@RequestParam String userId) throws TaskManagerException {
        return taskAnalyticsService.getFinishedTasks(userId);
    }

    /**
     * Calculates the average time spent on tasks, grouped by priority.
     *
     * @return A map where the keys are task priorities and the values are the average time spent on tasks of that priority.
     * @throws TaskManagerException If there is an error while calculating the averages.
     */
    @GetMapping("/getAverageByPriority")
    public Map<Integer, Double> getConsolidatedPriority(@RequestParam String userId) throws TaskManagerException {
        return taskAnalyticsService.getConsolidatedPriority(userId);
    }

    /**
     * Calculates the total time spent on tasks, grouped by difficulty.
     *
     * @return A map where the keys are task difficulties and the values are the total time spent on tasks of that difficulty.
     * @throws TaskManagerException If there is an error while calculating the total time.
     */
    @GetMapping("/getTotalTimeSpentByDifficulty")
    public Map<Difficulty, Double> getTotalTimeSpentByDifficulty(@RequestParam String userId) throws TaskManagerException {
        return taskAnalyticsService.getTotalTimeSpentByDifficulty(userId);
    }

    /**
     * Deletes all tasks from the system.
     * This action cannot be undone.
     */
    @DeleteMapping("/deleteAllTasks")
    public void deleteAll(@RequestParam String userId)throws TaskManagerException{
        taskAnalyticsService.deleteAll(userId);
    }
}
