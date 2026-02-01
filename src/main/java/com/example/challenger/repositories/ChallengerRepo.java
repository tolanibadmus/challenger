package com.example.challenger.repositories;

import com.example.challenger.models.Challengers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChallengerRepo extends JpaRepository<Challengers, UUID> {
    boolean existsByChallengeIdAndUsername(String challengeId, String username);
}
