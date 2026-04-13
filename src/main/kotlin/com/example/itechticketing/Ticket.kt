package com.example.itechticketing
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(name = "tickets")
class Ticket(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val ticketNo: Long = 0, // Primary Key (工单编号)

    @Column(nullable = false)
    var date: LocalDate, // Date (日期)

    var brand: String? = null, // Brand (品牌)

    var partsNumber: String? = null, // Parts Number (零件编号)

    var dcRoom: String? = null, // DC 机房

    var ticketTitle: String? = null, // Tickets (工单标题/内容)

    var leadEngineer: String? = null, // Lead Engineer (主工程师)

    var otherEngineer: String? = null, // Other Engineer (协助工程师)

    var manpower: Double? = null, // Total Manpower (总人天)

    var startTime: LocalTime? = null, // Start Time (开始时间)

    var endTime: LocalTime? = null, // End Time (结束时间)

    @Enumerated(EnumType.STRING)
    var status: TicketStatus = TicketStatus.OPEN
)

enum class TicketStatus {
    OPEN, IN_PROGRESS, COMPLETED
}