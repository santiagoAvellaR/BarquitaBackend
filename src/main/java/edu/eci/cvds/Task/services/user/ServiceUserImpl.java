package edu.eci.cvds.Task.services.user;

import edu.eci.cvds.Task.*;
import edu.eci.cvds.Task.jwt.JwtService;
import edu.eci.cvds.Task.models.*;
import edu.eci.cvds.Task.services.persistence.UserPersistence;
import edu.eci.cvds.Task.services.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class ServiceUserImpl implements ServiceUser {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private int id = 1;
    @Autowired
    private final UserPersistence userRepository;



    public List<User> getUsers(){ return userRepository.findAll(); }

    /**
     * This method returns a DT User by the given id
     * @param id The given username id
     * @return If the username is not correctly
     * @throws TaskManagerException If there is a problem with the user information.
     */
    @Override
    public UserDTO getUser(String id) throws TaskManagerException {
        return findUser(id).toDTO();
    }

    /**
     * This method creates a User and returns the User DTO with the User info.
     * @param registerDTO The given User DTO to create the User in the database.
     * @return The User DTO with the created User information.
     * @throws TaskManagerException If there is a problem with the user information.
     */
    @Override
    public TokenDTO createUser(RegisterDTO registerDTO) throws TaskManagerException{
        // Should check if the user already exist.
        validateData(registerDTO);
        if(!verificateEmail(registerDTO.getEmail())){
           throw new TaskManagerException(TaskManagerException.EMAIL_IN_USE);
        }
        User user = new User(
        generateId(registerDTO.getName()),
        registerDTO.getName(),
        passwordEncoder.encode(registerDTO.getPassword()), registerDTO.getEmail());

        // IF There is not any user created, the first user created should be the ADMIN
        if(userRepository.count()==0) user.setRole(Role.ADMIN);
        userRepository.save(user);
        return TokenDTO.builder().token(jwtService.getToken(user.getUsername())).build();
    }

    // ONLY ADMIN Creates ADMIN
    public TokenDTO createAdmin(RegisterDTO registerDTO, String creatorUserId) throws TaskManagerException {
        validateData(registerDTO);
        if(!verificateEmail(registerDTO.getEmail())) {
            throw new TaskManagerException(TaskManagerException.EMAIL_IN_USE);
        }
        User creator = findUser(creatorUserId);
        if (!creator.isAdmin()) {
            throw new TaskManagerException(TaskManagerException.ADMIN_CREATE_ADMIN);
        }


        User user = new User(
                generateId(registerDTO.getName()),
                registerDTO.getName(),
                passwordEncoder.encode(registerDTO.getPassword()),
                registerDTO.getEmail());
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        return TokenDTO.builder().token(jwtService.getToken(user.getUsername())).build();
    }

    /**
     * This method returns true if the username and password are correct. False otherwise. It has to exist in the database.
     * @param loginDTO The given User DTO to create the User in the database.
     * @return True if the user true if the username and password are correct. False otherwise.
     * @throws TaskManagerException If the user does not exist in the database.
     */
    @Override
    public TokenDTO login(LoginDTO loginDTO) throws TaskManagerException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        User user = userRepository.findByEmail(loginDTO.getEmail()).orElseThrow(()->new UsernameNotFoundException("The user with email not found." + loginDTO.getEmail()));
        return TokenDTO.builder().token(jwtService.getToken(user.getUsername())).build();
    }

    @Override
    public void changePassword(String id, String password) throws TaskManagerException {
        if(notValidatePassword(password)) throw new TaskManagerException(TaskManagerException.INVALID_PASSWORD);
        User user = findUser(id);
        user.changePassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public void changeName(String id, String name) throws TaskManagerException {
        User user = findUser(id);
        user.changeName(name);
        userRepository.save(user);
    }

    /**
     * This method returns a Task created by the given user id and the TaskDTO to create.
     * @param userId The user id to add the task
     * @param dto The given task to create
     * @return The created Task.
     * @throws TaskManagerException If there is a problem with the given user id or the given task.
     */
    @Override
    public Task addTask(String userId, TaskDTO dto) throws TaskManagerException {
        User user = findUser(userId);
        Task task = user.addTask(dto);
        userRepository.save(user);
        return task;
    }

    /**
     * This method removes a task from a given user by the id of both.
     * @param userId The user id.
     * @param id The task id.
     * @throws TaskManagerException If the user or the task are not found.
     */
    @Override
    public void deleteTask(String userId, String id) throws TaskManagerException {
        User user = findUser(userId);
        user.deleteTask(id);
        userRepository.save(user);
    }

    /**
     * This method changes the state of a Task by the given user and task id.
     * @param userId The user id.
     * @param id The task id.
     * @throws TaskManagerException If the user or the task are not found.
     */
    @Override
    public void changeStateTask(String userId, String id) throws TaskManagerException {
        User user = findUser(userId);
        user.getTasks().get(id).changeState();
        userRepository.save(user);
    }

    /**
     * This method updates a task of the user with the given id and the updated Task as Data Transfer Object.
     * @param userId The given user id.
     * @param dto The given Data Transfer Task.
     * @throws TaskManagerException If the user or the task are not found or the task information is not correct.
     */
    @Override
    public void updateTask(String userId, TaskDTO dto) throws TaskManagerException {
        User user = findUser(userId);
        user.updateTask(dto);
        userRepository.save(user);
    }

    /**
     * This method returns all the tasks from a user with the given id.
     * @param userId The given user id.
     * @return The list of tasks of the user.
     * @throws TaskManagerException If the user is not found.
     */
    @Override
    public List<Task> getAllTasks(String userId) throws TaskManagerException {
        User user = findUser(userId);
        return user.getAllTasks();
    }

    /**
     * This method returns the tasks from a user with the given id and the given state
     * @param userId The given user id.
     * @param state The given state
     * @return The tasks of the user with the given state.
     * @throws TaskManagerException If the user does not exist.
     */
    @Override
    public List<Task> getTasksByState(String userId, boolean state) throws TaskManagerException {
        User user = findUser(userId);
        return user.getTasksByState(state);
    }

    /**
     * This method returns the tasks from a user with the given id and the given deadline
     * @param userId The given user id.
     * @param deadline The given deadline
     * @return The tasks of the user with the given deadline.
     * @throws TaskManagerException If the user does not exist.
     */
    @Override
    public List<Task> getTasksByDeadline(String userId, LocalDateTime deadline) throws TaskManagerException {
        User user = findUser(userId);
        return user.getTasksByDeadline(deadline);
    }
    /**
     * This method returns the tasks from a user with the given id and the given priority
     * @param userId The given user id.
     * @param priority The given priority
     * @return The tasks of the user with the given priority.
     * @throws TaskManagerException If the user does not exist.
     */
    @Override
    public List<Task> getTaskByPriority(String userId, int priority) throws TaskManagerException {
        User user = findUser(userId);
        return user.getTaskByPriority(priority);
    }
    /**
     * This method returns the tasks from a user with the given id and the given difficulty
     * @param userId The given user id.
     * @param difficulty The given difficulty
     * @return The tasks of the user with the given difficulty.
     * @throws TaskManagerException If the user does not exist.
     */
    @Override
    public List<Task> getTaskByDifficulty(String userId, Difficulty difficulty) throws TaskManagerException {
        User user = findUser(userId);
        return user.getTaskByDifficulty(difficulty);
    }
    /**
     * This method returns the tasks from a user with the given id and the given estimatedTime
     * @param userId The given user id.
     * @param estimatedTime The given estimatedTime
     * @return The tasks of the user with the given estimatedTime.
     * @throws TaskManagerException If the user does not exist.
     */
    @Override
    public List<Task> getTaskByEstimatedTime(String userId, int estimatedTime) throws TaskManagerException {
        User user = findUser(userId);
        return user.getTaskByEstimatedTime(estimatedTime);
    }

    /**
     * This method deletes all the users from the database.
     */
    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public UserIDTO getUserId(String email) throws TaskManagerException {
        if(userRepository.findByEmail(email).isEmpty()) throw new TaskManagerException(TaskManagerException.USER_DOESNT_EXIST);
        return new UserIDTO(userRepository.findByEmail(email).get().getUsernameId());
    }

    @Override
    public RoleDTO getRoleUser(String email) throws TaskManagerException {
        if(userRepository.findByEmail(email).isEmpty()) throw new TaskManagerException(TaskManagerException.USER_DOESNT_EXIST);
        return new RoleDTO(userRepository.findByEmail(email).get().getRole().toString(), userRepository.findByEmail(email).get().getUsernameId(), userRepository.findByEmail(email).get().getName(),
                userRepository.findByEmail(email).get().getEmail());
    }

    private boolean verificateEmail(String email){
        return userRepository.findByEmail(email).isEmpty();
    }
    /**
     * This method deletes a user from the database by the given id.
     * @param id The given user id
     * @throws TaskManagerException If the user does not exist in the database.
     */
    @Override
    public void deleteUser(String id)throws TaskManagerException {
        User user = findUser(id);
        if(user.getRole().toString().equals("ADMIN")){
            throw new TaskManagerException(TaskManagerException.ADMIN_SHOULD_NOT_DELETE);
        }
        userRepository.deleteById(id);
    }
    private String generateId(String name){
        return name + "_"+UUID.randomUUID().toString().replace("-", "").substring(0, 2) + this.id++;
    }
    private User findUser(String id) throws TaskManagerException {
        if(userRepository.findById(id).isEmpty()) throw new TaskManagerException(TaskManagerException.USER_DOESNT_EXIST);
        return userRepository.findById(id).get();
    }
    private boolean notValidatePassword(String password){
           return password==null||!password.matches(".*[a-z].*")||!password.matches(".*[A-Z].*")
               ||!password.matches(".*\\d.*")|| !password.matches(".*[@$!%*?&#].*")||
                   password.length() < 8;
    }
    private void validateData(RegisterDTO registerDTO) throws TaskManagerException {
        if(notValidatePassword(registerDTO.getPassword())||registerDTO.getName()==null){
            throw new TaskManagerException(TaskManagerException.INVALID_PASSWORD);
        }
        String email = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if(!Pattern.matches(email, registerDTO.getEmail())){
            throw new TaskManagerException(TaskManagerException.INVALID_EMAIL);
        }
    }

}
