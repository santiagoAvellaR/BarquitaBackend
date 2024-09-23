package edu.eci.cvds.Task;

import edu.eci.cvds.Task.models.Priority;
import edu.eci.cvds.Task.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

@SpringBootTest
public class TaskTests {

    private Task task;
    @BeforeEach
    void setUp() throws TaskManagerException {
        task = new Task("1","Study","This is the study task.",false,
                Priority.MEDIA,LocalDateTime.now().plusDays(2));
    }

    @Test
    void testChangeState() {
        assertFalse(task.getState());
        task.changeState();
        assertTrue(task.getState());
    }

    @Test
    void testChangePriority() {
        assertEquals(Priority.MEDIA, task.getPriority());
        task.changePriority(Priority.ALTA);
        assertEquals(Priority.ALTA, task.getPriority());
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
    void testChangeDeadline() throws TaskManagerException {
        LocalDateTime newDeadline = LocalDateTime.now().plusDays(2);
        task.changeDeadline(newDeadline);
        assertEquals(newDeadline, task.getDeadline());
    }


    @Test
    void shouldNotAddPastDeadline() throws TaskManagerException {
        task.changeDeadline(LocalDateTime.now().plusDays(2));
        try{
            task.changeDeadline(LocalDateTime.now().minusDays(1));
        } catch (TaskManagerException e){
            assertEquals(TaskManagerException.IMPOSSIBLE_DATE, e.getMessage());
        }
    }

    @Test
    void shouldNotChangeEmptyName() throws TaskManagerException {
        try{
            task.changeName("");
        } catch (TaskManagerException e){
            assertEquals(TaskManagerException.NAME_NOT_NULL, e.getMessage());
        }
    }
}
