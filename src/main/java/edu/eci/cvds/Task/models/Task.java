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
    private int priority;
    private int estimatedTime;
    private Difficulty difficulty;
    private LocalDateTime deadline;


    /**
     * This method creates a Task with the given arguments, and throwing some exceptions if the arguments are incorrect.
     * @param id The id of the task to be identified in the database.
     * @param name The name of the task, should not be null.
     * @param description A description of the Task, this should be null.
     * @param state The state of task
     * @param priority The priority of the task, must be in the range [1,5].
     * @param estimatedTime The estimated time to complete the task, must be greater than zero.
     * @param difficulty The difficulty of the task
     * @param deadline The deadline of the task.
     * @throws TaskManagerException Throws an exception if the name, description or the priority are incorrect.
     */
    public Task(String id, String name, String description, boolean state, int priority,int estimatedTime,Difficulty difficulty, LocalDateTime deadline) throws TaskManagerException {
        if(!validateName(name)){
            throw new TaskManagerException(TaskManagerException.NAME_NOT_NULL);
        }
        if(!validateDescription(description)){
            throw new TaskManagerException(TaskManagerException.DESCRIPTION_NOT_NULL);
        }
        if(!validatePriority(priority)) throw new TaskManagerException(TaskManagerException.PRIORITY_OUT_OF_RANGE);
        if(!validateEstimatedTime(estimatedTime)) throw new TaskManagerException(TaskManagerException.TIME_INCORRECT);
        this.name = name;
        this.id = id;
        this.description = description;
        this.state = state;
        this.priority = priority;
        this.estimatedTime = estimatedTime;
        this.difficulty = difficulty;
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
     * @param newPriority The priority to set to the task, this most be in the range of [1,5]
     * @throws TaskManagerException If the given priority is not in the range of [1,5]
     */
    public void changePriority(int newPriority) throws TaskManagerException {
        if(!validatePriority(newPriority)) throw new TaskManagerException(TaskManagerException.PRIORITY_OUT_OF_RANGE);
        priority = newPriority;
    }

    /**
     * This method sets up the estimated time of the task by the new given one.
     * @param newEstimatedTime The new estimated time for the task, must be greater than zero.
     * @throws TaskManagerException If the given time is not greater than zero.
     */
    public void changeEstimatedTime(int newEstimatedTime) throws TaskManagerException {
        if(!validateEstimatedTime(newEstimatedTime)) throw new TaskManagerException(TaskManagerException.TIME_INCORRECT);
        estimatedTime = newEstimatedTime;
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
     * @throws TaskManagerException If the given description is null or empty
     */
    public void changeDescription(String newDescription) throws TaskManagerException {
        if (!validateDescription(newDescription)) throw new TaskManagerException(TaskManagerException.DESCRIPTION_NOT_NULL);
        description = newDescription;
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
    private boolean validatePriority(int priority){
        return priority >= 1 && priority <= 5;
    }
    private boolean validateEstimatedTime(int estimatedTime){
        return estimatedTime > 0;
    }
}
