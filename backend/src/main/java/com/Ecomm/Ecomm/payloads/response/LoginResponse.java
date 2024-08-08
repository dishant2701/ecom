package com.Ecomm.Ecomm.payloads.response;

import com.Ecomm.Ecomm.models.User;

public record LoginResponse(String token, User user) {
}
