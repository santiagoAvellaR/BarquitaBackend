package edu.eci.cvds.Task.services.user;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.*;
import org.apache.commons.lang3.builder.Diff;
import org.hibernate.dialect.function.TimestampdiffFunction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ServiceUserImplTest {

    private final LocalDateTime date = LocalDateTime.now();
    @Autowired
    private ServiceUserImpl serviceUser;

    @BeforeEach
    void setUp() {
        serviceUser.deleteAll();
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    void createUser() {
        try{
            User user = serviceUser.createUser(new UserDTO("123123", null, "Miguel", "Miguel123"));
            assertEquals("Miguel", user.getName());
            assertEquals("Miguel123", user.getPassword());
            assertEquals(Collections.emptyList(), user.getAllTasks());
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void login() {
        try{
            User user = serviceUser.createUser(new UserDTO("123123", null, "Miguel", "Miguel123"));
            boolean res = serviceUser.login(user.getUsernameId(), user.getPassword());
            assertTrue(res);
            assertFalse(serviceUser.login(user.getUsernameId(), "wrong Password"));
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void addTask() {
        try{
            User user = serviceUser.createUser(new UserDTO("123123", null, "Miguel", "Miguel123"));
            Task task = serviceUser.addTask(user.getUsernameId(), new TaskDTO("dontcare", "Study", "Description", true, 3, 2, Difficulty.BAJA,date));
            List<Task> tasks = serviceUser.getAllTasks(user.getUsernameId());
            assertEquals(1, tasks.size());
            assertEquals(task.getId(), tasks.get(0).getId());
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void deleteTask() {
        try{
            User user = serviceUser.createUser(new UserDTO("1", null, "User1", "User123"));
            Task task = serviceUser.addTask(user.getUsernameId(), new TaskDTO("dontcare", "Study", "Description", true, 3, 2, Difficulty.BAJA,date));
            assertNotNull(serviceUser.getUser(user.getUsernameId()).getTasks().get(task.getId()));
            serviceUser.deleteTask(user.getUsernameId(), task.getId());
            assertNull(serviceUser.getUser(user.getUsernameId()).getTasks().get(task.getId()));
        } catch(TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void changeStateTask() {
        try{
            User user = serviceUser.createUser(new UserDTO("1", null, "User1", "User123"));
            Task task = serviceUser.addTask(user.getUsernameId(), new TaskDTO("dontcare", "Study", "Description", true, 3, 2, Difficulty.BAJA,date));
            assertTrue(serviceUser.getUser(user.getUsernameId()).getTasks().get(task.getId()).getState());
            serviceUser.changeStateTask(user.getUsernameId(), task.getId());
            assertFalse(serviceUser.getUser(user.getUsernameId()).getTasks().get(task.getId()).getState());
        } catch(TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void updateTask() {
        try{
            User user = serviceUser.createUser(new UserDTO("1", null, "User1", "User123"));
            Task task = serviceUser.addTask(user.getUsernameId(), new TaskDTO("dontcare", "Study", "Description", true, 3, 2, Difficulty.BAJA,date));
            Task serviceTask = serviceUser.getUser(user.getUsernameId()).getTasks().get(task.getId());

            assertEquals(task.getName(),serviceTask.getName());
            assertEquals(task.getDifficulty(), serviceTask.getDifficulty());
            assertEquals(task.getDescription(),serviceTask.getDescription());
            assertEquals(task.getState(),serviceTask.getState());
            assertEquals(task.getEstimatedTime(),serviceTask.getEstimatedTime());
            assertEquals(task.getPriority(),serviceTask.getPriority());

            serviceUser.updateTask(user.getUsernameId(), new TaskDTO(task.getId(), "Do exercise", "Updated Description", false, 1, 8, Difficulty.ALTA,date.plusDays(1)));
            Task updatedTask = serviceUser.getUser(user.getUsernameId()).getTasks().get(task.getId());
            assertNotEquals(task.getName(),updatedTask.getName());
            assertNotEquals(task.getDifficulty(), updatedTask.getDifficulty());
            assertNotEquals(task.getDescription(),updatedTask.getDescription());
            assertNotEquals(task.getState(),updatedTask.getState());
            assertNotEquals(task.getEstimatedTime(),updatedTask.getEstimatedTime());
            assertNotEquals(task.getPriority(),updatedTask.getPriority());

        } catch(TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void getAllTasks() {
        try{
            User user = serviceUser.createUser(new UserDTO("1", null, "User1", "User123"));
            serviceUser.addTask(user.getUsernameId(), new TaskDTO("", "Study 1", "Description 1", true, 3, 1, Difficulty.ALTA,date));
            serviceUser.addTask(user.getUsernameId(), new TaskDTO("", "Study 2", "Description 2", true, 5, 4, Difficulty.MEDIA,date));
            serviceUser.addTask(user.getUsernameId(), new TaskDTO("", "Study 3", "Description 3", true, 1, 7, Difficulty.BAJA,date));
            serviceUser.addTask(user.getUsernameId(), new TaskDTO("", "Study 4", "Description 4", true, 4, 10, Difficulty.BAJA,date));
            List<Task> tasks = serviceUser.getAllTasks(user.getUsernameId());
            assertEquals(4, tasks.size());

        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void getTasksByState() {
        try{
            User user = serviceUser.createUser(new UserDTO("1", null, "User1", "User123"));
            addSomeTasks(user.getUsernameId());
            List<Task> tasksTrue = serviceUser.getTasksByState(user.getUsernameId(), true);
            List<Task> tasksFalse = serviceUser.getTasksByState(user.getUsernameId(), false);
            for(Task task : tasksTrue) {
                assertTrue(task.getState());
            }
            for(Task task : tasksFalse) {
                assertFalse(task.getState());
            }
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void getTaskByPriority() {
        try{
            User user = serviceUser.createUser(new UserDTO("1", null, "User1", "User123"));
            addSomeTasks(user.getUsernameId());
            List<Task> tasks1 = serviceUser.getTaskByPriority(user.getUsernameId(), 1);
            List<Task> tasks2 = serviceUser.getTaskByPriority(user.getUsernameId(), 2);
            List<Task> tasks3 = serviceUser.getTaskByPriority(user.getUsernameId(), 3);
            List<Task> tasks4 = serviceUser.getTaskByPriority(user.getUsernameId(), 4);
            List<Task> tasks5 = serviceUser.getTaskByPriority(user.getUsernameId(), 5);
            for(Task task : tasks1) {
                assertEquals(1, task.getPriority());}
            for(Task task : tasks2) {
                assertEquals(2, task.getPriority());}
            for(Task task : tasks3) {
                assertEquals(3, task.getPriority());}
            for(Task task : tasks4) {
                assertEquals(4, task.getPriority());}
            for(Task task : tasks5) {
                assertEquals(5, task.getPriority());}
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void getTaskByDifficulty() {
        try{
            User user = serviceUser.createUser(new UserDTO("1", null, "User1", "User123"));
            addSomeTasks(user.getUsernameId());
            List<Task> tasksA = serviceUser.getTaskByDifficulty(user.getUsernameId(), Difficulty.ALTA);
            List<Task> tasksM = serviceUser.getTaskByDifficulty(user.getUsernameId(), Difficulty.MEDIA);
            List<Task> tasksB = serviceUser.getTaskByDifficulty(user.getUsernameId(), Difficulty.BAJA);

            for(Task task : tasksA) {
                assertEquals(Difficulty.ALTA, task.getDifficulty());}
            for(Task task : tasksM) {
                assertEquals(Difficulty.MEDIA, task.getDifficulty());}
            for(Task task : tasksB) {
                assertEquals(Difficulty.BAJA, task.getDifficulty());}
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void getTaskByEstimatedTime() {
        try{
            User user = serviceUser.createUser(new UserDTO("1", null, "User1", "User123"));
            addSomeTasks(user.getUsernameId());
            List<Task> tasks1 = serviceUser.getTaskByEstimatedTime(user.getUsernameId(), 4);
            List<Task> tasks2 = serviceUser.getTaskByEstimatedTime(user.getUsernameId(), 3);
            List<Task> tasks3 = serviceUser.getTaskByEstimatedTime(user.getUsernameId(), 10);
            for(Task task : tasks1) {
                assertEquals(4, task.getEstimatedTime());}
            for(Task task : tasks2) {
                assertEquals(3, task.getEstimatedTime());}
            for(Task task : tasks3) {
                assertEquals(10, task.getEstimatedTime());}
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void getHistogram() {
        try{
            User user = serviceUser.createUser(new UserDTO("1", null, "User1", "User123"));
            serviceUser.createTasks(user.getUsernameId(), 500);
            Map<Difficulty, Long> histogram = serviceUser.getHistogram(user.getUsernameId());
            int total = 0;
            for(Map.Entry<Difficulty, Long> entry : histogram.entrySet()){
                total += entry.getValue();
            }
            //assertEquals(500, total);
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void getFinishedTasks() {
        try{
            User user = serviceUser.createUser(new UserDTO("1", null, "User1", "User123"));
            serviceUser.createTasks(user.getUsernameId(), 500);
            Map<Integer, Long> finished = serviceUser.getFinishedTasks(user.getUsernameId());
            int total = 0;
            for(Map.Entry<Integer, Long> entry: finished.entrySet()){
                assertTrue(entry.getValue() > 0 && entry.getValue() < 100);
                total += entry.getValue();
            }
            assertTrue(total >= 0 && total <= 1000);
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void deleteAll() {
        serviceUser.deleteAll();
        List<User> users = serviceUser.getUsers();
        assertTrue(users.isEmpty());
    }
    private void addSomeTasks(String userId)throws TaskManagerException{
        serviceUser.addTask(userId, new TaskDTO("", "Study 1", "Description 1", true, 3, 4, Difficulty.ALTA,date));
        serviceUser.addTask(userId, new TaskDTO("", "Study 2", "Description 2", true, 5, 4, Difficulty.MEDIA,date));
        serviceUser.addTask(userId, new TaskDTO("", "Study 3", "Description 3", true, 1, 4, Difficulty.BAJA,date));
        serviceUser.addTask(userId, new TaskDTO("", "Study 4", "Description 4", true, 4, 3, Difficulty.BAJA,date));
        serviceUser.addTask(userId, new TaskDTO("", "Study 5", "Description 5", true, 4, 3, Difficulty.BAJA,date));
        serviceUser.addTask(userId, new TaskDTO("", "Study 6", "Description 6", false, 5, 3, Difficulty.ALTA,date));
        serviceUser.addTask(userId, new TaskDTO("", "Study 7", "Description 7", false, 5, 3, Difficulty.ALTA,date));
        serviceUser.addTask(userId, new TaskDTO("", "Study 8", "Description 8", false, 5, 10, Difficulty.MEDIA,date));
        serviceUser.addTask(userId, new TaskDTO("", "Study 9", "Description 9", false, 4, 10, Difficulty.MEDIA,date));
        serviceUser.addTask(userId, new TaskDTO("", "Study 10", "Description 10", true, 4, 10, Difficulty.ALTA,date));
    }
}