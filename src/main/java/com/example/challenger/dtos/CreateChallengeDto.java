package com.example.challenger.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateChallengeDto {
    @NotBlank
    private String name;

//  duration of the game in minutes
    @NotNull
    @Positive
    private Integer duration;
}
