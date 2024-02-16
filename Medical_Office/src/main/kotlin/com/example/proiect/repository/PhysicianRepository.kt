package com.example.proiect.repository

import com.example.proiect.model.Physician
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PhysicianRepository : JpaRepository<Physician, Int> {
    @Query(value = "SELECT * FROM doctori WHERE id_user = :id", nativeQuery = true)
    fun findByIdUser(id: Int): Physician?

    override fun findAll(): List<Physician>

    fun findByEmail (email: String): Physician?

    @Query(value = "SELECT pacienti.*\n" +
            "FROM pacienti\n" +
            "         JOIN programari ON pacienti.id_user = programari.id_pacient\n" +
            "WHERE programari.id_doctor = :id", nativeQuery = true)
    fun findPatients(id: Int): List<String>

    @Query(value = "SELECT * FROM doctori " +
            "WHERE specializare = :specialization", nativeQuery = true)
    fun findBySpecialization (specialization: String): List<Physician>

    @Query(value = "SELECT * FROM doctori\n" +
            "WHERE nume LIKE :name%", nativeQuery = true)
    fun findByName (name: String): List<Physician>

    @Query(value = "DELETE FROM doctori\n" +
            "WHERE id_user = :id", nativeQuery = true)
    fun deleteByIdUser(id: Int)

    @Query(value = "SELECT * FROM programari\n" +
            "WHERE id_doctor = :id", nativeQuery = true)
    fun findAppointments(id: Int): List<String>
}