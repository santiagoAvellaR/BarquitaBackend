package edu.eci.cvds.Task;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

/**
 * This class represents a Task...
 * {@code @Autor} Daniel Aldana and Miguel Motta
 * {@code @Version} 1.0
 * {@code @Since} 20-09-2024
 */
@Setter
@Getter
@AllArgsConstructor
@Document(collection = "Tasks")
@Builder
public class Task {
    @Id
    private String id;
    private String name;
    private String description;
    private Boolean state;

    private Priority priority;
    private LocalDateTime deadline;

    /*
    public Task(String id, String name, String description, Boolean state, Priority priority, LocalDateTime deadline) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.state = state;
        this.priority = priority;
        this.deadline = deadline;
    }
     */
    /**
     * Methods changes the state of the task.
     */
    public void changeState(){
    }

    /**
     * This method sets up the priority of the task by the new given one.
     * @param newPriority The priority to set to the task.
     */
    public void changePriority(Priority newPriority){}

    /**
     * Method sets up the name of the Task
     * @param newName
     */
    public void changeName(String newName){}

    /**
     * Method sets up a Description of the Task.
     * @param newDescription
     */
    public void changeDescription(String newDescription){}

    /**
     * Method sets up a Deadline to the Task.
     * @param newDeadline
     */
    public void changeDeadline(LocalDateTime newDeadline){}

    /**
     * Method to get the Id of the Task.
     * @return String The Id of the Task
     */
    public String getId(){return id;}
}
