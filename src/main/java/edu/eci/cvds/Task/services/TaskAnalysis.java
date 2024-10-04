package edu.eci.cvds.Task.services;
import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Difficulty;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import com.github.javafaker.Faker;
import edu.eci.cvds.Task.models.Task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskAnalysis {
    @Autowired
    private static TaskPersistence taskPersistence;

    public static void randomData(int counter)throws TaskManagerException{
        if(isEmpty()){
            generateAnalysis(counter);
        }
    }

    public static List<Task> getRandomTasks(int numberOfTasks) throws TaskManagerException {
        ArrayList<Task> tasks = new ArrayList<>();
        for(int i = 0; i < numberOfTasks; i++) {
            Faker fake = new Faker();

            boolean state = fake.bool().bool();
            String id = String.valueOf(fake.number().numberBetween(1, 10));
            String name = fake.name().fullName();
            String description = fake.animal().name();
            int priority = fake.number().numberBetween(1, 6);
            int estimatedTime = fake.number().numberBetween(1, 100);
            Difficulty difficulty = generateDifficulty(fake);
            LocalDateTime dateTime = generateDate(fake);
            Task task1 =
                    new Task(
                            id,name,description,state,priority,
                            estimatedTime, difficulty, dateTime);
            tasks.add(task1);
        }
        return tasks;
    }
    private static Difficulty generateDifficulty(Faker faker) {
        Difficulty difficulty;
        int number = faker.number().numberBetween(0, 3);
        if(number == 1) {difficulty = Difficulty.BAJA;}
        else if(number == 2) {difficulty = Difficulty.MEDIA;}
        else {difficulty = Difficulty.ALTA;}
        return difficulty;
    }
    private static LocalDateTime generateDate(Faker faker) {
        Date date  = faker.date().birthday();
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
    private static boolean isEmpty()throws TaskManagerException{
        return taskPersistence.findAll() == null;
    }
    private static void generateAnalysis(int counter) throws TaskManagerException {
        List<Task> tasks = getRandomTasks(counter);
        for(Task task : tasks){
            taskPersistence.save(task);
        }
    }

    public static Map<Difficulty, Long> getHistogram()throws TaskManagerException{
        long difficultyALTA = taskPersistence.findByDifficulty(Difficulty.ALTA).size();
        long difficultyMEDIA = taskPersistence.findByDifficulty(Difficulty.MEDIA).size();
        long difficultyBAJA = taskPersistence.findByDifficulty(Difficulty.BAJA).size();
        return Map.of(Difficulty.ALTA, difficultyALTA, Difficulty.MEDIA, difficultyMEDIA, Difficulty.BAJA, difficultyBAJA);
    }

    public static Map<Integer, Long> getFinishedTasks()throws TaskManagerException{
        return taskPersistence.findByState(true).stream()
                .collect(Collectors.groupingBy(
                        Task::getEstimatedTime,
                        Collectors.counting()
                ));
    }

    public static Map<Integer, Double> getAverageByPriority()throws TaskManagerException{
        Map<Integer, Double> res = new HashMap<>();
        List<Task> totalTasks = taskPersistence.findAll();
        Map<Integer, Long> tasksGrouped = totalTasks.stream()
                .collect(Collectors.groupingBy(
                        Task::getPriority,
                        Collectors.counting()
                ));

        for(Map.Entry<Integer, Long> entry : tasksGrouped.entrySet()){
            res.put(entry.getKey(), (double) entry.getValue() / 3);//Se divide por 3 debido a que es el numero de prioridades disponibles
        }

        return res;
    }
    public static Map<Difficulty, Double> getTotalTimeSpentByDifficulty() throws TaskManagerException{
        List<Task> allTasks = taskPersistence.findByState(true);
        return allTasks.stream().collect(Collectors.groupingBy(
                Task::getDifficulty,
                Collectors.summingDouble(Task::getEstimatedTime)
        ));

    }

}
