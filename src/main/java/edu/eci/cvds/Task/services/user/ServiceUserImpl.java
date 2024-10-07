package edu.eci.cvds.Task.services.user;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.*;
import edu.eci.cvds.Task.services.persistence.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ServiceUserImpl implements ServiceUser {
    private int id = 1;
    private final UserRepository userRepository;

    public ServiceUserImpl(UserRepository userRepository) {
        this.userRepository =userRepository;
    }

    public List<User> getUsers(){ return userRepository.findAll(); }


    @Override
    public User getUser(String id) throws TaskManagerException {
        if(userRepository.findById(id).isEmpty()) throw new TaskManagerException(TaskManagerException.USER_DOESNT_EXIST);
        return userRepository.findById(id).get();
    }
    @Override
    public User createUser(UserDTO userDTO) throws TaskManagerException{
        User user = new User(
                generateId(),
                userDTO.getName(),
                userDTO.getPassword());
        userRepository.save(user);
        return user;
    }

    @Override
    public boolean login(String username, String password) throws TaskManagerException {
        /*
        User user = userRepository.findByUsername(username);
        if(user==null) throw new TaskManagerException(TaskManagerException.USER_DOESNT_EXIST);
        return user.getPassword().equals(password);
         */
        return false;
    }

    @Override
    public Task addTask(String userId, TaskDTO dto) throws TaskManagerException {
        /*
        User user = userRepository.findByUsername(userId);
        if(user==null) throw new TaskManagerException(TaskManagerException.USER_DOESNT_EXIST);
        return user.addTask(dto);
         */
        return null;
    }

    @Override
    public void deleteTask(String userId, String id) throws TaskManagerException {

    }

    @Override
    public void changeStateTask(String userId, String id) throws TaskManagerException {

    }

    @Override
    public void updateTask(String userId, TaskDTO dto) throws TaskManagerException {

    }

    @Override
    public List<Task> getAllTasks(String userId) throws TaskManagerException {
        return List.of();
    }

    @Override
    public List<Task> getTasksByState(String userId, boolean state) throws TaskManagerException {
        return List.of();
    }

    @Override
    public List<Task> getTasksByDeadline(String userId, LocalDateTime deadline) throws TaskManagerException {
        return List.of();
    }

    @Override
    public List<Task> getTaskByPriority(String userId, int priority) throws TaskManagerException {
        return List.of();
    }

    @Override
    public List<Task> getTaskByDifficulty(String userId, Difficulty difficulty) throws TaskManagerException {
        return List.of();
    }

    @Override
    public List<Task> getTaskByEstimatedTime(String userId, int estimatedTime) throws TaskManagerException {
        return List.of();
    }

    @Override
    public void createTasks(String userId, int numberTasks) throws TaskManagerException {

    }

    @Override
    public Map<Difficulty, Long> getHistogram(String userId) throws TaskManagerException {
        return Map.of();
    }

    @Override
    public Map<Integer, Long> getFinishedTasks(String userId) throws TaskManagerException {
        return Map.of();
    }

    @Override
    public Map<Integer, Double> getConsolidatedPriority(String userId) throws TaskManagerException {
        return Map.of();
    }

    @Override
    public Map<Difficulty, Double> getTotalTimeSpentByDifficulty(String userId) throws TaskManagerException {
        return Map.of();
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();

    }
    @Override
    public void deleteUser(String id){
        userRepository.deleteById(id);
    }
    private String generateId(){
        return UUID.randomUUID().toString().replace("-", "").substring(0, 9) + this.id++;
    }
}
