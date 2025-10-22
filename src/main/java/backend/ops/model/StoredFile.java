//stored file
//package backend.ops.model;
//
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//public class StoredFile {
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String fileName;
//    private String category;
//    private String source;
//    private Long size;
//    private LocalDateTime uploadedAt;
//    private String status;    // pending, validated, published
//    private String localPath;
//    // getters/setters
//}
//
// src/main/java/backend/ops/model/StoredFile.java
package backend.ops.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stored_file")
public class StoredFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    private String category;          // e.g., Compliance / Credit Risk / General
    private String source;            // e.g., remoteDir path or storage name
    private Long size;                // bytes
    private LocalDateTime uploadedAt; // when we ingested the file
    private String status;            // pending / validated / published
    private String localPath;         // absolute/relative path on RW storage

    public StoredFile() {} // JPA needs a no-args constructor

    // ----- getters -----
    public Long getId() { return id; }
    public String getFileName() { return fileName; }
    public String getCategory() { return category; }
    public String getSource() { return source; }
    public Long getSize() { return size; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public String getStatus() { return status; }
    public String getLocalPath() { return localPath; }

    // ----- setters -----
    public void setId(Long id) { this.id = id; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setCategory(String category) { this.category = category; }
    public void setSource(String source) { this.source = source; }
    public void setSize(Long size) { this.size = size; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
    public void setStatus(String status) { this.status = status; }
    public void setLocalPath(String localPath) { this.localPath = localPath; }
}
