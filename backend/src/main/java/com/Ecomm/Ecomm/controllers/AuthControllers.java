package com.Ecomm.Ecomm.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ecomm.Ecomm.exceptions.BadRequestException;
import com.Ecomm.Ecomm.models.Role;
import com.Ecomm.Ecomm.models.User;
import com.Ecomm.Ecomm.payloads.request.EncryptedRequest;
import com.Ecomm.Ecomm.payloads.request.LoginRequest;
import com.Ecomm.Ecomm.payloads.request.RegisterRequest;
import com.Ecomm.Ecomm.payloads.response.EncryptedResponse;
import com.Ecomm.Ecomm.payloads.response.LoginResponse;
import com.Ecomm.Ecomm.repositories.UserRepo;
import com.Ecomm.Ecomm.services.UserService;
import com.Ecomm.Ecomm.utils.OtpLimiter;
import com.Ecomm.Ecomm.utils.SmsThread;

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
}
