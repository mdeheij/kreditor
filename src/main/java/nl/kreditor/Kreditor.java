package nl.kreditor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"nl.kreditor.model"})
public class Kreditor {
    public static void main(String[] args) {
        SpringApplication.run(Kreditor.class, args);
    }
}
