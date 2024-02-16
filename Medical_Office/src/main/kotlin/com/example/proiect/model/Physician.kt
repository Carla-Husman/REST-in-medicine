package com.example.proiect.model

import jakarta.persistence.*
import lombok.Data

@Table(name = "doctori")
@Entity(name = "doctori")
@Data
data class Physician (
    @Id
    @Column(name = "id_doctor")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id_doctor: Int,

    //foreign key
    @Column(name = "id_user")
    var id_user: Int,

    @Column(name = "nume")
    var nume: String,

    @Column(name = "prenume")
    var prenume: String,

    //unique
    @Column(name = "email")
    var email: String,

    //check
    @Column(name = "telefon")
    var telefon: String,

    @Column(name = "specializare")
    var specializare: String,
) {
    constructor() : this(0, 0, "", "", "", "", "ORL")
}