package com.oc.P6Buddy.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Buddy")
public class Buddy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // L'utilisateur qui a ajouté un buddy
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Le buddy (ami) référencé
    @ManyToOne
    @JoinColumn(name = "buddy_id", nullable = false)
    private User buddy;


}
