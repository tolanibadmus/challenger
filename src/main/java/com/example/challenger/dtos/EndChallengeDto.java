package com.example.challenger.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EndChallengeDto {
    @NotNull
    private Integer score;

}
