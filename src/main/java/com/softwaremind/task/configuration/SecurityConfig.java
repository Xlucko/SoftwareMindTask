package com.softwaremind.task.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    public static final String SERVER_ROLE = "SERVER";
    public static final String ADMIN_ROLE = "ADMIN";

    @Bean
    public UserDetailsService users() {
        // This InMemoryUserDetailsManager is used because it is simple task, not fit for production
        User.UserBuilder users = User.builder();
        UserDetails user = users
                .username("user")
                .password("{noop}password")
                .roles(SERVER_ROLE)
                .build();
        UserDetails admin = users
                .username("admin")
                .password("{noop}password")
                .roles(ADMIN_ROLE)
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy(ADMIN_ROLE + ">" + SERVER_ROLE);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/table/**", "/reservation/**")
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/csrf").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());

        return http.build();
    }
}
