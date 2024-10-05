package edu.eci.cvds.Task;

import edu.eci.cvds.Task.models.Difficulty;
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
    public void createTasks(int numberTasks) throws TaskManagerException {
        taskAnalysis.randomData(numberTasks);
    }

    @Override
    public Map<Difficulty, Long> getHistogram() throws TaskManagerException {
        return taskAnalysis.getHistogram();
    }

    @Override
    public Map<Integer, Long> getFinishedTasks() throws TaskManagerException {
        return taskAnalysis.getFinishedTasks();
    }

    @Override
    public Map<Integer, Double> getConsolidatedPriority() throws TaskManagerException {
        return taskAnalysis.getConsolidatedPriority();
    }

    @Override
    public Map<Difficulty, Double> getTotalTimeSpentByDifficulty() throws TaskManagerException {
        return taskAnalysis.getTotalTimeSpentByDifficulty();
    }
    @Override
    public void  deleteAll(){
        taskAnalysis.deleteAllTasks();
    }
}
