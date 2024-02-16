package com.example.consultation

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories("com.example.consultation.*")
@EntityScan("com.example.consultation.*")
@OpenAPIDefinition(info = Info(title = "Medical Office", version = "1.0", description = "The application simplifies the " +
        "interaction of patients with a medical center and provides doctors with the opportunity to monitor over time " +
        "the evolution of the health status of each consulted patient.")
)

class ConsultationApplication

fun main(args: Array<String>) {
    runApplication<ConsultationApplication>(*args)
}
