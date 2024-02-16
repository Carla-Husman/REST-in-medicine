package com.example.consultation.services.interfaces

import com.example.consultation.model.Consultation
import org.springframework.http.ResponseEntity

interface IConsultationService {
    fun findAll(): ResponseEntity<out Any>

    fun createConsultation(consultation: Consultation): ResponseEntity<out Any>

    fun updateConsultation(consultation: Consultation): ResponseEntity<out Any>

    fun getConsultationByDataAndIdDoctorAndIdPacient(data: String, id_doctor: Int, id_pacient: Int): ResponseEntity<out Any>
}