package com.example.proiect.hateoas

import com.example.proiect.controller.PhysicianController
import com.example.proiect.model.Patient
import com.example.proiect.model.Physician
import org.springframework.data.domain.Page
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class PhysicianHateoas : RepresentationModelAssembler<Physician, EntityModel<Physician>> {
    override fun toModel(physician: Physician): EntityModel<Physician> {
        val links : List<Link> = listOf(
            linkTo(methodOn(PhysicianController::class.java).getPhysician(physician.id_user)).withSelfRel().withType("GET"),
            linkTo(methodOn(PhysicianController::class.java).getAllPhysicians()).withRel("parent")
        )

        return EntityModel.of(physician, links)
    }

    override fun toCollectionModel(entities: MutableIterable<Physician>): CollectionModel<EntityModel<Physician>> {
        //Continutul si linkurile pentru fiecare pacient
        val physicianModels = entities.map { physician ->
            toModel(physician)
        }

        //Linkurile pentru toți pacienții, cel care se adauga la final
        val links: List<Link> = listOf(
            linkTo(methodOn(PhysicianController::class.java).getAllPhysicians()).withSelfRel().withType("GET"),
        )

        return CollectionModel.of(physicianModels, links)
    }

    fun toModelSpecializationOrName (entities: MutableIterable<Physician>, option: Int, str: String) : CollectionModel<EntityModel<Physician>>{
        val physicianModels = entities.map { physician ->
            toModel(physician)
        }

        val links : List<Link> = if(option == 1){
            listOf(
                linkTo(methodOn(PhysicianController::class.java).getPhysiciansByName(str)).withSelfRel().withType("GET"),
                linkTo(methodOn(PhysicianController::class.java).getAllPhysicians()).withRel("parent")
            )
        } else{
            listOf(
                linkTo(methodOn(PhysicianController::class.java).getPhysiciansBySpecialization(str)).withSelfRel().withType("GET"),
                linkTo(methodOn(PhysicianController::class.java).getAllPhysicians()).withRel("parent")
            )
        }

        return CollectionModel.of(physicianModels, links)
    }

    fun toModelCreate (physician: Physician) : EntityModel<Physician> {
        val links : List<Link> = listOf(
            linkTo(methodOn(PhysicianController::class.java).createPhysician(physician)).withSelfRel().withType("POST"),
        )
        return EntityModel.of(physician, links)
    }

    fun toModelJoin (entities: MutableIterable<Patient>, id: Int) : CollectionModel<EntityModel<Patient>>{
        val patientModels = entities.map { patient ->
            PatientHateoas().toModel(patient)
        }

        val links: List<Link> = listOf(
            linkTo(methodOn(PhysicianController::class.java).getPatients(id)).withSelfRel().withType("GET"),
            linkTo(methodOn(PhysicianController::class.java).getPhysician(id)).withRel("parent")
        )

        return CollectionModel.of(patientModels, links)
    }

    fun toModelPut (physician: Physician): EntityModel<Physician> {
        val links : List<Link> = listOf(
            linkTo(methodOn(PhysicianController::class.java).getPhysician(physician.id_user)).withSelfRel().withType("PUT"),
            linkTo(methodOn(PhysicianController::class.java).getAllPhysicians()).withRel("parent")
        )

        return EntityModel.of(physician, links)
    }

    fun toModelPage (page: Int, itemsPerPage: Int, physiciansPage: Page<Physician>) : RepresentationModel<*> {
        val nextPage = page + 1
        val prevPage = if (page > 1) page - 1 else 1

        val links = mutableListOf<Link>()

        links.add(linkTo(methodOn(PhysicianController::class.java).getPageable(page, itemsPerPage)).withSelfRel().withType("GET"))
        links.add(linkTo(methodOn(PhysicianController::class.java).getAllPhysicians()).withRel("parent"))

        if (physiciansPage.hasNext()) {
            links.add(linkTo(methodOn(PhysicianController::class.java).getPageable(nextPage, itemsPerPage))
                .withRel("next"))
        }
        if (physiciansPage.hasPrevious()) {
            links.add(linkTo(methodOn(PhysicianController::class.java).getPageable(prevPage, itemsPerPage))
                .withRel("previous"))
        }

        val response: MutableMap<String, Any> = HashMap()
        response["physicians"] = physiciansPage.content.let { physicianModels ->
            physicianModels.map { physician ->
                toModel(physician)
            }
        }
        response["currentPage"] = page
        response["totalPhysicians"] = physiciansPage.totalElements
        response["totalPages"] = physiciansPage.totalPages

        return CollectionModel.of(response, links)
    }
}