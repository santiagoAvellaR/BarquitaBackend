package edu.eci.cvds.Task.services;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Difficulty;
import edu.eci.cvds.Task.models.TaskDTO;
import edu.eci.cvds.Task.models.User;
import edu.eci.cvds.Task.services.persistence.UserFilePersistenceImpl;
import edu.eci.cvds.Task.services.user.AdminServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
class AdminServiceImplTest {
    private final UserFilePersistenceImpl userFilePersistence = new UserFilePersistenceImpl("src/test/java/edu/eci/cvds/Task/services/persistence/DataTEST.txt");
    private final AdminServiceImpl adminServiceImpl = new AdminServiceImpl(new AnalyticsService(new TaskAnalysis(userFilePersistence)), userFilePersistence);


    @BeforeEach
    void setUp() {
        try{
            userFilePersistence.save(new User("TestUser1", "TestUserName1", "Password1", "email1@gmail.com"));
            userFilePersistence.save(new User("TestUser2", "TestUserName2", "Password2", "email2@gmail.com"));
            userFilePersistence.save(new User("TestUser3", "TestUserName3", "Password3", "email3@gmail.com"));
            userFilePersistence.save(new User("TestUser4", "TestUserName4", "Password4", "email4@gmail.com"));
            userFilePersistence.save(new User("TestUser5", "TestUserName5", "Password5", "email5@gmail.com"));
            saveSomeTasks("TestUser1");
            saveSomeTasks("TestUser2");
            saveSomeTasks("TestUser3");
            saveSomeTasks("TestUser4");
            saveSomeTasks("TestUser5");
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @AfterEach
    void tearDown() throws TaskManagerException{
        userFilePersistence.deleteAll();
    }

    @Test
    void getEachUserHistogram() {
        try{
            Map<String, Map<Difficulty, Long>> histogram = adminServiceImpl.getEachUserHistogram();
            for(Map.Entry<String, Map<Difficulty, Long>> entry : histogram.entrySet()){
                assertEquals(3, entry.getValue().size());
                for(Map.Entry<Difficulty, Long> data : entry.getValue().entrySet()){
                    if(data.getKey().equals(Difficulty.ALTA)){
                        assertEquals(1, data.getValue());
                    } else if(data.getKey().equals(Difficulty.MEDIA)){
                        assertEquals(2, data.getValue());
                    } else {
                        assertEquals(7, data.getValue());
                    }
                }
            }
        } catch (TaskManagerException e) {fail("Should not fail with: " + e.getMessage());}
    }

    @Test
    void getEachUserFinishedTask() {
        try{
            Map<String, Map<Integer, Long>> finishedTasks = adminServiceImpl.getEachUserFinishedTask();
            for(Map.Entry<String, Map<Integer, Long>> entry : finishedTasks.entrySet()){
                assertEquals(3, entry.getValue().size()); // Should be 3 size, because there are just 3 Difficulties.
                for(Map.Entry<Integer, Long> data : entry.getValue().entrySet()){
                    // Check the method @saveSomeTasks
                    if(data.getKey()==4)assertEquals(3, data.getValue());
                    else if(data.getKey()==10)assertEquals(1, data.getValue());
                    else {
                        assertEquals(65, data.getKey());
                        assertEquals(4, data.getValue());
                    }
                }
            }
        } catch (TaskManagerException e) {fail("Should not fail with: " + e.getMessage());}
    }

    @Test
    void getEachUserConsolidatedPriority() {
        try{
            Map<String, Map<Integer, Double>> histogram = adminServiceImpl.getEachUserConsolidatedPriority();
            for(Map.Entry<String, Map<Integer, Double>> entry : histogram.entrySet()){
                assertEquals(4, entry.getValue().size()); // Because despite having 5 priorities, the users added tasks with only 4
                for(Map.Entry<Integer, Double> data : entry.getValue().entrySet()){
                    if(data.getKey()==1) assertEquals(1, data.getValue());
                    else if(data.getKey()==2) assertEquals(2, data.getValue());
                    else if(data.getKey()==3) assertEquals(3, data.getValue());
                    else assertEquals(4, data.getValue());
                }
            }
        } catch (TaskManagerException e) {fail("Should not fail with: " + e.getMessage());}
    }

    @Test
    void getEachUserTotalTimeSpentByDifficulty() {
        try{
            Map<String, Map<Difficulty, Double>> histogram = adminServiceImpl.getEachUserTotalTimeSpentByDifficulty();
            for(Map.Entry<String, Map<Difficulty, Double>> entry : histogram.entrySet()){
                assertEquals(2, entry.getValue().size()); // Because there are 3 Difficulties, but the ALTA difficulty has state false
                for(Map.Entry<Difficulty, Double> data : entry.getValue().entrySet()){
                    if(data.getKey().equals(Difficulty.MEDIA))assertEquals(14, data.getValue());
                    else assertEquals(268, data.getValue());
                }
            }
        } catch (TaskManagerException e) { fail("Should not fail with: " + e.getMessage()); }
    }

    @Test
    void getUsersHistogram() {
        try{
            Map<Difficulty, Long> histogram = adminServiceImpl.getUsersHistogram();
            assertEquals(3, histogram.size()); // Because there are only 3 difficulties
            for(Map.Entry<Difficulty, Long> entry : histogram.entrySet()){
                    if(entry.getKey().equals(Difficulty.ALTA)) assertEquals(1*5, entry.getValue());
                    else if(entry.getKey().equals(Difficulty.MEDIA))assertEquals(2*5, entry.getValue());
                    else {
                        assertEquals(7*5, entry.getValue());
                    }
            }
        } catch (TaskManagerException e) {fail("Should not fail with: " + e.getMessage());}
    }

    @Test
    void getUsersFinishedTasks() {
        try{
            Map<Integer, Long> histogram = adminServiceImpl.getUsersFinishedTasks();
            assertEquals(3, histogram.size());
            for(Map.Entry<Integer, Long> entry : histogram.entrySet()){
                // Check the method @saveSomeTasks, there are 5 users in total, that's why its times 5
                if(entry.getKey()==4)assertEquals(3*5, entry.getValue());
                else if(entry.getKey()==10)assertEquals(1*5, entry.getValue());
                else {
                    assertEquals(65, entry.getKey());
                    assertEquals(4*5, entry.getValue());
                }
            }
        } catch (TaskManagerException e) {fail("Should not fail with: " + e.getMessage());}
    }

    @Test
    void getUsersConsolidatedPriority() {
        try{
            Map<Integer, Double> histogram = adminServiceImpl.getUsersConsolidatedPriority();
            assertEquals(4, histogram.size());
            for(Map.Entry<Integer, Double> entry : histogram.entrySet()){
                if(entry.getKey()==1) assertEquals(1*5, entry.getValue());
                else if(entry.getKey()==2) assertEquals(2*5, entry.getValue());
                else if(entry.getKey()==3) assertEquals(3*5, entry.getValue());
                else assertEquals(4*5, entry.getValue());
            }
        } catch(TaskManagerException e) {fail("Should not fail with: " + e.getMessage());}
    }

    @Test
    void getUsersTotalTimeSpentByDifficulty() {
        try{
            Map<Difficulty, Double> histogram = adminServiceImpl.getUsersTotalTimeSpentByDifficulty();
            assertEquals(2, histogram.size()); // Because all the users have just one task with priority ALTA and the state is false.
            for(Map.Entry<Difficulty, Double> entry : histogram.entrySet()){
                if(entry.getKey().equals(Difficulty.MEDIA))assertEquals(14*5, entry.getValue());
                else assertEquals(268*5, entry.getValue());
            }
        } catch(TaskManagerException e) {fail("Should not fail with: " + e.getMessage());}
    }

    @Test
    void getHistogram() {
        try{
            userFilePersistence.save(new User("Individual_ID", "Test Individual", "Test Individual", "TestIndividual@gmail.com"));
            Random random = new Random();
            int ALTA = random.nextInt(0,100), MEDIA = random.nextInt(0,100), BAJA = random.nextInt(2,100);
            saveSomeTasksHistogram("Individual_ID", ALTA,MEDIA,BAJA);
            Map<Difficulty, Long> histogram = adminServiceImpl.getHistogram("Individual_ID");
            assertEquals(ALTA, histogram.get(Difficulty.ALTA));
            assertEquals(MEDIA, histogram.get(Difficulty.MEDIA));
            assertEquals(BAJA, histogram.get(Difficulty.BAJA));
        } catch(TaskManagerException e) {fail("Should not fail with: " + e.getMessage());}
    }

    @Test
    void getFinishedTasks() {
        try{
            userFilePersistence.save(new User("Individual_ID", "Test Individual", "Test Individual", "TestIndividual@gmail.com"));
            // Agrupa por tiempo estimado
            Random rand = new Random();
            int value1 = rand.nextInt(2,100), tasks1 = rand.nextInt(2,100), value2 = rand.nextInt(2,100), tasks2=rand.nextInt(2,100), value3 = rand.nextInt(2,100), tasks3=rand.nextInt(2,100);
            saveSomeFinishedTasks("Individual_ID", value1, value2, value3, tasks1, tasks2, tasks3);
            assertEquals(3, adminServiceImpl.getFinishedTasks("Individual_ID").size());

            assertEquals(tasks1, adminServiceImpl.getFinishedTasks("Individual_ID").get(value1));
            assertEquals(tasks2, adminServiceImpl.getFinishedTasks("Individual_ID").get(value2));
            assertEquals(tasks3, adminServiceImpl.getFinishedTasks("Individual_ID").get(value3));
        } catch (TaskManagerException e) {fail("Should not fail with: " + e.getMessage());}
    }

    @Test
    void getConsolidatedPriority() {
        try{
            Random rand = new Random();
            int priority1 = rand.nextInt(2,100), priority2 = rand.nextInt(2,100), priority3=rand.nextInt(2,100), priority4=rand.nextInt(2,100), priority5=rand.nextInt(2,100);
            userFilePersistence.save(new User("Individual_ID", "Test Individual", "Test Individual", "TestIndividual@gmail.com"));
            saveSomeConsolidatedByPriority("Individual_ID", priority1, priority2, priority3, priority4, priority5);
            Map<Integer, Double> histogram = adminServiceImpl.getConsolidatedPriority("Individual_ID");
            assertEquals(5, histogram.size());
            assertEquals(priority1, histogram.get(1));
            assertEquals(priority2, histogram.get(2));
            assertEquals(priority3, histogram.get(3));
            assertEquals(priority4, histogram.get(4));
            assertEquals(priority5, histogram.get(5));

        } catch (TaskManagerException e) {fail("Should not fail with: " + e.getMessage());}
    }

    @Test
    void getTotalTimeSpentByDifficulty() {
        try{
            userFilePersistence.save(new User("Individual_ID", "Test Individual", "Test Individual", "TestIndividual@gmail.com"));
            Random random = new Random();
            int value1 = random.nextInt(2,100), value2 = random.nextInt(2,100),
                    value3=random.nextInt(2,100), timesV1 = random.nextInt(1,30),
                    timesV2 = random.nextInt(1,30), timesV3 = random.nextInt(1,30);
            saveByDifficulty("Individual_ID",value1, value2, value3, timesV1, timesV2, timesV3);
            Map<Difficulty, Double> histogram = adminServiceImpl.getTotalTimeSpentByDifficulty("Individual_ID");
            assertEquals(3, histogram.size());
            assertEquals(value1*timesV1, histogram.get(Difficulty.ALTA));
            assertEquals(value2*timesV2, histogram.get(Difficulty.MEDIA));
            assertEquals(value3*timesV3, histogram.get(Difficulty.BAJA));
        } catch(TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void deleteUser() {
        try{
            userFilePersistence.save(new User("Individual_ID", "Test Individual", "Test Individual", "TestIndividual@gmail.com"));
            assertEquals(userFilePersistence.findById("Individual_ID").get().getName(), "Test Individual");
            adminServiceImpl.deleteUser("Individual_ID");
            assertEquals(userFilePersistence.findById("Individual_ID"), Optional.empty());
        } catch (TaskManagerException e) {fail("Should not fail with: " + e.getMessage());}
    }

    private void saveSomeTasks(String userId) throws TaskManagerException{
        User user = userFilePersistence.findById(userId).get();
        user.addTask(new TaskDTO("task1" + userId, user.getName() + " Task", "Description ...", false, 1, 39, Difficulty.ALTA, LocalDateTime.now()));
        user.addTask(new TaskDTO("task2" + userId, user.getName() + " Task", "Description ...", false, 2, 88, Difficulty.BAJA, LocalDateTime.now().minusDays(1)));

        user.addTask(new TaskDTO("task3" + userId, user.getName() + " Task", "Description ...", true, 3, 4, Difficulty.BAJA, LocalDateTime.now().plusDays(3)));
        user.addTask(new TaskDTO("task4" + userId, user.getName() + " Task", "Description ...", true, 4, 10, Difficulty.MEDIA, LocalDateTime.now()));
        user.addTask(new TaskDTO("task5" + userId, user.getName() + " Task", "Description ...", true, 4, 4, Difficulty.MEDIA, LocalDateTime.now().plusDays(44)));
        user.addTask(new TaskDTO("task6" + userId, user.getName() + " Task", "Description ...", true, 2, 65, Difficulty.BAJA, LocalDateTime.now().plusDays(20)));
        user.addTask(new TaskDTO("task7" + userId, user.getName() + " Task", "Description ...", true, 3, 4, Difficulty.BAJA, LocalDateTime.now().plusDays(20)));
        user.addTask(new TaskDTO("task8" + userId, user.getName() + " Task", "Description ...", true, 3, 65, Difficulty.BAJA, LocalDateTime.now().plusDays(20)));
        user.addTask(new TaskDTO("task9" + userId, user.getName() + " Task", "Description ...", true, 4, 65, Difficulty.BAJA, LocalDateTime.now().plusDays(20)));
        user.addTask(new TaskDTO("task10" + userId, user.getName() + " Task", "Description ...", true, 4, 65, Difficulty.BAJA, LocalDateTime.now().plusDays(20)));
        userFilePersistence.save(user);
    }

    private void saveSomeTasksHistogram(String userId, int ALTA, int MEDIA, int BAJA) throws TaskManagerException{
        User user = userFilePersistence.findById(userId).get();
        for(int i = 0; i < ALTA; i++) user.addTask(new TaskDTO("task" + i, user.getName() + " Task" + i, "Description ...", true, 4, 65, Difficulty.ALTA, LocalDateTime.now().plusDays(20)));
        for(int i = 0; i < MEDIA; i++) user.addTask(new TaskDTO("task" + i, user.getName() + " Task" + i, "Description ...", true, 4, 65, Difficulty.MEDIA, LocalDateTime.now().plusDays(20)));
        for(int i = 0; i < BAJA; i++) user.addTask(new TaskDTO("task" + i, user.getName() + " Task" + i, "Description ...", true, 4, 65, Difficulty.BAJA, LocalDateTime.now().plusDays(20)));
        userFilePersistence.save(user);
    }

    private void saveSomeFinishedTasks(String userId,int value1,int  value2,int  value3,int  tasksWithValue1,int  tasksWithValue2,int  tasksWithValue3 )throws TaskManagerException{
        User user = userFilePersistence.findById(userId).get();
        for(int i = 0; i < tasksWithValue1; i ++) user.addTask(new TaskDTO("task000" + i, user.getName() + " Task" + i, "Description ...", true, 3, value1, Difficulty.ALTA, LocalDateTime.now().plusDays(20)));
        for(int i = 0; i < tasksWithValue2; i ++) user.addTask(new TaskDTO("task000" + i, user.getName() + " Task" + i, "Description ...", true, 4, value2, Difficulty.BAJA, LocalDateTime.now().plusDays(20)));
        for(int i = 0; i < tasksWithValue3; i ++) user.addTask(new TaskDTO("task000" + i, user.getName() + " Task" + i, "Description ...", true, 1, value3, Difficulty.MEDIA, LocalDateTime.now().plusDays(20)));
        // Saving more tasks but with state false, those should not appear in the finished Tasks query.
        for(int i = 0; i < tasksWithValue1; i ++) user.addTask(new TaskDTO("task000" + i, user.getName() + " Task" + i, "Description ...", false, 3, value1, Difficulty.ALTA, LocalDateTime.now().plusDays(20)));
        for(int i = 0; i < tasksWithValue2; i ++) user.addTask(new TaskDTO("task000" + i, user.getName() + " Task" + i, "Description ...", false, 4, value2, Difficulty.BAJA, LocalDateTime.now().plusDays(20)));
        for(int i = 0; i < tasksWithValue3; i ++) user.addTask(new TaskDTO("task000" + i, user.getName() + " Task" + i, "Description ...", false, 1, value3, Difficulty.MEDIA, LocalDateTime.now().plusDays(20)));
        userFilePersistence.save(user);
    }

    private void saveSomeConsolidatedByPriority(String userId, int priority1, int priority2, int priority3, int priority4, int priority5) throws TaskManagerException{
        User user = userFilePersistence.findById(userId).get();
        for(int i = 0; i < priority1; i ++) user.addTask(new TaskDTO("task000" + i, user.getName() + " Task" + i, "Description ...", true, 1, 77, Difficulty.ALTA, LocalDateTime.now().plusDays(20)));
        for(int i = 0; i < priority2; i ++) user.addTask(new TaskDTO("task000" + i, user.getName() + " Task" + i, "Description ...", true, 2, 4, Difficulty.BAJA, LocalDateTime.now().plusDays(20)));
        for(int i = 0; i < priority3; i ++) user.addTask(new TaskDTO("task000" + i, user.getName() + " Task" + i, "Description ...", true, 3, 13, Difficulty.MEDIA, LocalDateTime.now().plusDays(20)));
        for(int i = 0; i < priority4; i ++) user.addTask(new TaskDTO("task000" + i, user.getName() + " Task" + i, "Description ...", false, 4, 37, Difficulty.ALTA, LocalDateTime.now().plusDays(20)));
        for(int i = 0; i < priority5; i ++) user.addTask(new TaskDTO("task000" + i, user.getName() + " Task" + i, "Description ...", false, 5, 19, Difficulty.BAJA, LocalDateTime.now().plusDays(20)));
        userFilePersistence.save(user);
    }
    private void saveByDifficulty(String userId, int valueA, int valueM, int valueB, int timesA, int timesM, int timesB) throws TaskManagerException{
        User user = userFilePersistence.findById(userId).get();
        for(int i = 0; i < timesA; i ++) user.addTask(new TaskDTO("task000" + i, user.getName() + " Task" + i, "Description ...", true, 1, valueA, Difficulty.ALTA, LocalDateTime.now().plusDays(20)));
        for(int i = 0; i < timesM; i ++) user.addTask(new TaskDTO("task000" + i, user.getName() + " Task" + i, "Description ...", true, 2, valueM, Difficulty.MEDIA, LocalDateTime.now().plusDays(20)));
        for(int i = 0; i < timesB; i ++) user.addTask(new TaskDTO("task000" + i, user.getName() + " Task" + i, "Description ...", true, 3, valueB, Difficulty.BAJA, LocalDateTime.now().plusDays(20)));
        // Adding some tasks but with state false, those should not appear in the query
        for(int i = 0; i < timesA; i ++) user.addTask(new TaskDTO("task000" + i, user.getName() + " Task" + i, "Description ...", false, 1, valueA, Difficulty.ALTA, LocalDateTime.now().plusDays(20)));
        for(int i = 0; i < timesM; i ++) user.addTask(new TaskDTO("task000" + i, user.getName() + " Task" + i, "Description ...", false, 2, valueM, Difficulty.MEDIA, LocalDateTime.now().plusDays(20)));
        for(int i = 0; i < timesB; i ++) user.addTask(new TaskDTO("task000" + i, user.getName() + " Task" + i, "Description ...", false, 3, valueB, Difficulty.BAJA, LocalDateTime.now().plusDays(20)));
        userFilePersistence.save(user);
    }
}