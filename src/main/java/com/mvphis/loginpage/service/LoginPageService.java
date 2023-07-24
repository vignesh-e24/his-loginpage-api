package com.mvphis.loginpage.service;

import com.mvphis.loginpage.model.LoginRequest;
import com.mvphis.loginpage.model.RegisterRequest;

public interface LoginPageService {
	
    String login(LoginRequest loginRequest);

    String register(RegisterRequest registerRequest);
}
