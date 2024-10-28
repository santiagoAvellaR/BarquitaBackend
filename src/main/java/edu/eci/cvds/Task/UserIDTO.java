package edu.eci.cvds.Task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents the Data transfer id of a user
 * in order to just provide the id of the user.
 * @version 1.0
 * @since 28-10-2024
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserIDTO {
    private String userId;
}
