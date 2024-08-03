package com.stigg.lernwortspiel.enteties;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role implements Serializable {
    public Role(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true, length = 25)
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users;

}
