package backend.ops.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String user;
    private String action;
    private String entity;
    private String status;
    private LocalDateTime time;
}
