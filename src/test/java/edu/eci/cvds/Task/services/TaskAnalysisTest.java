package edu.eci.cvds.Task.services;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Difficulty;
import edu.eci.cvds.Task.models.Task;
import edu.eci.cvds.Task.services.persistence.FilePersistenceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TaskAnalysisTest {

    private static TaskAnalysis taskAnalysis;
    private static String fileRoute = "src/main/java/edu/eci/cvds/Task/services/persistence/DataAnalysis.txt";
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        taskAnalysis = new TaskAnalysis(new FilePersistenceImpl(fileRoute));
        taskAnalysis.randomData(1000);
    }
    @Test
    void shouldGenerateRandomData() throws TaskManagerException {
        List<Task> tasks = taskAnalysis.getRandomTasks(100);
        assertEquals(100, tasks.size());
    }

    @Test
    void shouldNotGenerateRandomData() throws TaskManagerException {
        List<Task> tasks = taskAnalysis.getRandomTasks(0);
        assertEquals(Collections.emptyList(), tasks);
    }
    @Test
    void shouldGenerateNotEmptyTasks() throws TaskManagerException {
        List<Task> tasks = taskAnalysis.getRandomTasks(10);
        for(Task task : tasks){
            assertNotEquals(null, task.getName());
            assertNotEquals(null, task.getDescription());
            assertNotEquals(null,task.getState());
            assertNotEquals(0,task.getPriority());
            assertNotEquals(0,task.getEstimatedTime());
            assertNotEquals(null, task.getDifficulty());
            assertNotEquals(null,task.getDeadline());
        }
    }

    @Test
    void shouldGetHistogram() throws TaskManagerException {
        Map<Difficulty,Long> histogram = taskAnalysis.getHistogram();
        int total = 0;
        for(Map.Entry<Difficulty, Long> entry: histogram.entrySet()){
            assertNotEquals(0, entry.getValue());
            assertTrue(entry.getValue() > 0);
            total += entry.getValue();
        }
        assertEquals(1000, total);
    }

    @Test
    void getFinishedTasks() throws TaskManagerException {
        Map<Integer, Long> finishedTasks = taskAnalysis.getFinishedTasks();
        int total = 0;
        for(Map.Entry<Integer, Long> entry: finishedTasks.entrySet()){
            assertTrue(entry.getValue() > 0 && entry.getValue() < 100);
            total += entry.getValue();
        }
        assertTrue(total >= 0 && total <= 1000);
    }

    @Test
    void getAverageByPriority() throws TaskManagerException {
        Map<Integer, Double> averages = taskAnalysis.getConsolidatedPriority();
        Double total = 0.0;
        for(Map.Entry<Integer, Double> entry: averages.entrySet()){
            assertTrue(entry.getValue() > 1);
            total += entry.getValue();
        }
        assertEquals(1000.0, total);
    }
}