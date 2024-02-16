package com.example.proiect.services.interfaces

import com.example.proiect.model.Appointment
import org.springframework.http.ResponseEntity

interface IAppointmentService {
    fun createAppointment (appointment: Appointment) : ResponseEntity<out Any>

    fun updateStatus(appointment: Appointment): ResponseEntity<out Any>

    fun getAllAppointments () : ResponseEntity<out Any>

    fun getAppointment(id_doctor: Int, id_pacient: Int, data: String): ResponseEntity<out Any>

    fun getAllAppointmentsForPatient(id: Int): ResponseEntity<out Any>

    fun getAllAppointmentsForPhysician(id: Int): ResponseEntity<out Any>
}