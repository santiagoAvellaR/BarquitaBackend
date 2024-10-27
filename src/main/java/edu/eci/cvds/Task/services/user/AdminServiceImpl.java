package edu.eci.cvds.Task.services.user;
import edu.eci.cvds.Task.RoleDTO;
import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Difficulty;
import edu.eci.cvds.Task.models.User;
import edu.eci.cvds.Task.services.TaskAnalyticsService;
import edu.eci.cvds.Task.services.persistence.AdminService;
import edu.eci.cvds.Task.services.persistence.UserPersistence;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class AdminServiceImpl implements AdminService {
    private final TaskAnalyticsService taskAnalysis;
    private final UserPersistence userPersistence;
    public AdminServiceImpl(TaskAnalyticsService taskAnalysis, UserPersistence userPersistence) {
        this.taskAnalysis = taskAnalysis;
        this.userPersistence = userPersistence;
    }

    @Override
    public Map<String, Map<Difficulty, Long>> getEachUserHistogram() throws TaskManagerException {
        List<User> users = userPersistence.findAll();
        Map<String, Map<Difficulty, Long>> histogramTotal = new HashMap<>();
        for(User user : users) {
            Map<Difficulty, Long> histogram = taskAnalysis.getHistogram(user.getUsernameId());
            histogramTotal.put(user.getUsernameId(), histogram);
        }
        return histogramTotal;
    }

    @Override
    public Map<String, Map<Integer, Long>> getEachUserFinishedTask() throws TaskManagerException{
        Map<String, Map<Integer, Long>> histogramTotal = new HashMap<>();
        List<User> users = userPersistence.findAll();
        for(User user : users) {
            Map<Integer, Long> histogram = taskAnalysis.getFinishedTasks(user.getUsernameId());
            histogramTotal.put(user.getUsernameId(), histogram);
        }
        return histogramTotal;
    }

    @Override
    public Map<String, Map<Integer, Double>> getEachUserConsolidatedPriority() throws TaskManagerException{
        Map<String, Map<Integer, Double>> histogramTotal = new HashMap<>();
        List<User> users = userPersistence.findAll();
        for(User user : users) {
            Map<Integer, Double> histogram = taskAnalysis.getConsolidatedPriority(user.getUsernameId());
            histogramTotal.put(user.getUsernameId(), histogram);
        }
        return histogramTotal;
    }

    @Override
    public Map<String, Map<Difficulty, Double>> getEachUserTotalTimeSpentByDifficulty() throws TaskManagerException{
        Map<String, Map<Difficulty, Double>> histogramTotal = new HashMap<>();
        List<User> users = userPersistence.findAll();
        for(User user : users) {
            Map<Difficulty, Double> histogram = taskAnalysis.getTotalTimeSpentByDifficulty(user.getUsernameId());
            histogramTotal.put(user.getUsernameId(), histogram);
        }
        return histogramTotal;
    }

    @Override
    public Map<Difficulty, Long> getUsersHistogram() throws TaskManagerException {
        Map<Difficulty, Long> histogramTotal = new HashMap<>(3);
        List<User> users = userPersistence.findAll();
        for(User user : users) {
            Map<Difficulty, Long> histogram = taskAnalysis.getHistogram(user.getUsernameId());

            if(histogramTotal.containsKey(Difficulty.ALTA))
                histogramTotal.put(Difficulty.ALTA,histogramTotal.get(Difficulty.ALTA)+ histogram.get(Difficulty.ALTA));
            else
                histogramTotal.put(Difficulty.ALTA, histogram.get(Difficulty.ALTA));

            if(histogramTotal.containsKey(Difficulty.MEDIA))
                histogramTotal.put(Difficulty.MEDIA,histogramTotal.get(Difficulty.MEDIA) + histogram.get(Difficulty.MEDIA));
            else
                histogramTotal.put(Difficulty.MEDIA, histogram.get(Difficulty.MEDIA));

            if(histogramTotal.containsKey(Difficulty.BAJA))
                histogramTotal.put(Difficulty.BAJA, histogramTotal.get(Difficulty.BAJA) + histogram.get(Difficulty.BAJA));
            else
                histogramTotal.put(Difficulty.BAJA, histogram.get(Difficulty.BAJA));
        }
        return histogramTotal;
    }

    @Override
    public Map<Integer, Long> getUsersFinishedTasks() throws TaskManagerException {
        Map<Integer, Long> histogramTotal = new HashMap<>();
        List<User> users = userPersistence.findAll();
        for(User user : users) {
            Map<Integer, Long> histogram = taskAnalysis.getFinishedTasks(user.getUsernameId());

            for(Map.Entry<Integer, Long> entry : histogram.entrySet()) {
                if(histogramTotal.containsKey(entry.getKey())) {
                    histogramTotal.put(entry.getKey(), histogramTotal.get(entry.getKey()) + entry.getValue());
                } else{
                    histogramTotal.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return histogramTotal;
    }

    @Override
    public Map<Integer, Double> getUsersConsolidatedPriority() throws TaskManagerException {
        Map<Integer, Double> histogramTotal = new HashMap<>();
        List<User> users = userPersistence.findAll();
        for(User user: users) {
            Map<Integer, Double> histogram = taskAnalysis.getConsolidatedPriority(user.getUsernameId());
            for(Map.Entry<Integer, Double> entry : histogram.entrySet()) {
                if(histogramTotal.containsKey(entry.getKey())) {
                    histogramTotal.put(entry.getKey(), histogramTotal.get(entry.getKey()) + entry.getValue());
                } else{
                    histogramTotal.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return histogramTotal;
    }

    @Override
    public Map<Difficulty, Double> getUsersTotalTimeSpentByDifficulty() throws TaskManagerException {
        List<User> users = userPersistence.findAll();
        Map<Difficulty, Double> histogramTotal = new HashMap<>();
        for(User user : users) {
            Map<Difficulty, Double> histogram = taskAnalysis.getTotalTimeSpentByDifficulty(user.getUsernameId());
            for(Map.Entry<Difficulty, Double> entry : histogram.entrySet()) {
                if(histogramTotal.containsKey(entry.getKey())) {
                    histogramTotal.put(entry.getKey(), histogramTotal.get(entry.getKey()) + entry.getValue());
                } else {
                    histogramTotal.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return histogramTotal;
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
    public void deleteUser(String userId) throws TaskManagerException {
        userPersistence.deleteById(userId);
    }
    @Override
    public List<RoleDTO> getUsersDTO(){
        ArrayList<User> users = new ArrayList<>(userPersistence.findAll());
        ArrayList<RoleDTO> roleDTOS = new ArrayList<>();
        for(User user: users){
            roleDTOS.add(new RoleDTO(user.getRole().toString(), user.getUsernameId(), user.getName(), user.getEmail()));
        }
        return roleDTOS;


    }
}
