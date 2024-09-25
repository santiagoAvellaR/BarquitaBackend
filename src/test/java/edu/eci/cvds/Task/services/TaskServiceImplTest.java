package edu.eci.cvds.Task.services;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Priority;
import edu.eci.cvds.Task.models.Task;
import edu.eci.cvds.Task.models.TaskDTO;
import edu.eci.cvds.Task.services.persistence.FilePersistenceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
class TaskServiceImplTest {
    private TaskServiceImpl taskService;
    private LocalDateTime deadline = LocalDateTime.now().plusDays(1);
    private FilePersistenceImpl filePersistence = new FilePersistenceImpl("src/test/java/edu/eci/cvds/Task/services/persistence/DataTEST.txt");
    @BeforeEach
    void setUp(){
        filePersistence.cleanFileForTest();
        taskService = new TaskServiceImpl(filePersistence);

    }

    @Test
    void addTask() {
        try{
            Task t = taskService.addTask(new TaskDTO("1", "Task 1", "Description 1", false, Priority.ALTA, deadline));
            Task t2 = taskService.getAllTasks().stream().filter(task -> task.getId().equals(t.getId())).findFirst().get();
            assertTrue(t.equals(t2));
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }


    @Test
    void deleteTask() {
    }

    @Test
    void changeStateTask() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void getAllTasks() {
    }

    @Test
    void getTasksByState() {
    }

    @Test
    void getTasksByDeadline() {
    }

    @Test
    void getTaskByPriority() {
    }
}