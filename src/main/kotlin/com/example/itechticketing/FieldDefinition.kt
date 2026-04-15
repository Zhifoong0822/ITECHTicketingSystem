package com.example.itechticketing

import jakarta.persistence.*

@Entity
@Table(name = "field_definitions")
class FieldDefinition(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var label: String, // e.g., "Serial Number" or "DC Room"

    @Column(nullable = false)
    var fieldType: String, // e.g., "TEXT", "NUMBER", "DATE"

    var isRequired: Boolean = false
)