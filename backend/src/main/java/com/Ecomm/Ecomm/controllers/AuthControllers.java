package com.Ecomm.Ecomm.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ecomm.Ecomm.exceptions.BadRequestException;
import com.Ecomm.Ecomm.exceptions.NotFoundException;
import com.Ecomm.Ecomm.models.Role;
import com.Ecomm.Ecomm.models.User;
import com.Ecomm.Ecomm.payloads.request.EncryptedRequest;
import com.Ecomm.Ecomm.payloads.request.LoginRequest;
import com.Ecomm.Ecomm.payloads.request.RegisterRequest;
import com.Ecomm.Ecomm.payloads.response.EncryptedResponse;
import com.Ecomm.Ecomm.payloads.response.LoginResponse;
import com.Ecomm.Ecomm.repositories.UserRepo;
import com.Ecomm.Ecomm.services.UserService;
import com.Ecomm.Ecomm.utils.Json;
import com.Ecomm.Ecomm.utils.OtpLimiter;
import com.Ecomm.Ecomm.utils.SmsThread;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthControllers {

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    OtpLimiter otpLimiter;

    @Autowired
    SmsThread smsThread;

    @Autowired
    UserService userService;

    @Autowired
    UserRepo userRepo;

    @PostMapping("/sign-in")
    public <json> EncryptedResponse signIn(@Valid @RequestBody EncryptedRequest req)
            throws Exception {

        var body = req.bodyAs(LoginRequest.class);
        String username = body.username();
        String password = body.password();
        String fp = body.fp();

        // userService.validateUsername(username);
        userService.validatePassword(password);
        User user = userService.authenticateUser(username, password);

        if (((Role) user.getRoles().toArray()[0]).getRoleId() == body.roleId()) {
            String jwt = userService.generateJwt(user, fp);
            return new EncryptedResponse(new LoginResponse(jwt, user));
        } else {
            throw new BadRequestException("Invalid Credentials for selected role!");
        }
    }

    @PostMapping("/register")
    public <json> EncryptedResponse signUp(@Valid @RequestBody EncryptedRequest req)
            throws Exception {
        var body = req.bodyAs(RegisterRequest.class);
        String username = body.username();
        String name = body.name();
        String password = body.password();
        String email = body.email();
        if (userRepo.existsByUsername(email)) {
            throw new BadRequestException("Email already exists!");
        }

        if (userRepo.existsByUsername(username)) {
            throw new BadRequestException("Mobile already exists!");
        }

        User user = new User(username, encoder.encode(password));
        user.setRoles(Set.of(new Role(body.roleId())));
        user.setName(name);
        user.setEmail(email);

        userRepo.save(user);
        return new EncryptedResponse("Registered!");
    }

    @PostMapping("/reset-password/send-OTP")
    public <json> EncryptedResponse resetPassword2fa(
            @Valid @RequestBody EncryptedRequest req,
            HttpServletResponse response) throws Exception {
        boolean otpLimitExceeded = false;
        LoginRequest loginRequest = Json.deserialize(LoginRequest.class, req.getData());
        String username = loginRequest.username();

        var user = userRepo.findFirstByUsername(username);

        String otp = userService.generateOtp();
        System.out.println("OTP" + otp);
        String mobile = user.getUsername();
        System.out.println("Mobile :" + mobile);
        String message = "OTP Sent.";
        if (otpLimiter.shouldForgotPasswordOtp(mobile, otp)) {
            smsThread.send(otp, mobile);
        } else {
            otpLimitExceeded = true;
        }

        if (otpLimitExceeded) {
            message = "OTP limit exceeded. Please use most recent OTP until 24 hours.";
        }
        return new EncryptedResponse(message);
    }

    @PostMapping("/reset-password/verify-OTP")
    public <json> EncryptedResponse resetPasswordOtp(
            @Valid @RequestBody EncryptedRequest req,
            HttpServletResponse response) throws Exception {

        LoginRequest loginRequest = Json.deserialize(LoginRequest.class, req.getData());
        String username = loginRequest.username();
        String otp = loginRequest.forgotOtp();

        // userService.validateUsername(username);
        var user = userRepo.findFirstByUsername(username);
        if (user == null) {
            throw new NotFoundException("User does not exist.");
        }
        // System.out.println("otp" + otp);
        userService.verifyForgotOtp(otp, user);

        return new EncryptedResponse("verified");
    }

    @PostMapping("/reset-password")
    public <json> EncryptedResponse resetPassword(
            @Valid @RequestBody EncryptedRequest req,
            HttpServletResponse response) throws Exception {

        LoginRequest loginRequest = Json.deserialize(LoginRequest.class, req.getData());
        String username = loginRequest.username();
        String password = loginRequest.password();
        String otp = loginRequest.forgotOtp();
        // userService.validateUsername(username);
        userService.validatePassword(password);

        var user = userRepo.findFirstByUsername(username);
        if (user == null) {
            throw new NotFoundException("User does not exist.");
        }
        userService.verifyForgotOtp(otp, user);

        user.setPassword(encoder.encode(password));

        user.setJwtTimestamp(null);
        user.setForgotOtp(null);
        user.setForgotOtpCount(null);
        user.setForgotOtpTimestamp(null);
        try {
            userRepo.save(user);
            return new EncryptedResponse("Password successfully reset.");
        } catch (Exception e) {
            throw new Exception("something went wrong");
        }

    }

}
