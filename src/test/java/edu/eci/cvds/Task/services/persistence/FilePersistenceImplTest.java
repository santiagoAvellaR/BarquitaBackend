package edu.eci.cvds.Task.services.persistence;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Difficulty;
import edu.eci.cvds.Task.models.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FilePersistenceImplTest {

    private FilePersistenceImpl filePersistence;
    private final LocalDateTime deadline = LocalDateTime.now().plusDays(1);
    private final List<Task> tasks = generateTasks();

    private List<Task> generateTasks(){
        List<Task> taskArray = null;
        try {
            taskArray = new ArrayList<>(List.of(
                    new Task("aurqtwkbcmdhga1", "Task 1", "Description 1", false, 2,10,Difficulty.ALTA, deadline.plusDays(1)),
                    new Task("2", "Task 2", "Description 2", false, 1,10,Difficulty.ALTA, deadline.plusDays(2)),
                    new Task("3", "Task 3", "Description 3", false, 3,10, Difficulty.MEDIA, deadline.plusDays(3)),
                    new Task("4", "Task 4", "Description 4", false, 3,10,Difficulty.BAJA, deadline.plusDays(4))
            ));
        }catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
        return taskArray;
    }

    @BeforeEach
    void setUp() {
        filePersistence = new FilePersistenceImpl("src/test/java/edu/eci/cvds/Task/services/persistence/DataTEST.txt");
        try {
            for(Task task : tasks){
                filePersistence.save(task);
            }
        } catch (TaskManagerException e ){fail("Should not fail with error: " + e.getMessage());}
    }

    @AfterEach
    void tearDown(){
        try{
            filePersistence.cleanFileForTest();
        } catch(TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void shouldFindById()throws TaskManagerException {
        try{
            Task task = filePersistence.findById(tasks.get(0).getId()).get();
            assertEquals("Task 1", task.getName());
        } catch (TaskManagerException e ) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void shouldSave() throws TaskManagerException {
        Task task = new Task("5", "Task 5", "Description 5", false, 1, 10,Difficulty.BAJA, deadline.plusDays(4));
        filePersistence.save(task);
        assertTrue(filePersistence.findById(task.getId()).get().equals(task));
        filePersistence.deleteById(task.getId());
    }

    @Test
    void shouldDeleteById() throws TaskManagerException {
        Task newTask1 = new Task("6", "Task NN", "NN Description", true, 1,10, Difficulty.ALTA, deadline.plusDays(2));
        Task newTask2 = new Task("D", "Task NN", "NN Description", true, 1, 10,Difficulty.ALTA, deadline.plusDays(2));
        filePersistence.save(newTask1);
        filePersistence.save(newTask2);

        filePersistence.deleteById(newTask1.getId());
        filePersistence.deleteById(newTask2.getId());

        assertEquals(filePersistence.findById(newTask1.getId()), Optional.empty());
        assertEquals(filePersistence.findById(newTask2.getId()), Optional.empty());
    }


    @Test
    void shouldFindAll() throws TaskManagerException {
        List<Task> foundList = filePersistence.findAll();
        for(int i = 0; i < tasks.size(); i++){
            assertTrue(tasks.get(i).equals(foundList.get(i)));
        }
    }

    @Test
    void shouldFindByState() throws TaskManagerException {
        List<Task> trueTasks = filePersistence.findByState(true);
        List<Task> falseTasks = filePersistence.findByState(false);
        assertNotNull(trueTasks);
        assertEquals(falseTasks.size(), tasks.size());
        for(Task task : falseTasks){
            assertFalse(task.getState());
        }
    }

    @Test
    void shouldFindByDeadline() throws TaskManagerException {
        List<Task> trueTasks = filePersistence.findByDeadline(deadline.plusDays(1));
        assertEquals(1, trueTasks.size());
        for(Task task : trueTasks){
            assertEquals(task.getDeadline().toString(),
                    deadline.plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")).toString());
        }

    }

    @Test
    void shouldFindByDifficulty() throws TaskManagerException {
        List<Task> tasksDifficulty = filePersistence.findByDifficulty(Difficulty.ALTA);
        assertEquals(2, tasksDifficulty.size());
        for(Task task : tasksDifficulty){
            assertEquals(task.getDifficulty(), Difficulty.ALTA);
        }
    }

    @Test
    void shouldNotFindById() throws TaskManagerException {
        String id = tasks.get(0).getId() + "NOT_EXIST_ID";
        assertEquals(filePersistence.findById(id), Optional.empty());
    }

    @Test
    void shouldNotSaveDuplicates() throws TaskManagerException {
        Task newTask = new Task("NEW", "Task NN", "NN Description", true, 1, 10,Difficulty.ALTA, deadline.plusDays(2));
        filePersistence.save(newTask);
        filePersistence.save(newTask);
        filePersistence.save(newTask);
        filePersistence.save(newTask);
        assertEquals(filePersistence.findAll().size(), tasks.size() + 1);
        filePersistence.deleteById(newTask.getId());
    }


    @Test
    void shouldNotDeleteById(){
        try{
            filePersistence.deleteById("NOT AN ID");
        } catch (TaskManagerException e){
            assertEquals(TaskManagerException.TASK_NOT_FOUND, e.getMessage());
        }
    }

    @Test
    void shouldChangeState() throws TaskManagerException {
        Task task = filePersistence.findById(tasks.get(0).getId()).get();
        assertFalse(task.getState());
        task.changeState();
        assertTrue(task.getState());
        filePersistence.save(task);
        assertTrue(filePersistence.findById(task.getId()).get().getState());
    }

    @Test
    void shouldNotChangeState() throws TaskManagerException {
        Task task = filePersistence.findById(tasks.get(0).getId()).get();
        assertFalse(task.getState());
        task.changeState();
        assertTrue(task.getState());
        assertFalse(filePersistence.findById(task.getId()).get().getState());
    }

    @Test
    void shouldUpdateTask() throws TaskManagerException {
        Task task = filePersistence.findById(tasks.get(0).getId()).get();
        task.setDescription("NEW UPDATING DESCRIPTION");
        filePersistence.save(task);
        assertEquals(filePersistence.findById(task.getId()).get().getDescription(), "NEW UPDATING DESCRIPTION");
    }

    @Test
    void shouldFindByPriority1(){
        try{
            List<Task> tasks2 = filePersistence.findByPriority(2);
            List<Task> tasks1 = filePersistence.findByPriority(1);
            List<Task> tasks3 = filePersistence.findByPriority(3);
            assertEquals(1, tasks2.size());
            assertEquals(1, tasks1.size());
            assertEquals(2, tasks3.size());
        } catch (TaskManagerException e){fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void shouldFindByPriority2(){
        try{
            saveSomeTasks();
            List<Task> tasks1 = filePersistence.findByPriority(1);
            List<Task> tasks2 = filePersistence.findByPriority(2);
            List<Task> tasks3 = filePersistence.findByPriority(3);
            List<Task> tasks4 = filePersistence.findByPriority(4);
            List<Task> tasks5 = filePersistence.findByPriority(5);
            for(Task task : tasks1){
                assertEquals(1,task.getPriority());
            }
            for(Task task : tasks2){
                assertEquals(2,task.getPriority());
            }
            for(Task task : tasks3){
                assertEquals(3,task.getPriority());
            }
            for(Task task : tasks4){
                assertEquals(4,task.getPriority());
            }
            for(Task task : tasks5){
                assertEquals(5,task.getPriority());
            }
        } catch (TaskManagerException e){fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void shouldNotFindByPriority(){
        try{
            List<Task> tasksEmpty = filePersistence.findByPriority(6);
            assertEquals(Collections.emptyList(), tasksEmpty);
        } catch (TaskManagerException e){fail("Should not fail with error: " + e.getMessage());}
    }
    @Test
    void shouldNotFindByDeadline() throws TaskManagerException {
        List<Task> tasks1 = filePersistence.findByDeadline(LocalDateTime.now().minusDays(1));
        assertEquals(tasks1.size(), 0);
    }

    @Test
    void shouldNotFindByDifficulty() throws TaskManagerException {
        Task task = filePersistence.findById("4").get();
        task.setDifficulty(Difficulty.ALTA);
        filePersistence.save(task);
        List<Task> tasks1 = filePersistence.findByDifficulty(Difficulty.BAJA);
        assertEquals(tasks1.size(), 0);
    }

    private void saveSomeTasks() throws TaskManagerException {
        filePersistence.save(new Task("01", "Task 1", "Description 1", false, 1, 10,Difficulty.MEDIA, deadline.plusDays(2)));
        filePersistence.save(new Task("02", "Task 2", "Description 2", false, 2, 10,Difficulty.MEDIA, deadline.plusDays(2)));
        filePersistence.save(new Task("03", "Task 3", "Description 3", false, 3, 10,Difficulty.MEDIA, deadline.plusDays(4)));
        filePersistence.save(new Task("04", "Task 4", "Description 4", false, 4, 10,Difficulty.MEDIA, deadline.plusDays(4)));
        filePersistence.save(new Task("05", "Task 5", "Description 5", true, 5, 10,Difficulty.BAJA, deadline.plusDays(4)));
        filePersistence.save(new Task("06", "Task 6", "Description 6", true, 5, 10,Difficulty.BAJA, deadline.plusDays(4)));
        filePersistence.save(new Task("07", "Task 7", "Description 7", true, 5, 10,Difficulty.BAJA, deadline.plusDays(1)));
        filePersistence.save(new Task("08", "Task 8", "Description 8", true, 5, 10,Difficulty.BAJA, deadline.plusDays(1)));
        filePersistence.save(new Task("09", "Task 9", "Description 9", true, 5, 10,Difficulty.BAJA, deadline.plusDays(1)));
        filePersistence.save(new Task("010", "Task 10", "Description 10", false, 5, 10,Difficulty.ALTA, deadline.plusDays(1)));
        filePersistence.save(new Task("011", "Task 11", "Description 11", false, 5, 10,Difficulty.ALTA, deadline));
        filePersistence.save(new Task("012", "Task 12", "Description 12", false, 5, 10,Difficulty.ALTA, deadline));
    }
}