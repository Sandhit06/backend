package backend.ops.model;


import jakarta.persistence.*;

@Entity
public class ValidationItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String organization;
    private String integrity; // Pass/Fail
    private String status; // validating, pending, validated
}
