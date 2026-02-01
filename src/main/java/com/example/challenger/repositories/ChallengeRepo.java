package com.example.challenger.repositories;

import com.example.challenger.models.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChallengeRepo extends JpaRepository<Challenge, UUID>{
    Optional<Challenge> findByChallengeId(String challengeId);
}
