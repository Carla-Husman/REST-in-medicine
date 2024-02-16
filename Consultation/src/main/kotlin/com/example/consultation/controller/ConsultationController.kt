package com.example.consultation.controller

import com.example.consultation.model.Consultation
import com.example.consultation.services.ConsultationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/medical_office/consultation")
@Tag(
    name = "Consultation Api", description = "Set of operations to process consultations"
)
class ConsultationController(
    private val consultationService: ConsultationService
) {
    @Operation(
        summary = "Create a consultation", description = "This operation creates a new consultation"
    )
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Successfully created consultation"),
        ApiResponse(responseCode = "409", description = "Consultation already exists"),
        ApiResponse(responseCode = "422", description = "Invalid consultation diagnostic"),
        ApiResponse(responseCode = "422", description = "Invalid consultation date"),
        ApiResponse(responseCode = "422", description = "Invalid investigation name"),
        ApiResponse(responseCode = "422", description = "Invalid investigation result"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
    @PostMapping
    fun createConsultation(@RequestBody consultation: Consultation): ResponseEntity<out Any> {
        return consultationService.createConsultation(consultation)
    }

    @Operation(
        summary = "Update a consultation", description = "This operation updates an existing consultation"
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully updated consultation"),
        ApiResponse(responseCode = "404", description = "Consultation not found"),
        ApiResponse(responseCode = "409", description = "Consultation already exists"),
        ApiResponse(responseCode = "422", description = "Invalid consultation diagnostic"),
        ApiResponse(responseCode = "422", description = "Invalid consultation date"),
        ApiResponse(responseCode = "422", description = "Invalid investigation name"),
        ApiResponse(responseCode = "422", description = "Invalid investigation result"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
    @PutMapping
    fun updateConsultation(@RequestBody consultation: Consultation): ResponseEntity<out Any> {
        return consultationService.updateConsultation(consultation)
    }

    @Operation(
        summary = "Obtain all consultations", description = "This operation obtains all consultations"
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully retrieved consultations"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
    @GetMapping
    fun getAllConsultations() : ResponseEntity<out Any> {
        return consultationService.findAll()
    }

    @Operation(
        summary = "Obtain a consultation", description = "This operation obtains a consultation by date, id_doctor and id_pacient"
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully retrieved consultation"),
        ApiResponse(responseCode = "404", description = "Consultation not found"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
    @GetMapping(params = ["data", "id_doctor", "id_pacient"])
    fun getConsultation(
        @RequestParam("data") date: String,
        @RequestParam("id_doctor") id_doctor: Int,
        @RequestParam("id_pacient") id_pacient: Int
    ): ResponseEntity<out Any> {
        return consultationService.getConsultationByDataAndIdDoctorAndIdPacient(date, id_doctor, id_pacient)
    }
}