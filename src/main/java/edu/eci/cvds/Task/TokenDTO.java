package edu.eci.cvds.Task;

import lombok.Builder;
import lombok.Getter;

import lombok.Setter;
/**
 * This method represents the data transfer token, as a string
 * in order to provide just the immutable content of the token.
 * @version 1.0
 * @since 28-10-2024
 */
@Getter
@Setter
@Builder

public class TokenDTO {
    private String token;
}
