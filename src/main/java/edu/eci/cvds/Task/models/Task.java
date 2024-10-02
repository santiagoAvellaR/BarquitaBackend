package edu.eci.cvds.Task.models;

import edu.eci.cvds.Task.TaskManagerException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

/**
 * This class represents a Task, and prevents an incorrect use of it
 * by trying to assign wrong values to its attributes.
 * @Version 1.0
 * @Since 20-09-2024
 */
@Setter
@Getter
@Document(collection = "Tasks")
public class Task {
    @Id
    private String id;
    private String name;
    private String description;
    private boolean state;
    private Priority priority;
    private LocalDateTime deadline;


    /**
     * This method creates a Task with the given arguments, and throwing some exceptions if the arguments are incorrect.
     * @param id The id of the task to be identified in the database.
     * @param name The name of the task, should not be null.
     * @param description A description of the Task, this should be null.
     * @param state The state of task
     * @param priority The priority of the task.
     * @param deadline The deadline of the task.
     * @throws TaskManagerException Throws an exception if the name or description are incorrect.
     */
    public Task(String id, String name, String description, boolean state, Priority priority, LocalDateTime deadline) throws TaskManagerException {
        if(!validateName(name)){
            throw new TaskManagerException(TaskManagerException.NAME_NOT_NULL);
        }
        if(!validateDescription(description)){
            throw new TaskManagerException(TaskManagerException.DESCRIPTION_NOT_NULL);
        }
        this.name = name;
        this.id = id;
        this.description = description;
        this.state = state;
        this.priority = priority;

        this.deadline = deadline;
    }
    /**
     * Methods changes the state of the task.
     */
    public void changeState(){
        state = (!state);
    }

    /**
     * This method sets up the priority of the task by the new given one.
     * @param newPriority The priority to set to the task.
     */
    public void changePriority(Priority newPriority){
        priority = newPriority;
    }

    /**
     * Method sets up the name of the Task
     * @param newName The new name of the task
     * @throws TaskManagerException the name should not be empty
     */
    public void changeName(String newName) throws TaskManagerException {
        if (!validateName(newName)) throw new TaskManagerException(TaskManagerException.NAME_NOT_NULL);
        name = newName;
    }

    /**
     * Method sets up a Description of the Task.
     * @param newDescription the new description to set up to the task
     */
    public void changeDescription(String newDescription) throws TaskManagerException {
        if (!validateDescription(newDescription)) throw new TaskManagerException(TaskManagerException.DESCRIPTION_NOT_NULL);
        description = newDescription;
    }

    /**
     * Method sets up a Deadline to the Task.
     * @param newDeadline The new deadline
     */
    public void changeDeadline(LocalDateTime newDeadline) {
        deadline = newDeadline;
    }

    /**
     * This method returns the state of a Task
     * @return the state of the task
     */
    public boolean getState() {
        return state;
    }

    /**
     * This method is made to verify if two Tasks are equals
     * @param task The task to compare with.
     * @return true if they have the same attributes, false otherwise
     */
    public boolean equals(Task task) {
        return id.equals(task.getId())&&name.equals(task.getName())&&
                description.equals(task.getDescription())&&state==task.getState()&&
                priority==task.getPriority();
    }

    private boolean validateName(String name){
        return name != null && !name.isEmpty();
    }
    private boolean validateDescription(String description){
        return description != null && !description.isEmpty();
    }
}
