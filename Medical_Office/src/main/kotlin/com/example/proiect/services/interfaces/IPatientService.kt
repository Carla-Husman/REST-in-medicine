package com.example.proiect.services.interfaces

import com.example.proiect.model.Patient
import com.example.proiect.model.Physician
import org.springframework.http.ResponseEntity
import com.example.proiect.model.Appointment

interface IPatientService {
    fun getAllPatients(): ResponseEntity<out Any>

    fun findByIdUser(id: Int): ResponseEntity<out Any>

    fun getPhysicians(id: Int): ResponseEntity<out Any>

    fun findAppointmentByDate(id: Int, date: String, type: String?): ResponseEntity<out Any>

    fun creatOrUpdatePatient(cnp: String, patient: Patient): ResponseEntity<out Any>

    fun deletePatient(id: Int) : ResponseEntity<out Any>
}