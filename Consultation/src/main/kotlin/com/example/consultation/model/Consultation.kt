package com.example.consultation.model

import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "consultatii")
data class Consultation (
    @Id
    @Field("_id")
    val id: ObjectId = ObjectId(),

    @Field("id_pacient")
    val id_pacient: Int = 0,

    @Field("id_doctor")
    val id_doctor: Int = 0,

    @Field("data")
    var data: String = "",

    @Field("diagnostic")
    val diagnostic: String = "",

    @Field("investigatii")
    val investigatii: List<Investigations> = listOf(),
)

