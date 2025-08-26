package tech.kvothe.leaderboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.kvothe.leaderboard.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);
}
