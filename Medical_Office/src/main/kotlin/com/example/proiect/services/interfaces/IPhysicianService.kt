package com.example.proiect.services.interfaces

import com.example.proiect.model.Patient
import com.example.proiect.model.Physician
import org.springframework.http.ResponseEntity

interface IPhysicianService {
    fun createPhysician(physician: Physician): ResponseEntity<out Any>

    fun getAllPhysicians(): ResponseEntity<out Any>

    fun findById(id: Int): ResponseEntity<out Any>

    fun getPatients(id: Int): ResponseEntity<out Any>

    fun findBySpecialization(specialization: String): ResponseEntity<out Any>

    fun findByName(name: String): ResponseEntity<out Any>

    fun updatePhysician(physician: Physician, id: Int): ResponseEntity<out Any>

    fun deletePhysician(id: Int): ResponseEntity<out Any>

    fun getPageable(page: Int, itemsPerPage: Int): ResponseEntity<out Any>
}
