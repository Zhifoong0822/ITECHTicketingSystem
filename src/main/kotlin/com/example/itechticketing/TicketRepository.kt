package com.example.itechticketing

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface TicketRepository : JpaRepository<Ticket, Long> {

    // 1. Search by Ticket Number (Built-in: findById)

    // 2. Search by Date Range
    fun findByDateCreatedBetween(start: LocalDate, end: LocalDate): List<Ticket>
    // 3. Search by Lead Engineer
    @Query("SELECT t FROM Ticket t JOIN t.dynamicValues v WHERE v.fieldDefinition.label = 'Lead Engineer' AND v.value LIKE %:name%")
    fun findByLeadEngineer(name: String): List<Ticket>
}