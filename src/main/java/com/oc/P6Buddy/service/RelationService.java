// com.oc.P6Buddy.service.RelationService.java
package com.oc.P6Buddy.service;

import com.oc.P6Buddy.model.Buddy;
import com.oc.P6Buddy.model.User;
import com.oc.P6Buddy.repository.BuddyRepository;
import com.oc.P6Buddy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RelationService {
    private final UserRepository userRepository;
    private final BuddyRepository buddyRepository;

    public RelationService(UserRepository userRepository, BuddyRepository buddyRepository) {
        this.userRepository = userRepository;
        this.buddyRepository = buddyRepository;
    }

    public String addRelation(User currentUser, String buddyEmail) {
        return userRepository.findByEmail(buddyEmail)
                .map(buddyUser -> {
                    if (buddyUser.getId().equals(currentUser.getId())) {
                        return "Vous ne pouvez pas vous ajouter vous-même.";
                    }
                    boolean exists = buddyRepository.findByUserAndBuddy(currentUser, buddyUser).isPresent();
                    if (exists) {
                        return "Cette relation existe déjà.";
                    }
                    Buddy buddy = new Buddy();
                    buddy.setUser(currentUser);
                    buddy.setBuddy(buddyUser);
                    buddyRepository.save(buddy);
                    return "Relation ajoutée avec succès ✅";
                })
                .orElse("Aucun utilisateur trouvé avec cet email.");
    }
    /**
     * Retourne la liste des adresses email des buddies de l'utilisateur connecté
     */
    public List<String> getBuddyEmails(User currentUser) {
        return buddyRepository.findByUser(currentUser).stream()
                .map(b -> b.getBuddy().getEmail())
                .toList();
    }
}
