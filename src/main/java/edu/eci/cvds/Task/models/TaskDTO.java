package edu.eci.cvds.Task.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
public class TaskDTO {
    private String id;
    private String name;
    private String description;
    private Boolean state;

    private Priority priority;
    private LocalDateTime deadline;

}
