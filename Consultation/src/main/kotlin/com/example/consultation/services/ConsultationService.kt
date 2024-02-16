package com.example.consultation.services

import com.example.consultation.hateoas.ConsultationHateoas
import com.example.consultation.model.Consultation
import com.example.consultation.repository.ConsultationRepository
import com.example.consultation.services.interfaces.IConsultationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ConsultationService(
    private val consultationRepository: ConsultationRepository
)  : IConsultationService{

    override fun findAll(): ResponseEntity<out Any> {
        return try{
            ResponseEntity(ConsultationHateoas().toCollectionModel(consultationRepository.findAll().toMutableList()), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    override fun createConsultation(consultation: Consultation): ResponseEntity<out Any> {
        try{
            val consultations = consultationRepository.findAll().toMutableList()

            if (consultations.any { it.id_doctor == consultation.id_doctor && it.data == consultation.data && it.id_pacient == consultation.id_pacient } ||
                consultations.contains(consultation))
                    return ResponseEntity("Consultation already exists", HttpStatus.CONFLICT)

            if (verify(consultation).statusCode != HttpStatus.OK)
                return verify(consultation)

            return ResponseEntity(ConsultationHateoas().toModel(consultationRepository.save(consultation)), HttpStatus.CREATED)
        }catch(e: Exception) {
            return ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    override fun updateConsultation(consultation: Consultation): ResponseEntity<out Any> {
        try {
            val findedConsultation = consultationRepository.getConsultationByDataAndIdDoctorAndIdPacient(
                consultation.data,
                consultation.id_doctor,
                consultation.id_pacient
            ) ?: return ResponseEntity("Consultation not found", HttpStatus.NOT_FOUND)

            if (verify(consultation).statusCode != HttpStatus.OK)
                return verify(consultation)

            val investigations = consultation.investigatii.toMutableList()
            var index = 0
            for (investigation in findedConsultation.investigatii) {
                if (investigation.denumire != investigations[index].denumire)
                    investigation.denumire = investigations[index].denumire

                if (investigation.rezultat != investigations[index].rezultat)
                    investigation.rezultat = investigations[index].rezultat

                if (investigation.durata_de_procesare != investigations[index].durata_de_procesare)
                    investigation.durata_de_procesare = investigations[index].durata_de_procesare

                index += 1
            }

            consultationRepository.update(findedConsultation.id, findedConsultation.investigatii)
            return ResponseEntity(ConsultationHateoas().toPutModel(findedConsultation), HttpStatus.OK)

        }catch (e: Exception) {
            return ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    override fun getConsultationByDataAndIdDoctorAndIdPacient(data: String, id_doctor: Int, id_pacient: Int): ResponseEntity<out Any> {
        try {
            val consultation = consultationRepository.getConsultationByDataAndIdDoctorAndIdPacient(
                data,
                id_doctor,
                id_pacient
            ) ?: return ResponseEntity("Consultation not found", HttpStatus.NOT_FOUND)

            return ResponseEntity(ConsultationHateoas().toGetModel(consultation), HttpStatus.OK)
        }catch (e: Exception) {
            return ResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    private fun verify (consultation: Consultation) : ResponseEntity<out Any> {
        if (!consultation.diagnostic.matches(Regex("(sanatos|bolnav)")))
            return ResponseEntity("Invalid consultation diagnostic", HttpStatus.UNPROCESSABLE_ENTITY)

        if (!consultation.data.matches(Regex("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}(:\\d{2})?\$")))
            return ResponseEntity("Invalid consultation date", HttpStatus.UNPROCESSABLE_ENTITY)

        for (investigatie in consultation.investigatii) {
            if (!investigatie.denumire.matches(Regex("^[a-zA-Z\\s]+\$")))
                return ResponseEntity("Invalid investigation name", HttpStatus.UNPROCESSABLE_ENTITY)

            if (!investigatie.rezultat.matches(Regex("^[a-zA-Z\\s]+\$")))
                return ResponseEntity("Invalid investigation result", HttpStatus.UNPROCESSABLE_ENTITY)
        }

        return ResponseEntity("OK", HttpStatus.OK)
    }
}