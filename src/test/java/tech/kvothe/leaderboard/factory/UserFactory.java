package tech.kvothe.leaderboard.factory;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.kvothe.leaderboard.entity.User;

public class UserFactory {

    public static User buildOneUser() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        var encodedPassword = passwordEncoder.encode("kvothe123");

       return new User(
                "kvothe",
                "Kvothe Red Maedre",
               encodedPassword
        );

    }
}
