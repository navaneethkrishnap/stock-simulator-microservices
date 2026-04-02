package com.NKP.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginDTO {

    @NotBlank(message = "email id required")
    private String emailId;
    @NotBlank(message = "password required")
    private String password;
}
