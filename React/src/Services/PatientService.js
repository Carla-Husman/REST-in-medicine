import axios from "axios";
import AuthenticationService from "./AuthenticationService";
import PhysicianService from "./PhysicianService";

class PatientService {
  // Method to handle the patient information
  async getUserInformation(id) {
    try {
      // Get the token from local storage
      const auth = new AuthenticationService();
      let token = auth.getCurrentToken();

      // Make a GET request to the server for the patient information
      const response = await axios.get(`http://localhost:8000/patients/${id}`,
        {
          headers: {
            'Content-Type': 'application/json', // Specify the content type of the request
            Authorization: `Bearer ${token}`, // Inject the token into the Authorization header
          },
        })

      // Parse the response
      const jsonPatient = JSON.parse(response.data);

      // Return the user information
      return {
        cnp: jsonPatient.cnp,
        nume: jsonPatient.nume,
        prenume: jsonPatient.prenume,
        email: jsonPatient.email,
        telefon: jsonPatient.telefon,
        data_nastere: jsonPatient.data_nasterii,
      };
    } catch (error) {
      if (error.response.status === 401 && error.response.data.detail === "Token expired") {
        return -1;
      }

      return null;
    }
  }

  // Method to handle the patient appointments
  async getAppointments(id) {
    try {
      // Get the token from local storage
      const auth = new AuthenticationService();
      const physicianService = new PhysicianService();
      let token = auth.getCurrentToken();

      // Make a GET request to the server for the patient appointments
      const response = await axios.get(`http://localhost:8000/appointments/patient/${id}`,
        {
          headers: {
            'Content-Type': 'application/json', // Specify the content type of the request
            Authorization: `Bearer ${token}`, // Inject the token into the Authorization header
          },
        })

      // Parse the response
      const jsonAppointments = JSON.parse(response.data);

      // Try to get the length of the appointments
      // If there are no appointments, return an empty array
      let length = 0;
      try {
        length = jsonAppointments._embedded.appointmentList.length;
      } catch (error) {
        return [];
      }

      // Get the physicians
      const physicians = await physicianService.getPhysicians();

      // Create an array of appointments
      let outputAppointments = [];
      for (let i = 0; i < length; i++) {
        let status = jsonAppointments._embedded.appointmentList[i].status;
        if (status == null) {
          status = undefined;
        }

        let physician = physicians.find((physician) => physician.id === jsonAppointments._embedded.appointmentList[i].id_doctor);

        outputAppointments.push({
          id: i,
          id_doctor: jsonAppointments._embedded.appointmentList[i].id_doctor,
          status: status,
          data: jsonAppointments._embedded.appointmentList[i].data,
          nume: physician.nume,
          prenume: physician.prenume,
          specializare: physician.specializare
        });
      }

      // Return the appointments
      return outputAppointments;
    } catch (error) {
      // Log an error if there's an issue 
      // console.error("Error getting the appointments:", error);
      if (error.response.status === 401 && error.response.data.detail === "Token expired") {
        return -1;
      }
      return null;
    }
  }

  // Method to handle the status change
  async updateAppointmentStatus(appointment) {
    // Check if the appointment status is valid
    if (appointment.status === undefined || appointment.status === null || appointment.status === "") {
      return false;
    }

    try {
      // Get the token from local storage
      const auth = new AuthenticationService();
      let token = auth.getCurrentToken();

      let data, id;

      // This method will be called by a physician or a patient
      // If the appointment is called by a physician, the id will be the id of the patient and vice versa
      // The data will be the date of the appointment, but was declared with different names
      if (appointment.data === undefined) {
        data = appointment.date;
      } else if (appointment.date === undefined) {
        data = appointment.data;
      }

      if (appointment.id_doctor === undefined) {
        id = appointment.id_user;
      } else if (appointment.id_user === undefined) {
        id = appointment.id_doctor;
      }

      // Make a PUT request to the server to update the appointment status
      await axios.put(`http://localhost:8000/appointments`, {
        appointment: {
          id_doctor: id,
          data: data,
          status: appointment.status,
        },
      },
        {
          headers: {
            'Content-Type': 'application/json', // Specify the content type of the request
            Authorization: `Bearer ${token}`, // Inject the token into the Authorization header
          },
        })

      return true;
    } catch (error) {
      // Log an error if there's an issue with the update request
      //console.error("Error updating the appointments:", error);
      if (error.response.status === 401 && error.response.data.detail === "Token expired") {
        return -1;
      }
      return false;
    }
  }

  // Method to handle the appointment creation
  async makeAppointment(id_doctor, data) {
    try {
      // Get the token from local storage
      const auth = new AuthenticationService();
      let token = auth.getCurrentToken();

      // Make a POST request to the server for a new appointment
      await axios.post(`http://localhost:8000/appointments`, {
        appointment: {
          id_doctor: id_doctor,
          data: data,
        },
      },
        {
          headers: {
            'Content-Type': 'application/json', // Specify the content type of the request
            Authorization: `Bearer ${token}`, // Inject the token into the Authorization header
          },
        })

      return "Successful!";
    } catch (error) {
      // Log an error if there's an issue with the appointment creation request
      if (error.response.status === 401 && error.response.data.detail === "Token expired") {
        return -1;
      } else if (error.response.status === 409 || error.response.status === 422 || error.response.status === 401) {
        return error.response.data.detail;
      }

      return "";
    }
  }

  // Method to handle the deactivation of an account
  async deactivateAccount() {
    try {
      // Get the token from local storage
      const auth = new AuthenticationService();
      let token = auth.getCurrentToken();

      // Make a DELETE request to the server
      await axios.delete(`http://localhost:8000/patients`,
        {
          headers: {
            'Content-Type': 'application/json', // Specify the content type of the request
            Authorization: `Bearer ${token}`, // Inject the token into the Authorization header
          },
        })

      return true;
    } catch (error) {
      if (error.response.status === 401 && error.response.data.detail === "Token expired") {
        return -1;
      }

      //console.error("Error desactivating the account:", error);
      return false;
    }
  }

  // Method to handle the update of the patient information
  async updateInfo(undatedInfo) {
    try {
      // Get the token from local storage
      const auth = new AuthenticationService();
      let token = auth.getCurrentToken();

      // Make a PUT request to the server to update the patient information
      const response = await axios.put(`http://localhost:8000/update`, {
        username: undatedInfo.username,
        password: undatedInfo.password,
        oldPassword: undatedInfo.oldPassword,
      },
        {
          headers: {
            'Content-Type': 'application/json', // Specify the content type of the request
            Authorization: `Bearer ${token}`, // Inject the token into the Authorization header
          },
        })

      return response.data;
    } catch (error) {
      // Log an error if there's an issue with the update request
      if (error.response.status === 401 && error.response.data.detail === "Token expired") {
        return error.response.data.detail;
      }

      return "";
    }
  }

  // Method to handle the update of the patient information
  async updatePersonalInformations(patient) {
    try {
      // Get the token from local storage
      const auth = new AuthenticationService();
      let token = auth.getCurrentToken();
      console.log(patient);
      // Make a PUT request to the server to update the patient information
      const response = await axios.put(`http://localhost:8000/patients`, {
        cnp: patient.cnp,
        nume: patient.nume,
        prenume: patient.prenume,
        email: patient.email,
        telefon: patient.telefon,
        data_nasterii: patient.data_nastere,
      },
        {
          headers: {
            'Content-Type': 'application/json', // Specify the content type of the request
            Authorization: `Bearer ${token}`, // Inject the token into the Authorization header
          },
        })

      return response.data;
    } catch (error) {
      console.log(error);
      // Log an error if there's an issue with the update request
      if (error.response.status === 401 && error.response.data.detail === "Token expired") {
        return -1;
      }

      return error.response.data.detail;
    }
  }
}

export default PatientService;