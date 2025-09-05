// com.oc.P6Buddy.repository.BuddyRepository.java
package com.oc.P6Buddy.repository;

import com.oc.P6Buddy.model.Buddy;
import com.oc.P6Buddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuddyRepository extends JpaRepository<Buddy, Integer> {
    Optional<Buddy> findByUserAndBuddy(User user, User buddy);
}
