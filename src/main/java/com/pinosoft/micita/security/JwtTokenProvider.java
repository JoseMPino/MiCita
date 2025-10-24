package com.pinosoft.micita.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

	 @Value("${app.jwt-secret}")
	    private String jwtSecret;

	    @Value("${app.jwt-expiration-milliseconds}")
	    private int jwtExpirationInMs;
	    
	    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

	    public String generateToken(Authentication authentication) {
	        String username = authentication.getName();
	        Date currentDate = new Date();
	        Date expireDate = new Date(currentDate.getTime() + jwtExpirationInMs);

	        String authorities = authentication.getAuthorities().stream()
	                .map(GrantedAuthority::getAuthority)
	                .collect(Collectors.joining(","));

	        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

	        return Jwts.builder()
	                .setSubject(username)
	                .claim("auth", authorities)
	                .setIssuedAt(new Date())
	                .setExpiration(expireDate)
	                .signWith(key, SignatureAlgorithm.HS512)
	                .compact();
	    }

	    public String getUsernameFromJWT(String token) {
	        Claims claims = Jwts.parserBuilder()
	                .setSigningKey(jwtSecret.getBytes())
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	        return claims.getSubject();
	    }

	    public boolean validateToken(String token) {
	        try {
	            Jwts.parserBuilder()
	                .setSigningKey(jwtSecret.getBytes())
	                .build()
	                .parseClaimsJws(token);
	            return true;
	        } catch (MalformedJwtException ex) {
	            logger.error("Token JWT mal formado");
	        } catch (ExpiredJwtException ex) {
	            logger.error("Token JWT expirado");
	        } catch (UnsupportedJwtException ex) {
	            logger.error("Token JWT no soportado");
	        } catch (IllegalArgumentException ex) {
	            logger.error("El claims JWT está vacío");
	        }
	        return false;
	    }

	    public Authentication getAuthentication(String token) {
	        Claims claims = Jwts.parserBuilder()
	                .setSigningKey(jwtSecret.getBytes())
	                .build()
	                .parseClaimsJws(token)
	                .getBody();

	        String username = claims.getSubject();
	        String authoritiesStr = claims.get("auth", String.class);

	        Collection<? extends GrantedAuthority> authorities = 
	            Arrays.stream(authoritiesStr.split(","))
	                  .map(SimpleGrantedAuthority::new)
	                  .collect(Collectors.toList());

	        User principal = new User(username, "", authorities);

	        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	    }
	

}
