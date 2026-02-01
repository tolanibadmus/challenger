package com.example.challenger.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JoinChallengeDto {
    @NotBlank
    private String username;
}
