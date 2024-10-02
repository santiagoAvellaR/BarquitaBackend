package edu.eci.cvds.Task.services.persistence;
import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Difficulty;
import edu.eci.cvds.Task.models.Task;
import edu.eci.cvds.Task.services.TaskPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a database, working with plane text file to store the information.
 * @Version 1.0
 * @Since 29-09-2024
 */
@Component
public class FilePersistenceImpl implements TaskPersistence {
    private String fileName;
    private File file;

    @Autowired // Porque no podemos tener el mismo archivo para hacer pruebas que para la Base de Datos
    public FilePersistenceImpl(String fileName){
        this.fileName = fileName;
         this.file = new File(fileName);
    }

    /**
     * This method returns an optional Task by the given id.
     * an exception otherwise.
     * @param id The id of the task to search in the text plane file.
     * @return The task if it is founded, returns Optional empty otherwise.
     * @throws TaskManagerException Throws an exception if there is a problem
     * with the information stored in the text plane file.
     */
    @Override
    public Optional<Task> findById(String id) throws TaskManagerException {
        Optional<Task> res = Optional.empty();
        for(Task t: findAll()){
            if(t.getId().equals(id)){
                res = Optional.of(t);
            }
        }
        return res;
    }

    /**
     * This method saves a Task in the Database, if the id already exist, it updates it. It saves the Task otherwise.
     * @param task The Task to save.
     * @return the task saved in the file.
     * @throws TaskManagerException Throws an exception if there is a problem
     * with the information stored in the text plane file.
     */
    @Override
    public Task save(Task task) throws TaskManagerException {

        if(!isNew(task.getId())){
            deleteById(task.getId());
        }
        // Anotacion, van a haber duplicado, si lo hay, la actualizas, si no, si la creas.
        File file = new File(fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(task.getId() + "," + task.getName() + "," + task.getDescription() + "," +
                    task.getState() + "," + task.getPriority()+ "," + task.getDifficulty() + "," + task.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return task;
    }

    /**
     * This method  deletes a task from the text plane file if it exists. It throws an exception otherwise.
     * @param id The id of the task to delete in the file.
     * @throws TaskManagerException Throws an exception if there is a problem
     * with the information stored in the text plane file.
     */
    @Override
    public void deleteById(String id) throws TaskManagerException{
        if(isNew(id)) throw new TaskManagerException(TaskManagerException.TASK_NOT_FOUND);
        ArrayList<String> lines = searchId(id);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String filteredLine : lines) {
                writer.write(filteredLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method returns a List of all the Tasks founded in the file
     * @return List of all the Tasks founded in the file
     * @throws TaskManagerException Throws an exception if there is a problem
     * with the information stored in the text plane file.
     */
    @Override
    public List<Task> findAll() throws TaskManagerException {
        ArrayList<String>  lineas= new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineas.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return createTask(lineas);
    }

    /**
     * This method returns a list of Tasks with the given state from the storage file.
     * @param state the state to filter the list of all tasks
     * @return Returns a list of tasks with the given state
     * @throws TaskManagerException Throws an exception if there is a problem
     * with the information stored in the text plane file.
     */
    @Override
    public List<Task> findByState(boolean state) throws TaskManagerException {
        List<Task> allTasks = findAll();
        ArrayList<Task> tasksByState = new ArrayList<>();
        for(Task task: allTasks){
            if(task.getState() == (state)){
                tasksByState.add(task);
            }
        }
        return tasksByState;
    }

    /**
     * This method returns a list of tasks with the given deadline. This tasks have a deadline before or equal
     * of the given deadline.
     * @param deadline the deadline to filter the list tasks
     * @return the list of tasks that meets the condition.
     * @throws TaskManagerException Throws an exception if there is a problem
     * with the information stored in the text plane file.
     */
    @Override
    public List<Task> findByDeadline(LocalDateTime deadline) throws TaskManagerException {
        List<Task> tasks = findAll();
        ArrayList<Task> tasksByDeadline = new ArrayList<>();
        for(Task task: tasks){
            if(task.getDeadline().isBefore(deadline)){
                tasksByDeadline.add(task);
            }
        }
        return tasksByDeadline;
    }

    /**
     * This method returns a list of tasks with the given priority
     * @param priority the priority to filter the list of tasks
     * @return The list of Tasks from the storage file with the given priority
     * @throws TaskManagerException Throws an exception if there is a problem
     * with the information stored in the text plane file.
     */
    @Override
    public List<Task> findByPriority(int priority) throws TaskManagerException {
        List<Task> tasks = findAll();
        ArrayList<Task> taskByPriority = new ArrayList<>();
        for(Task task: tasks){
            if(task.getPriority()== priority){
                taskByPriority.add(task);
            }
        }
        return taskByPriority;
    }

    /**
     * This method returns a list of tasks with the given difficulty
     * @param difficulty The difficulty of the tasks
     * @return THe list of task from the storage file with the given Difficulty
     * @throws TaskManagerException Throws an exception if there is a problem
     * with the information stored in the text plane file.
     */
    @Override
    public List<Task> findByDifficulty(Difficulty difficulty) throws TaskManagerException{
        List<Task> tasks = findAll();
        ArrayList<Task> taskByDifficulty = new ArrayList<>();
        for(Task task: tasks){
            if(task.getDifficulty().equals(difficulty)){
                taskByDifficulty.add(task);
            }
        }
        return taskByDifficulty;
    }

    /**
     * This method is made for the test, in order to clean the file where the data is stored.
     */
    public void cleanFileForTest(){
        try (PrintWriter writer = new PrintWriter(fileName)) {
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private boolean isNew(String id)throws TaskManagerException{
        boolean res = true;
        for(Task t: findAll()){
            if(t.getId().equals(id)){
                res = false;
                break;
            }
        }
        return res;
    }

    private ArrayList<String> searchId(String id){

        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String key = line.split(",")[0];
                if (!key.contains(id)) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
    private ArrayList<Task> createTask(ArrayList<String> lineas) throws TaskManagerException {

        ArrayList<Task> tasks = new ArrayList<>();
        for(String linea: lineas){
            String[] contenido = linea.split(",");
            int priority = Integer.parseInt(contenido[4]);
            Difficulty difficulty = Difficulty.valueOf(contenido[5]);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(contenido[6], formatter);
            boolean state = Boolean.parseBoolean(contenido[3]);// El estado se puede agregar directamente sin hacer: setState(state);
            Task task = new Task(contenido[0], contenido[1], contenido[2], state,priority,difficulty, localDateTime);
            tasks.add(task);
        }
        return tasks;
    }

}
