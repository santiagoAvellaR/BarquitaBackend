package edu.eci.cvds.Task;
import edu.eci.cvds.Task.models.Priority;
import edu.eci.cvds.Task.models.Task;
import edu.eci.cvds.Task.services.FilePersistenceException;
import edu.eci.cvds.Task.services.TaskPersistenceImpl;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class FilePersistenceImplTest {
    private TaskPersistenceImpl filePersistence;

    @BeforeEach
    public void setUp(){

        filePersistence = new TaskPersistenceImpl();
        try{
            filePersistence.save(new Task("1","Task 1", "Description 1", false, Priority.ALTA, LocalDateTime.now()));
            filePersistence.save(new Task("2","Task 2", "Description 2", false, Priority.MEDIA, LocalDateTime.now().plusDays(2)));
            filePersistence.save(new Task("3","Task 3", "Description 3", false, Priority.BAJA, LocalDateTime.now().plusDays(3)));
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void shouldFindById(){
        Task  t = filePersistence.findById("1").get();
        assertEquals("Task 1", t.getName());
        assertTrue(t.getDescription().equals("Description 1"));
    }
    @Test
    void shouldSave(){
        try {
            filePersistence.save(new Task("4","Task 1", "Description 1", false, Priority.ALTA, LocalDateTime.now()));
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
        assertTrue(filePersistence.findById("4").isPresent());
    }

    @Test
    void shouldDeleteById(){
        try{
            filePersistence.deleteById("1");
        } catch (FilePersistenceException e) {fail("Should not fail with error: " + e.getMessage());}
        assertFalse(filePersistence.findById("1").isPresent());
    }
    @Test
    void shouldFindAll(){
        List<Task> list = filePersistence.findAll();
        assertEquals(3, list.size());
        assertEquals("Task 1", list.get(0).getName());
        assertEquals("Task 2", list.get(1).getName());
        assertEquals("Task 3", list.get(2).getName());
    }
    @Test
    void shouldFindByState(){
        List<Task> list = filePersistence.findByState(false);
        assertFalse(list.get(0).getState());
        assertFalse(list.get(1).getState());
        assertFalse(list.get(2).getState());
    }
    @Test
    void shouldFindByDeadline(){
        List<Task> list = filePersistence.findByDeadline(LocalDateTime.now());
        assertFalse(list.isEmpty());
    }
    @Test
    void shouldFindByPriority() {
        List<Task> list = filePersistence.findByPriority(Priority.MEDIA);
        assertEquals("Task 2", list.get(0).getName());
    }

    @Test
    void shouldNotFindById() {
            assertNull(filePersistence.findById("4"));
    }
    @Test
    void shouldNotDeleteById(){
        try {
            filePersistence.deleteById("4");
            fail("Should not be able to delete a none existing task");
        } catch (FilePersistenceException e ){
            assertEquals(FilePersistenceException.TAKS_NOT_FOUND, e.getMessage());
        }
    }
    @Test
    void shouldNotFindByState(){
        assertNull(filePersistence.findByState(true));
    }
    @Test
    void shouldNotFindByDeadline(){
        assertNull(filePersistence.findByDeadline(LocalDateTime.now().minusDays(2)));
    }
    @Test
    void shouldNotFindByPriority() {
        try{
            filePersistence.deleteById("2");
        } catch (FilePersistenceException e) {fail("Should not fail with error: " + e.getMessage());}
        assertNull(filePersistence.findByPriority(Priority.MEDIA));
    }

}
