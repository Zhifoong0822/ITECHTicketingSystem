package com.example.itechticketing

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(userName: String): UserDetails {
        return userRepository.findByUserName(userName)
            .orElseThrow { UsernameNotFoundException("User not found: $userName") }
    }
}