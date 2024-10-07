package edu.eci.cvds.Task.models;

import com.github.javafaker.Faker;
import edu.eci.cvds.Task.TaskManagerException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private User user;
    private TaskDTO taskDTO;
    @BeforeEach
    void setUp() {
        try{
            user = new User("1", "User 1", "user1");
            taskDTO = new TaskDTO("001", "Task 1", "Description 1", true, 4, 8, Difficulty.BAJA, LocalDateTime.now());
        } catch (TaskManagerException e) {fail("Should not have failed with error: " + e.getMessage());}
    }

    @AfterEach
    void tearDown() {
        user = null;
        taskDTO = null;
    }

    @Test
    void addTask() {
        try{
            user.addTask(taskDTO);
            Task task = user.getTasks().get(taskDTO.getId());
            assertEquals(taskDTO.getId(), task.getId());
            assertEquals(taskDTO.getDescription(), task.getDescription());
            assertEquals(taskDTO.getState(), task.getState());
            assertEquals(taskDTO.getDifficulty(), task.getDifficulty());
            assertEquals(taskDTO.getPriority(), task.getPriority());
            assertEquals(taskDTO.getEstimatedTime(), task.getEstimatedTime());
            assertEquals(taskDTO.getDeadline(), task.getDeadline());
        } catch (TaskManagerException e) {fail("Should not have failed with error: " + e.getMessage());}
    }

    @Test
    void deleteTask() {
        try{
            user.addTask(taskDTO);
            Task task = user.getTasks().get(taskDTO.getId());
            assertEquals(taskDTO.getId(), task.getId());
            user.deleteTask(taskDTO.getId());
            Task taskDeleted = user.getTasks().get(taskDTO.getId());
            assertNull(taskDeleted);
        } catch (TaskManagerException e) {fail("Should not have failed with error: " + e.getMessage());}
    }

    @Test
    void changeStateTask() {
        try{
            user.addTask(taskDTO);
            Task task = user.getTasks().get(taskDTO.getId());
            boolean oldState = task.getState();
            user.changeStateTask(taskDTO.getId());
            boolean newState = user.getTasks().get(taskDTO.getId()).getState();
            assertNotEquals(newState, oldState);
        } catch (TaskManagerException e) {fail("Should not have failed with error: " + e.getMessage());}
    }

    @Test
    void updateTask() {
        try{
            user.addTask(taskDTO);
            String taskId = user.getTasks().get(taskDTO.getId()).getId();
            user.updateTask(new TaskDTO(taskId, "Task Updated", "Description Updated", false, 5, 10, Difficulty.ALTA, LocalDateTime.now()));
            assertEquals("Task Updated", user.getTasks().get(taskId).getName());
            assertEquals("Description Updated", user.getTasks().get(taskId).getDescription());
            assertFalse(user.getTasks().get(taskId).getState());
            assertEquals(5, user.getTasks().get(taskId).getPriority());
            assertEquals(10, user.getTasks().get(taskId).getEstimatedTime());
            assertEquals(Difficulty.ALTA, user.getTasks().get(taskId).getDifficulty());
        } catch (TaskManagerException e) {fail("Should not have failed with error: " + e.getMessage());}
    }

    @Test
    void getAllTasks() {
        try{
            user.addTask(new TaskDTO("001", "Task 1", "Description 1", false, 5, 10, Difficulty.ALTA, LocalDateTime.now().plusDays(5)));
            user.addTask(new TaskDTO("002", "Task 2", "Description 2", false, 3, 8, Difficulty.BAJA, LocalDateTime.now()));
            user.addTask(new TaskDTO("003", "Task 3", "Description 3", true, 1, 13, Difficulty.MEDIA, LocalDateTime.now().plusDays(1)));
            user.addTask(new TaskDTO("004", "Task 4", "Description 4", true, 2, 4, Difficulty.ALTA, LocalDateTime.now().plusDays(3)));
            List<Task> tasks = user.getAllTasks();
            assertEquals(4, tasks.size());
        } catch (TaskManagerException e) {fail("Should not have failed with error: " + e.getMessage());}
    }

    @Test
    void getTasksByState() {
        try{
            addSomeTasks(30);
            List<Task> tasksTrue = user.getTasksByState(true);
            List<Task> tasksFalse = user.getTasksByState(false);
            for(Task task : tasksTrue) {
                assertTrue(task.getState());
            }
            for(Task task : tasksFalse) {
                assertFalse(task.getState());
            }
        } catch (TaskManagerException e) {fail("Should not have failed with error: " + e.getMessage());}
    }

    @Test
    void getTaskByPriority() {
        try{
            addSomeTasks(30);
            List<Task> tasks1 = user.getTaskByPriority(1);
            List<Task> tasks2 = user.getTaskByPriority(2);
            List<Task> tasks3 = user.getTaskByPriority(3);
            List<Task> tasks4 = user.getTaskByPriority(4);
            List<Task> tasks5 = user.getTaskByPriority(5);
            for(Task task : tasks1) {
                assertEquals(1, task.getPriority());
            }
            for(Task task : tasks2) {
                assertEquals(2, task.getPriority());
            }
            for(Task task : tasks3) {
                assertEquals(3, task.getPriority());
            }
            for(Task task : tasks4) {
                assertEquals(4, task.getPriority());
            }
            for(Task task : tasks5) {
                assertEquals(5, task.getPriority());
            }
        } catch (TaskManagerException e) {fail("Should not have failed with error: " + e.getMessage());}
    }

    @Test
    void shouldNotGetByPriority(){
        try{
            addSomeTasks(40);
            List<Task> tasks0 = user.getTaskByPriority(0);
            List<Task> tasks6 = user.getTaskByPriority(6);
            assertEquals(Collections.emptyList(), tasks0);
            assertEquals(Collections.emptyList(), tasks6);
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void getTaskByDifficulty() {
        try{
            addSomeTasks(35);
            List<Task> tasksA = user.getTaskByDifficulty(Difficulty.ALTA);
            List<Task> tasksM = user.getTaskByDifficulty(Difficulty.MEDIA);
            List<Task> tasksB = user.getTaskByDifficulty(Difficulty.BAJA);
            for(Task task : tasksA) {
                assertEquals(Difficulty.ALTA, task.getDifficulty());
            }
            for(Task task : tasksM) {
                assertEquals(Difficulty.MEDIA, task.getDifficulty());
            }
            for(Task task : tasksB) {
                assertEquals(Difficulty.BAJA, task.getDifficulty());
            }
        } catch (TaskManagerException e) {fail("Should not have failed with error: " + e.getMessage());}
    }

    @Test
    void getTaskByEstimatedTime() {
        try{
            addSomeTasks(150);
            for(int i = 0; i < 10; i++){
                List<Task> tasks = user.getTaskByEstimatedTime(i);
                for(Task task : tasks) {
                    assertEquals(i, task.getEstimatedTime());
                }
            }
        } catch (TaskManagerException e) {fail("Should not have failed with error: " + e.getMessage());}
    }

    @Test
    void shouldNotGetByEstimatedTime() {
        try{
            addSomeTasks(20);
            List<Task> empty = user.getTaskByEstimatedTime(11);
            assertEquals(Collections.emptyList(), empty);
        }catch (TaskManagerException e) {fail("Should not have failed with error: " + e.getMessage());}
    }

    private void addSomeTasks(int tasks)throws TaskManagerException {
        Faker faker = new Faker();
        for(int i = 0; i < tasks; i++){
            user.addTask(new TaskDTO(null,faker.name().name(),faker.funnyName().name(),faker.bool().bool(),faker.number().numberBetween(1,6),faker.number().numberBetween(1,11),generateDifficulty(faker),LocalDateTime.now()));
        }
    }

    private Difficulty generateDifficulty(Faker faker) {
        int number = faker.number().numberBetween(0, 3);
        if (number == 1) {
            return Difficulty.BAJA;
        } else if (number == 2) {
            return Difficulty.MEDIA;
        } else {
            return Difficulty.ALTA;
        }
    }
}