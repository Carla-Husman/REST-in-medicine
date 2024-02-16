package com.example.consultation.repository

import com.example.consultation.model.Consultation
import com.example.consultation.model.Investigations
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.Update
import org.springframework.stereotype.Repository

@Repository
interface ConsultationRepository : MongoRepository<Consultation, String>{
    override fun findAll(): List<Consultation>

    @Query(" { 'data' : ?0, id_doctor : ?1, 'id_pacient' : ?2 } ")
    fun getConsultationByDataAndIdDoctorAndIdPacient(data: String, id_doctor: Int, id_pacient: Int): Consultation?

    @Query("{'_id' : ?0}")
    @Update("{'\$set': {'investigatii': ?1}}")
    fun update (id: ObjectId, investigations: List<Investigations>)
}