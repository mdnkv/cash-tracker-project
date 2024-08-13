package dev.mednikov.cashtracker.users.repositories;

import dev.mednikov.cashtracker.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Modifying
    @Query("update User u set u.password = :password where u.id = :id")
    void setNewPassword(@Param("id") Long userId, @Param("password") String password);

}
