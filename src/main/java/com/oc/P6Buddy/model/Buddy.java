// com.oc.P6Buddy.model.Buddy.java
package com.oc.P6Buddy.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Buddy", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "buddy_id"})
})
public class Buddy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "buddy_id", nullable = false)
    private User buddy;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public User getBuddy() { return buddy; }
    public void setBuddy(User buddy) { this.buddy = buddy; }
}
