package com.example.challenger.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "ChallengeBoard")
public class ChallengeBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String challengeId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = true)
    private int timespent;

    @Column(nullable = true)
    private Integer score;

    private OffsetDateTime startedAt;

    private OffsetDateTime endedAt;

    @PrePersist
    protected void onCreate() {
        this.startedAt = OffsetDateTime.now();
    }
}
