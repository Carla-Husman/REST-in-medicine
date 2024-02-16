package com.example.proiect.model

import jakarta.persistence.*
import lombok.*

@Table(name = "programari")
@Entity(name = "programari")
@Data
@IdClass(AppointmentId::class)
data class Appointment(
    @Id
    @Column(name = "id_pacient")
    //foreign key
    var id_pacient: Int,

    @Id
    //foreign key
    @Column(name = "id_doctor")
    var id_doctor: Int,

    @Id
    //check
    @Column(name = "data")
    var data: String,

    @Column(name = "status")
    var status: String
) {
    constructor() : this(0, 0, "", "")
}