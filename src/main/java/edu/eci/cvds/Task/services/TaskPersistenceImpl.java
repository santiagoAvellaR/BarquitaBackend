package edu.eci.cvds.Task.services;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Priority;
import edu.eci.cvds.Task.models.Task;
import edu.eci.cvds.Task.models.TaskDTO;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This Class simulates the simple behaviour of a Data base, saving the data
 * in plane text.
 * @version 1.0
 * @since 23-09-2024
 */
public class TaskPersistenceImpl implements TaskPersistence {
    private final File file = new File("tasks.txt");
    /**
     * This method searches a task by the given id, sends an exception otherwise
     * @param id The id of the task
     * @return The Task with the given id.
     */
    @Override
    public Optional<Task> findById(String id) {
        return findAll().stream().filter(task ->
                task.getId().equals(id)).findFirst();
    }

    @Override
    public Task save(Task task) {
        return isNew(task.getId()) ? insert(task) : merge(task);
    }
    @Override
    public void update(TaskDTO task) {
    }
    @Override
    public void deleteById(String id) throws FilePersistenceException{
        if(isNew(id)) throw new FilePersistenceException(FilePersistenceException.TAKS_NOT_FOUND);
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>();
    }

    @Override
    public List<Task> findByState(boolean state) {
        return new ArrayList<>();
    }

    @Override
    public List<Task> findByDeadline(LocalDateTime deadline) {
        return new ArrayList<>();
    }

    @Override
    public List<Task> findByPriority(Priority priority) {
        return new ArrayList<>();
    }

    private Task insert(Task task){
        return task;
    }

    private Task merge(Task task){
        // Buscar la tarea:
        return null;
    }
    private boolean isNew(String id){
        boolean duplicate = true;
        for (Task task1 : findAll()) {
            if (id.equals(task1.getId())) {
                duplicate = false;
                break;
            }
        } return duplicate;
    }
}
