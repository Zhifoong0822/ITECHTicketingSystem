package com.example.itechticketing

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    // Change from findByUsername to findByUserName to match the entity field
    fun findByUserName(userName: String): Optional<User>
    fun existsByUserName(userName: String): Boolean
}