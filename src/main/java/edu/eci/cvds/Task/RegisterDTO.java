package edu.eci.cvds.Task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor

public class RegisterDTO {
    private String usernameId;
    private String name;
    private String password;
    private String email;
}
