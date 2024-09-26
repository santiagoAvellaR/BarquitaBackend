package edu.eci.cvds.Task.services.persistence;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Priority;
import edu.eci.cvds.Task.models.Task;
import edu.eci.cvds.Task.models.TaskDTO;
import edu.eci.cvds.Task.services.TaskPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class FilePersistenceImpl implements TaskPersistence {
    @Autowired // Porque no podemos tener el mismo archivo para hacer pruebas que para la Base de Datos
    public FilePersistenceImpl(String fileName){
        this.fileName = fileName;
         this.file = new File(fileName);
    }

    private String fileName;
    private File file;

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

    @Override
    public Task save(Task task) throws TaskManagerException {

        if(!isNew(task.getId())){
            deleteById(task.getId());
        }
        // Anotacion, van a haber duplicado, si lo hay, la actualizas, si no, si la creas.
        File file = new File(fileName);
        //task.setId(String.valueOf(UUID.randomUUID())); No debe genrar las claves, lo hacemos en TaskServiceImpl.java
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(task.getId() + "," + task.getName() + "," + task.getDescription() + "," +
                    task.getState() + "," + task.getPriority() + "," + task.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return task;
    }
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
    private ArrayList<Task> createTask(ArrayList<String> lineas) throws TaskManagerException {

        ArrayList<Task> tasks = new ArrayList<>();
        for(String linea: lineas){
            String[] contenido = linea.split(",");
            Priority priority = Priority.valueOf(contenido[4]);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(contenido[5], formatter);
            boolean state = Boolean.parseBoolean(contenido[3]);// El estado se puede agregar directamente sin hacer: setState(state);
            Task task = new Task(contenido[0], contenido[1], contenido[2], state,priority, localDateTime);
            //task.setState(state);
            tasks.add(task);
        }
        return tasks;
    }

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
    @Override
    public List<Task> findByPriority(Priority priority) throws TaskManagerException {
        List<Task> tasks = findAll();
        ArrayList<Task> taskByPriority = new ArrayList<>();
        for(Task task: tasks){
            if(task.getPriority().equals(priority)){
                taskByPriority.add(task);
            }
        }
        return taskByPriority;
    }

    public void cleanFileForTest(){
        try (PrintWriter writer = new PrintWriter(fileName)) {
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
