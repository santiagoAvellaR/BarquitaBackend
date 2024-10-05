package edu.eci.cvds.Task.controller;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Difficulty;
import edu.eci.cvds.Task.services.TaskAnalyticsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/Analytics")
public class TaskAnalyticsController {
    public final TaskAnalyticsService taskAnalyticsService;

    public TaskAnalyticsController(TaskAnalyticsService taskAnalyticsService) {
        this.taskAnalyticsService = taskAnalyticsService;
    }
    @PostMapping("/createTasks")
    public void createTasks(@RequestParam int numberTasks) throws TaskManagerException {
        taskAnalyticsService.createTasks(numberTasks);
    }
    @GetMapping("/getHistogram")
    public Map<Difficulty, Long> getHistogram() throws TaskManagerException {
        return taskAnalyticsService.getHistogram();
    }
    @GetMapping("/getFinishedTasks")
    public Map<Integer, Long> getFinishedTasks() throws TaskManagerException {
        return taskAnalyticsService.getFinishedTasks();
    }
    @GetMapping("/getAverageByPriority")
    public Map<Integer, Double> getAverageByPriority() throws TaskManagerException {
        return taskAnalyticsService.getAverageByPriority();
    }
    @GetMapping("/getTotalTimeSpentByDifficulty")
    public Map<Difficulty, Double> getTotalTimeSpentByDifficulty() throws TaskManagerException {
        return taskAnalyticsService.getTotalTimeSpentByDifficulty();
    }
    @DeleteMapping("/deleteAllTasks")
    public void deleteAll(){
        taskAnalyticsService.deleteAll();
    }
}
