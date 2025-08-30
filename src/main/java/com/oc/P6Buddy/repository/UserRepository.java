package com.oc.P6Buddy.repository;


import com.oc.P6Buddy.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UserRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByEmail(String email);
}