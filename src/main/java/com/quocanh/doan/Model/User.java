package com.quocanh.doan.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.ManyToAny;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private boolean verifyEmail = false;
    @JsonIgnore
    private String password;

    private String imageUrl;
    @NonNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id")
    )
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Role> roles = new HashSet<Role>();
    public User() {}
}
