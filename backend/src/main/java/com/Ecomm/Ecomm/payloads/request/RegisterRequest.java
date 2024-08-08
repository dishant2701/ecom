package com.Ecomm.Ecomm.payloads.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(@NotBlank @Size(min = 3, max = 20) String username, String name,
        @NotBlank @Size(max = 50) @Email String email, int roleId,
        @NotBlank @Size(min = 6, max = 40) String password) {

}
