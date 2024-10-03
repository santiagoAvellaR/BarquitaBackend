package edu.eci.cvds.Task.services;
import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Difficulty;
import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import com.github.javafaker.Faker;
import edu.eci.cvds.Task.models.Task;
import edu.eci.cvds.Task.models.TaskDTO;
import edu.eci.cvds.Task.services.persistence.FilePersistenceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskAnalysis {
    private static final String fileName = "src/main/java/edu/eci/cvds/Task/services/persistence/DataAnalysis.txt";
    private static final Logger logger = LoggerFactory.getLogger(TaskAnalysis.class);
    private static final TaskServiceImpl taskService = new TaskServiceImpl(new FilePersistenceImpl(fileName));

    public static void randomData(int counter)throws TaskManagerException{
        if(isEmpty()){
            generateAnalysis(counter);
        }
    }


    private TaskAnalysis(){}
    public static List<TaskDTO> getRandomTasks(int numberOfTasks){
        ArrayList<TaskDTO> tasks = new ArrayList<>();
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
            TaskDTO task1 =
                    new TaskDTO(
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
        boolean empty = false;
        try{
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            String line = reader.readLine();
            reader.close();
            empty = line == null;
        } catch (IOException e) {
            throw new TaskManagerException(TaskManagerException.DATA_BASE_FILE_ERROR);
        }
        return empty;
    }
    private static void generateAnalysis(int counter) throws TaskManagerException {
        List<TaskDTO> tasks = getRandomTasks(counter);
        for(TaskDTO task : tasks){
                taskService.addTask(task);
        }
    }

    public static Map<Difficulty, Long> getHistogram()throws TaskManagerException{
        long difficultyALTA = taskService.getTaskByDifficulty(Difficulty.ALTA).size();
        long difficultyMEDIA = taskService.getTaskByDifficulty(Difficulty.MEDIA).size();
        long difficultyBAJA = taskService.getTaskByDifficulty(Difficulty.BAJA).size();
        return Map.of(Difficulty.ALTA, difficultyALTA, Difficulty.MEDIA, difficultyMEDIA, Difficulty.BAJA, difficultyBAJA);
    }

    public static Map<Integer, Long> getFinishedTasks()throws TaskManagerException{
        return taskService.getTasksByState(true).stream()
                .collect(Collectors.groupingBy(
                        Task::getEstimatedTime,
                        Collectors.counting()
                ));
    }

    public static Map<Integer, Double> getAverageByPriority()throws TaskManagerException{
        Map<Integer, Double> res = new HashMap<>();
        List<Task> totalTasks = taskService.getAllTasks();
        Map<Integer, Long> tasksGrouped = totalTasks.stream()
                .collect(Collectors.groupingBy(
                        Task::getPriority,
                        Collectors.counting()
                ));
        int total = totalTasks.size();
        for(Map.Entry<Integer, Long> entry : tasksGrouped.entrySet()){
            res.put(entry.getKey(), (double) entry.getValue() / total);
        }
        return res;
    }
}
