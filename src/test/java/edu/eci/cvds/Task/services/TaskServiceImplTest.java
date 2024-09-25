package edu.eci.cvds.Task.services;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Priority;
import edu.eci.cvds.Task.models.Task;
import edu.eci.cvds.Task.models.TaskDTO;
import edu.eci.cvds.Task.services.persistence.FilePersistenceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
class TaskServiceImplTest {
    private final LocalDateTime deadline = LocalDateTime.now().plusDays(1);
    private final FilePersistenceImpl filePersistence = new FilePersistenceImpl("src/test/java/edu/eci/cvds/Task/services/persistence/DataTEST.txt");
    private final TaskServiceImpl taskService = new TaskServiceImpl(filePersistence);

    @AfterEach
    void tearDown(){
        filePersistence.cleanFileForTest();
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
        try{
            Task t = taskService.addTask(new TaskDTO("1", "Task 1", "Description 1", false, Priority.ALTA, deadline));
            Task t2 = taskService.addTask(new TaskDTO("1", "Task 2", "Description 1", false, Priority.ALTA, deadline));
            taskService.deleteTask(t.getId());
            taskService.deleteTask(t2.getId());
            assertEquals(Optional.empty(),taskService.getAllTasks().stream().filter(task -> task.getId().equals(t.getId())).findFirst());
            assertEquals(Optional.empty(),taskService.getAllTasks().stream().filter(task -> task.getId().equals(t2.getId())).findFirst());
        }catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void changeStateTask() {
        try{
            Task t = taskService.addTask(new TaskDTO("1", "Task 1", "Description 1", false, Priority.ALTA, deadline));
            taskService.changeStateTask(t.getId());
            assertTrue(taskService.getAllTasks().stream().filter(task -> task.getId().equals(t.getId())).findFirst().get().getState());
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void updateTask() {
        try{
            Task t = taskService.addTask(new TaskDTO("1", "Task 1", "Description 1", false, Priority.ALTA, deadline));
            taskService.updateTask(new TaskDTO(t.getId(), "Other name", "New Description", true, Priority.BAJA, deadline.plusDays(1)));
            assertTrue(taskService.getAllTasks().stream().filter(task -> task.getId().equals(t.getId())).findFirst().get().getState());
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void getAllTasks() {
        try{
            List<Task> tasks = taskService.getAllTasks();
            assertTrue(tasks.isEmpty());
            taskService.addTask(new TaskDTO("1", "Task 1", "Description 1", false, Priority.ALTA, deadline.plusDays(2)));
            taskService.addTask(new TaskDTO("2", "Task 2", "Description 2", false, Priority.BAJA, deadline.plusDays(1)));
            taskService.addTask(new TaskDTO("3", "Task 3", "Description 3", false, Priority.MEDIA, deadline));
            tasks = taskService.getAllTasks();
            assertEquals(3, tasks.size());
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void getTasksByState() {
        try{
            taskService.addTask(new TaskDTO("1", "Task 1", "Description 1", false, Priority.ALTA, deadline.plusDays(2)));
            taskService.addTask(new TaskDTO("2", "Task 2", "Description 2", false, Priority.BAJA, deadline.plusDays(1)));
            taskService.addTask(new TaskDTO("3", "Task 3", "Description 3", true, Priority.MEDIA, deadline));

            taskService.getTasksByState(true).forEach(task -> assertTrue(task.getState()));
            taskService.getTasksByState(false).forEach(task -> assertFalse(task.getState()));

            assertEquals(1, taskService.getTasksByState(true).size());
            assertEquals(2, taskService.getTasksByState(false).size());
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void getTasksByDeadline() {
        try{
            taskService.addTask(new TaskDTO("fa", "Task 1", "Description 1", false, Priority.ALTA, deadline.plusDays(2)));
            taskService.addTask(new TaskDTO("65", "Task 2", "Description 2", false, Priority.BAJA, deadline));
            taskService.addTask(new TaskDTO("", "Task 3", "Description 3", true, Priority.MEDIA, deadline));

            taskService.getTasksByDeadline(deadline).forEach(task ->
                    assertEquals(deadline.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")).toString(),
                            task.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")).toString()));
            taskService.getTasksByDeadline(deadline.plusDays(2)).forEach(task ->
                    assertTrue(deadline.plusDays(2).isAfter(task.getDeadline())));

            assertEquals(2, taskService.getTasksByDeadline(deadline).size());
            assertEquals(3, taskService.getTasksByDeadline(deadline.plusDays(2)).size());
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void getTaskByPriority() {
        try{
            taskService.addTask(new TaskDTO("fa", "Task 1", "Description 1", false, Priority.ALTA, deadline.plusDays(2)));
            taskService.addTask(new TaskDTO("65", "Task 2", "Description 2", false, Priority.BAJA, deadline));
            taskService.addTask(new TaskDTO("", "Task 3", "Description 3", true, Priority.MEDIA, deadline));
            taskService.addTask(new TaskDTO("", "Task 3", "Description 3", true, Priority.MEDIA, deadline.plusDays(1)));

            taskService.getTaskByPriority(Priority.ALTA).forEach(task ->
                    assertEquals(Priority.ALTA,task.getPriority()));
            taskService.getTaskByPriority(Priority.MEDIA).forEach(task ->
                    assertEquals(Priority.MEDIA,task.getPriority()));
            taskService.getTaskByPriority(Priority.BAJA).forEach(task ->
                    assertEquals(Priority.BAJA,task.getPriority()));


            assertEquals(1, taskService.getTaskByPriority(Priority.ALTA).size());
            assertEquals(1, taskService.getTaskByPriority(Priority.BAJA).size());
            assertEquals(2, taskService.getTaskByPriority(Priority.MEDIA).size());
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }
}