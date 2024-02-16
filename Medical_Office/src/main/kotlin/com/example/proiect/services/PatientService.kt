package com.example.proiect.services

import com.example.proiect.hateoas.PatientHateoas
import com.example.proiect.repository.PatientRepository
import com.example.proiect.model.Patient
import com.example.proiect.model.Physician
import org.springframework.stereotype.*
import com.example.proiect.services.interfaces.IPatientService
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import com.example.proiect.model.Appointment
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class PatientService(
    private val patientRepository: PatientRepository
) : IPatientService {

    override fun getAllPatients(): ResponseEntity<out Any> {
        return try {
            val patients = patientRepository.findAll().toMutableList()
            ResponseEntity(PatientHateoas().toCollectionModel(patients), HttpStatus.OK)
        }catch (e: Exception) {
            ResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    override fun findByIdUser(id: Int): ResponseEntity<out Any> {
        try {
            val patient = patientRepository.findByIdUser(id)
                ?: return ResponseEntity("Patient not found", HttpStatus.NOT_FOUND)

            return ResponseEntity(patient.let { PatientHateoas().toModel(it) }, HttpStatus.OK)
        }catch (e: Exception) {
            return ResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    override fun getPhysicians(id: Int): ResponseEntity<out Any> {
        try {
            if (patientRepository.findByIdUser(id) == null) {
                return ResponseEntity("Patient doesn't exist", HttpStatus.NOT_FOUND)
            }

            val stringList = patientRepository.findPhysicians(id)
            val physicians = mutableListOf<Physician>()

            stringList.forEach {
                val current = it.split(",")
                physicians.add(Physician(
                    current[0].toInt(),
                    current[1].toInt(),
                    current[2],
                    current[3],
                    current[4],
                    current[5],
                    current[6])
                )
            }

            return ResponseEntity(PatientHateoas().toModelJoin(physicians.toMutableList(), id), HttpStatus.OK)
        }catch (e: Exception) {
            return ResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    override fun findAppointmentByDate(id: Int, date: String, type: String?): ResponseEntity<out Any> {
        try {
            if (patientRepository.findByIdUser(id) == null) {
                return ResponseEntity("Patient doesn't exist", HttpStatus.NOT_FOUND)
            }

            if ((type != null && (!type.matches(Regex("\\b(day|month)\\b")) ||          // day | month
                        !date.matches(Regex("\\b\\d{1,2}\\b")))) ||                     // one or two digits
                (type == null && !date.matches(Regex("\\d{4}-\\d{2}-\\d{2}")))          // YYYY-MM-DD
            ) {
                return ResponseEntity("Invalid date or type format", HttpStatus.UNPROCESSABLE_ENTITY)
            }

            val stringList = patientRepository.findAppointmentByDate(id, date, type)
            val appointments = mutableListOf<Appointment>()

            stringList.forEach {
                val current = it.split(",")
                appointments.add(
                    Appointment(
                        current[2].toInt(),
                        current[3].toInt(),
                        current[0],
                        current[1],
                    ))
            }

            return ResponseEntity(PatientHateoas().toModelDate(appointments.toMutableList(), id, date, type), HttpStatus.OK)
        }catch (e: Exception) {
            return ResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    override fun creatOrUpdatePatient(cnp: String, patient: Patient): ResponseEntity<out Any> {
        try {
            if (!patient.cnp.matches(Regex("^\\d{13}\$")) ||
                !patient.id_user.toString().matches(Regex("^\\d{1,10}\$")) ||
                !patient.nume.matches(Regex("^[a-zA-Z]+(([ -][a-zA-Z ])?[a-zA-Z]*)*\$")) ||
                !patient.prenume.matches(Regex("^[a-zA-Z]+(([ -][a-zA-Z ])?[a-zA-Z]*)*\$")) ||
                !patient.telefon.matches(Regex("^07[2-9][0-9]{7}\$")) ||
                !patient.email.matches(Regex("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})\$")) ||
                !patient.is_active.toString().matches(Regex("^(0|1|true|false)\$")) ||
                !patient.data_nasterii.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))
            ) {
                return ResponseEntity("Invalid patient data", HttpStatus.UNPROCESSABLE_ENTITY)
            }

            val dataNasterii = LocalDate.parse(patient.data_nasterii, DateTimeFormatter.ISO_DATE)
            val varsta = LocalDate.now().year - dataNasterii.year

            // Verificarea vârstei minime
            val varstaMinima = 18
            if (varsta < varstaMinima) {
                return ResponseEntity("The patient must be at least $varstaMinima years old", HttpStatus.UNPROCESSABLE_ENTITY)
            }

            val existingPatient = patientRepository.findByCnp(cnp)

            if (existingPatient == null) { // cream un nou pacient
                if (patientRepository.findByEmail(patient.email) != null) {
                    return ResponseEntity("Email is already used", HttpStatus.CONFLICT)
                }

                return ResponseEntity(
                    patientRepository.save(patient).let { PatientHateoas().toModelCreate(it) },
                    HttpStatus.CREATED
                )
            } else { // update
                if (existingPatient.cnp == patient.cnp && existingPatient.id_user == patient.id_user) { //update
                    val patientEmail = patientRepository.findByEmail(patient.email)

                    if (patientEmail != null && patientEmail.cnp != patient.cnp) {
                        return ResponseEntity("Email is already used", HttpStatus.CONFLICT)
                    }

                    return ResponseEntity(patientRepository.save(patient).let { PatientHateoas().toModelCreate(it) }, HttpStatus.OK)
                }
                else {
                    return ResponseEntity("Cnp and id_user fields cannot be updated", HttpStatus.CONFLICT)
                }
            }
        } catch (e: Exception) {
            return ResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    override fun deletePatient(id: Int) : ResponseEntity<out Any> {
        return try{
            val existingPatient = patientRepository.findByIdUser(id)

            if(existingPatient == null) {
                ResponseEntity("Patient doesn't exist", HttpStatus.NOT_FOUND)
            } else{
                existingPatient.is_active = false
                ResponseEntity(patientRepository.save(existingPatient).let { PatientHateoas().toModelDelete(it) }, HttpStatus.OK)
            }
        }catch (e: EmptyResultDataAccessException) {
            ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}