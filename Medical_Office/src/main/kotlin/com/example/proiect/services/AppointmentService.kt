package com.example.proiect.services


import com.example.proiect.hateoas.AppointmentHateoas
import com.example.proiect.hateoas.PatientHateoas
import com.example.proiect.model.Appointment
import com.example.proiect.repository.AppointmentRepository
import com.example.proiect.services.interfaces.IAppointmentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Service
class AppointmentService (
    private val appointmentRepository: AppointmentRepository
) : IAppointmentService{

    override fun createAppointment(appointment: Appointment): ResponseEntity<out Any> {
        try{
            if (!appointment.data.matches(Regex("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\$")))
                return ResponseEntity("Invalid date format", HttpStatus.UNPROCESSABLE_ENTITY)

            val appointments = appointmentRepository.findAll().toMutableList()
            val now = LocalDateTime.now()
            val newDate = LocalDateTime.parse(appointment.data, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

            if(appointments.any { it.id_doctor == appointment.id_doctor &&
                        it.id_pacient == appointment.id_pacient &&
                        it.data.split(' ')[0] == appointment.data.split(' ')[0] &&
                        it.data.split(' ')[1].substring(0, 5) == appointment.data.split(' ')[1].substring(0, 5)}
            ){
                return ResponseEntity("Appointment already exists", HttpStatus.CONFLICT)
            }

            if (newDate.isBefore(now)) {
                return ResponseEntity("Appointments must be for future dates", HttpStatus.UNPROCESSABLE_ENTITY)
            }

            if (!now.plusMinutes(15).isBefore(newDate)) {
                return ResponseEntity("Appointments must be made at least 15 minutes later than the current time", HttpStatus.UNPROCESSABLE_ENTITY)
            }

            if (newDate.minute % 15 != 0) {
                return ResponseEntity("The appointment time must be a multiple of 15 minutes", HttpStatus.UNPROCESSABLE_ENTITY)
            }

            if (newDate.hour < 8 || newDate.hour >= 17) {
                return ResponseEntity("Schedule: 08:00 - 17:00", HttpStatus.UNPROCESSABLE_ENTITY)
            }

            if (newDate.hour == 12) {
                return ResponseEntity("The doctors are on their lunch break", HttpStatus.UNPROCESSABLE_ENTITY)
            }

            val appointmentsSameHourSamePatient = appointments.filter { a->
                a.id_pacient == appointment.id_pacient &&
                a.data.split(' ')[0] == appointment.data.split(' ')[0] &&
                newDate.hour == a.data.split(' ')[1].substring(0, 2).toInt() &&
                newDate.minute == a.data.split(' ')[1].substring(3, 5).toInt()
            }

            val appointmentsSameDay = appointments.filter {
                it.id_doctor == appointment.id_doctor &&
                it.id_pacient == appointment.id_pacient &&
                LocalDateTime.parse(it.data, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate() == newDate.toLocalDate()
            }

            if (appointmentsSameDay.size == 1 || appointmentsSameHourSamePatient.size == 1) {
                return ResponseEntity("A patient cannot have more than one appointment on the same day with the same doctor", HttpStatus.CONFLICT)
            }

            val appointmentsSameHour = appointments.filter {
                it.id_doctor == appointment.id_doctor &&
                        LocalDateTime.parse(it.data, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate() == newDate.toLocalDate() &&
                        LocalDateTime.parse(it.data, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).hour == newDate.hour &&
                        LocalDateTime.parse(it.data, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).minute == newDate.minute
            }

            if (appointmentsSameHour.size == 1) {
                return ResponseEntity("There is another appointment at the same time for that doctor", HttpStatus.CONFLICT)
            }

            return ResponseEntity(appointmentRepository.save(appointment).let { AppointmentHateoas().toModel(it) }, HttpStatus.CREATED)
        }catch (e: Exception){
            println(e.message)
            return ResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    override fun getAllAppointments(): ResponseEntity<out Any> {
        return try {
            val appointments = appointmentRepository.findAll()
            ResponseEntity(AppointmentHateoas().toCollectionModel(appointments), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    override fun updateStatus(appointment: Appointment): ResponseEntity<out Any>{
        return try{
            val newAppointment =
                appointmentRepository.getAppointment(appointment.id_pacient, appointment.id_doctor, appointment.data)
                    ?: return ResponseEntity("Appointment not found", HttpStatus.NOT_FOUND)

            newAppointment.status = appointment.status
            ResponseEntity(appointmentRepository.save(newAppointment).let { AppointmentHateoas().toPutModel(it) }, HttpStatus.OK)
        }catch (e: Exception){
            ResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    override fun getAppointment(id_doctor: Int, id_pacient: Int, data: String): ResponseEntity<out Any> {
        try{
            val appointment = appointmentRepository.getAppointment(id_pacient, id_doctor, data)
                ?: return ResponseEntity("Appointment not found", HttpStatus.NOT_FOUND)

            return ResponseEntity(appointment.let { AppointmentHateoas().toGetModel(it) }, HttpStatus.OK)
        }catch (e: Exception){
            return ResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    override fun getAllAppointmentsForPatient(id: Int): ResponseEntity<out Any> {
        try{
            val appointments = appointmentRepository.getAllAppointmentsForPatient(id)
                ?: return ResponseEntity("Patient not found", HttpStatus.NOT_FOUND)

            return ResponseEntity(AppointmentHateoas().toCollectionModel(appointments), HttpStatus.OK)
        }catch (e: Exception){
            return ResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    override fun getAllAppointmentsForPhysician(id: Int): ResponseEntity<out Any> {
        try{
            val appointments = appointmentRepository.getAllAppointmentsForPhysician(id)
                ?: return ResponseEntity("Physician not found", HttpStatus.NOT_FOUND)

            return ResponseEntity(AppointmentHateoas().toCollectionModel(appointments), HttpStatus.OK)
        }catch (e: Exception){
            return ResponseEntity("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}