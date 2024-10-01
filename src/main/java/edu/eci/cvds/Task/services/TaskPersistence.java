package edu.eci.cvds.Task.services;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Priority;
import edu.eci.cvds.Task.models.Task;
import edu.eci.cvds.Task.models.TaskDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskPersistence {

    /*
    Optional<Task> findById(String id);
    Task save(Task task) throws FilePersistenceException;
    void deleteById(String id) throws FilePersistenceException;
    List<Task> findAll();
    List<Task> findByState(boolean state);
    List<Task> findByDeadline(LocalDateTime deadline);
    List<Task> findByPriority(Priority priority);
     */


    Task save(Task task) throws TaskManagerException;
    void deleteById(String id)throws TaskManagerException;
    List<Task> findAll() throws TaskManagerException;
    List<Task> findByState(boolean state) throws TaskManagerException;
    List<Task> findByDeadline(LocalDateTime deadline) throws TaskManagerException;
    List<Task> findByPriority(Priority priority) throws TaskManagerException;


    Optional<Task> findById(String id) throws TaskManagerException;


}
