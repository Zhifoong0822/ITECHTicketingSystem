package com.example.itechticketing

data class SummaryDTO(
    val totalTickets: Int,
    val totalManpower: Int,
    val activeTickets: Int,
    val completedTickets: Int,
    val recentTickets: List<TicketRowDTO>,
    val engineerSummary: List<EngineerSummaryDTO>,
    val brands: List<String>
)

data class TicketRowDTO(
    val ticketNo: Long,
    val date: String,
    val brand: String?,
    val leadEngineer: String?,
    val manpower: Int?,
    val status: String?
)

data class EngineerSummaryDTO(
    val name: String,
    val ticketCount: Int,
    val totalManpower: Int
)