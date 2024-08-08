package com.Ecomm.Ecomm.controllers;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ecomm.Ecomm.models.User;
import com.Ecomm.Ecomm.payloads.response.EncryptedResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ApiController {

  @PostMapping("/verify")
  public <json> EncryptedResponse verifyUser(HttpServletRequest req) throws Exception {
    var user = (User) req.getAttribute("user");
    var result = Map.of("verified", true, "user", user);
    return new EncryptedResponse(result);
  }
}
