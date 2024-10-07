package edu.eci.cvds.Task.services.persistence;

import edu.eci.cvds.Task.models.Difficulty;
import edu.eci.cvds.Task.models.Task;
import edu.eci.cvds.Task.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
}
