package com.example.proiect.model

import jakarta.persistence.*
import lombok.*

@Table(name = "pacienti")
@Entity(name = "pacienti")
@Data
data class Patient(
    @Id
    @Column(name = "cnp")
    val cnp: String,

    //foreign key
    @Column(name = "id_user")
    var id_user: Int,

    @Column(name = "nume")
    val nume: String,

    @Column(name = "prenume")
    val prenume: String,

    //unique
    @Column(name = "email")
    val email: String,

    //check
    @Column(name = "telefon")
    val telefon: String,

    @Column(name = "data_nasterii")
    var data_nasterii: String,

    @Column(name = "is_active")
    var is_active: Boolean
) {
    constructor() : this("", 0, "", "", "", "", "", false)
}
