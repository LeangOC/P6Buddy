package com.oc.P6Buddy.repository;

import com.oc.P6Buddy.model.Buddy;
import com.oc.P6Buddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BuddyRepository extends JpaRepository<Buddy, Integer> {

    // Vérifie si une relation existe déjà
    Optional<Buddy> findByUserAndBuddy(User user, User buddy);

    // Récupère toutes les relations (amis) d’un utilisateur
    List<Buddy> findByUser(User user);
}
