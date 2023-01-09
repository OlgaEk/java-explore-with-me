package ru.practicum.ewm.user.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class NewUserRequest {
    @NotBlank
    private String name;
    @Email
    private String email;
}
