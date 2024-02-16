package com.example.proiect.controller

import com.example.proiect.model.Appointment
import com.example.proiect.services.AppointmentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/medical_office/appointments")
@Tag(
    name = "Appointment API", description = "This provides all operations for managing appointments"
)
class AppointmentController (
    private val appointmentService: AppointmentService
) {
    @Operation(
        summary = "Create Appointment", description = "This operation creates an appointment"
    )
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Successfully created appointment"),
        ApiResponse(responseCode = "422", description = "Invalid appointment data"),
        ApiResponse(responseCode = "422", description = "A patient cannot have more than one appointment on the same day with the same doctor"),
        ApiResponse(responseCode = "422", description = "There is another appointment at the same time for that doctor"),
        ApiResponse(responseCode = "409", description = "Appointment already exists"),
        ApiResponse(responseCode = "409", description = "Appointments must be for future dates"),
        ApiResponse(responseCode = "409", description = "Appointments must be made at least 15 minutes later than the current time"),
        ApiResponse(responseCode = "409", description = "The doctors are on their lunch break"),
        ApiResponse(responseCode = "409", description = "Schedule: 08:00 - 17:00"),
        ApiResponse(responseCode = "409", description = "The appointment time must be a multiple of 15 minutes."),
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
    // de catre pacient
    @PostMapping
    fun createAppointment(@RequestBody appointment: Appointment): ResponseEntity<out Any> {
        return appointmentService.createAppointment(appointment)
    }

    @Operation(
        summary = "Update Appointment", description = "This operation updates an appointment"
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully updated appointment"),
        ApiResponse(responseCode = "404", description = "Appointment not found"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
    // de catre doctor si pacient
    // doctorul face update la status in onorata/neprezentata
    // pacientul face update la status in anulata
    // consider ca nu trebuie sa am un pathVariable deoarece din interfata
    // o sa iau toate informatiile si o sa le trimit
    @PutMapping
    fun updateStatus(@RequestBody appointment: Appointment) : ResponseEntity<out Any> {
        return appointmentService.updateStatus(appointment)
    }

    @Operation(
        summary = "Get an appointment", description = "This operation gets an appointment by id_doctor, id_pacient and data"
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully retrieved appointment"),
        ApiResponse(responseCode = "404", description = "Appointment not found"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
    @GetMapping(params = ["id_doctor", "id_pacient", "data"])
    fun getAppointment(@RequestParam id_doctor: Int,
                       @RequestParam id_pacient: Int,
                       @RequestParam data: String): ResponseEntity<out Any> {
        return appointmentService.getAppointment(id_doctor, id_pacient, data)
    }

    @Operation(
        summary = "Get all appointments", description = "This operation gets all appointments"
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully retrieved appointments"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
    @GetMapping
    fun getAllAppointments() : ResponseEntity<out Any> {
        return appointmentService.getAllAppointments()
    }

    @Operation(
        summary = "Get all appointments for a patient", description = "This operation gets all appointments for a patient"
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully retrieved appointments"),
        ApiResponse(responseCode = "404", description = "Patient not found"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
    @GetMapping("/patient/{id}")
    fun getAllAppointmentsForPatient(@PathVariable id: Int) : ResponseEntity<out Any> {
        return appointmentService.getAllAppointmentsForPatient(id)
    }

    @Operation(
        summary = "Get all appointments for a doctor", description = "This operation gets all appointments for a doctor"
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully retrieved appointments"),
        ApiResponse(responseCode = "404", description = "Doctor not found"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
    @GetMapping("/physician/{id}")
    fun getAllAppointmentsForPhysician(@PathVariable id: Int) : ResponseEntity<out Any> {
        return appointmentService.getAllAppointmentsForPhysician(id)
    }
}