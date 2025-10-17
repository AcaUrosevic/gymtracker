package rs.ac.fon.gymtracker;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptGen {
    public static void main(String[] args) {
        var enc = new BCryptPasswordEncoder();
        System.out.println(enc.encode("ana123"));
    }
}

