package edu.eci.cvds.Task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService{
    private final TaskRepository taskRepository;


    @Override
    public Task addTask(TaskDTO dto) {
        Task task = Task.builder()
                .name(dto.getName())
                .state(dto.getState())
                .priority(dto.getPriority())
                .deadline(dto.getDeadline())
                .description(dto.getDescription())
                .build();
        taskRepository.save(task);
        return task;
    }
    @Override
    public void deleteTask(String id) {
        taskRepository.deleteById(id);
    }

    @Override
    public void changeStateTask(String id) {
        // Asi estaba antes Task task = taskRepository.findById(id);  // Y daba error de tipo.
        taskRepository.findById(id).get().changeState();
        
    }

    @Override
    public void updateTask(TaskDTO dto) {

    }

    @Override
    public List<Task> getAllTasks() {
        return null;
    }

    @Override
    public List<Task> getTasksByState(boolean state) {
        return null;
    }

    @Override
    public List<Task> getTasksByDeadline(LocalDateTime deadline) {
        return null;
    }

    @Override
    public List<Task> getTaskByPriority(Priority priority) {
        return null;
    }
}
