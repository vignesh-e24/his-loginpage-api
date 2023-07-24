package com.mvphis.loginpage.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mvphis.loginpage.dao.entity.Role;
import com.mvphis.loginpage.dao.entity.User;
import com.mvphis.loginpage.dao.repository.RoleRepository;
import com.mvphis.loginpage.dao.repository.UserRepository;
import com.mvphis.loginpage.exception.TokenApiException;
import com.mvphis.loginpage.model.LoginRequest;
import com.mvphis.loginpage.model.RegisterRequest;
import com.mvphis.loginpage.security.JwtTokenProvider;
import com.mvphis.loginpage.service.LoginPageService;
@Service
public class LoginPageServiceImpl implements LoginPageService{
	 private AuthenticationManager authenticationManager;
	    private UserRepository userRepository;
	    private RoleRepository roleRepository;
	    private PasswordEncoder passwordEncoder;
	    private JwtTokenProvider jwtTokenProvider;


	    public LoginPageServiceImpl(AuthenticationManager authenticationManager,
	                           UserRepository userRepository,
	                           RoleRepository roleRepository,
	                           PasswordEncoder passwordEncoder,
	                           JwtTokenProvider jwtTokenProvider) {
	        this.authenticationManager = authenticationManager;
	        this.userRepository = userRepository;
	        this.roleRepository = roleRepository;
	        this.passwordEncoder = passwordEncoder;
	        this.jwtTokenProvider = jwtTokenProvider;
	    }
	    /**
	     * login Service layer
	     */
	    @Override
	    public String login(LoginRequest loginRequest) {

	        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
	        		loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

	        SecurityContextHolder.getContext().setAuthentication(authentication);

	        String token = jwtTokenProvider.generateToken(authentication);

	        return token;
	    }
	    
	    /**
	     * register service layer
	     */
	    @Override
	    public String register(RegisterRequest registerRequest) {
	    	  // add check for username exists in database
	        if(userRepository.existsByUsername(registerRequest.getUsername())){
	            throw new TokenApiException(HttpStatus.BAD_REQUEST, "Username is already exists!.");
	        }

	        // add check for email exists in database
	        if(userRepository.existsByEmail(registerRequest.getEmail())){
	            throw new TokenApiException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
	        }

	        User user = new User();
	        user.setName(registerRequest.getName());
	        user.setUsername(registerRequest.getUsername());
	        user.setEmail(registerRequest.getEmail());
	        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

	        Set<Role> roles = new HashSet<>();
	        Role userRole = roleRepository.findByName("ROLE_USER").get();
	        roles.add(userRole);
	        user.setRoles(roles);

	        userRepository.save(user);

	        return "User registered successfully!.";
	    }

}
