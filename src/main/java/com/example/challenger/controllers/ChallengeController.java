package com.example.challenger.controllers;

import com.example.challenger.dtos.CreateChallengeDto;
import com.example.challenger.dtos.EndChallengeDto;
import com.example.challenger.dtos.JoinChallengeDto;
import com.example.challenger.dtos.LeaderBoardDto;
import com.example.challenger.models.Challenge;
import com.example.challenger.models.ChallengeBoard;
import com.example.challenger.models.Challengers;
import com.example.challenger.services.ChallengeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenges")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    @PostMapping()
    public Challenge createChallenge (@Valid @RequestBody CreateChallengeDto createChallengeDto) {
        return challengeService.createChallenge(createChallengeDto);
    }

    @PostMapping("/{challengeId}/join")
    public Challengers joinChallenge(
            @PathVariable @NotBlank String challengeId,
            @Valid @RequestBody JoinChallengeDto joinChallengeDto
    ) {
        return challengeService.joinChallenge(challengeId, joinChallengeDto.getUsername());
    }

    @PostMapping("/{challengeId}/start/{username}")
    public ChallengeBoard startChallenge(
            @PathVariable @NotBlank String challengeId,
            @PathVariable @NotBlank String username
    ) {
        return challengeService.startChallenge(challengeId, username);
    }

    @PostMapping("/{challengeId}/end/{username}")
    public ChallengeBoard endChallenge(
            @PathVariable @NotBlank String challengeId,
            @PathVariable @NotBlank String username,
            @Valid @RequestBody EndChallengeDto endChallengeDto
    ) {
        return challengeService.endChallenge(challengeId, username, endChallengeDto.getScore());
    }

    @GetMapping("/{challengeId}/leaderBoard")
    public List<LeaderBoardDto> getLeaderBoard(
            @PathVariable @NotBlank String challengeId
    ) {
        return challengeService.getLeaderBoard(challengeId);
    }
}
