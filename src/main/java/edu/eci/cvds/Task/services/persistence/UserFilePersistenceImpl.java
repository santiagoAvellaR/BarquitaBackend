package edu.eci.cvds.Task.services.persistence;
import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Difficulty;
import edu.eci.cvds.Task.models.Task;
import edu.eci.cvds.Task.models.TaskDTO;
import edu.eci.cvds.Task.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class represents a database for Users, working with plane text file to store the information.
 * @Version 1.0
 */
@Component
public class UserFilePersistenceImpl implements UserPersistence {
    private String fileName;
    private File file;

    @Autowired
    public UserFilePersistenceImpl(String fileName) {
        this.fileName = fileName;
        this.file = new File(fileName);
    }

    /**
     * This method returns an optional User by the given username id.
     * @param usernameId The username id of the user to search in the text plane file.
     * @return The user if it is found, returns Optional empty otherwise.
     * @throws TaskManagerException If there is a problem with the file operations
     */
    @Override
    public Optional<User> findById(String usernameId) throws TaskManagerException {
        Optional<User> res = Optional.empty();
        for (User u : findAll()) {
            if (u.getUsernameId().equals(usernameId)) {
                res = Optional.of(u);
            }
        }
        return res;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return findAll().stream().filter(u -> u.getEmail().equals(email)).findFirst();
    }

    /**
     * This method saves a User in the Database, if the id already exists, it updates it. It saves the User otherwise.
     * @param user The User to save.
     * @return the user saved in the file.
     * @throws TaskManagerException If there is a problem with the file operations
     */
    @Override
    public User save(User user) throws TaskManagerException {
        if (!isNew(user.getUsernameId())) {
            deleteById(user.getUsernameId());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(
                    user.getUsernameId() + "-#-" +
                            user.getName() + "-#-" +
                            user.getPassword() + "-#-" +
                            user.getEmail() + "-#-" +
                            serializeTasks(user)
            );
            writer.newLine();
        } catch (IOException e) {
            throw new TaskManagerException(TaskManagerException.DATA_BASE_FILE_ERROR);
        }
        return user;
    }

    /**
     * This method deletes a user from the text plane file if it exists.
     * @param usernameId The username id of the user to delete in the file.
     * @throws TaskManagerException If there is a problem with the file operations
     */
    @Override
    public void deleteById(String usernameId) throws TaskManagerException {
        if (isNew(usernameId)) throw new TaskManagerException(TaskManagerException.USER_DOESNT_EXIST);
        ArrayList<String> lines = searchId(usernameId);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String filteredLine : lines) {
                writer.write(filteredLine);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new TaskManagerException(TaskManagerException.DATA_BASE_FILE_ERROR);
        }
    }

    /**
     * This method returns a List of all the Users found in the file
     * @return List of all the Users found in the file
     * @throws TaskManagerException If there is a problem with the file operations
     */
    @Override
    public List<User> findAll() {
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            //throw new TaskManagerException(TaskManagerException.DATA_BASE_FILE_ERROR);
        }
        List<User> res = null;
        try{
            res=  createUsers(lines);
        } catch (TaskManagerException e) {}
        return res; // TODO -- > refactorizar
    }

    /**
     * Cleans the file for testing purposes
     * @throws TaskManagerException If there is a problem with the file operations
     */
    @Override
    public void deleteAll()  {
        try (PrintWriter writer = new PrintWriter(fileName)) {
        } catch (FileNotFoundException e) {
            //throw new TaskManagerException(TaskManagerException.DATA_BASE_FILE_ERROR);
        } // TODO
    }

    private boolean isNew(String usernameId) throws TaskManagerException {
        boolean res = true;
        for (User u : findAll()) {
            if (u.getUsernameId().equals(usernameId)) {
                res = false;
                break;
            }
        }
        return res;
    }

    private ArrayList<String> searchId(String usernameId) throws TaskManagerException {
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String key = line.split("-#-")[0];
                if (!key.equals(usernameId)) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            throw new TaskManagerException(TaskManagerException.DATA_BASE_FILE_ERROR);
        }
        return lines;
    }

    private ArrayList<User> createUsers(ArrayList<String> lines) throws TaskManagerException {
        ArrayList<User> users = new ArrayList<>();
        for (String line : lines) {
            String[] content = line.split("-#-");
            User user = new User(
                    content[0],  // usernameId
                    content[1],  // name
                    content[2],  // password
                    content[3]   // email
            );
            // Deserialize and add tasks if present
            if (content.length > 4) {
                deserializeTasks(content[4], user);
            }
            users.add(user);
        }
        return users;
    }

    private String serializeTasks(User user) throws TaskManagerException {
        StringBuilder tasksStr = new StringBuilder();
        for (Task task : user.getAllTasks()) {
            tasksStr.append(task.getId()).append(";")
                    .append(task.getName()).append(";")
                    .append(task.getDescription()).append(";")
                    .append(task.getState()).append(";")
                    .append(task.getPriority()).append(";")
                    .append(task.getEstimatedTime()).append(";")
                    .append(task.getDifficulty()).append(";")
                    .append(task.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")))
                    .append("|");
        }
        return tasksStr.toString();
    }

    private void deserializeTasks(String tasksStr, User user) throws TaskManagerException {
        if (tasksStr.isEmpty()) return;

        String[] tasks = tasksStr.split("\\|");
        for (String taskStr : tasks) {
            if (taskStr.isEmpty()) continue;

            String[] taskData = taskStr.split(";");
            Task taskDTO = new Task(
                    (taskData[0]),
            (taskData[1]),
            (taskData[2]),
            (Boolean.parseBoolean(taskData[3])),
            (Integer.parseInt(taskData[4])),
            (Integer.parseInt(taskData[5])),
            (Difficulty.valueOf(taskData[6])),
            (LocalDateTime.parse(taskData[7], DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")))
            );
            user.getTasks().put(taskDTO.getId(), taskDTO);
        }
    }
}