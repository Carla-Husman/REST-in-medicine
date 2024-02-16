import axios from "axios";
import AuthenticationService from "./AuthenticationService";

class ConsultationService {
    // Method to handle the consultation details
    async getConsultationDetails(id_doctor, data) {
        try {
            // Get the token from local storage
            const auth = new AuthenticationService();
            let token = auth.getCurrentToken();

            // Make a GET request to the server for the consultation details
            const response = await axios.get(`http://localhost:8000/consultation/`,
                {
                    params: {
                        id_doctor: id_doctor,
                        data: data,
                    },
                    headers: {
                        'Content-Type': 'application/json', // Specify the content type of the request
                        Authorization: `Bearer ${token}`, // Inject the token into the Authorization header,
                    },
                })

            // Parse the response
            const jsonConsultations = JSON.parse(response.data);
            const outputInvestigations = [];

            // Add the id_react field to the investigations 
            // for mapping purposes in the frontend
            for (let i = 0; i < jsonConsultations.investigatii.length; i++) {
                outputInvestigations.push({
                    id_react: i,
                    id: jsonConsultations.investigatii[i].id,
                    denumire: jsonConsultations.investigatii[i].denumire,
                    durata_de_procesare: jsonConsultations.investigatii[i].durata_de_procesare,
                    rezultat: jsonConsultations.investigatii[i].rezultat,
                });
            }

            jsonConsultations.investigatii = outputInvestigations;

            // Return the consultation details
            return jsonConsultations;
        } catch (error) {
            try {
                if (error.response.status === 401 && error.response.data.detail === "Token expired") {
                    // If the token has expired
                    return -1;
                }
                else if (error.response.status === 404) {
                    // If the consultation details were not found
                    return -2;
                }
            } catch (e) {
                return -3;
            }
        }
    }

    // Method to handle the consultation addition
    async addConsultation(id_pacient, data, diagnostic, investigatii) {
        try {
            if (data === "" || data === undefined || data === null) {
                return -3;
            }

            // Get the token from local storage
            const auth = new AuthenticationService();
            let token = auth.getCurrentToken();

            // Make a POST request to the server for the consultation addition
            await axios.post(`http://localhost:8000/consultation/`,
                {
                    id_pacient: id_pacient,
                    data: data,
                    diagnostic: diagnostic,
                    investigatii: investigatii,
                },
                {
                    headers: {
                        'Content-Type': 'application/json', // Specify the content type of the request
                        Authorization: `Bearer ${token}`, // Inject the token into the Authorization header,
                    },
                })

            // Return an empty string if the request is successful
            return ""
        } catch (error) {
            try {
                // Return the error message if the request is unsuccessful
                if (error.response.status === 401 && error.response.data.detail === "Token expired") {
                    // If the token has expired
                    return -1;
                } else if (error.response.status === 422 || error.response.status === 409) {
                    // If the request has failed because of invalid data
                    return error.response.data.detail;
                }
            } catch (e) {
                return -3;
            }
        }
    }

    // Method to handle the consultation update
    async updateConsultation(consultation) {
        try {
            if (consultation.data === "" || consultation.data === undefined || consultation.data === null) {
                return -3;
            }
            // Get the token from local storage
            const auth = new AuthenticationService();
            let token = auth.getCurrentToken();

            // Make a PUT request to the server for the consultation update
            await axios.put(`http://localhost:8000/consultation/`,
                {
                    id_pacient: consultation.id_pacient,
                    id_doctor: consultation.id_doctor,
                    id_consultatie: consultation.id_consultatie,
                    data: consultation.data,
                    diagnostic: consultation.diagnostic,
                    investigatii: consultation.investigatii,
                },
                {
                    headers: {
                        'Content-Type': 'application/json', // Specify the content type of the request
                        Authorization: `Bearer ${token}`, // Inject the token into the Authorization header,
                    },
                })

            return "Successfully updated the consultation details"
        } catch (error) {
            try {
                // Return the error message if the request is unsuccessful
                if (error.response.status === 401 && error.response.data.detail === "Token expired") {
                    // If the token has expired
                    return -1;
                } else if (error.response.status === 422 || error.response.status === 409 || error.response.status === 404) {
                    // If the request has failed because of invalid data
                    return error.response.data.detail;
                }
            } catch (e) {
                return -3;
            }
        }
    }
}

export default ConsultationService;