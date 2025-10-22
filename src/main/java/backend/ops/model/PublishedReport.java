//published report
package backend.ops.model;


import jakarta.persistence.*;

@Entity
public class PublishedReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String category;
    private String version;
    private int downloads;
    private String notificationStatus;
}
