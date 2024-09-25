package edu.eci.cvds.Task.services;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.Priority;
import edu.eci.cvds.Task.models.Task;
import edu.eci.cvds.Task.models.TaskDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskPersistence taskRepository;


    /**
     * This method adds a Task by the given DTO Task and stores it in thd Date Base.
     * @param dto The given DTO Object, in this case, TaskDTO
     * @return the created Task
     * @throws TaskManagerException In the case the information given to create the Task is not correct.
     */
    @Override
    public Task addTask(TaskDTO dto) throws TaskManagerException {
        Task task = new Task(dto.getId(),
                dto.getName(), dto.getDescription(), dto.getState(),
                dto.getPriority(), dto.getDeadline());
        taskRepository.save(task);
        return task;
    }
    @Override
    public void deleteTask(String id) throws TaskManagerException{
        taskRepository.deleteById(id);
    }

    @Override
    public void changeStateTask(String id) throws TaskManagerException{
        Task task = taskRepository.findById(id).get();
        task.changeState();
        taskRepository.save(task);
    }

    @Override
    public void updateTask(TaskDTO dto) throws TaskManagerException {
        Task task = taskRepository.findById(dto.getId()).get();
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setState(dto.getState());
        task.setDeadline(dto.getDeadline());
        taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTasks() throws TaskManagerException {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> getTasksByState(boolean state) throws TaskManagerException{
        return taskRepository.findByState(state);
    }

    @Override
    public List<Task> getTasksByDeadline(LocalDateTime deadline) throws TaskManagerException {
        return taskRepository.findByDeadline(deadline);
    }

    @Override
    public List<Task> getTaskByPriority(Priority priority) throws TaskManagerException{
        return taskRepository.findByPriority(priority);
    }
}
