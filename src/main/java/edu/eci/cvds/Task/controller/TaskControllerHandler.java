package edu.eci.cvds.Task.controller;

import edu.eci.cvds.Task.TaskManagerException;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

/**
 * This class handles the possible exceptions with the backend to hide the information and just throw
 * a generic error message to the client.
 * @Version 1.0
 * @Since 29-09-2024
 */
@RestControllerAdvice
public class TaskControllerHandler {
    private final Logger logger = LoggerFactory.getLogger(TaskControllerHandler.class);

    /**
     * This method catches the errors from the Task Manager Exceptions, those could be by trying to
     * create a Task with wrong arguments or trying to update wrong values from those.
     * @param ex
     * @return
     */
    @ExceptionHandler(TaskManagerException.class)
    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleTaskManagerException(TaskManagerException ex) {
        logger.error(ex.getMessage());
        logger.error(Arrays.toString(ex.getStackTrace()));
        return "Internal Server Error";
    }

    // Para atrapar errores genericos
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex) {
        logger.error(ex.getMessage());
        logger.error(Arrays.toString(ex.getStackTrace()));
        return "Internal Server Error";
    }
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(value=HttpStatus.UNAUTHORIZED)
    public String badCredentialsException(BadCredentialsException ex){
        logger.error(ex.getMessage());
        logger.error(Arrays.toString(ex.getStackTrace()));
        return "The Email Or Password Is Incorrect";
    }


}
