package com.Extra2Essentials.Extra2Essentials.repository;

import com.Extra2Essentials.Extra2Essentials.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}   