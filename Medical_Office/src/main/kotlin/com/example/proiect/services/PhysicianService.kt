package com.example.proiect.services

import com.example.proiect.controller.PhysicianController
import com.example.proiect.hateoas.PhysicianHateoas
import com.example.proiect.model.Patient
import com.example.proiect.model.Physician
import com.example.proiect.repository.PhysicianRepository
import com.example.proiect.services.interfaces.IPhysicianService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import src.main.kotlin.com.example.proiect.model.utils.Specialization


@Service
class PhysicianService(
    private val physicianRepository: PhysicianRepository
) : IPhysicianService {
    override fun createPhysician(physician: Physician): ResponseEntity<out Any> {
        try {
            if (!physician.email.matches(Regex("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})\$")) ||
                !physician.telefon.matches(Regex("^07[2-9][0-9]{7}\$")) ||
                !physician.nume.matches(Regex("^[a-zA-Z]+(([ -][a-zA-Z ])?[a-zA-Z]*)*\$")) ||
                !physician.prenume.matches(Regex("^[a-zA-Z]+(([ -][a-zA-Z ])?[a-zA-Z]*)*\$"))
            ) {
                return ResponseEntity("Invalid physician data", HttpStatus.UNPROCESSABLE_ENTITY)
            }

            try {
                Specialization.valueOf(physician.specializare.uppercase())
            } catch (e: IllegalArgumentException) {
                return ResponseEntity("Invalid physician data", HttpStatus.UNPROCESSABLE_ENTITY)
            }

            if(physicianRepository.findByIdUser(physician.id_user) != null){
                return ResponseEntity("Physician already exists", HttpStatus.CONFLICT)
            }

            if(physicianRepository.findByEmail(physician.email) != null){
                return ResponseEntity("Email is already used", HttpStatus.CONFLICT)
            }

            physician.specializare = physician.specializare.uppercase()
            return ResponseEntity(physicianRepository.save(physician).let {
                PhysicianHateoas().toModelCreate(it) }, HttpStatus.CREATED)

        }catch (e: Exception) {
            println(e.message)
            return ResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    override fun getAllPhysicians(): ResponseEntity<out Any> {
        return try {
            val physicians = physicianRepository.findAll().toMutableList()
            ResponseEntity(PhysicianHateoas().toCollectionModel(physicians), HttpStatus.OK)
        }catch (e: Exception) {
            ResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    override fun findById(id: Int): ResponseEntity<out Any> {
        try {
            val physician = physicianRepository.findByIdUser(id)
                ?: return ResponseEntity("Physician not found", HttpStatus.NOT_FOUND)

            return ResponseEntity(physician.let { PhysicianHateoas().toModel(it) }, HttpStatus.OK)
        }catch (e: Exception) {
            return ResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    override fun getPatients(id: Int): ResponseEntity<out Any> {
        try {
            if (physicianRepository.findByIdUser(id) == null) {
                return ResponseEntity("Physician doesn't exist", HttpStatus.NOT_FOUND)
            }

            val stringList = physicianRepository.findPatients(id)
            val patients = mutableListOf<Patient>()

            stringList.forEach {
                val current = it.split(",")
                patients.add(Patient(
                    current[0],
                    current[1].toInt(),
                    current[2],
                    current[3],
                    current[4],
                    current[5],
                    current[7],
                    current[7].toBoolean())
                )
            }

            return ResponseEntity(PhysicianHateoas().toModelJoin(patients, id), HttpStatus.OK)
        }catch (e: Exception) {
            return ResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    override fun findBySpecialization(specialization: String): ResponseEntity<out Any> {
        return try {
            val physicians = physicianRepository.findBySpecialization(specialization).toMutableList()
            ResponseEntity(PhysicianHateoas().toModelSpecializationOrName(physicians, 2, specialization), HttpStatus.OK)
        }catch (e: Exception) {
            ResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    override fun findByName(name: String): ResponseEntity<out Any> {
        return try {
            val physicians = physicianRepository.findByName(name).toMutableList()
            ResponseEntity(PhysicianHateoas().toModelSpecializationOrName(physicians, 1, name), HttpStatus.OK)
        }catch (e: Exception) {
            ResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    override fun updatePhysician(physician: Physician, id: Int): ResponseEntity<out Any> {
        try {
            if (!physician.nume.matches(Regex("^[a-zA-Z]+(([ -][a-zA-Z ])?[a-zA-Z]*)*\$")) ||
                !physician.prenume.matches(Regex("^[a-zA-Z]+(([ -][a-zA-Z ])?[a-zA-Z]*)*\$")) ||
                !physician.telefon.matches(Regex("^07[2-9][0-9]{7}\$")) ||
                !physician.email.matches(Regex("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})\$"))
            ) {
                return ResponseEntity("Invalid physician data", HttpStatus.UNPROCESSABLE_ENTITY)
            }

            try {
                Specialization.valueOf(physician.specializare.uppercase())
            } catch (e: IllegalArgumentException) {
                return ResponseEntity("Invalid physician data", HttpStatus.UNPROCESSABLE_ENTITY)
            }

            val existingPhysician = physicianRepository.findByIdUser(id) //id_user

            if (existingPhysician != null) {
                val physicianEmail = physicianRepository.findByEmail(physician.email)

                if (physicianEmail != null && physicianEmail.id_user != id) {
                    return ResponseEntity("Email is already used", HttpStatus.CONFLICT)
                }

                existingPhysician.nume = physician.nume
                existingPhysician.prenume = physician.prenume
                existingPhysician.telefon = physician.telefon
                existingPhysician.email = physician.email
                existingPhysician.specializare = physician.specializare.uppercase()

                return ResponseEntity(
                    physicianRepository.save(existingPhysician).let { PhysicianHateoas().toModelPut(it) }, HttpStatus.OK
                )
            }

            return ResponseEntity("Physician not exist", HttpStatus.NOT_FOUND)
        }catch (e: Exception){
            return ResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    override fun deletePhysician(id: Int): ResponseEntity<out Any> {
        return try{
            val existingPhysician = physicianRepository.findByIdUser(id)

            if(existingPhysician == null) {
                ResponseEntity("Physician doesn't exist", HttpStatus.NOT_FOUND)
            } else if(physicianRepository.findAppointments(id).isNotEmpty()){
                ResponseEntity("Physician has an active appointment", HttpStatus.METHOD_NOT_ALLOWED)
            } else{
                physicianRepository.deleteByIdUser(id)
                ResponseEntity("Physician deleted", HttpStatus.OK)
            }
        }catch (e: Exception){
            ResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    override fun getPageable(page: Int, itemsPerPage: Int): ResponseEntity<out Any> {
        return try {
            val pageable: Pageable = PageRequest.of(page - 1, itemsPerPage)

            val physiciansPage: Page<Physician> = physicianRepository.findAll(pageable)

            ResponseEntity(PhysicianHateoas().toModelPage(page, itemsPerPage, physiciansPage), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}