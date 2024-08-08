package com.Ecomm.Ecomm.payloads.request;

public record LoginRequest(String username, String password, String otp, String captcha,
        String fp, int roleId, String forgotOtp, Integer forgotOtpCount,
        Long forgotOtpTimestamp) {

}
