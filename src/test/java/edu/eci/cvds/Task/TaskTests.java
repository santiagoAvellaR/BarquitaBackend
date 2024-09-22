package edu.eci.cvds.Task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

@SpringBootTest
public class TaskTests {

    private Task task;
    @BeforeEach
    void setUp() throws TaskManagerExceptions {
        task = Task.builder()
                .id("1")
                .name("Study")
                .state(false)
                .priority(Priority.MEDIA)
                .deadline(LocalDateTime.now().plusDays(1))
                .description("This is the study task.")
                .build();
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
    void testChangeName() throws TaskManagerExceptions {
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
        } catch (TaskManagerExceptions e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    void testChangeDeadline() throws TaskManagerExceptions {
        LocalDateTime newDeadline = LocalDateTime.now().plusDays(2);
        task.changeDeadline(newDeadline);
        assertEquals(newDeadline, task.getDeadline());
    }

    @Test
    void testGetId() {
        assertEquals("1", task.getId());
    }

    @Test
    void shouldNotAddPastDeadline() throws TaskManagerExceptions {
        task.changeDeadline(LocalDateTime.now().plusDays(2));
        try{
            task.changeDeadline(LocalDateTime.now().minusDays(1));
        } catch (TaskManagerExceptions e){
            assertEquals(TaskManagerExceptions.IMPOSSIBLE_DATE, e.getMessage());
        }
    }

    @Test
    void shouldNotChangeEmptyName() throws TaskManagerExceptions {
        try{
            task.changeName("");
        } catch (TaskManagerExceptions e){
            assertEquals(TaskManagerExceptions.NAME_NOT_NULL, e.getMessage());
        }
    }
}
