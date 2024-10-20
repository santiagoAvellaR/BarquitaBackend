package edu.eci.cvds.Task.controller;

import edu.eci.cvds.Task.services.persistence.FilePersistenceImpl;
import edu.eci.cvds.Task.services.persistence.UserPersistence;
import edu.eci.cvds.Task.services.persistence.UserPersistenceMongo;
import edu.eci.cvds.Task.services.persistence.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {
    @Bean
    public String fileName(){
        return "src/main/java/edu/eci/cvds/Task/services/persistence/Data.txt";
    }
    @Bean
    public UserPersistence userPersistence(UserRepository userRepository){
        return new UserPersistenceMongo(userRepository);
    }
}
