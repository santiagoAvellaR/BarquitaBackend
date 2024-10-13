package edu.eci.cvds.Task.services;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Difficulty;
import edu.eci.cvds.Task.models.User;

import java.util.Map;

public interface TaskAnalyticsService {
    void createTasks(String userId, int numberTasks) throws TaskManagerException;
    Map<Difficulty, Long> getHistogram(String userId)throws TaskManagerException;
    Map<Integer, Long> getFinishedTasks(String userId)throws TaskManagerException;
    Map<Integer, Double> getConsolidatedPriority(String userId)throws TaskManagerException;
    Map<Difficulty, Double> getTotalTimeSpentByDifficulty(String userId) throws TaskManagerException;
    void deleteAll(String userId) throws TaskManagerException;
}

