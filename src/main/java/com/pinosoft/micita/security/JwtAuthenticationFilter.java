package com.pinosoft.micita.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.Authentication;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	 private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	    
	    private final JwtTokenProvider tokenProvider;
	    private final UserDetailsService userDetailsService;

	    @Autowired
	    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, 
	                                 UserDetailsService userDetailsService) {
	        this.tokenProvider = tokenProvider;
	        this.userDetailsService = userDetailsService;
	    }

	    @Override
	    protected void doFilterInternal(HttpServletRequest request, 
	                                  HttpServletResponse response, 
	                                  FilterChain filterChain) throws ServletException, IOException {
	        try {
	            String jwt = getJwtFromRequest(request);
	            
	            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
	                // Usamos el nuevo método getAuthentication del tokenProvider
	                Authentication authentication = tokenProvider.getAuthentication(jwt);
	                
	                logger.info("Usuario autenticado: " + authentication.getName() + 
	                          ", Roles: " + authentication.getAuthorities());
	                
	                SecurityContextHolder.getContext().setAuthentication(authentication);
	            }
	        } catch (Exception ex) {
	            logger.error("No se pudo establecer la autenticación en el contexto de seguridad", ex);
	        }
	        
	        filterChain.doFilter(request, response);
	    }
	    
	    private String getJwtFromRequest(HttpServletRequest request) {
	        String bearerToken = request.getHeader("Authorization");
	        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
	            return bearerToken.substring(7);
	        }
	        return null;
	    }

}
