package com.example.consultation.model

import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document
data class Investigations(
    @Id
    @Field("_id")
    val id: ObjectId = ObjectId(),

    @Field("denumire")
    var denumire: String = "",

    @Field("durata_de_procesare")
    var durata_de_procesare: Int = 0,

    @Field("rezultat")
    var rezultat: String = ""
)
