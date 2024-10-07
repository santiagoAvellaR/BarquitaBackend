package edu.eci.cvds.Task.services;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Difficulty;

import java.util.Map;

public interface TaskAnalyticsService {
    void createTasks(int numberTasks) throws TaskManagerException;
    Map<Difficulty, Long> getHistogram()throws TaskManagerException;
    Map<Integer, Long> getFinishedTasks()throws TaskManagerException;
    Map<Integer, Double> getConsolidatedPriority()throws TaskManagerException;
    Map<Difficulty, Double> getTotalTimeSpentByDifficulty() throws TaskManagerException;
    void deleteAll() throws TaskManagerException;
}

