package edu.eci.cvds.Task.services;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Difficulty;
import edu.eci.cvds.Task.models.User;
import edu.eci.cvds.Task.services.TaskAnalysis;
import edu.eci.cvds.Task.services.TaskAnalyticsService;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class AnalyticsService implements TaskAnalyticsService {
    private final TaskAnalysis taskAnalysis;

    public AnalyticsService(TaskAnalysis taskAnalysis) {
        this.taskAnalysis = taskAnalysis;
    }

    @Override
    public void createTasks(String userId, int numberTasks) throws TaskManagerException {
        taskAnalysis.randomData(userId, numberTasks);
    }

    @Override
    public Map<Difficulty, Long> getHistogram(String userId) throws TaskManagerException {
        return taskAnalysis.getHistogram(userId);
    }

    @Override
    public Map<Integer, Long> getFinishedTasks(String userId) throws TaskManagerException {
        return taskAnalysis.getFinishedTasks(userId);
    }

    @Override
    public Map<Integer, Double> getConsolidatedPriority(String userId) throws TaskManagerException {
        return taskAnalysis.getConsolidatedPriority(userId);
    }

    @Override
    public Map<Difficulty, Double> getTotalTimeSpentByDifficulty(String userId) throws TaskManagerException {
        return taskAnalysis.getTotalTimeSpentByDifficulty(userId);
    }
    @Override
    public void  deleteAll(String userId)throws TaskManagerException{
        taskAnalysis.deleteAllTasks(userId);
    }
}
