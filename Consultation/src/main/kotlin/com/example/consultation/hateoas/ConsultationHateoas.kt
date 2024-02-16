package com.example.consultation.hateoas

import com.example.consultation.controller.ConsultationController
import com.example.consultation.model.Consultation
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Component

@Component
class ConsultationHateoas : RepresentationModelAssembler<Consultation, EntityModel<Consultation>> {
    override fun toModel(entity: Consultation): EntityModel<Consultation> {
        val links : List<Link> = listOf(
            linkTo(
                WebMvcLinkBuilder.methodOn(ConsultationController::class.java).createConsultation(entity)
            ).withSelfRel().withType("POST"),
            //WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ConsultationController::class.java).getAllConsultations()).withRel("parent")
        )

        return EntityModel.of(entity, links)
    }

    fun toPutModel(entity: Consultation): EntityModel<Consultation> {
        val links : List<Link> = listOf(
            linkTo(
                WebMvcLinkBuilder.methodOn(ConsultationController::class.java).updateConsultation(entity)
            ).withSelfRel().withType("PUT"),
            //WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ConsultationController::class.java).getAllConsultations()).withRel("parent")
        )

        return EntityModel.of(entity, links)
    }

    fun toGetModel (entity: Consultation) : EntityModel<Consultation> {
        val links : List<Link> = listOf(
            linkTo(
                WebMvcLinkBuilder.methodOn(ConsultationController::class.java).getConsultation(entity.data, entity.id_pacient, entity.id_doctor)
            ).withSelfRel().withType("GET"),
            linkTo(WebMvcLinkBuilder.methodOn(ConsultationController::class.java).getAllConsultations()).withRel("parent")
        )

        return EntityModel.of(entity, links)
    }

    override fun toCollectionModel(entities: MutableIterable<Consultation>): CollectionModel<EntityModel<Consultation>> {
        val consultationsModels = entities.map { consultation ->
            toGetModel(consultation)
        }

        val links: List<Link> = listOf(
            linkTo(WebMvcLinkBuilder.methodOn(ConsultationController::class.java).getAllConsultations()).withSelfRel(),
        )

        return CollectionModel.of(consultationsModels, links)
    }
}