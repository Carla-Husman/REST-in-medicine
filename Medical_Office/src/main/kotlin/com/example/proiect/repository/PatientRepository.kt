package com.example.proiect.repository

import org.springframework.data.jpa.repository.JpaRepository
import com.example.proiect.model.Patient
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.*

@Repository
interface PatientRepository : JpaRepository<Patient, String>{

    @Query(value = "SELECT * FROM pacienti\n" +
            "WHERE id_user = :id", nativeQuery = true)
    fun findByIdUser(id: Int): Patient?

    fun findByCnp(id: String): Patient?

    fun findByEmail(email: String): Patient?

    override fun findAll(): List<Patient>

    @Query(value = "SELECT doctori.*\n" +
            "FROM doctori\n" +
            "         JOIN programari ON doctori.id_user = programari.id_doctor\n" +
            "WHERE programari.id_pacient = :id", nativeQuery = true)
    fun findPhysicians(id: Int): List<String>

    @Query(value = "SELECT * FROM programari WHERE id_pacient = :id\n" +
            "            AND ((:type IS NULL AND DATE_FORMAT(data, '%Y-%m-%d') = :date)\n" +
            "            OR (:type = 'month' AND MONTH(data) = :date)\n" +
            "            OR (:type = 'day' AND DAY(data) = :date))", nativeQuery = true)
    fun findAppointmentByDate(id: Int, date: String, type: String?): List<String>
}