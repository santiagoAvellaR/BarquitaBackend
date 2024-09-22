package edu.eci.cvds.Task;

/**
 * The exception class for the Task Manager
 * @Autor Miguel Motta
 * @Version 1.0
 * @Since 20-09-2024
 */
public class TaskManagerExceptions extends Exception{
    public static String IMPOSSIBLE_DATE = "The date for the task should not be before the current date.";
    public static String NAME_NOT_NULL = "The name of the task should not be null.";
    public static String DESCRIPTION_NOT_NULL ="The description of the task should not be null.";
    /**
     * Constructor TaskManagerExceptions.
     * @param message The message of error.
     */
    public TaskManagerExceptions(String message){
        super(message);
    }
}
