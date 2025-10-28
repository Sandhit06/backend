package backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"backend"})
public class RwToolBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(RwToolBackendApplication.class, args);
    }
}
