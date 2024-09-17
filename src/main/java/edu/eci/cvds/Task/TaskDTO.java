package edu.eci.cvds.Task;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
public class TaskDTO {
    private String id;
    private String name;
    private String description;
    private Boolean state;

    private Priority priority;
    private LocalDateTime deadline;

}
