package edu.eci.cvds.Task;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * This class is made to represent a Data Transfer User
 * by providing the information of the user, except the password.
 * @version 1.0
 * @since 28-10-2024
 */
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
