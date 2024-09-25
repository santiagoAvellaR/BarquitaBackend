package edu.eci.cvds.Task.services.persistence;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Priority;
import edu.eci.cvds.Task.models.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FilePersistenceImplTest {

    private FilePersistenceImpl filePersistence;
    private final LocalDateTime deadline = LocalDateTime.now().plusDays(1);
    private List<Task> tasks = generateTasks();

    private List<Task> generateTasks(){
        List<Task> taskArray = null;
        try {
            taskArray = new ArrayList<>(List.of(
                    new Task("", "Task 1", "Description 1", false, Priority.ALTA, deadline.plusDays(1)),
                    new Task("", "Task 2", "Description 2", false, Priority.ALTA, deadline.plusDays(2)),
                    new Task("", "Task 3", "Description 3", false, Priority.MEDIA, deadline.plusDays(3)),
                    new Task("", "Task 4", "Description 4", false, Priority.BAJA, deadline.plusDays(4))
            ));
        }catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
        return taskArray;
    }

    @BeforeEach
    void setUp() {
        filePersistence = new FilePersistenceImpl();
        try {
            for(Task task : tasks){
                filePersistence.save(task);
            }
        } catch (TaskManagerException e ){fail("Should not fail with error: " + e.getMessage());}
    }

    @AfterEach
    void tearDown() throws TaskManagerException {
        filePersistence.deleteById(tasks.get(0).getId());
        filePersistence.deleteById(tasks.get(1).getId());
        filePersistence.deleteById(tasks.get(2).getId());
        filePersistence.deleteById(tasks.get(3).getId());
    }

    @Test
    void shouldFindById(){
        try{
            Task task = filePersistence.findById(tasks.get(0).getId()).get();
            assertEquals("Task 1", task.getName());
        } catch (TaskManagerException e ) {fail("Should not fail with error: " + e.getMessage());}

    }

    @Test
    void shouldSave() throws TaskManagerException {
        Task task = new Task("", "Task 4", "Description 4", false, Priority.BAJA, deadline.plusDays(4));
        filePersistence.save(task);
        assertTrue(filePersistence.findById(task.getId()).get().equals(task));
        filePersistence.deleteById(task.getId());
    }

    @Test
    void shouldDeleteById() throws TaskManagerException {
        Task newTask1 = new Task("", "Task NN", "NN Description", true, Priority.ALTA, deadline.plusDays(2));
        Task newTask2 = new Task("", "Task NN", "NN Description", true, Priority.ALTA, deadline.plusDays(2));
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
        assertEquals(trueTasks.size(), 1);
        for(Task task : trueTasks){
            assertEquals(task.getDeadline().toString(),
                    deadline.plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")).toString());
        }

    }

    @Test
    void shouldFindByPriority() throws TaskManagerException {
        List<Task> taksPriority = filePersistence.findByPriority(Priority.ALTA);
        assertEquals(taksPriority.size(), 2);
        for(Task task : taksPriority){
            assertEquals(task.getPriority(), Priority.ALTA);
        }
    }

    @Test
    void shouldNotFindById() throws TaskManagerException {
        String id = tasks.get(0).getId() + "NOT_EXIST_ID";
        assertEquals(filePersistence.findById(id), Optional.empty());
    }

    @Test
    void shouldNotSaveDuplicates() throws TaskManagerException {
        Task newTask = new Task("", "Task NN", "NN Description", true, Priority.ALTA, deadline.plusDays(2));
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
    void shouldNotChangeStateTask() {
    }

    @Test
    void shouldNotChangeState() {
    }

    @Test
    void shouldNotUpdateTask() {
    }

    @Test
    void shouldNotFindAll() {
    }

    @Test
    void shouldNotFindByState() {
    }

    @Test
    void shouldNotFindByDeadline() {
    }

    @Test
    void shouldNotFindByPriority() {
    }

}