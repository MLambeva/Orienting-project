package com.orienting.orienting.spring.configuration;

import com.orienting.common.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    @Autowired
    private final JwtAuthenticationFilter jwtAuthFilter;
    @Autowired
    private final AuthenticationProvider authenticationProvider;
    @Autowired
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/*").permitAll()
                        .requestMatchers("/api/users/all").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers("/api/users/byId/*").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers("/api/users/me").hasAnyAuthority(UserRole.ADMIN.name(), UserRole.COACH.name(), UserRole.COMPETITOR.name())
                        .requestMatchers("/api/users/byUcn/*").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers("/api/users/role/**").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers("/api/users/role").hasAnyAuthority(UserRole.ADMIN.name(), UserRole.COACH.name(), UserRole.COMPETITOR.name())
                        .requestMatchers("/api/users/allCoaches").hasAnyAuthority(UserRole.ADMIN.name(), UserRole.COACH.name(), UserRole.COMPETITOR.name())
                        .requestMatchers("/api/users/allCompetitors").hasAnyAuthority(UserRole.ADMIN.name(), UserRole.COACH.name())
                        .requestMatchers("/api/users/withCoaches/*").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers("/api/users/withCoaches").hasAuthority(UserRole.COMPETITOR.name())
                        .requestMatchers("/api/users/competition/*").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers("/api/users/competition").hasAnyAuthority(UserRole.COACH.name(), UserRole.COMPETITOR.name())
                        .requestMatchers("/api/users/allUsersInClub/**").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers("/api/users/allCompetitorsInClub/**").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers("/api/users/allCoachesInClub/**").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers("/api/users/coaches").hasAnyAuthority(UserRole.COACH.name(), UserRole.COMPETITOR.name())
                        .requestMatchers("/api/users/competitors").hasAnyAuthority(UserRole.COACH.name(), UserRole.COMPETITOR.name())
                        .requestMatchers("/api/users/club").hasAnyAuthority(UserRole.COACH.name(), UserRole.COMPETITOR.name())
                        .requestMatchers("/api/users/remove/*/*").hasAnyAuthority(UserRole.ADMIN.name(), UserRole.COACH.name())
                        .requestMatchers("/api/users/update/*/*").hasAnyAuthority(UserRole.ADMIN.name(), UserRole.COACH.name())
                        .requestMatchers("/api/users/update").hasAnyAuthority(UserRole.COACH.name(), UserRole.COMPETITOR.name(), UserRole.ADMIN.name())
                        .requestMatchers("/api/users/leftClub/*").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers("/api/users/leftClub").hasAuthority(UserRole.COMPETITOR.name())
                        .requestMatchers("/api/users/makeCoach/*").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers("/api/users/removeCoach/*").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers("/api/users/setCoach/*/*").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers("/api/users/addClub/*/*").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers("/api/users/addClub/*").hasAnyAuthority(UserRole.COACH.name(), UserRole.COMPETITOR.name())
                        //Clubs:
                        .requestMatchers("/api/clubs/all").hasAnyAuthority(UserRole.COACH.name(), UserRole.COMPETITOR.name(), UserRole.ADMIN.name())
                        .requestMatchers("/api/clubs/allWithUsers").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers("/api/clubs/*/*").hasAnyAuthority(UserRole.COACH.name(), UserRole.ADMIN.name())
                        .requestMatchers("/api/clubs/withUsers/*/*").hasAnyAuthority(UserRole.COACH.name(), UserRole.ADMIN.name())
                        .requestMatchers("/api/clubs/add/*").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers("/api/clubs/delete/*/*").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers("/api/clubs/update/*/*").hasAuthority(UserRole.ADMIN.name())
                        //Competitions
                        .requestMatchers("/api/competitions/all").permitAll()
                        .requestMatchers("/api/competitions/allWithParticipants").hasAnyAuthority(UserRole.COACH.name(), UserRole.COMPETITOR.name(), UserRole.ADMIN.name())
                        .requestMatchers("/api/competitions/*/*").hasAnyAuthority(UserRole.COACH.name(), UserRole.COMPETITOR.name(), UserRole.ADMIN.name())
                        .requestMatchers("/api/competitions/withUsers/*/*").hasAnyAuthority(UserRole.COACH.name(), UserRole.COMPETITOR.name(), UserRole.ADMIN.name())
                        .requestMatchers("/api/competitions/add").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers("/api/competitions/delete/*/*").hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers("/api/competitions/update/*/*").hasAuthority(UserRole.ADMIN.name())

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout((logout) -> logout.logoutUrl("api/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext())));

        return httpSecurity.build();
    }
}

