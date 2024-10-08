package edu.eci.cvds.Task;

/**
 * The exception class for the Task Manager
 * @Version 1.0
 * @Since 20-09-2024
 */
public class TaskManagerException extends Exception{
    public static final String NAME_NOT_NULL = "The name of the task should not be null.";
    public static final String DESCRIPTION_NOT_NULL ="The description of the task should not be null.";
    public static final String TASK_NOT_FOUND = "The task was not be found.";
    public static final String PRIORITY_OUT_OF_RANGE = "The difficulty must be in the range [1,5].";
    public static final String DATA_BASE_FILE_ERROR = "There has been an error with the plane text database";
    public static final String TIME_INCORRECT = "The time must be greater than zero";
    public static final String USER_DOESNT_EXIST = "The user was not found in the database.";
    public static final String INVALID_USER_NAME = "The user name should not be null";
    public static final String INVALID_USER_PASSWD = "The ";
    public static final String INVALID_USER_ID = "";
    public static final String TASK_ALREADY_EXIST = "The task already exists.";

    /**
     * Constructor TaskManagerExceptions.
     * @param message The message of error.
     */
    public TaskManagerException(String message){
        super(message);
    }
}
