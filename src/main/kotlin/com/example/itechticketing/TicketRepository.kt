package com.example.itechticketing

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface TicketRepository : JpaRepository<Ticket, Long> {

    // 1. Search by Ticket Number (Built-in: findById)

    // 2. Search by Date Range
    fun findByDateBetween(startDate: LocalDate, endDate: LocalDate): List<Ticket>

    // 3. Search by Lead Engineer
    fun findByLeadEngineerContainingIgnoreCase(name: String): List<Ticket>
}