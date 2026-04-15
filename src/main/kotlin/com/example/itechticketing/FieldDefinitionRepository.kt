package com.example.itechticketing // Ensure this matches your other files!

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FieldDefinitionRepository : JpaRepository<FieldDefinition, Long>