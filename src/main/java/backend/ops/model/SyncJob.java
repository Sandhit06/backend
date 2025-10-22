package backend.ops.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class SyncJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String source;
    private String status; // in-progress, completed, failed
    private int progress;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private int filesFetched;

    // Getters and Setters
}
