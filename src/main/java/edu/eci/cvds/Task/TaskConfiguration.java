package edu.eci.cvds.Task;

import edu.eci.cvds.Task.services.TaskPersistence;
import edu.eci.cvds.Task.services.persistence.TaskPersistenceMongo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskConfiguration {
    @Bean
    public TaskPersistence taskPersistence(){
        return new TaskPersistenceMongo();

    }
}
