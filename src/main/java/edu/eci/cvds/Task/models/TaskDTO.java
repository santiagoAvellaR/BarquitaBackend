package edu.eci.cvds.Task.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * This class is a Data Transfer Object, it is supposed to handle just basic information of a task and
 * transfer the information from the frontend to the backend, and the with that information create the
 * real object with that is more complex and handles more logic than the DTO Task.
 * @Version 1.0
 * @Since 26-09-2024
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class TaskDTO {
    private String id;
    private String name;
    private String description;
    private Boolean state;
    private int priority;
    private Difficulty difficulty;
    private LocalDateTime deadline;

}
