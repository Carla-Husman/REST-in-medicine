# REST-in-medicine
This is a software application designed to streamline patient interaction with a medical center and provide doctors with the ability to monitor the health status of each consulted patient over time. The medical institution operates with medical staff specializing in various fields.

The project will implement Representational State Transfer (REST) principles, with a focus on HATEOAS (Hypermedia as the Engine of Application State) representations for resources. This will include, at a minimum, navigational links between containers and individual subordinate instances, as well as self-links.

## Technologies 
- Kotlin
- Python
- React
- Docker (only for running the database server)
- MariaDB
- MongoDB

## Informations
In Kotlin, I've developed REST controllers for the Medical Office component, handling endpoints for appointments, doctors, and patients, along with consultations, and orchestrating their interactions. Additionally, Kotlin was utilized to create the server for GRPC, responsible for token JWT generation, verification, user table updates (including username, password, and role), as well as user registration. The logic for login and logout functionalities is also implemented within this server.

Python was employed to craft the Gateway, which acts as a mediator between the React server and my API. Using FastAPI, the Gateway serves as both the client for GRPC and the API server. CORS (Cross-Origin Resource Sharing) is implemented to allow access, enabling client-side web applications loaded from one domain to interact seamlessly with resources hosted on different domains. The Gateway conducts all necessary verifications, and based on the received request from React, redirect it to the appropriate API endpoint.

## Features

- **JWT Authentication and Authorization** - User authentication is implemented using JSON Web Tokens (JWT), ensuring secure access for clients and doctors. Access control is enforced to restrict unauthorized access to patient or doctor data. Users will be automatically logged out when their JWT expires and prompted to log in again for continued access.
- **Access Control and Error Handling** - Unauthorized access attempts to restricted pages or endpoints prompt users with appropriate error messages. HTTP error codes are thrown to communicate errors effectively, ensuring a good user experience
- **User Registration and Profile Management** - Only patients can register within the system, providing essential personal information and validating fields such as email, phone number, name, and password. Users can update their profiles as needed, ensuring accurate and up-to-date information
- **Appointment Making** - Patients can schedule appointments, adhering to various constraints such as not being able to schedule on the same day with the same doctor or at the same time with different doctors. Appointments must be in multiples of 15 minutes and scheduled at least 15 minutes in advance. Additionally, appointments cannot be made in the past, outside of working hours (08:00-17:00), or during lunch break (12:00-13:00). Doctors cannot have overlapping appointments
- **Viewing Personal Information** - Both patients and doctors can view their respective information within the system, ensuring transparency and facilitating personalized care
- **Consultation Management** - Doctors can add or modify consultations, including diagnosis and investigations
- **Appointment and Consultation Visualization** - Patients can view their scheduled appointments and consultations if added by the doctor
- **Account Deactivation and Data Retention** - Patients have the option to deactivate their accounts, maintaining their data within the system for reference purposes. Account deactivation toggles a field in the patient database to indicate the account's active status
- **Appointment Modification** - Patients can cancel appointments, ensuring flexibility. Doctors have the capability to modify appointments, marking them as "not attended" (neprezentat) or "attended" (onorata) based on the patient's attendance status. 
- **Doctor Information Access** - Patients can view paginated information about doctors, including their names, surnames, and specialties.

## Component Diagram
![Captură de ecran 2024-02-16 210915](https://github.com/Carla-Husman/REST-in-medicine/assets/125916556/915c16f3-99bf-4286-907b-4cd16e744920)

