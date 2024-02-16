package com.example.proiect.hateoas

import com.example.proiect.controller.AppointmentController
import com.example.proiect.controller.PatientController
import com.example.proiect.model.Appointment
import com.example.proiect.model.Patient
import com.example.proiect.model.Physician
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.stereotype.Component


@Component
class PatientHateoas : RepresentationModelAssembler<Patient, EntityModel<Patient>> {
    override fun toModel(patient: Patient): EntityModel<Patient> {
        val links : List<Link> = listOf(
            linkTo(methodOn(PatientController::class.java).getPatient(patient.id_user)).withSelfRel().withType("GET"),
            linkTo(methodOn(PatientController::class.java).getAllPatients()).withRel("parent")
        )

        return EntityModel.of(patient, links)
    }

    override fun toCollectionModel(entities: MutableIterable<Patient>): CollectionModel<EntityModel<Patient>> {
        //Continutul si linkurile pentru fiecare pacient
        val patientModels = entities.map { patient ->
            toModel(patient)
        }

        //Linkurile pentru toți pacienții, cel care se adauga la final
        val links: List<Link> = listOf(
            linkTo(methodOn(PatientController::class.java).getAllPatients()).withSelfRel().withType("GET"),
        )

        return CollectionModel.of(patientModels, links)
    }

    fun toModelCreate (patient: Patient) : EntityModel<Patient> {
        val links : List<Link> = listOf(
            linkTo(methodOn(PatientController::class.java).createOrUpdatePatient(patient.cnp, patient)).withSelfRel().withType("PUT"),
            linkTo(methodOn(PatientController::class.java).getAllPatients()).withRel("parent")
        )
        return EntityModel.of(patient, links)
    }

    fun toModelDelete (patient: Patient) : EntityModel<Patient> {
        val links : List<Link> = listOf(
            linkTo(methodOn(PatientController::class.java).deletePatient(patient.id_user)).withSelfRel().withType("DELETE"),
            linkTo(methodOn(PatientController::class.java).getAllPatients()).withRel("parent")
        )
        return EntityModel.of(patient, links)
    }

    fun toModelJoin (entities: MutableIterable<Physician>, id: Int) : CollectionModel<EntityModel<Physician>>{
        val physicianModels = entities.map { physician ->
            PhysicianHateoas().toModel(physician)
        }

        val links: List<Link> = listOf(
            linkTo(methodOn(PatientController::class.java).getPhysicians(id)).withSelfRel().withType("GET"),
            linkTo(methodOn(PatientController::class.java).getPatient(id)).withRel("parent")
        )

        return CollectionModel.of(physicianModels, links)
    }

    fun toModelDate (entities: MutableIterable<Appointment>, id: Int, date: String, type: String?) : CollectionModel<EntityModel<Appointment>>{
        val appointmentModels = entities.map { appointment ->
            AppointmentHateoas().toGetModel(appointment)
        }

        val links: List<Link> = listOf(
            linkTo(methodOn(PatientController::class.java).getPatientAppointmentsByDate(id, date, type)).withSelfRel().withType("GET"),
            linkTo(methodOn(PatientController::class.java).getPatient(id)).withRel("parent")
        )

        return CollectionModel.of(appointmentModels, links)
    }
}
