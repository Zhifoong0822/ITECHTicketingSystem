package com.example.itechticketing

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketValueRepository : JpaRepository<TicketValue, Long>