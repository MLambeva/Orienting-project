package com.orienting.orienting.spring.configuration;

import com.orienting.common.repository.TokenRepository;
import com.orienting.common.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@ComponentScan(basePackages = "com.orienting.common.utils")
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private final JwtUtils jwtUtils;
    @Autowired
    private final UserDetailsService userDetailsService;
    @Autowired
    private final TokenRepository tokenRepository;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if(request.getServletPath().contains("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
         if(authHeader == null || !authHeader.startsWith("Bearer ")) {
             filterChain.doFilter(request, response);
             return;
         }
        final String jwt = authHeader.substring(7);
         final String userEmail = jwtUtils.extractEmail(jwt);
         if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) { //user is not connected yet
             UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
             boolean isTokenValid = tokenRepository.findByToken(jwt)
                     .map(t -> !t.isExpired() && !t.isRevoked())
                     .orElse(false);
             if(jwtUtils.isTokenValid(jwt, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
         }
    }
}
