package ru.practicum.ewm.user.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewUserRequest {
    @NotBlank
    @Size(max = 256)
    private String name;
    @Email
    @Size(max = 256)
    private String email;
}
