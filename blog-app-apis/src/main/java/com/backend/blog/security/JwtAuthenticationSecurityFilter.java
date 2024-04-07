package com.backend.blog.security;

import java.io.IOException;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.backend.blog.utils.CustomUserDetailsService;



public class JwtAuthenticationSecurityFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//1. get jwt token from request
	       String token= getTokenFromRequest(request);
	    //2. validate token
	       if(StringUtils.hasText(token) && this.tokenProvider.validateToken(token)) {
	    	   //get username from token
	    	   String username = this.tokenProvider.getUsernameFromToken(token);
	    	   //load user associated with token
	    	   UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
	    
	    	   UsernamePasswordAuthenticationToken passwordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
	    	   
	    	   passwordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	    	   
	    	   //set spring security
	    	   SecurityContextHolder.getContext().setAuthentication(passwordAuthenticationToken);
	    	   
	       }
	       filterChain.doFilter(request, response);
		
		
		
	}

	private String getTokenFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		
		//if token is not empty and it starts with bearer
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			//remove the bearer from token and return it
			return bearerToken.substring(7,bearerToken.length());
		}
		return null;
	}

}
