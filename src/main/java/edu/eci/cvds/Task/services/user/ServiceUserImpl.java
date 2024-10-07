package edu.eci.cvds.Task.services.user;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.*;
import edu.eci.cvds.Task.services.AnalyticsService;
import edu.eci.cvds.Task.services.TaskAnalyticsService;
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
    private final TaskAnalyticsService analyticsService;

    public ServiceUserImpl(UserRepository userRepository, TaskAnalyticsService analyticsService) {
        this.userRepository =userRepository;
        this.analyticsService = analyticsService;
    }

    public List<User> getUsers(){ return userRepository.findAll(); }


    @Override
    public UserDTO getUser(String id) throws TaskManagerException {
        return findUser(id).toDTO();
    }

    private User findUser(String id) throws TaskManagerException {
        if(userRepository.findById(id).isEmpty()) throw new TaskManagerException(TaskManagerException.USER_DOESNT_EXIST);
        return userRepository.findById(id).get();
    }
    @Override
    public UserDTO createUser(UserDTO userDTO) throws TaskManagerException{
        User user = new User(
                generateId(),
                userDTO.getName(),
                userDTO.getPassword());
        userRepository.save(user);
        return user.toDTO();
    }

    @Override
    public boolean login(String username, String password) throws TaskManagerException {
        User user = findUser(username);
        if(user==null) throw new TaskManagerException(TaskManagerException.USER_DOESNT_EXIST);
        return user.getPassword().equals(password);
    }

    @Override
    public Task addTask(String userId, TaskDTO dto) throws TaskManagerException {
        User user = findUser(userId);
        if(user==null) throw new TaskManagerException(TaskManagerException.USER_DOESNT_EXIST);
        Task task = user.addTask(dto);
        userRepository.save(user);
        return task;
    }

    @Override
    public void deleteTask(String userId, String id) throws TaskManagerException {
        User user = findUser(userId);
        if(user==null) throw new TaskManagerException(TaskManagerException.USER_DOESNT_EXIST);
        user.deleteTask(id);
        userRepository.save(user);
    }

    @Override
    public void changeStateTask(String userId, String id) throws TaskManagerException {
        User user = findUser(userId);
        if(user==null) throw new TaskManagerException(TaskManagerException.USER_DOESNT_EXIST);
        user.getTasks().get(id).changeState();
        userRepository.save(user);
    }

    @Override
    public void updateTask(String userId, TaskDTO dto) throws TaskManagerException {
        User user = findUser(userId);
        if(user==null) throw new TaskManagerException(TaskManagerException.USER_DOESNT_EXIST);
        user.updateTask(dto);
        userRepository.save(user);
    }

    @Override
    public List<Task> getAllTasks(String userId) throws TaskManagerException {
        User user = findUser(userId);
        if(user==null) throw new TaskManagerException(TaskManagerException.USER_DOESNT_EXIST);
        return user.getAllTasks();
    }

    @Override
    public List<Task> getTasksByState(String userId, boolean state) throws TaskManagerException {
        User user = findUser(userId);
        if(user==null) throw new TaskManagerException(TaskManagerException.USER_DOESNT_EXIST);
        return user.getTasksByState(state);
    }

    @Override
    public List<Task> getTasksByDeadline(String userId, LocalDateTime deadline) throws TaskManagerException {
        User user = findUser(userId);
        if(user==null) throw new TaskManagerException(TaskManagerException.USER_DOESNT_EXIST);
        return user.getTasksByDeadline(deadline);
    }

    @Override
    public List<Task> getTaskByPriority(String userId, int priority) throws TaskManagerException {
        User user = findUser(userId);
        if(user==null) throw new TaskManagerException(TaskManagerException.USER_DOESNT_EXIST);
        return user.getTaskByPriority(priority);
    }

    @Override
    public List<Task> getTaskByDifficulty(String userId, Difficulty difficulty) throws TaskManagerException {
        User user = findUser(userId);
        if(user==null) throw new TaskManagerException(TaskManagerException.USER_DOESNT_EXIST);
        return user.getTaskByDifficulty(difficulty);
    }

    @Override
    public List<Task> getTaskByEstimatedTime(String userId, int estimatedTime) throws TaskManagerException {
        User user = findUser(userId);
        if(user==null) throw new TaskManagerException(TaskManagerException.USER_DOESNT_EXIST);
        return user.getTaskByEstimatedTime(estimatedTime);
    }

    @Override
    public void createTasks(String userId, int numberTasks) throws TaskManagerException {
        User user = findUser(userId);
        if(user==null) throw new TaskManagerException(TaskManagerException.USER_DOESNT_EXIST);
        //user.createTasks(numberTasks, analyticsService);
    }

    @Override
    public Map<Difficulty, Long> getHistogram(String userId) throws TaskManagerException {
        // User user = findUser()(userId);
        // return analyticsService.getHistogram(user);
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
    public void deleteUser(String id)throws TaskManagerException {
        User user = findUser(id);
        if(user==null) throw new TaskManagerException(TaskManagerException.USER_DOESNT_EXIST);
        userRepository.deleteById(id);
    }
    private String generateId(){
        return UUID.randomUUID().toString().replace("-", "").substring(0, 9) + this.id++;
    }
}
