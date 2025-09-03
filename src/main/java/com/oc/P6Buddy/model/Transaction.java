package com.oc.P6Buddy.model;


import jakarta.persistence.*;

@Entity
@Table(name = "Transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    private Double amount;

    // L’expéditeur
    @ManyToOne
    @JoinColumn(name = "sender", nullable = false)
    private User sender;

    // Le destinataire
    @ManyToOne
    @JoinColumn(name = "receiver", nullable = false)
    private User receiver;

    // Getters & setters
    // ...
}
