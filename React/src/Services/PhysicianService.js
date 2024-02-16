import axios from "axios";
import AuthenticationService from "./AuthenticationService";

class PatientService {
    // Method to handle the physicians
    async getPhysicians() {
        try {
            // Get the token from local storage
            const auth = new AuthenticationService();
            let token = auth.getCurrentToken();

            // Make a GET request to the server for the physicians
            const response = await axios.get(`http://localhost:8000/physicians`,
                {
                    headers: {
                        'Content-Type': 'application/json', // Specify the content type of the request
                        Authorization: `Bearer ${token}`, // Inject the token into the Authorization header
                    },
                })

            // Parse the response
            const jsonPhysicians = JSON.parse(response.data);

            // Create an array of physicians
            let outputPhysicians = [];
            for (let i = 0; i < jsonPhysicians._embedded.physicianList.length; i++) {
                outputPhysicians.push({
                    id: jsonPhysicians._embedded.physicianList[i].id_user,
                    nume: jsonPhysicians._embedded.physicianList[i].nume,
                    prenume: jsonPhysicians._embedded.physicianList[i].prenume,
                    specializare: jsonPhysicians._embedded.physicianList[i].specializare,
                    email: jsonPhysicians._embedded.physicianList[i].email,
                    telefon: jsonPhysicians._embedded.physicianList[i].telefon,
                });
            }

            // Return the all physicians
            return outputPhysicians;
        } catch (error) {
            // Log an error if there's an issue
            if (error.response.status === 401 && error.response.data.detail === "Token expired") {
                return -1;
            }
            return null;
        }
    }

    // Method to handle the physicians with pagination
    async getPhysiciansPage(page, itemsPerPage = 2) {
        try {
            // Get the token from local storage
            const auth = new AuthenticationService();
            let token = auth.getCurrentToken();

            // Make a GET request to the server for the physicians with pagination parameters
            const response = await axios.get(`http://localhost:8000/physicians/paginate/?page=${page}&size=${itemsPerPage}`, {
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`,
                },
            });

            // Parse the response
            const jsonPhysicians = response.data;

            // Create an array of physicians
            let outputPhysicians = jsonPhysicians.physicians.map(physician => ({
                id: physician.id_user,
                nume: physician.nume,
                prenume: physician.prenume,
                specializare: physician.specializare,
                email: physician.email,
                telefon: physician.telefon,
            }));

            // Return the paginated physicians along with pagination information
            return {
                physicians: outputPhysicians,
                totalPages: jsonPhysicians.totalPages,
                currentPage: jsonPhysicians.currentPage,
            };
        } catch (error) {
            if (error.response.status === 401 && error.response.data.detail === "Token expired") {
                return -1;
            }
            return null;
        }
    }

    // Method to handle the physician informations
    async getPhycianInformation(id) {
        try {
            // Get the token from local storage
            const auth = new AuthenticationService();
            let token = auth.getCurrentToken();

            // Make a GET request to the server for the physician informations
            const response = await axios.get(`http://localhost:8000/physicians/${id}`,
                {
                    headers: {
                        'Content-Type': 'application/json', // Specify the content type of the request
                        Authorization: `Bearer ${token}`, // Inject the token into the Authorization header
                    },
                })

            // Return the phycian informations
            return {
                nume: response.data.nume,
                prenume: response.data.prenume,
                specializare: response.data.specializare,
                email: response.data.email,
                telefon: response.data.telefon,
            };
        } catch (error) {
            // Log an error if there's an issue with the login request
            if (error.response.status === 401 && error.response.data.detail === "Token expired") {
                return -1;
            }

            //console.error("Error getting the info:", error);
            return null;
        }
    }

    // Method to handle the patients
    async getPatients(id) {
        try {
            // Get the token from local storage
            const auth = new AuthenticationService();
            let token = auth.getCurrentToken();

            // Make a GET request to the server for the patients for a physician
            const response = await axios.get(`http://localhost:8000/physicians/${id}/patients`,
                {
                    headers: {
                        'Content-Type': 'application/json', // Specify the content type of the request
                        Authorization: `Bearer ${token}`, // Inject the token into the Authorization header
                    },
                });


            // If there are no patients return an empty array
            if (response.data === "0") {
                return [];
            }

            for (let i = 0; i < response.data.length; ++i) {
                response.data[i].id = i;
            }

            // Return the list of patients
            return response.data;
        } catch (error) {
            // Log an error if there's an issue
            if (error.response.status === 401 && error.response.data.detail === "Token expired") {
                return -1;
            }

            //console.error("Error getting the info:", error);
            return null;
        }
    }

    // Method to handle the appointments
    async getAppointments(id) {
        try {
            // Get the token from local storage
            const auth = new AuthenticationService();
            let token = auth.getCurrentToken();

            // Make a GET request to the server for the physician appointments
            const response = await axios.get(`http://localhost:8000/appointments/physician/${id}`,
                {
                    headers: {
                        'Content-Type': 'application/json', // Specify the content type of the request
                        Authorization: `Bearer ${token}`, // Inject the token into the Authorization header
                    },
                });

            // If there are no appointments return an empty array
            if (response.data === "0") {
                return [];
            }

            // Obtain the patients for the physician
            const patients = await this.getPatients(id);

            // Create an array of appointments
            let outputAppointments = [];
            for (let i = 0; i < response.data.length; i++) {
                const patient = patients.find(patient => patient.id_user === response.data[i].id_pacient);
                let s = "";
                if (response.data[i].status != null) {
                    s = response.data[i].status
                }

                outputAppointments.push({
                    id: i + 1,
                    id_user: response.data[i].id_pacient,
                    lastName: patient.nume,
                    firstName: patient.prenume,
                    email: patient.email,
                    phoneNumber: patient.telefon,
                    date: response.data[i].data,
                    status: s,
                });
            }

            // Return the all appointments
            return outputAppointments;
        } catch (error) {
            if (error.response.status === 401 && error.response.data.detail === "Token expired") {
                return -1;
            }

            //console.error("Error getting the appointment info:", error);
            return null;
        }
    }

    async updatePersonalInformations(physician) {
        try {
            // Get the token from local storage
            const auth = new AuthenticationService();
            let token = auth.getCurrentToken();

            // Make a PUT request to the server for the physician informations
            const response = await axios.put(`http://localhost:8000/physicians`,
                {
                    nume: physician.nume,
                    prenume: physician.prenume,
                    email: physician.email,
                    telefon: physician.telefon,
                    specializare: physician.specializare,
                },
                {
                    headers: {
                        'Content-Type': 'application/json', // Specify the content type of the request
                        Authorization: `Bearer ${token}`, // Inject the token into the Authorization header
                    },
                });


            // Return the response
            return response.data;
        } catch (error) {
            console.log(error);
            if (error.response.status === 401 && error.response.data.detail === "Token expired") {
                return -1;
            }

            return error.response.data.detail;
        }
    }
}

export default PatientService;