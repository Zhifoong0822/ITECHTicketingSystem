package com.example.itechticketing

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customUserDetailsService: CustomUserDetailsService
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    // Public access
                    .requestMatchers("/login", "/css/**", "/js/**", "/webjars/**").permitAll()
                    // Admin only - use hasAuthority instead of hasRole since your User class returns ROLE_ prefix
                    .requestMatchers("/admin/**", "/summary/**").hasAuthority("ROLE_ADMIN")
                    // Engineer access (can create tickets)
                    .requestMatchers("/tickets/create", "/tickets/save").hasAnyAuthority("ROLE_ADMIN", "ROLE_ENGINEER")
                    // Engineer and China can view/search
                    .requestMatchers("/tickets/search", "/tickets/details/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_ENGINEER", "ROLE_CHINA")
                    // Engineer can edit/delete their tickets
                    .requestMatchers("/tickets/edit/**", "/tickets/update", "/tickets/delete/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_ENGINEER")
                    // Everything else requires authentication
                    .anyRequest().authenticated()
            }
            .formLogin { form ->
                form
                    .loginPage("/login")
                    .defaultSuccessUrl("/dashboard", true)
                    .permitAll()
            }
            .logout { logout ->
                logout
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
            }
            .userDetailsService(customUserDetailsService)

        return http.build()
    }
}