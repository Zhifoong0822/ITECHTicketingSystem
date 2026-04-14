import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class PageController {

    @GetMapping("/summary")
    fun summaryPage(): String {
        return "summary"
    }
}