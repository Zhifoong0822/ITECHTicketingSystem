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
                    // 1. Static resources and login page are open to everyone
                    .requestMatchers("/login", "/css/**", "/js/**", "/webjars/**").permitAll()

                    // 2. ADMIN ONLY: Manage System Fields (/admin/**) and Delete functionality
                    // Note: Removed /edit and /update as requested
                    .requestMatchers("/admin/**", "/tickets/delete/**").hasAuthority("ROLE_ADMIN")

                    // 3. ADMIN & ENGINEER: Can access the creation forms and save data
                    .requestMatchers("/tickets/create", "/tickets/save").hasAnyAuthority("ROLE_ADMIN", "ROLE_ENGINEER")

                    // 4. EVERYONE (Admin, Engineer, China): Can search and view details
                    .requestMatchers("/tickets/search", "/tickets/details/**").authenticated()

                    // 5. Global catch-all: any other page requires a login
                    .anyRequest().authenticated()
            }
            .formLogin { form ->
                form
                    .loginPage("/login")
                    // Redirects to /tickets/search after a successful login
                    .defaultSuccessUrl("/tickets/search", true)
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