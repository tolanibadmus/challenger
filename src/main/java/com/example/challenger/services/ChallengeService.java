package com.example.challenger.services;

import com.example.challenger.dtos.CreateChallengeDto;
import com.example.challenger.dtos.LeaderBoardDto;
import com.example.challenger.models.Challenge;
import com.example.challenger.models.ChallengeBoard;
import com.example.challenger.models.Challengers;
import com.example.challenger.models.User;
import com.example.challenger.repositories.ChallengeBoardRepo;
import com.example.challenger.repositories.ChallengerRepo;
import com.example.challenger.repositories.ChallengeRepo;
import com.example.challenger.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ChallengeService {
    @Autowired
    private ChallengeRepo challengeRepo;

    @Autowired
    private ChallengerRepo challengerRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ChallengeBoardRepo challengeboardRepo;

    public Challenge createChallenge(CreateChallengeDto createChallengeDto){
        Challenge challenge = new Challenge();
        challenge.setName(createChallengeDto.getName());
        challenge.setDuration(createChallengeDto.getDuration());
        challenge.setChallengeId(RandomStringUtils.randomAlphanumeric(10));

        return challengeRepo.save(challenge);
    }

    public Challengers joinChallenge(String challengeId, String username){
        Challenge challenge = challengeRepo.findByChallengeId(challengeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Challenge does not exist"));

        boolean alreadyJoined = challengerRepo.existsByChallengeIdAndUsername(challengeId, username);
        if (alreadyJoined) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "User already part of challenge");
        }

        Optional<User> userExist = userRepo.findByUsername(username);
        if (userExist.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Username exist");
        }

        Challengers challengeEntrants = new Challengers();
        challengeEntrants.setChallengeId(challengeId);
        challengeEntrants.setUsername(username);

        User user = new User();
        user.setUsername(username);
        userRepo.save(user);

        return challengerRepo.save(challengeEntrants);
    }

    public ChallengeBoard startChallenge(String challengeId, String username){
        Challenge challenge = challengeRepo.findByChallengeId(challengeId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Challenge does not exist"));

        boolean alreadyJoined = challengerRepo.existsByChallengeIdAndUsername(challengeId, username);
        if (!alreadyJoined) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "User not part of challenge");
        }

        Optional<ChallengeBoard> startedChallenge = challengeboardRepo.findByChallengeIdAndUsername(challengeId, username);
        if (startedChallenge.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "User already starts challenge");
        }

        ChallengeBoard challengeBoard = new ChallengeBoard();
        challengeBoard.setChallengeId(challengeId);
        challengeBoard.setUsername(username);

        return challengeboardRepo.save(challengeBoard);
    }

    public ChallengeBoard endChallenge(String challengeId, String username, Integer score) {
        Challenge challenge = challengeRepo.findByChallengeId(challengeId)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Challenge does not exist"));

        Optional<ChallengeBoard> startedChallenge = challengeboardRepo.findByChallengeIdAndUsername(challengeId, username);
        if (startedChallenge.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "User not part of challenge");
        }

        ChallengeBoard challengeBoard = startedChallenge.get();

        if (challengeBoard.getEndedAt() != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Challenge already submitted");
        }

        OffsetDateTime endedAt = OffsetDateTime.now();
        challengeBoard.setEndedAt(endedAt);
        challengeBoard.setScore(score);

        OffsetDateTime startedAt = challengeBoard.getStartedAt();
        Duration durationBetween = Duration.between(startedAt, endedAt);
        Integer durationInSeconds = Integer.valueOf((int) durationBetween.getSeconds());
        Integer durationInMinutes = durationInSeconds/ 60;
        challengeBoard.setTimespent(durationInMinutes);

        return challengeboardRepo.save(challengeBoard);
    }


    public List<LeaderBoardDto> getLeaderBoard(String challengeId){
        Challenge challenge = challengeRepo.findByChallengeId(challengeId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Challenge does not exist"));

        List<ChallengeBoard> data =  challengeboardRepo.getLeaderBoard(challengeId);
        List<LeaderBoardDto> rankedList = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            var item = data.get(i);
            rankedList.add(new LeaderBoardDto(
                    i + 1,
                    item.getScore(),
                    item.getTimespent(),
                    item.getUsername()
            ));
        }

        return rankedList;
    }
}
