package com.example.challenger.repositories;

import com.example.challenger.models.ChallengeBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChallengeBoardRepo extends JpaRepository<ChallengeBoard, UUID> {
    Optional<ChallengeBoard> findByChallengeIdAndUsername(String challengeId, String username);

    @Query("SELECT c FROM ChallengeBoard c WHERE c.challengeId = :challengeId ORDER BY c.timespent DESC, c.score DESC")
    List<ChallengeBoard> getLeaderBoard(@Param("challengeId") String challengeId);
}
