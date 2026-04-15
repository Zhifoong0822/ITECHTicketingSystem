package com.example.itechticketing

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "ticket_values")
class TicketValue(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    val ticket: Ticket,

    @ManyToOne
    @JoinColumn(name = "field_id")
    val fieldDefinition: FieldDefinition,

    @Column(name = "field_value")
    var value: String // Everything is stored as a String, converted back when needed
)