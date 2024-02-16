package com.example.proiect.controller

import com.example.proiect.hateoas.PatientHateoas
import com.example.proiect.model.Appointment
import com.example.proiect.services.PatientService
import com.example.proiect.model.Patient
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/medical_office/patients")
@Tag(
    name = "Patient Api", description = "Set of operations to process patients"
)
class PatientController(
    private val patientService: PatientService
){
    @Operation(
        summary = "Create or Update Patient", description = "This operation creates or updates an existing patient"
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully updated patient"),
        ApiResponse(responseCode = "201", description = "Successfully created patient"),
        ApiResponse(responseCode = "422", description = "Invalid patient data"),
        ApiResponse(responseCode = "409", description = "Email already used"),
        ApiResponse(responseCode = "409", description = "Cnp and id_user fields cannot be updated"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
    @PutMapping("/{cnp}")
    fun createOrUpdatePatient(@PathVariable cnp: String, @RequestBody patient: Patient): ResponseEntity<out Any> {
        //logica este in serviciu ca sa nu se aglomereze controllerul
        return patientService.creatOrUpdatePatient(cnp, patient)
    }

    @Operation(
        summary = "Obtain Patient", description = "This operation obtains a patient by id"
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully retrieved patient"),
        ApiResponse(responseCode = "404", description = "Patient not found"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
    @GetMapping("/{id}")
    fun getPatient(@PathVariable id: Int): ResponseEntity<out Any> {
        return patientService.findByIdUser(id)
    }

    @Operation(
        summary = "Obtain patients", description = "This operation obtains all patients"
    )
    @ApiResponses(
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
    @GetMapping
    fun getAllPatients(): ResponseEntity<out Any> {
        return patientService.getAllPatients()
    }

    @Operation(
        summary = "Get a patient's physicians", description = "This operation is obtained by the doctors to whom a patient is scheduled"
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully retrieved patient's physicians"),
        ApiResponse(responseCode = "404", description = "Patient doesn't exist"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
    @GetMapping("/{id}/physicians")
    fun getPhysicians(@PathVariable id: Int): ResponseEntity<out Any> {
        return patientService.getPhysicians(id)
    }

    @Operation(
        summary = "Get patient's appointments by date", description = "Get appointments for a patient filtered by date and type."
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully retrieved patient's appointments"),
        ApiResponse(responseCode = "404", description = "Patient doesn't exist"),
        ApiResponse(responseCode = "422", description = "Invalid date or type format"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
    @GetMapping("/{id}", params = ["date"])
    fun getPatientAppointmentsByDate(@PathVariable(required = true) id: Int,
                                     @RequestParam(required = true) date: String,
                                     @RequestParam(required = false) type: String?): ResponseEntity<out Any> {
        return patientService.findAppointmentByDate(id, date, type)
    }

    @Operation(
        summary = "Deactivate account of a specific patient", description = "This operation delete a patient"
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully deleted the patient."),
        ApiResponse(responseCode = "404", description = "Patient doesn't exist"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
    @DeleteMapping("/{id}")
    fun deletePatient(@PathVariable id: Int): ResponseEntity<out Any> {
        return patientService.deletePatient(id)
    }

/*
    // de catre pacient
    @PostMapping
    fun createAppointment(@RequestBody appointment: Appointment): ResponseEntity<out Any> {
        return appointmentService.createAppointment(appointment)
    }


    // de catre doctor si pacient
    // doctorul face update sta status in onorata/neprezentata
    // pacientul face update la status in anulata
    // consider ca nu trebuie sa am un pathVariable deoarece din interfata
    // o sa iau toate informatiile si o sa le trimit
    @PutMapping
    fun updateStatus(@RequestBody appointment: Appointment) : ResponseEntity<out Any> {
        return appointmentService.updateStatus(appointment)
    }
*/

}