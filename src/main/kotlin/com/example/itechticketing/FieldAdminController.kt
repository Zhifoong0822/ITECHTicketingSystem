package com.example.itechticketing

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/admin/fields")
class AdminFieldController(val fieldRepo: FieldDefinitionRepository) {

    @GetMapping
    fun showManager(model: Model): String {
        // This method is already protected by SecurityConfig (requires ADMIN role)
        model.addAttribute("activeFields", fieldRepo.findAll())
        return "admin-fields"
    }

    @PostMapping("/add")
    fun addField(@RequestParam fieldName: String, @RequestParam fieldType: String): String {
        val newField = FieldDefinition(
            label = fieldName,
            fieldType = fieldType,
            isRequired = false
        )
        fieldRepo.save(newField)
        return "redirect:/admin/fields"
    }

    @PostMapping("/delete/{id}")
    fun deleteField(@PathVariable id: Long): String {
        fieldRepo.deleteById(id)
        return "redirect:/admin/fields"
    }
}