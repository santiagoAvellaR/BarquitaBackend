package edu.eci.cvds.Task.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDTO {
    private String usernameId;
    private List<Task> tasks;
    private String name;
    private String password;
}