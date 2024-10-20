package edu.eci.cvds.Task.services.persistence;

import edu.eci.cvds.Task.TaskManagerException;
import edu.eci.cvds.Task.models.User;

import java.util.List;
import java.util.Optional;

public interface UserPersistence {
    Optional<User> findByEmail(String email);
    User save(User user) throws TaskManagerException;
    List<User> findAll();
    void deleteById(String id) throws TaskManagerException;
    Optional<User> findById(String id) throws TaskManagerException;
    void deleteAll();
}
