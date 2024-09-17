package edu.eci.cvds.Task;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@Document(collection = "Tasks")
@Builder
public class Task {
    @Id
    private String id;
    private String name;
    private String description;
    private Boolean state;

    private Priority priority;
    private LocalDateTime deadline;


}
