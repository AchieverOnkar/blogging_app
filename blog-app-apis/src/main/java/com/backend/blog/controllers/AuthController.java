package com.backend.blog.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.blog.payloads.JwtAuthRequest;
import com.backend.blog.payloads.JwtAuthResponse;
import com.backend.blog.repositories.UserRepo;
import com.backend.blog.security.JwtTokenProvider;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private HttpServletRequest request;

	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> authenticateUser(@RequestBody JwtAuthRequest authRequest) {

		// we are creating a authecation obj by using authentication manager and passing
		// th username and password in a obj of usenamepassordAuthenctivation token
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		//get token from token provider
		String token = this.tokenProvider.generateToken(authentication);
		
		
		
		JwtAuthResponse response = new JwtAuthResponse();
		response.setToken(token);
		
		// Also check session status
		HttpSession session = this.request.getSession(false);
		System.out.println((session == null) ? "No session present" : "Session present: " + session.getId());

		return new ResponseEntity<JwtAuthResponse>(response,HttpStatus.OK);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<String> logoutUser() {

		//get session from request
		HttpSession session = this.request.getSession(false);
		//if session is present then invalidate the session
		if(session != null) {
			session.invalidate();
		}
		//clear the security context
		SecurityContextHolder.clearContext();
		return new ResponseEntity<String>("Successfully logged out !!!",HttpStatus.OK);
	}
	

}
