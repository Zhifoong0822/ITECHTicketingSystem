package com.example.itechticketing

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, nullable = false)
    var userName: String,   // ✅ NOT "username"

    @Column(nullable = false)
    var userPassword: String, // ✅ NOT "password"

    @Column(nullable = false)
    var fullName: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var role: UserRole,

    @Column(nullable = false)
    var enabled: Boolean = true

) : UserDetails {

    override fun getUsername(): String = userName
    override fun getPassword(): String = userPassword

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority("ROLE_${role.name}"))
    }

    override fun isAccountNonExpired(): Boolean = enabled
    override fun isAccountNonLocked(): Boolean = enabled
    override fun isCredentialsNonExpired(): Boolean = enabled
    override fun isEnabled(): Boolean = enabled
}

enum class UserRole {
    ADMIN,
    ENGINEER,
    CHINA
}