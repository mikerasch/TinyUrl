package com.michael.tinyurl.security.database.repos;

import com.michael.tinyurl.security.database.entity.LoginAttempt;
import com.michael.tinyurl.security.database.entity.UserEntity;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    @Query(
            "SELECT l FROM LoginAttempt l WHERE l.user = :userEntity AND l.attemptedAt > :instant ORDER BY l.attemptedAt DESC LIMIT 3")
    List<LoginAttempt> fetchLastThreeAttempts(UserEntity userEntity, Instant instant);
}
