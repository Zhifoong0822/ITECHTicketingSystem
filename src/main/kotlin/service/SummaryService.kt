//import com.example.itechticketing.TicketRepository
//import org.springframework.stereotype.Service
//
//@Service
//class SummaryService(
//    private val ticketRepository: TicketRepository
//) {
//
//    fun getSummary(): SummaryDTO {
//
//        val totalTickets = ticketRepository.count()
//        val totalManpower = ticketRepository.findAll()
//            .sumOf { it.manpower ?: 0.0 }
//
//        return SummaryDTO(
//            totalTickets = totalTickets,
//            totalManpower = totalManpower
//        )
//    }
//}