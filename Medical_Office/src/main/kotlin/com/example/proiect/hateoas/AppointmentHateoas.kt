package com.example.proiect.hateoas

import com.example.proiect.controller.AppointmentController
import com.example.proiect.model.Appointment
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component

@Component
class AppointmentHateoas : RepresentationModelAssembler<Appointment, EntityModel<Appointment>> {
    override fun toModel(entity: Appointment): EntityModel<Appointment> {
        val links : List<Link> = listOf(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(AppointmentController::class.java).createAppointment(entity)
            ).withSelfRel().withType("POST"),
            //WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AppointmentController::class.java).getAllPatients()).withRel("parent")
        )

        return EntityModel.of(entity, links)
    }

    fun toPutModel(entity: Appointment): EntityModel<Appointment> {
        val links : List<Link> = listOf(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(AppointmentController::class.java).createAppointment(entity)
            ).withSelfRel().withType("PUT"),
            //WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AppointmentController::class.java).getAllPatients()).withRel("parent")
        )

        return EntityModel.of(entity, links)
    }

    fun toGetModel (entity: Appointment) : EntityModel<Appointment> {
        val links : List<Link> = listOf(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(AppointmentController::class.java).getAppointment(entity.id_doctor, entity.id_pacient, entity.data)
            ).withSelfRel().withType("GET"),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AppointmentController::class.java).getAllAppointments()).withRel("parent")
        )

        return EntityModel.of(entity, links)
    }

    override fun toCollectionModel(entities: MutableIterable<Appointment>): CollectionModel<EntityModel<Appointment>> {
        val appointmentsModels = entities.map { appointment ->
            toGetModel(appointment)
        }

        val links: List<Link> = listOf(
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AppointmentController::class.java).getAllAppointments()).withSelfRel().withType("GET"),
        )

        return CollectionModel.of(appointmentsModels, links)
    }
}