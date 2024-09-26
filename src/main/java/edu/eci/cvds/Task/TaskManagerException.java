package edu.eci.cvds.Task;

/**
 * The exception class for the Task Manager
 * @Version 1.0
 * @Since 20-09-2024
 */
public class TaskManagerException extends Exception{
    public static final String IMPOSSIBLE_DATE = "The date for the task should not be before the current date.";
    public static final String NAME_NOT_NULL = "The name of the task should not be null.";
    public static final String DESCRIPTION_NOT_NULL ="The description of the task should not be null.";
    public static final String TASK_NOT_FOUND = "The task was not be found.";

    /**
     * Constructor TaskManagerExceptions.
     * @param message The message of error.
     */
    public TaskManagerException(String message){
        super(message);
    }
}
