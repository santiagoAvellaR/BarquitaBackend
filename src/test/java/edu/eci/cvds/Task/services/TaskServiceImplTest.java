package edu.eci.cvds.Task.services;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Difficulty;
import edu.eci.cvds.Task.models.Task;
import edu.eci.cvds.Task.models.TaskDTO;
import edu.eci.cvds.Task.services.persistence.FilePersistenceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
class TaskServiceImplTest {
    private final LocalDateTime deadline = LocalDateTime.now().plusDays(1);
    private final FilePersistenceImpl filePersistence = new FilePersistenceImpl("src/test/java/edu/eci/cvds/Task/services/persistence/DataTEST.txt");
    private final TaskServiceImpl taskService = new TaskServiceImpl(filePersistence);

    @AfterEach
    void tearDown(){
        try{
            filePersistence.deleteAll();
        } catch(TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void addTask() {
        try{
            Task t = taskService.addTask(new TaskDTO("1", "Task 1", "Description 1", false, 1, 10,Difficulty.ALTA, deadline));
            Task t2 = taskService.getAllTasks().stream().filter(task -> task.getId().equals(t.getId())).findFirst().get();
            assertTrue(t.equals(t2));
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void shouldNotAddTask(){
        try{
            taskService.addTask(new TaskDTO("1", null, "Description 1", false, 1, 10,Difficulty.ALTA, deadline));
            fail("Should fail with error: " + TaskManagerException.NAME_NOT_NULL);
        } catch (TaskManagerException e) {
            assertEquals(TaskManagerException.NAME_NOT_NULL, e.getMessage());
        }
    }


    @Test
    void deleteTask() {
        try{
            Task t = taskService.addTask(new TaskDTO("1", "Task 1", "Description 1", false, 1, 10,Difficulty.ALTA, deadline));
            Task t2 = taskService.addTask(new TaskDTO("1", "Task 2", "Description 1", false, 1, 10,Difficulty.ALTA, deadline));
            taskService.deleteTask(t.getId());
            taskService.deleteTask(t2.getId());
            assertEquals(Optional.empty(),taskService.getAllTasks().stream().filter(task -> task.getId().equals(t.getId())).findFirst());
            assertEquals(Optional.empty(),taskService.getAllTasks().stream().filter(task -> task.getId().equals(t2.getId())).findFirst());
        }catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void shouldNotDeleteTask(){
        try{
            taskService.deleteTask("1");
            fail("Should fail with error: " + TaskManagerException.TASK_NOT_FOUND);
        } catch (TaskManagerException e) {
            assertEquals(TaskManagerException.TASK_NOT_FOUND, e.getMessage());
        }
    }

    @Test
    void changeStateTask() {
        try{
            Task t = taskService.addTask(new TaskDTO("1", "Task 1", "Description 1", false, 1, 10,Difficulty.ALTA, deadline));
            taskService.changeStateTask(t.getId());
            assertTrue(taskService.getAllTasks().stream().filter(task -> task.getId().equals(t.getId())).findFirst().get().getState());
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void shouldNotChangeStateTask(){
        try{
            taskService.changeStateTask("1");
            fail("Should fail with error: " + TaskManagerException.TASK_NOT_FOUND);
        }catch (TaskManagerException e) {
            assertEquals(TaskManagerException.TASK_NOT_FOUND, e.getMessage());
        }
    }
    @Test
    void updateTask() {
        try{
            Task t = taskService.addTask(new TaskDTO("1", "Task 1", "Description 1", false, 1, 10,Difficulty.ALTA, deadline));
            taskService.updateTask(new TaskDTO(t.getId(), "Other name", "New Description", true, 1, 10,Difficulty.BAJA, deadline.plusDays(1)));
            assertTrue(taskService.getAllTasks().stream().filter(task -> task.getId().equals(t.getId())).findFirst().get().getState());
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void shouldNotUpdateTask(){
        try{
            taskService.updateTask(new TaskDTO("1", "Other name", "New Description", true, 1, 10,Difficulty.BAJA, deadline.plusDays(1)));
            fail("Should fail with error: " + TaskManagerException.TASK_NOT_FOUND);
        }catch (TaskManagerException e) {
            assertEquals(TaskManagerException.TASK_NOT_FOUND, e.getMessage());
        }
    }

    @Test
    void shouldNotUpdateNullName(){
        try{
            Task t = taskService.addTask(new TaskDTO("1", "Task 1", "Description 1", false, 1,10, Difficulty.ALTA, deadline));
            taskService.updateTask(new TaskDTO(t.getId(), null, "New Description", true, 1, 10,Difficulty.BAJA, deadline.plusDays(1)));
            fail("Should fail with error: " + TaskManagerException.NAME_NOT_NULL);
        }catch (TaskManagerException e) {
            assertEquals(TaskManagerException.NAME_NOT_NULL, e.getMessage());
        }
    }

    @Test
    void shouldNotUpdateNullDescription(){
        try{
            Task t = taskService.addTask(new TaskDTO("1", "Task 1", "Description 1", false,1,10, Difficulty.ALTA, deadline));
            taskService.updateTask(new TaskDTO(t.getId(), "Task 1", "", true,1, 10,Difficulty.BAJA, deadline.plusDays(1)));
            fail("Should fail with error: " + TaskManagerException.DESCRIPTION_NOT_NULL);
        }catch (TaskManagerException e) {
            assertEquals(TaskManagerException.DESCRIPTION_NOT_NULL, e.getMessage());
        }
    }

    @Test
    void getAllTasks() {
        try{
            List<Task> tasks = taskService.getAllTasks();
            assertTrue(tasks.isEmpty());
            taskService.addTask(new TaskDTO("1", "Task 1", "Description 1", false, 1,10,Difficulty.ALTA, deadline.plusDays(2)));
            taskService.addTask(new TaskDTO("2", "Task 2", "Description 2", false,1, 10,Difficulty.BAJA, deadline.plusDays(1)));
            taskService.addTask(new TaskDTO("3", "Task 3", "Description 3", false, 1,10,Difficulty.MEDIA, deadline));
            tasks = taskService.getAllTasks();
            assertEquals(3, tasks.size());
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void getTasksByState() {
        try{
            taskService.addTask(new TaskDTO("1", "Task 1", "Description 1", false, 1,10,Difficulty.ALTA, deadline.plusDays(2)));
            taskService.addTask(new TaskDTO("2", "Task 2", "Description 2", false, 1,10,Difficulty.BAJA, deadline.plusDays(1)));
            taskService.addTask(new TaskDTO("3", "Task 3", "Description 3", true, 1,10,Difficulty.MEDIA, deadline));

            taskService.getTasksByState(true).forEach(task -> assertTrue(task.getState()));
            taskService.getTasksByState(false).forEach(task -> assertFalse(task.getState()));

            assertEquals(1, taskService.getTasksByState(true).size());
            assertEquals(2, taskService.getTasksByState(false).size());
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void getTasksByDeadline() {
        try{
            taskService.addTask(new TaskDTO("fa", "Task 1", "Description 1", false, 1,10,Difficulty.ALTA, deadline.plusDays(2)));
            taskService.addTask(new TaskDTO("65", "Task 2", "Description 2", false, 1,10,Difficulty.BAJA, deadline));
            taskService.addTask(new TaskDTO("", "Task 3", "Description 3", true, 1,10,Difficulty.MEDIA, deadline));

            taskService.getTasksByDeadline(deadline).forEach(task ->
                    assertEquals(deadline.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")).toString(),
                            task.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")).toString()));
            taskService.getTasksByDeadline(deadline.plusDays(2)).forEach(task ->
                    assertTrue(deadline.plusDays(2).isAfter(task.getDeadline())));

            assertEquals(2, taskService.getTasksByDeadline(deadline).size());
            assertEquals(3, taskService.getTasksByDeadline(deadline.plusDays(2)).size());
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void getTaskByDifficulty() {
        try{
            taskService.addTask(new TaskDTO("fa", "Task 1", "Description 1", false, 1,10,Difficulty.ALTA, deadline.plusDays(2)));
            taskService.addTask(new TaskDTO("65", "Task 2", "Description 2", false, 1,10,Difficulty.BAJA, deadline));
            taskService.addTask(new TaskDTO("", "Task 3", "Description 3", true, 1,10,Difficulty.MEDIA, deadline));
            taskService.addTask(new TaskDTO("", "Task 3", "Description 3", true, 1,10,Difficulty.MEDIA, deadline.plusDays(1)));

            taskService.getTaskByDifficulty(Difficulty.ALTA).forEach(task ->
                    assertEquals(Difficulty.ALTA,task.getDifficulty()));
            taskService.getTaskByDifficulty(Difficulty.MEDIA).forEach(task ->
                    assertEquals(Difficulty.MEDIA,task.getDifficulty()));
            taskService.getTaskByDifficulty(Difficulty.BAJA).forEach(task ->
                    assertEquals(Difficulty.BAJA,task.getDifficulty()));


            assertEquals(1, taskService.getTaskByDifficulty(Difficulty.ALTA).size());
            assertEquals(1, taskService.getTaskByDifficulty(Difficulty.BAJA).size());
            assertEquals(2, taskService.getTaskByDifficulty(Difficulty.MEDIA).size());
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    /*
    Dado que tengo 1 tarea registrada, Cuando lo consulto a
    nivel de servicio, Entonces la consulta será exitosa validando el campo id.
     */
    @Test
    void shouldAssertIdTask(){
        try{
            Task task = taskService.addTask(new TaskDTO("fa", "Task 1", "Description 1", false, 1,10,Difficulty.ALTA, deadline.plusDays(2)));
            String id = task.getId();
            Task taskSaved = taskService.getAllTasks().get(0);

            assertEquals(id, taskSaved.getId());
            assertTrue(task.equals(taskSaved));

        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }
    /*
    Dado que no hay ninguna tarea registrada,
    Cuándo la consulto a nivel de servicio,
    Entonces la consulta no retornará ningún resultado.
     */
     @Test
    void shouldAssertNullTask(){
         try{
             List<Task> listTasks = taskService.getAllTasks();
             assertEquals(Collections.emptyList(), listTasks);
         } catch (TaskManagerException e ){fail("Should not fail with error: " + e.getMessage());}
     }

    /*
    Dado que no hay ninguna tarea registrada,
    Cuándo lo creo a nivel de servicio, Entonces la creación será exitosa.
     */
    @Test
    void shouldAddTask(){
        try{
            Task task = taskService.addTask(new TaskDTO("fa", "Task 1", "Description 1", false, 1,10,Difficulty.ALTA, deadline.plusDays(2)));
            assertEquals("Task 1", task.getName());
            assertEquals("Description 1", task.getDescription());
            assertFalse(task.getState());
            assertEquals(Difficulty.ALTA, task.getDifficulty());
            assertEquals(deadline.plusDays(2), task.getDeadline());
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    /*
    Dado que tengo 1 tarea registrada,
    Cuándo la elimino a nivel de servicio,
    Entonces la eliminación será exitosa.
     */
    @Test
    void shouldDeleteTask(){
        try{
            Task task = taskService.addTask(new TaskDTO("fa", "Task 1", "Description 1", false, 1,10,Difficulty.ALTA, deadline.plusDays(2)));
            Task taskSaved = taskService.getAllTasks().get(0);
            assertTrue(task.equals(taskSaved));
            taskService.deleteTask(task.getId());
            List<Task> tasks = taskService.getAllTasks();
            assertEquals(Collections.emptyList(), tasks);
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }
    /*
    Dado que tengo 1 tarea registrada,
    Cuándo la elimino y consulto a nivel de servicio,
    Entonces el resultado de la consulta no retornará ningún resultado.
     */
    @Test
    void shouldNotGetDeletedTask(){
        try{
            String id = taskService.addTask(new TaskDTO("fa", "Task 1", "Description 1", false, 1,10,Difficulty.ALTA, deadline.plusDays(2))).getId();
            taskService.deleteTask(id);
            List<Task> tasks = taskService.getAllTasks();
            assertEquals(Collections.emptyList(), tasks);

        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void shouldGetByPriority(){
        try{
            saveSomeTasksPriorities(10,20,30,40,50);
            List<Task> tasksP1 = taskService.getTaskByPriority(1);
            List<Task> tasksP2 = taskService.getTaskByPriority(2);
            List<Task> tasksP3 = taskService.getTaskByPriority(3);
            List<Task> tasksP4 = taskService.getTaskByPriority(4);
            List<Task> tasksP5 = taskService.getTaskByPriority(5);
            for(Task task : tasksP1){
                assertEquals(1, task.getPriority());
            }
            for(Task task : tasksP2){
                assertEquals(2, task.getPriority());
            }
            for(Task task : tasksP3){
                assertEquals(3, task.getPriority());
            }
            for(Task task : tasksP4){
                assertEquals(4, task.getPriority());
            }
            for(Task task : tasksP5){
                assertEquals(5, task.getPriority());
            }
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void shouldGetByEstimatedTime(){
        try{
            saveSomeTasksEstimatedTimes(20,5);
            List<Task> tasksP1 = taskService.getTaskByEstimatedTime(1);
            List<Task> tasksP2 = taskService.getTaskByEstimatedTime(2);
            List<Task> tasksP3 = taskService.getTaskByEstimatedTime(3);
            List<Task> tasksP4 = taskService.getTaskByEstimatedTime(4);
            List<Task> tasksP5 = taskService.getTaskByEstimatedTime(5);
            assertNotEquals(0, tasksP1.size());
            assertNotEquals(0, tasksP2.size());
            assertNotEquals(0, tasksP3.size());
            assertNotEquals(0, tasksP4.size());
            assertNotEquals(0, tasksP5.size());
            for(Task task : tasksP1){
                assertEquals(1, task.getEstimatedTime());
            }
            for(Task task : tasksP2){
                assertEquals(2, task.getEstimatedTime());
            }
            for(Task task : tasksP3){
                assertEquals(3, task.getEstimatedTime());
            }
            for(Task task : tasksP4){
                assertEquals(4, task.getEstimatedTime());
            }
            for(Task task : tasksP5){
                assertEquals(5, task.getEstimatedTime());
            }
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void shouldNotGetByEstimatedTime(){
        try{
            saveSomeTasksEstimatedTimes(10,4);
            List<Task> tasksE5 = taskService.getTaskByEstimatedTime(5);
            List<Task> tasksE0 = taskService.getTaskByEstimatedTime(0);
            assertEquals(Collections.emptyList(), tasksE5);
            assertEquals(Collections.emptyList(), tasksE0);
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }
    @Test
    void shouldNotGetByPriority(){
        try{
            saveSomeTasksPriorities(1,2,3,4,5);
            List<Task> listTasks6 = taskService.getTaskByPriority(6);
            List<Task> listTasks0 = taskService.getTaskByPriority(0);
            assertEquals(Collections.emptyList(), listTasks6);
            assertEquals(Collections.emptyList(), listTasks0);
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void shouldNotFindByDeadline(){
        try{
            List<Task> tasks = taskService.getTasksByDeadline(deadline.plusYears(9));
            assertEquals(Collections.emptyList(), tasks);
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }
    @Test
    void shouldUpdateDeadline(){
        try{
            Task task = taskService.addTask(new TaskDTO("id", "A Task", "A Description", false, 1,10,Difficulty.ALTA, deadline));
            taskService.updateTask(new TaskDTO(task.getId(), "A Task", "A Description", false, 1,10,Difficulty.ALTA, deadline.plusDays(2)));
            Task updatedTask = taskService.getAllTasks().stream().filter(t ->
                    t.getId().equals(task.getId())).findFirst().get();
            assertNotEquals(
                    task.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
                    updatedTask.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
            assertEquals(task.getId(), updatedTask.getId());
            assertEquals("A Task", updatedTask.getName());
            assertEquals("A Description", updatedTask.getDescription());
            assertEquals(task.getPriority(), updatedTask.getPriority());
            assertEquals(task.getState(), updatedTask.getState());
            assertEquals(task.getDifficulty(), updatedTask.getDifficulty());
            assertEquals(task.getEstimatedTime(), updatedTask.getEstimatedTime());
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void shouldUpdateDescription(){
        try{
            Task task = taskService.addTask(new TaskDTO("id", "A Task", "A Description", false, 1,10,Difficulty.ALTA, deadline));
            taskService.updateTask(new TaskDTO(task.getId(), "A Task", "A Completely different Description", false, 1,10,Difficulty.ALTA, deadline));
            Task updatedTask = taskService.getAllTasks().stream().filter(t ->
                    t.getId().equals(task.getId())).findFirst().get();
            assertNotEquals(task.getDescription(), updatedTask.getDescription());
            assertEquals(
                    task.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
                    updatedTask.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
            assertEquals(task.getPriority(), updatedTask.getPriority());
            assertEquals(task.getId(), updatedTask.getId());
            assertEquals("A Task", updatedTask.getName());
            assertEquals(task.getState(), updatedTask.getState());
            assertEquals(task.getDifficulty(), updatedTask.getDifficulty());
            assertEquals(task.getEstimatedTime(), updatedTask.getEstimatedTime());
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void shouldNotUpdateDescription(){
        try{
            Task task = taskService.addTask(new TaskDTO("id", "A Task", "A Description", false, 1,10,Difficulty.ALTA, deadline));
            taskService.updateTask(new TaskDTO(task.getId(), "A Task", null, false, 1,10,Difficulty.ALTA, deadline));
            fail("Should have thrown an exception");
        } catch (TaskManagerException e) {
            assertEquals(TaskManagerException.DESCRIPTION_NOT_NULL, e.getMessage());
        }
    }
    @Test
    void shouldUpdateName(){
        try{
            Task task = taskService.addTask(new TaskDTO("id", "A Task", "A Description", false, 1,10,Difficulty.ALTA, deadline));
            taskService.updateTask(new TaskDTO(task.getId(), "A completely different Name", "A Description", false, 1,10,Difficulty.ALTA, deadline));
            Task updatedTask = taskService.getAllTasks().stream().filter(t ->
                    t.getId().equals(task.getId())).findFirst().get();
            assertNotEquals(task.getName(), updatedTask.getName());
            assertEquals(task.getDescription(), updatedTask.getDescription());
            assertEquals(
                    task.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
                    updatedTask.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
            assertEquals(task.getId(), updatedTask.getId());
            assertEquals(task.getPriority(), updatedTask.getPriority());
            assertEquals(task.getState(), updatedTask.getState());
            assertEquals(task.getDifficulty(), updatedTask.getDifficulty());
            assertEquals(task.getEstimatedTime(), updatedTask.getEstimatedTime());
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void shouldNotUpdateName(){
        try{
            Task task = taskService.addTask(new TaskDTO("id", "A Task", "A Description", false, 1,10,Difficulty.ALTA, deadline));
            taskService.updateTask(new TaskDTO(task.getId(), "", "A Description", false, 1,10,Difficulty.ALTA, deadline));
            fail("Should have thrown an exception");
        } catch (TaskManagerException e) {
            assertEquals(TaskManagerException.NAME_NOT_NULL, e.getMessage());
        }
    }
    @Test
    void shouldUpdateEstimatedTime(){
        try{
            Task task = taskService.addTask(new TaskDTO("id", "A Task", "A Description", false, 1,10,Difficulty.ALTA, deadline));
            taskService.updateTask(new TaskDTO(task.getId(), "A Task", "A Description", false, 1,5,Difficulty.ALTA, deadline));
            Task updatedTask = taskService.getAllTasks().stream().filter(t ->
                    t.getId().equals(task.getId())).findFirst().get();
            assertEquals(task.getName(), updatedTask.getName());
            assertEquals(task.getDescription(), updatedTask.getDescription());
            assertEquals(task.getPriority(), updatedTask.getPriority());
            assertEquals(
                    task.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
                    updatedTask.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
            assertEquals(task.getId(), updatedTask.getId());
            assertEquals(task.getState(), updatedTask.getState());
            assertEquals(task.getDifficulty(), updatedTask.getDifficulty());
            assertNotEquals(task.getEstimatedTime(), updatedTask.getEstimatedTime());
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void shouldNotUpdateEstimatedTime(){
        try{
            Task task = taskService.addTask(new TaskDTO("id", "A Task", "A Description", false, 1,10,Difficulty.ALTA, deadline));
            taskService.updateTask(new TaskDTO(task.getId(), "A Task", "A Description", false, 1,0,Difficulty.ALTA, deadline));
            fail("Should have thrown an exception");
        } catch (TaskManagerException e) {
            assertEquals(TaskManagerException.TIME_INCORRECT, e.getMessage());
        }
    }

    @Test
    void shouldUpdatePriority(){
        try{
            Task task = taskService.addTask(new TaskDTO("id", "A Task", "A Description", false, 1,10,Difficulty.ALTA, deadline));
            taskService.updateTask(new TaskDTO(task.getId(), "A Task", "A Description", false, 5,10,Difficulty.ALTA, deadline));
            Task updatedTask = taskService.getAllTasks().stream().filter(t ->
                    t.getId().equals(task.getId())).findFirst().get();
            assertEquals(task.getName(), updatedTask.getName());
            assertEquals(task.getDescription(), updatedTask.getDescription());
            assertNotEquals(task.getPriority(), updatedTask.getPriority());
            assertEquals(
                    task.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
                    updatedTask.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
            assertEquals(task.getId(), updatedTask.getId());
            assertEquals(task.getState(), updatedTask.getState());
            assertEquals(task.getDifficulty(), updatedTask.getDifficulty());
            assertEquals(task.getEstimatedTime(), updatedTask.getEstimatedTime());
        } catch (TaskManagerException e) {fail("Should not fail with error: " + e.getMessage());}
    }

    @Test
    void shouldNotUpdatePriority(){
        try{
            Task task = taskService.addTask(new TaskDTO("id", "A Task", "A Description", false, 1,10,Difficulty.ALTA, deadline));
            taskService.updateTask(new TaskDTO(task.getId(), "A Task", "A Description", false, 0,10,Difficulty.ALTA, deadline));
            fail("Should have thrown an exception");
        } catch (TaskManagerException e) {
            assertEquals(TaskManagerException.PRIORITY_OUT_OF_RANGE, e.getMessage());
        }
    }


    // Operaciones para llenar el archivo de tareas para pruebas.
    private void saveSomeTasksPriorities(int priority1, int priority2, int priority3, int priority4, int priority5) throws TaskManagerException {
        for(int i = 0; i < priority1; i++){
            taskService.addTask(new TaskDTO("id", "A Task", "A Description", false, 1,10,Difficulty.ALTA, deadline));
        }
        for(int i = 0; i < priority2; i++){
            taskService.addTask(new TaskDTO("id", "A Task", "A Description", false, 2,10,Difficulty.ALTA, deadline));
        }
        for(int i = 0; i < priority3; i++){
            taskService.addTask(new TaskDTO("id", "A Task", "A Description", false, 3,10,Difficulty.ALTA, deadline));
        }
        for(int i = 0; i < priority4; i++){
            taskService.addTask(new TaskDTO("id", "A Task", "A Description", false, 4,10,Difficulty.ALTA, deadline));
        }
        for(int i = 0; i < priority5; i++){
            taskService.addTask(new TaskDTO("id", "A Task", "A Description", false, 5,10,Difficulty.ALTA, deadline));
        }
    }

    private void saveSomeTasksEstimatedTimes(int numberTasks, int taksRanges) throws TaskManagerException {
        for(int i = 1; i <= taksRanges; i++){
            for(int j = 0; j < numberTasks; j++){
                taskService.addTask(new TaskDTO("id", "A Task", "A Description", false, 5,i,Difficulty.ALTA, deadline));
            }
        }
    }
}