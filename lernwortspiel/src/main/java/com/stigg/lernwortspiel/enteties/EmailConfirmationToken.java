package com.stigg.lernwortspiel.enteties;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class EmailConfirmationToken implements Serializable {

    public EmailConfirmationToken(User user) {
        this.user = user;
    }

    @Id
    private UUID id;

    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID();
        this.createdDate = new Date();
        this.token = UUID.randomUUID().toString();
    }

}
