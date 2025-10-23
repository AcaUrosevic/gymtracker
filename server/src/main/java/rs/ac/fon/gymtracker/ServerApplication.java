package rs.ac.fon.gymtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication {

    /** metoda za pokretanje servera**/
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
