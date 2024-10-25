package edu.eci.cvds.Task;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RoleDTO {
    private String role;
    private String usernameId;
    private String name;
    private String email;
}
