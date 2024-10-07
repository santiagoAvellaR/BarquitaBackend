package edu.eci.cvds.Task.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDTO {
    private String usernameId;
    private HashMap<String, Task> tasks;
    private String name;
    private String password;
}