//activity
package backend.ops.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_log")
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;
    private String entity;
    private String status;

    private LocalDateTime time;

    // 'user' is a reserved word in PostgreSQL; use a safe column name.
    @Column(name = "actor")
    private String user;

    public ActivityLog() {}

    public ActivityLog(String action, String entity, String status, LocalDateTime time, String user) {
        this.action = action;
        this.entity = entity;
        this.status = status;
        this.time = time;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getEntity() { return entity; }
    public void setEntity(String entity) { this.entity = entity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }

    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }
}
