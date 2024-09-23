package edu.eci.cvds.Task.controller;

import edu.eci.cvds.Task.services.TaskPersistence;
import edu.eci.cvds.Task.services.TaskPersistenceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {

    @Bean
    public TaskPersistence taskService() {
        return new TaskPersistenceImpl();
    }
    /*
    @Bean
    public TaskPersistence taskService() {
        return new TaskPersistenceImpl();
    }
     */
}
