package com.example.proiect.controller

import com.example.proiect.services.PhysicianService
import com.example.proiect.model.Physician
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.example.proiect.hateoas.PhysicianHateoas
import com.example.proiect.model.Appointment
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.jetbrains.kotlin.resolve.calls.util.replaceReturnTypeByUnknown

@RestController
@RequestMapping("/api/medical_office/physicians")
@Tag(
    name = "Physician API", description = "This provides all operations for managing physicians"
)
class PhysicianController(
    private val physicianService: PhysicianService
) {
    @Operation(
        summary = "Create Physician", description = "This operation creates a physician"
    )
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Successfully created physician"),
        ApiResponse(responseCode = "422", description = "Invalid physician data"),
        ApiResponse(responseCode = "409", description = "Email is already used"),
        ApiResponse(responseCode = "409", description = "Physician already exists"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
    @PostMapping
    fun createPhysician(@RequestBody physician: Physician): ResponseEntity<out Any> {
        return physicianService.createPhysician(physician)
    }

    @Operation(
        summary = "Create Physician", description = "This operation updates an existing physician"
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully updated physician"),
        ApiResponse(responseCode = "404", description = "Physician not found"),
        ApiResponse(responseCode = "409", description = "Email already used"),
        ApiResponse(responseCode = "422", description = "Invalid physician data"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
    @PutMapping("/{id}")
    fun updatePhysician(@PathVariable id: Int, @RequestBody physician: Physician): ResponseEntity<out Any> {
        // toate verificarile se fac in serviciu, ca sa nu se aglomereze controllerul
        return physicianService.updatePhysician(physician, id)
    }

    @Operation(
        summary = "Find a physician", description = "This operation searches for a physician by id"
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully retrieved physician"),
        ApiResponse(responseCode = "404", description = "Physician not found"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
    @GetMapping("/{id}")
    fun getPhysician(@PathVariable id: Int): ResponseEntity<out Any> {
        return physicianService.findById(id)
    }

    @Operation(
        summary = "Obtain physicians", description = "This operation obtains all physicians"
    )
    @ApiResponses(
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
    @GetMapping
    fun getAllPhysicians(): ResponseEntity<out Any> {
        return physicianService.getAllPhysicians()
    }

    @Operation(
        summary = "Paginated display", description = "This operation displays physicians on different pages"
    )
    @ApiResponses(
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
    @GetMapping(params = ["page"])
    fun getPageable(@RequestParam("page") page: Int,
            @RequestParam(name = "size", required = false) itemsPerPage: Int?): ResponseEntity<out Any> {

        if (itemsPerPage == null) {
            return physicianService.getPageable(page, 2)
        }
        return physicianService.getPageable(page, itemsPerPage)
    }

    @Operation(
        summary = "Get a doctor's patients", description = "This operation gets patients scheduled with a certain doctor"
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully retrieved physician's patients"),
        ApiResponse(responseCode = "404", description = "Physician not found"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
    @GetMapping("/{id}/patients")
    fun getPatients(@PathVariable id: Int): ResponseEntity<out Any> {
        return physicianService.getPatients(id)
    }

    @Operation(
        summary = "Get physicians by specialization", description = "This operation gets physicians by specialization"
    )
    @ApiResponses(
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
    @GetMapping(params = ["specialization"])
    fun getPhysiciansBySpecialization(@RequestParam(name = "specialization", required = true) specialization : String): ResponseEntity<out Any> {
        return physicianService.findBySpecialization(specialization)
    }

    @Operation(
        summary = "Get physicians by name", description = "This operation gets physicians by name"
    )
    @ApiResponses(
        ApiResponse(responseCode = "404", description = "Physician not found"),
        ApiResponse(responseCode = "500", description = "Internal server error"),
    )
    @GetMapping(params = ["name"])
    fun getPhysiciansByName(@RequestParam(name = "name", required = true) name: String): ResponseEntity<out Any> {
        return physicianService.findByName(name)
    }

    @Operation(
        summary = "Delete a physician", description = "This operation deletes a physician"
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully deleted physician"),
        ApiResponse(responseCode = "404", description = "Physician not found"),
        ApiResponse(responseCode = "405", description = "Physician has an active appointment"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
    @DeleteMapping("/{id}")
    fun deletePhysician(@PathVariable id: Int): ResponseEntity<out Any> {
        return physicianService.deletePhysician(id)
    }
/*
    @Operation(
        summary = "Update Appointment", description = "This operation updates an appointment"
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully updated appointment"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    )
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