package edu.eci.cvds.Task;

import edu.eci.cvds.Task.models.Difficulty;
import edu.eci.cvds.Task.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class TaskTests {

    private Task task;
    @BeforeEach
    void setUp() throws TaskManagerException {
        task = new Task("1","Study","This is the study task.",false,
                3,10,Difficulty.MEDIA,LocalDateTime.now().plusDays(2));
    }

    @Test
    void testChangeState() {
        assertFalse(task.getState());
        task.changeState();
        assertTrue(task.getState());
    }

    @Test
    void testChageDifficulty() {
        assertEquals(Difficulty.MEDIA, task.getDifficulty());
        task.changeDifficulty(Difficulty.ALTA);
        assertEquals(Difficulty.ALTA, task.getDifficulty());
    }

    @Test
    void testChangeName() throws TaskManagerException {
        assertEquals("Study", task.getName());
        task.changeName("Task Study");
        assertEquals("Task Study", task.getName());
    }

    @Test
    void testChangeDescription() {
        try {
            assertEquals("This is the study task.", task.getDescription());
            task.changeDescription("Updated description");
            assertEquals("Updated description", task.getDescription());
        } catch (TaskManagerException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    void testChangeDeadline(){
        LocalDateTime newDeadline = LocalDateTime.now().plusDays(2);
        task.changeDeadline(newDeadline);
        assertEquals(newDeadline, task.getDeadline());
    }

    @Test
    void checkEquals(){
        try{
            Task task1 = new Task("1","Study","This is the study task.",false,3,
                    10, Difficulty.MEDIA,LocalDateTime.now().plusDays(2));
            assertTrue(task1.equals(task));
            assertEquals(task.getId(), task1.getId());
            assertEquals(task.getName(), task1.getName());
            assertEquals(task.getDescription(), task1.getDescription());
            assertEquals(task.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")).toString(), task1.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")).toString());
            assertEquals(task.getDifficulty(), task1.getDifficulty());
            assertEquals(task.getState(), task1.getState());
        } catch (TaskManagerException e) {fail("An exception happened, should not fail with this: " + e.getMessage());}
    }


    @Test
    void shouldNotChangeEmptyName() throws TaskManagerException {
        try{
            task.changeName("");
            fail("Should not change name empty");
        } catch (TaskManagerException e){
            assertEquals(TaskManagerException.NAME_NOT_NULL, e.getMessage());
        }
    }

    @Test
    void shouldNotChangeNullName() throws TaskManagerException {
        try{
            task.changeName(null);
            fail("Should not change name to null:");
        } catch (TaskManagerException e){
            assertEquals(TaskManagerException.NAME_NOT_NULL, e.getMessage());
        }
    }

    @Test
    void shouldNotChangeEmptyDescription() throws TaskManagerException {
        try{
            task.changeDescription("");
            fail("Should not change description to empty:");
        } catch (TaskManagerException e){
            assertEquals(TaskManagerException.DESCRIPTION_NOT_NULL, e.getMessage());
        }
    }

    @Test
    void shouldNotChangeNullDescription() throws TaskManagerException {
        try{
            task.changeDescription(null);
            fail("Should not change description to null:");
        } catch (TaskManagerException e){
            assertEquals(TaskManagerException.DESCRIPTION_NOT_NULL, e.getMessage());
        }
    }

    @Test
    void shouldNotChangeEstimatedTime() throws TaskManagerException {
        try{
            task.changeEstimatedTime(0);
            fail("Should not change estimated time lower than zero:");
        } catch (TaskManagerException e){
            assertEquals(TaskManagerException.TIME_INCORRECT, e.getMessage());
        }
    }
}
