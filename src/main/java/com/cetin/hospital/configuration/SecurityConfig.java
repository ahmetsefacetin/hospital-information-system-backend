package com.cetin.hospital.configuration;

import com.cetin.hospital.enumeration.Role;
import com.cetin.hospital.security.JwtAuthenticationFilter;
import com.cetin.hospital.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, UserDetailsServiceImpl userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers("/auth/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/patients/all")
                                .hasAnyRole(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.GET, "/doctors/all")
                                .hasAnyRole(Role.ADMIN.name(), Role.PATIENT.name())
                                .requestMatchers(HttpMethod.POST, "/doctors")
                                .hasAnyRole(Role.ADMIN.name(), Role.DOCTOR.name())
                                .requestMatchers(HttpMethod.POST, "/patients")
                                .hasAnyRole(Role.ADMIN.name(), Role.PATIENT.name())
                                .requestMatchers(HttpMethod.POST, "/prescriptions")
                                .hasAnyRole(Role.ADMIN.name(), Role.DOCTOR.name())
                                .requestMatchers(HttpMethod.POST, "/appointments")
                                .hasAnyRole(Role.ADMIN.name(), Role.PATIENT.name())
                                .requestMatchers(HttpMethod.GET, "/appointments")
                                .hasAnyRole(Role.ADMIN.name(), Role.DOCTOR.name())
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractAuthenticationFilterConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


