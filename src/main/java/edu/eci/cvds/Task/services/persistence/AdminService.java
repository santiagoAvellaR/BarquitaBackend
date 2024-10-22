package edu.eci.cvds.Task.services.persistence;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Difficulty;

import java.util.Map;

public interface AdminService {
    // For all users, each one
    Map<String, Map<Difficulty, Long>> getEachUserHistogram() throws TaskManagerException;
    Map<String, Map<Integer, Long>> getEachUserFinishedTask()throws TaskManagerException;
    Map<String, Map<Integer, Double>> getEachUserConsolidatedPriority()throws TaskManagerException;
    Map<String, Map<Difficulty, Double>> getEachUserTotalTimeSpentByDifficulty()throws TaskManagerException;

    // For all users General
    Map<Difficulty, Long> getUsersHistogram()throws TaskManagerException;
    Map<Integer, Long> getUsersFinishedTasks()throws TaskManagerException;
    Map<Integer, Double> getUsersConsolidatedPriority()throws TaskManagerException;
    Map<Difficulty, Double> getUsersTotalTimeSpentByDifficulty() throws TaskManagerException;

    // For just one user
    Map<Difficulty, Long> getHistogram(String userId)throws TaskManagerException;
    Map<Integer, Long> getFinishedTasks(String userId)throws TaskManagerException;
    Map<Integer, Double> getConsolidatedPriority(String userId)throws TaskManagerException;
    Map<Difficulty, Double> getTotalTimeSpentByDifficulty(String userId) throws TaskManagerException;
    void deleteUser(String userId) throws TaskManagerException;
}
