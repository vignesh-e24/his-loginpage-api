package com.mvphis.loginpage.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mvphis.loginpage.model.JWTAuthResponse;
import com.mvphis.loginpage.model.LoginRequest;
import com.mvphis.loginpage.model.RegisterRequest;
import com.mvphis.loginpage.service.LoginPageService;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginPageController {
	private LoginPageService loginService;

    public LoginPageController(LoginPageService loginService) {
        this.loginService = loginService;
    }
    
    // Build Login REST API
    @PostMapping(value = {"/login"})
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginRequest loginrequest){
        String token = loginService.login(loginrequest);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }

    // Build Register REST API
    @PostMapping(value = {"/register"})
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest){
        String response = loginService.register(registerRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
