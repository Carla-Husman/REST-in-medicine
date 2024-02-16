package com.example.proiect.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import com.example.proiect.model.Appointment
import org.springframework.data.jpa.repository.Query

@Repository
interface AppointmentRepository : JpaRepository<Appointment, Int> {

    @Query(value = "SELECT * FROM programari\n" +
            "WHERE id_doctor = :id_doctor AND\n" +
            "id_pacient = :id_pacient AND\n" +
            "data = :data", nativeQuery = true)
    fun getAppointment(id_pacient: Int, id_doctor: Int, data: String): Appointment?

    @Query(value = "SELECT * FROM programari WHERE id_pacient = :id", nativeQuery = true)
    fun getAllAppointmentsForPatient(id: Int): MutableList<Appointment>

    @Query(value = "SELECT * FROM programari WHERE id_doctor = :id", nativeQuery = true)
    fun getAllAppointmentsForPhysician(id: Int): MutableList<Appointment>
}