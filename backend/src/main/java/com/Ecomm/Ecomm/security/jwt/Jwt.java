package com.Ecomm.Ecomm.security.jwt;

public record Jwt(String subject, String fp, long timestamp) {
}
