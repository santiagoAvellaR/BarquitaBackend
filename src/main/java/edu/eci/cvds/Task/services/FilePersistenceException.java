package edu.eci.cvds.Task.services;

public class FilePersistenceException extends Exception {
    public static final String TAKS_NOT_FOUND = "The task does not exist";
    public FilePersistenceException(String message) {
        super(message);
    }
}
