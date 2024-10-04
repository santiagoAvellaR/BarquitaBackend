package edu.eci.cvds.Task.services;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Difficulty;
import edu.eci.cvds.Task.models.Task;
import edu.eci.cvds.Task.models.TaskDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TaskAnalysisTest {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        TaskAnalysis.randomData(1000);
    }
    @Test
    void shouldGenerateRandomData() throws TaskManagerException {
        List<Task> tasks = TaskAnalysis.getRandomTasks(100);
        assertEquals(100, tasks.size());
    }

    @Test
    void shouldNotGenerateRandomData() throws TaskManagerException {
        List<Task> tasks = TaskAnalysis.getRandomTasks(0);
        assertEquals(Collections.emptyList(), tasks);
    }
    @Test
    void shouldGenerateNotEmptyTasks() throws TaskManagerException {
        List<Task> tasks = TaskAnalysis.getRandomTasks(10);
        for(Task task : tasks){
            assertNotEquals(null, task.getId());
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
        Map<Difficulty,Long> histogram = TaskAnalysis.getHistogram();
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
        Map<Integer, Long> finishedTasks = TaskAnalysis.getFinishedTasks();
        int total = 0;
        for(Map.Entry<Integer, Long> entry: finishedTasks.entrySet()){
            assertTrue(entry.getValue() > 0 && entry.getValue() < 100);
            total += entry.getValue();
        }
        assertTrue(total >= 0 && total <= 1000);
    }

    @Test
    void getAverageByPriority() throws TaskManagerException {
        Map<Integer, Double> averages = TaskAnalysis.getAverageByPriority();
        Double total = 0.0;
        for(Map.Entry<Integer, Double> entry: averages.entrySet()){
            assertTrue(entry.getValue() <= 1);
            total += entry.getValue();
        }
        assertEquals(1.0, total);
    }
}