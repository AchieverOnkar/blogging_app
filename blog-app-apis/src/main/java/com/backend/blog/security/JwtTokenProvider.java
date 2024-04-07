package com.backend.blog.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.backend.blog.exceptions.BlogAPIException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {

	// we are using properties stored in app.prop file
	@Value("${app.jwt-secret}")
	private String secret;

	@Value("${app.jwt-expiration-ms}")
	private long jwtExpirationInMs;

	// 1.generate token
	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		Date currentDate = new Date();
		// create expiration date
		Date expirationDate = new Date(this.jwtExpirationInMs + currentDate.getTime());

		// build the actual token
		String token = Jwts.builder().setSubject(username).setIssuedAt(currentDate).setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, this.secret).compact();

		return token;
	}
	
	
	//2. get username from the token
	public String getUsernameFromToken(String token) {
		//creating claim obj using the secrete key and token 
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		
		//extract and return the username/subject
		return claims.getSubject();
	}
	
	//3.validate jwt token
	public boolean validateToken(String token )  {
		try {
			//check if secret matches
			Jwts.parser().setSigningKey(this.secret).parseClaimsJwt(token).getBody();
			return true;
		}catch(SignatureException e) {
			throw new BlogAPIException(HttpStatus.UNAUTHORIZED,"token sigature is invalid");
		}catch (MalformedJwtException e) {
			throw new BlogAPIException(HttpStatus.UNAUTHORIZED,"jwt token  is malformed");
		}catch (ExpiredJwtException e) {
			throw new BlogAPIException(HttpStatus.UNAUTHORIZED,"jwt token is expired ");
		}catch (UnsupportedJwtException e) {
			throw new BlogAPIException(HttpStatus.UNAUTHORIZED,"jwt token is unsupported");
		}catch (IllegalArgumentException e) {
			throw new BlogAPIException(HttpStatus.UNAUTHORIZED,"jwt token claims string is empty");
		}
		
	}

}
