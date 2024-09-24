package edu.eci.cvds.Task.services.persistence;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Priority;
import edu.eci.cvds.Task.models.Task;
import edu.eci.cvds.Task.models.TaskDTO;
import edu.eci.cvds.Task.services.TaskPersistence;
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
    @Override
    public Optional<Task> findById(String id) {
        return Optional.empty();
    }

    private String fileName = "src/main/java/edu/eci/cvds/Task/tasks.txt";
    private File file = new File(fileName);
    @Override
    public Task save(Task task) throws TaskManagerException {
        File file = new File(fileName);
        task.setId(String.valueOf(UUID.randomUUID()));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(task.getId() + "," + task.getName() + "," + task.getDescription() + "," + task.getState() + "," + task.getPriority() + "," + task.getDeadline());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return task;
    }
    @Override
    public void deleteById(String id) {

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
                if (!line.contains(id)) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    @Override
    public void changeStateTask(String id) {

        int count =0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(id)) {
                    changeState(count, line);
                }
                else{
                    break;
                }
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void changeState(int linea, String contenido){

        int count = 0;
        String[] palabras = contenido.split(",");
        if(palabras[3].equals("false")){
            palabras[3] = "true";
        }
        else{
            palabras[3] = "false";
        }
        try{
            String tempFileName = "tempArchivo.txt";
            BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFileName));
            String line;
            while ((line = reader.readLine()) != null) {
                if (count == linea) {
                    writer.write( palabras[0]+ ","+ palabras[1] + "," +  palabras[2] + "," +palabras[3] + "," +palabras[4] + "," + palabras[5]);
                } else {
                    writer.write(line);
                }
                writer.newLine();
                count++;
            }
            reader.close();
            writer.close();
            new java.io.File(fileName).delete();
            new java.io.File(tempFileName).renameTo(new java.io.File(fileName));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void updateTask(TaskDTO dto) throws TaskManagerException {

        deleteById(dto.getId());
        Task task = new Task(dto.getId() ,dto.getName(), dto.getDescription(), dto.getState(), dto.getPriority(), dto.getDeadline());
        task.setState(dto.getState());
        save(task);
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


}
