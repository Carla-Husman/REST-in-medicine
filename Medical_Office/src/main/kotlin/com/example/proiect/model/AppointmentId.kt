package com.example.proiect.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class AppointmentId(
    @Column(name = "id_pacient")
    var id_pacient: Int,

    @Column(name = "id_doctor")
    var id_doctor: Int,

    @Column(name = "data")
    var data: String
) : Serializable {
    constructor() : this(0, 0, "")
}