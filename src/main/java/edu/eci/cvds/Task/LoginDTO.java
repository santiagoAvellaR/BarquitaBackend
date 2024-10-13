package edu.eci.cvds.Task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoginDTO {
    private String usernameId;
    private String password;
    private String email;

}
