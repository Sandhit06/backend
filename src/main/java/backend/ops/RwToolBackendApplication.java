package backend.ops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RwToolBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RwToolBackendApplication.class, args);
        System.out.println("✅ RW Tool Backend Application Started Successfully!");
    }
}
