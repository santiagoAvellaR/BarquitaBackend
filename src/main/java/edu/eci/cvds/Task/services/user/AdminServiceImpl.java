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

/**
 * This class implements the Admin User service and provides a solution
 * to generate the required facilities of the Admin service.
 * @version 1.0
 * @since 28-10-2024
 */
@Service
public class AdminServiceImpl implements AdminService {
    private final TaskAnalyticsService taskAnalysis;
    private final UserPersistence userPersistence;

    /**
     * This method is the AdminServiceImpl Constructor, it requires the taskAnalysis service (Autowired)
     * and a userPersistence service (Autowired too).
     * @param taskAnalysis The service of task Analysis.
     * @param userPersistence The Persistence service.
     */
    public AdminServiceImpl(TaskAnalyticsService taskAnalysis, UserPersistence userPersistence) {
        this.taskAnalysis = taskAnalysis;
        this.userPersistence = userPersistence;
    }

    /**
     * This method returns a histogram for each user by the respective user_id and providing for each one
     * the score for the histogram.
     * @return A Map with the histogram results for each user.
     * @throws TaskManagerException If there is any error with DB.
     */
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

    /**
     * This method returns the finished tasks grouping by the estimated time for each user.
     * @return The Map with the given user's finished tasks grouped by estimated time and user id.
     * @throws TaskManagerException If there is any problem with the DB.
     */
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

    /**
     * This method returns the tasks sorted by priority for each user.
     * @return The Map with the respective user id, the priorities and the respective scores for each one.
     * @throws TaskManagerException If there is any error with the DB.
     */
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

    /**
     * This method returns the total time spend by difficulty for each user.
     * @return The Map with the given total time by grouping by difficulty for each user.
     * @throws TaskManagerException If there is any problem with the DB.
     */
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

    /**
     * This method returns the total histogram of all the users
     * @return The Map with the histogram with number of all the tasks of the users sorted by difficulty.
     * @throws TaskManagerException If there is a problem with the DB.
     */
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

    /**
     * This method returns the finished tasks grouped by priority of all the users.
     * @return The Map with the given tasks grouped by priority.
     * @throws TaskManagerException If there is any error with the DB.
     */
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

    /**
     * This method returns all the users consolidated as a Map.
     * @return The consolidated priority of all the users as a Map.
     * @throws TaskManagerException If there is an error with the DB.
     */
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

    /**
     * This method returns the total time spend by difficulty of all users.
     * @return The Map with the values of the difficulty and the total spend time of all the users.
     * @throws TaskManagerException If there is any error with the DB.
     */
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

    /**
     * This method returns the histogram of a specific user.
     * @param userId The given id of the user.
     * @return The histogram of the given user.
     * @throws TaskManagerException If there is any error with the given id or the DB.
     */
    @Override
    public Map<Difficulty, Long> getHistogram(String userId) throws TaskManagerException {
        return taskAnalysis.getHistogram(userId);
    }

    /**
     * This method returns the finished tasks grouped by priority of the given user.
     * @param userId The given user id to generate the analysis.
     * @return The Map of priorities and it's respective scores.
     * @throws TaskManagerException If there is any error with the given id or the DB.
     */
    @Override
    public Map<Integer, Long> getFinishedTasks(String userId) throws TaskManagerException {
        return taskAnalysis.getFinishedTasks(userId);
    }

    /**
     * This method returns the consolidated priorities of a user
     * @param userId the given user id to generate the respective analysis.
     * @return The Map of consolidated priorities of a given user.
     * @throws TaskManagerException If there is any error with the given id or the DB.
     */
    @Override
    public Map<Integer, Double> getConsolidatedPriority(String userId) throws TaskManagerException {
        return taskAnalysis.getConsolidatedPriority(userId);
    }

    /**
     * This method returns the total time spend of tasks grouped by difficulty of a given user.
     * @param userId The given user id to generate the analysis.
     * @return The Map of The total time spend grouped by difficulty.
     * @throws TaskManagerException If there is any error with the given id or the DB.
     */
    @Override
    public Map<Difficulty, Double> getTotalTimeSpentByDifficulty(String userId) throws TaskManagerException {
        return taskAnalysis.getTotalTimeSpentByDifficulty(userId);
    }

    /**
     * This method deletes a user by the given id.
     * @param userId the user id to delete.
     * @throws TaskManagerException If there is any error with the given id or the DB.
     */
    @Override
    public void deleteUser(String userId) throws TaskManagerException {
        userPersistence.deleteById(userId);
    }

    /**
     * This method returns all the users in the DB as RoleDTO.
     * @return The List of Users as RoleDTO.
     * @throws TaskManagerException If there is any error with the given id or the DB.
     */
    @Override
    public List<RoleDTO> getUsersDTO()throws TaskManagerException{
        ArrayList<User> users = new ArrayList<>(userPersistence.findAll());
        ArrayList<RoleDTO> roleDTOS = new ArrayList<>();
        for(User user: users){
            if(!user.isAdmin()) {
                roleDTOS.add(new RoleDTO(user.getRole().toString(), user.getUsernameId(), user.getName(), user.getEmail()));
            }
        }
        return roleDTOS;


    }
}
