import React, { useState, useEffect } from "react";
import { useParams, useLocation, useNavigate } from "react-router-dom";
import PhysicianService from "../Services/PhysicianService";
import PatientService from "../Services/PatientService";

const UpdateInfoPatientPhysicianPage = () => {
    // Extracting the type of the user from the URL
    const { type } = useParams();

    // For obtaining the patient/physician object from the previous page
    const location = useLocation()

    // Hook from react-router-dom to navigate between pages
    const navigate = useNavigate();

    // State variables and functions to update them
    const [error, setError] = useState('');
    const [physician, setPhysician] = useState(null);
    const [patient, setPatient] = useState(null);
    const [isAuthorize, setisAuthorize] = useState(-2);

    // Update the physician/patient state variable when the location state changes
    useEffect(() => {
        if (location.state && type === "physician") {
            setPhysician(location.state.physician);
        }
        else if (location.state && type === "patient") {
            setPatient(location.state.patient);
        }
    }, [location.state, type]);

    // Based on the type of the user, display the corresponding page
    if (type === "patient" && location.state && patient !== null && patient !== undefined) {
        const handleSubmit = async (event) => {
            event.preventDefault();

            const patientService = new PatientService();
            const response = await patientService.updatePersonalInformations(patient);

            if (response === -1) {
                setisAuthorize(-1);
            } else {
                setError(response);
            }
        }

        if (isAuthorize === -1) {
            localStorage.removeItem("token");
            return (
                <div>
                    <h1>Your session has most likely timed out. Please log in again.</h1>
                    <button onClick={() => navigate(`/login`)}>Back To Login</button>
                </div>
            );
        }
        else {
            return (
                <div>
                    <h1>Update Info Patient</h1>
                    <form onSubmit={handleSubmit}>
                        <label>First Name:</label>&emsp;&emsp;&emsp;
                        <input
                            type="text"
                            name="prenume"
                            value={patient.prenume}
                            onChange={(e) => setPatient({ ...patient, prenume: e.target.value })}
                        /> <br />

                        <label>Last Name:</label>&emsp;&emsp;&emsp;
                        <input
                            type="text"
                            name="nume"
                            value={patient.nume}
                            onChange={(e) => setPatient({ ...patient, nume: e.target.value })}
                        /> <br />

                        <label>CNP:</label>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
                        <input
                            type="text"
                            name="cnp"
                            value={patient.cnp}
                            readOnly
                        /> <br />

                        <label>Birth Date:</label>&emsp;&emsp;&emsp;
                        <input
                            type="text"
                            name="data_nastere"
                            value={patient.data_nastere}
                            readOnly
                        /> <br />

                        <label>Phone number:</label>&emsp;
                        <input
                            type="text"
                            name="telefon"
                            value={patient.telefon}
                            onChange={(e) => setPatient({ ...patient, telefon: e.target.value })}
                        /> <br />

                        <label>Email: </label> &emsp;&emsp;&emsp;&emsp;&emsp;
                        <input
                            type="text"
                            name="email"
                            value={patient.email}
                            onChange={(e) => setPatient({ ...patient, email: e.target.value })}
                        /> <br />
                        <br />
                        <button type="submit">Change information</button>
                    </form>
                    <br />
                    <p>{error}</p>
                </div>
            );
        }
    }
    else if (type === "physician" && location.state && physician !== null && physician !== undefined) {
        const handleSubmit = async (event) => {
            event.preventDefault();

            const physicianService = new PhysicianService();
            const response = await physicianService.updatePersonalInformations(physician);

            if (response === -1) {
                setisAuthorize(-1);
            } else {
                setError(response);
            }
        }

        if (isAuthorize === -1) {
            localStorage.removeItem("token");
            return (
                <div>
                    <h1>Your session has most likely timed out. Please log in again.</h1>
                    <button onClick={() => navigate(`/login`)}>Back To Login</button>
                </div>
            );
        }
        else {
            return (
                <div>
                    <h1>Update Info Physician</h1>
                    <form onSubmit={handleSubmit}>
                        <label>First Name:</label>&emsp;&emsp;&emsp;&emsp;&emsp;
                        <input
                            type="text"
                            name="prenume"
                            value={physician.prenume}
                            onChange={(e) => setPhysician({ ...physician, prenume: e.target.value })}
                        /> <br />

                        <label>Last Name:</label>&emsp;&emsp;&emsp;&emsp;&emsp;
                        <input
                            type="text"
                            name="nume"
                            value={physician.nume}
                            onChange={(e) => setPhysician({ ...physician, nume: e.target.value })}
                        /> <br />

                        <label style={{ marginRight: '10px' }}>Specialization:</label>&emsp;&emsp;&emsp;
                        <input
                            type="text"
                            name="specializare"
                            value={physician.specializare}
                            readOnly
                        /> <br />

                        <label>Office Phone Number:</label>
                        <input
                            type="text"
                            name="telefon"
                            value={physician.telefon}
                            onChange={(e) => setPhysician({ ...physician, telefon: e.target.value })}
                        /> <br />

                        <label style={{ marginRight: '5px' }}>Office Email:</label>&emsp;&emsp;&emsp;&emsp;
                        <input
                            type="text"
                            name="email"
                            value={physician.email}
                            onChange={(e) => setPhysician({ ...physician, email: e.target.value })}
                        /> <br /> <br />

                        <button type="submit">Change information</button>
                    </form>
                    <br />
                    <p>{error}</p>
                </div>
            );
        }
    }
    else {
        return (
            <div>
                <h1>404 This is not the web page you are looking for</h1>
            </div>
        );
    }
}

export default UpdateInfoPatientPhysicianPage;