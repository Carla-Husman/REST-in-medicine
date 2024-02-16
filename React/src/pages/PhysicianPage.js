import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import PhysicianService from '../Services/PhysicianService';
import AuthenticationService from '../Services/AuthenticationService';
import PatientService from '../Services/PatientService';

const PhysicianPage = () => {
    // Extracting physicianId from the URL parameters
    const { id } = useParams();

    // Extracting the type of the user from the URL
    let type = "";
    if (window.location.href.includes("physician")) {
        type = "physician";
    } else if (window.location.href.includes("client")) {
        type = "patient";
    }

    // State variables and functions to update them
    // isAuthorize: -2 - loading, -1 - token expired, 0 - unauthorized, 1 - authorized
    const [isAuthorize, setisAuthorize] = useState(-2);
    const [physician, setPhysician] = useState(null);
    const [appointments, setAppointments] = useState(null);
    const [messagePatients, setMessagePatients] = useState("");
    const [selectedStatus, setSelectedStatus] = useState({});

    // Hook from react-router-dom to navigate between pages
    const navigate = useNavigate();

    // Function to handle the logout process
    const handleLogout = async () => {
        const auth = new AuthenticationService();
        try {
            // Call the logout method from the AuthenticationService
            // If logout is successful, navigate to the login page
            await auth.logout();
            navigate("/login");
        }
        catch (error) {
            // Handle errors during logout
            // console.error('Error during logout:', error);
        }
    };

    // Function to check access and update authorization status
    const handleAccess = async () => {
        const physicianService = new PhysicianService();
        try {
            // Call the getPhycianInformation method from PhysicianService
            // If successful, update isAuthorize with the response
            // And set the user information in the physician state variable
            const response = await physicianService.getPhycianInformation(id);

            if (response === null) {
                setisAuthorize(0);
            } else if (response === -1) {
                setisAuthorize(-1);
            } else {
                setPhysician(response);
                setisAuthorize(1);

                // Call the getAppointments method from PhysicianService to obtain the appointments
                const appointments = await physicianService.getAppointments(id);

                if (appointments == null) {
                    // If there are errors, set isAuthorize to 0
                    setisAuthorize(0);
                } else if (appointments === -1) {
                    // If the token is expired, set isAuthorize to -1
                    setisAuthorize(-1);
                } else if (appointments.length === 0) {
                    // If there are no patients, set isAuthorize to 1 and display a message
                    setMessagePatients("No patients yet");
                    setisAuthorize(1);
                } else {
                    // If there are patients, set isAuthorize to 1 and update the appointments state variable
                    setAppointments(appointments);
                    setMessagePatients("");
                    setisAuthorize(1);
                }
            }
        }
        catch (error) {
            // Handle errors during access check
            // Remove the token from local storage and set isAuthorize to false
            localStorage.removeItem("token");
            setisAuthorize(false);
        }
    };

    // Function to handle the status change
    const handleChangeStatus = (appointmentId) => {
        // Find the appointment with the given id
        let newAppointment = appointments.find((appointment) => appointment.id === appointmentId);

        // Update the status of the appointment
        newAppointment.status = selectedStatus[appointmentId];

        // Update the appointment in the database
        let patientService = new PatientService();
        const retVal = patientService.updateAppointmentStatus(newAppointment);

        // If the update was successful, reload the page
        if (retVal) {
            window.location.reload();
        }
        else if (retVal === -1) {
            setisAuthorize(-1);
        }
    };

    // useEffect to run the access check on component
    useEffect(() => {
        handleAccess();
    }, []);

    if (isAuthorize === 0) {
        return (
            <div>
                <h1>You aren't authorized to access this page!</h1>
                <button onClick={handleLogout}>Back To Login Page</button>
            </div>
        );
    } else if (isAuthorize === -1) {
        localStorage.removeItem("token");
        return (
            <div>
                <h1>Your session has most likely timed out. Please log in again.</h1>
                <button onClick={() => navigate(`/login`)}>Back To Login</button>
            </div>
        );
    } else if (isAuthorize === -2) {
        return (
            <div>
                <h1>Loading...</h1>
            </div>
        );
    } else {
        return (
            <div>
                <div>
                    <div style={{ display: 'flex', flexDirection: "row" }}>
                        <h1>{physician.nume} {physician.prenume}</h1>
                        <button style={{ height: '20px', marginTop: '35px', marginLeft: '20px' }} onClick={handleLogout}>Logout</button>
                        <button style={{ height: '20px', marginTop: '35px', marginLeft: '20px' }} onClick={() => navigate(`/update`)}>Update username or password</button>
                        <button style={{ height: '20px', marginTop: '35px', marginLeft: '20px' }} onClick={() => navigate(`/update/${type}`, { state: { physician } })}>Update personal informations</button>
                    </div>
                    <p>Specialization: {physician.specializare}</p>
                    <p>Email: {physician.email}</p>
                    <p>Phone Number: {physician.telefon}</p>
                </div>

                <div>
                    <h2>My Patients</h2>
                    {messagePatients && <p style={{ color: 'red' }}>{messagePatients}</p>}
                    {appointments && appointments.length > 0 &&
                        <table>
                            <thead>
                                <tr>
                                    <th>Id</th>
                                    <th>Last Name</th>
                                    <th>First Name</th>
                                    <th>Email</th>
                                    <th>Phone Number</th>
                                    <th>Date</th>
                                    <th>Status</th>
                                    <th>Change Status</th>
                                    <th>View Consultation</th>
                                </tr>
                            </thead>
                            <tbody>
                                {appointments && appointments.map((appointment) => (
                                    <tr key={appointment.id}>
                                        <td>{appointment.id}</td>
                                        <td>{appointment.lastName}</td>
                                        <td>{appointment.firstName}</td>
                                        <td>{appointment.email}</td>
                                        <td>{appointment.phoneNumber}</td>
                                        <td>{appointment.date}</td>
                                        <td>
                                            {!appointment.status ? (
                                                // Render a dropdown if appointment.data is null
                                                <select defaultValue="Choose" onChange={(e) => setSelectedStatus({ ...selectedStatus, [appointment.id]: e.target.value })}>
                                                    <option defaultValue="option2"></option>
                                                    <option defaultValue="option1">onorata</option>
                                                    <option defaultValue="option3">neprezentat</option>
                                                </select>
                                            ) : (
                                                // Render the text if appointment.data is not null
                                                appointment.status
                                            )}
                                        </td>
                                        <td>
                                            <button onClick={() => handleChangeStatus(appointment.id)}
                                                disabled={appointment.status !== ""}>
                                                Change status
                                            </button>
                                        </td>
                                        <td><button onClick={() => navigate(`/consultation/${appointment.id_user}`, { state: { physicianId: id, date: appointment.date } })}>Consultation</button></td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>}
                </div>
            </div>
        );
    }
};

export default PhysicianPage;
