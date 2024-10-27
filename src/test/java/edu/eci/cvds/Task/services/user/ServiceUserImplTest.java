package edu.eci.cvds.Task.services.user;

import edu.eci.cvds.Task.*;
import edu.eci.cvds.Task.jwt.JwtService;
import edu.eci.cvds.Task.models.*;
import edu.eci.cvds.Task.services.persistence.FilePersistenceImpl;
import edu.eci.cvds.Task.services.persistence.UserFilePersistenceImpl;
import org.antlr.v4.runtime.Token;
import org.apache.commons.lang3.builder.Diff;
import org.hibernate.dialect.function.TimestampdiffFunction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class  ServiceUserImplTest {
    private final LocalDateTime date = LocalDateTime.now();
    private final ServiceUserImpl serviceUser;
    @Autowired
    ServiceUserImplTest(JwtService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        UserFilePersistenceImpl filePersistence= new UserFilePersistenceImpl("src/test/java/edu/eci/cvds/Task/services/persistence/DataUserTEST.txt");
        this.serviceUser  = new ServiceUserImpl(jwtService, authenticationManager, passwordEncoder, filePersistence);
    }

    @AfterEach
    void tearDown(){
        serviceUser.deleteAll();
    }

    @Test
    void createUser() {
        try{
            assertNotNull(serviceUser.createUser(new RegisterDTO("123123", "Miguel", "Miguel1234#", "miguel@gmail.com")));
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }
    @Test
    void shouldNotCreateUserWithSameEmail() throws TaskManagerException {
        try {
            serviceUser.createUser(new RegisterDTO("123123", "Miguel", "Miguel1234#", "miguel@gmail.com"));
            serviceUser.createUser(new RegisterDTO("123123",  "Miguel", "Miguel1234#", "miguel@gmail.com"));
            fail("Did not throw exception");
        }catch (TaskManagerException e) {
            assertEquals(TaskManagerException.EMAIL_IN_USE, e.getMessage());
        }
    }

    @Test
    void login() {

        try {
            TokenDTO user = serviceUser.createUser(new RegisterDTO("123123",  "Miguel", "Miguel1234#", "miguel@gmail.com"));
            serviceUser.login(new LoginDTO("123123", "Miguel1234#", "miguel@gmail.com"));
            serviceUser.login(new LoginDTO("123123", "Miguel1234", "miguel@gmail.com"));
            fail("Did not throw exception");
        }catch (BadCredentialsException ex){
            assertEquals("Bad credentials",ex.getMessage());
        } catch (TaskManagerException e) {
            fail("Should not fail with error: " + e.getMessage());
        }
    }

    @Test
    void shouldChangeName() throws TaskManagerException {
        try{
            serviceUser.createUser(new RegisterDTO("123123", "Miguel", "Miguel1234#", "myemail@gmail.com"));
            assertEquals("Miguel", serviceUser.getUser(serviceUser.getUserId("myemail@gmail.com").getUserId()).getName());
            serviceUser.changeName(serviceUser.getUserId("myemail@gmail.com").getUserId(), "Jonas");
            assertEquals("Jonas", serviceUser.getUser(serviceUser.getUserId("myemail@gmail.com").getUserId()).getName());
        } catch (TaskManagerException e) {fail("There has been an error: " + e.getMessage());}
    }

    @Test
    void shouldChangePassword() throws TaskManagerException {
        try{
            serviceUser.createUser(new RegisterDTO("123123", "Miguel", "Miguel1234#", "myemail@gmail.com"));
            String oldPasswd = serviceUser.getUsers().get(0).getPassword();
            serviceUser.changePassword(serviceUser.getUserId("myemail@gmail.com").getUserId(), "Jonas");
            assertNotEquals(oldPasswd, serviceUser.getUsers().get(0).getPassword());
        } catch (TaskManagerException e) {fail("There has been an error: " + e.getMessage());}
    }

    @Test
    void shouldNotChangeName1() throws TaskManagerException {
        try{
            serviceUser.createUser(new RegisterDTO("123123", "Miguel", "Miguel1234#", "myemail@gmail.com"));
            serviceUser.changeName(serviceUser.getUserId("myemail@gmail.com").getUserId(), null);
            fail("Did not throw exception");
        } catch (TaskManagerException e) {
            assertEquals(TaskManagerException.INVALID_USER_NAME, e.getMessage());
        }
    }
    @Test
    void shouldNotChangeName2() throws TaskManagerException {
        try{
            serviceUser.createUser(new RegisterDTO("123123", "Miguel", "Miguel1234#", "myemail@gmail.com"));
            serviceUser.changeName(serviceUser.getUserId("myemail@gmail.com").getUserId(), "");
            fail("Did not throw exception");
        } catch (TaskManagerException e) {
            assertEquals(TaskManagerException.INVALID_USER_NAME, e.getMessage());
        }
    }

    @Test
    void addTask() {
        try{
            serviceUser.createUser(new RegisterDTO("123123","Sara", "Miguel1234#", "sara@gmail.com"));
            UserIDTO idto = serviceUser.getUserId("sara@gmail.com");

            Task task = serviceUser.addTask(idto.getUserId(), new TaskDTO("dontcare", "Study", "Description", true, 3, 2, Difficulty.BAJA,date));
            List<Task> tasks = serviceUser.getAllTasks(idto.getUserId());

            assertEquals(1, tasks.size());


            assertEquals(task.getId(), tasks.get(0).getId());
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void deleteTask() {
        try{
            serviceUser.createUser(new RegisterDTO("123123","User1", "User124#", "miguel@gmail.com"));
            UserIDTO idto = serviceUser.getUserId("miguel@gmail.com");
            Task task = serviceUser.addTask(idto.getUserId(), new TaskDTO("dontcare", "Study", "Description", true, 3, 2, Difficulty.BAJA,date));


            assertNotNull(serviceUser.getUser(idto.getUserId()).getTasks().stream().filter(task1 -> task1.getId().equals(task.getId())).findFirst().get());
            serviceUser.deleteTask(idto.getUserId(), task.getId());
        } catch(TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }



    @Test
    void changeStateTask() {
        try{
            serviceUser.createUser(new RegisterDTO("123123","User1", "User1234#", "miguel@gmail.com"));
            UserIDTO idto = serviceUser.getUserId("miguel@gmail.com");
            Task task = serviceUser.addTask(idto.getUserId(), new TaskDTO("dontcare", "Study", "Description", true, 3, 2, Difficulty.BAJA,date));
            assertTrue(serviceUser.getUser(idto.getUserId()).getTasks().stream().filter(task1 -> task1.getId().equals(task.getId())).findFirst().get().getState());
            serviceUser.changeStateTask(idto.getUserId(), task.getId());
            assertFalse(serviceUser.getUser(idto.getUserId()).getTasks().stream().filter(task1 -> task1.getId().equals(task.getId())).findFirst().get().getState());
        } catch(TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void updateTask() {
        try{
            serviceUser.createUser(new RegisterDTO("123123","User1", "User1234#", "miguel@gmail.com"));
            UserIDTO idto = serviceUser.getUserId("miguel@gmail.com");
            Task task = serviceUser.addTask(idto.getUserId(), new TaskDTO("dontcare", "Study", "Description", true, 3, 2, Difficulty.BAJA,date));
            Task serviceTask = serviceUser.getUser(idto.getUserId()).getTasks().stream().filter(task1 -> task1.getId().equals(task.getId())).findFirst().get();
            assertEquals(task.getName(),serviceTask.getName());
            assertEquals(task.getDifficulty(), serviceTask.getDifficulty());
            assertEquals(task.getDescription(),serviceTask.getDescription());
            assertEquals(task.getState(),serviceTask.getState());
            assertEquals(task.getEstimatedTime(),serviceTask.getEstimatedTime());
            assertEquals(task.getPriority(),serviceTask.getPriority());

            serviceUser.updateTask(idto.getUserId(), new TaskDTO(task.getId(), "Do exercise", "Updated Description", false, 1, 8, Difficulty.ALTA,date.plusDays(1)));
            Task updatedTask = serviceUser.getUser(idto.getUserId()).getTasks().stream().filter(task1 -> task1.getId().equals(task.getId())).findFirst().get();
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
            serviceUser.createUser(new RegisterDTO("123123","User1", "User1234#", "miguel@gmail.com"));
            UserIDTO userIDTO = serviceUser.getUserId("miguel@gmail.com");
            serviceUser.addTask(userIDTO.getUserId(), new TaskDTO("", "Study 1", "Description 1", true, 3, 1, Difficulty.ALTA,date));
            serviceUser.addTask(userIDTO.getUserId(), new TaskDTO("", "Study 2", "Description 2", true, 5, 4, Difficulty.MEDIA,date));
            serviceUser.addTask(userIDTO.getUserId(), new TaskDTO("", "Study 3", "Description 3", true, 1, 7, Difficulty.BAJA,date));
            serviceUser.addTask(userIDTO.getUserId(), new TaskDTO("", "Study 4", "Description 4", true, 4, 10, Difficulty.BAJA,date));
            List<Task> tasks = serviceUser.getAllTasks(userIDTO.getUserId());
            assertEquals(4, tasks.size());

        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void getTasksByState() {
        try{
            serviceUser.createUser(new RegisterDTO("123123","User1", "User1234#", "miguel@gmail.com"));
            UserIDTO idto = serviceUser.getUserId("miguel@gmail.com");
            addSomeTasks(idto.getUserId());
            List<Task> tasksTrue = serviceUser.getTasksByState(idto.getUserId(), true);
            List<Task> tasksFalse = serviceUser.getTasksByState(idto.getUserId(), false);
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
            serviceUser.createUser(new RegisterDTO("123123","User1", "User1234#", "miguel@gmail.com"));
            UserIDTO idto = serviceUser.getUserId("miguel@gmail.com");
            addSomeTasks(idto.getUserId());
            List<Task> tasks1 = serviceUser.getTaskByPriority(idto.getUserId(), 1);
            List<Task> tasks2 = serviceUser.getTaskByPriority(idto.getUserId(), 2);
            List<Task> tasks3 = serviceUser.getTaskByPriority(idto.getUserId(), 3);
            List<Task> tasks4 = serviceUser.getTaskByPriority(idto.getUserId(), 4);
            List<Task> tasks5 = serviceUser.getTaskByPriority(idto.getUserId(), 5);
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
            serviceUser.createUser(new RegisterDTO("123123","User1", "User1234#","miguel@gmail.com"));
            UserIDTO idto = serviceUser.getUserId("miguel@gmail.com");
            addSomeTasks(idto.getUserId());
            List<Task> tasksA = serviceUser.getTaskByDifficulty(idto.getUserId(), Difficulty.ALTA);
            List<Task> tasksM = serviceUser.getTaskByDifficulty(idto.getUserId(), Difficulty.MEDIA);
            List<Task> tasksB = serviceUser.getTaskByDifficulty(idto.getUserId(), Difficulty.BAJA);

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
            serviceUser.createUser(new RegisterDTO("123123","User1", "User1234#", "miguel@gmail.com"));
            UserIDTO idto = serviceUser.getUserId("miguel@gmail.com");
            addSomeTasks(idto.getUserId());
            List<Task> tasks1 = serviceUser.getTaskByEstimatedTime(idto.getUserId(), 4);
            List<Task> tasks2 = serviceUser.getTaskByEstimatedTime(idto.getUserId(), 3);
            List<Task> tasks3 = serviceUser.getTaskByEstimatedTime(idto.getUserId(), 10);
            for(Task task : tasks1) {
                assertEquals(4, task.getEstimatedTime());}
            for(Task task : tasks2) {
                assertEquals(3, task.getEstimatedTime());}
            for(Task task : tasks3) {
                assertEquals(10, task.getEstimatedTime());}
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
    @Test
    void shouldNotCreateUserWithInvalidPassword(){
        try {
            serviceUser.createUser(new RegisterDTO("123123", "Daniel", "hola", "daniel@gmail.com"));
        }catch (TaskManagerException e){
            assertEquals(e.getMessage(), TaskManagerException.INVALID_PASSWORD);
        }
    }
    @Test
    void shouldNotCreateUserWithInvalidEmail(){
        try {
            serviceUser.createUser(new RegisterDTO("123123", "Daniel", "Hola142*", "daniel"));
        }catch (TaskManagerException e){
            assertEquals(e.getMessage(), TaskManagerException.INVALID_EMAIL);
        }
    }

}