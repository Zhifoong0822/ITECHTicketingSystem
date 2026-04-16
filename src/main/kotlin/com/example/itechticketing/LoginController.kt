package com.example.itechticketing

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginController {

    @GetMapping("/login")
    fun login(model: Model): String {
        return "login"
    }

    @GetMapping("/dashboard")
    fun dashboard(): String {
        return "redirect:/tickets/search"
    }
}