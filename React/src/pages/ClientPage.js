import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import AuthenticationService from '../Services/AuthenticationService';
import PatientService from "../Services/PatientService";
import PhysicianService from "../Services/PhysicianService";
import ConsultationService from "../Services/ConsultationService";

const ClientPage = () => {
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
    const [patient, setPatient] = useState();
    const [errorConsultation, setErrorConsultation] = useState("");
    const [appointments, setAppointments] = useState();
    const [selectedStatus, setSelectedStatus] = useState({});
    const [selectedConsultation, setSelectedConsultation] = useState(null);
    const [physiciansData, setPhysiciansData] = useState({
        physicians: [],
        totalPages: 0,
        currentPage: 1,
    });

    // Hook from react-router-dom to navigate between pages
    const navigate = useNavigate();

    // Function to handle logout
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

    // Function to handle account deactivation
    const handleDesactivateAccount = async () => {
        // After deactivation the account, the user must to confirm the action
        const userConfirmed = window.confirm('Are you sure you want to deactivate your account? This action is irreversible!');

        // If the user confirmed the action, call the deactivateAccount method from PatientService
        if (userConfirmed) {
            const patientService = new PatientService();
            const action = patientService.deactivateAccount();

            if (action) {
                localStorage.removeItem("token");
                navigate("/login");
            } else if (action === -1) {
                setisAuthorize(-1);
            }
        }
    };

    // Function to get physicians from the next page or previous page
    const getPhysiciansPage = async (page) => {
        const physicianService = new PhysicianService();
        const paginatedPhysicians = await physicianService.getPhysiciansPage(page);

        if (paginatedPhysicians === null) {
            setisAuthorize(0);
        } else if (paginatedPhysicians === -1) {
            setisAuthorize(-1);
        } else if (paginatedPhysicians) {
            setPhysiciansData({
                physicians: paginatedPhysicians.physicians,
                totalPages: paginatedPhysicians.totalPages,
                currentPage: paginatedPhysicians.currentPage,
            })
        }
    };

    // Function to check access and update authorization status
    const handleAccess = async () => {
        const patSer = new PatientService();

        // Call the getUserInformation method from PatientService
        // If successful, update isAuthorize with the response
        // And set the user information in the patient state variable
        const response = await patSer.getUserInformation(id);

        if (response === null) {
            setisAuthorize(0);
        } else if (response === -1) {
            setisAuthorize(-1);
        } else {
            setPatient(response);
            setisAuthorize(1);

            // Call the getPhysicians method from PhysicianService
            const physicianService = new PhysicianService();
            const paginatedPhysicians = await physicianService.getPhysiciansPage(1);

            // If successful, update the physicians state variable
            // Otherwise, set isAuthorize to false
            if (paginatedPhysicians.physicians === null) {
                setisAuthorize(0);
            } else if (paginatedPhysicians.physicians === -1) {
                setisAuthorize(-1);
            } else {
                setisAuthorize(1);
                if (paginatedPhysicians.totalPages === 0) {
                    setPhysiciansData({
                        physicians: paginatedPhysicians.physicians,
                        totalPages: 1,
                        currentPage: paginatedPhysicians.currentPage,
                    })
                } else {
                    setPhysiciansData({
                        physicians: paginatedPhysicians.physicians,
                        totalPages: paginatedPhysicians.totalPages,
                        currentPage: paginatedPhysicians.currentPage,
                    })
                }

                // Obtain the appointments for the current patient
                const appointments = await patSer.getAppointments(id);
                if (appointments === null) {
                    setisAuthorize(0);
                } else if (appointments === -1) {
                    setisAuthorize(-1);
                } else {
                    setisAuthorize(1);
                    // Update the appointments state variable
                    setAppointments(appointments);
                }
            }
        }

    };

    // Function to handle the change of status for an appointment
    const handleChangeStatus = (appointmentId) => {
        // Find the appointment with the given id
        let newAppointment = appointments.find((appointment) => appointment.id === appointmentId);

        // Update the status of the appointment
        newAppointment.status = selectedStatus[appointmentId];

        // Update the appointment in the database
        let patientService = new PatientService();
        const retVal = patientService.updateAppointmentStatus(newAppointment);

        if (retVal === -1) {
            setisAuthorize(-1);
        } else if (retVal) {
            window.location.reload();
        }
    };

    // Function to handle the view of a consultation
    const handleViewConsultation = async (data, id_doctor) => {
        const consultationsService = new ConsultationService();
        const consultation = await consultationsService.getConsultationDetails(id_doctor, data);

        if (consultation === -1) {
            setisAuthorize(-1);
            setErrorConsultation("")
        } else if (consultation === -2) {
            setErrorConsultation("The physician hasn't added the consultation yet!")
        } else {
            setSelectedConsultation(consultation);
            setErrorConsultation("")
        }
    };

    // useEffect hook to run the access check on component mount
    useEffect(() => {
        handleAccess();
    }, []);

    // Conditional rendering based on authorization status
    if (isAuthorize === 0) {
        return (
            <div>
                <h1>You aren't authorized to access this page!</h1>
                <button onClick={handleLogout}>Back To Login Page</button>
            </div>
        );
    }
    else if (isAuthorize === -1) {
        localStorage.removeItem("token");
        return (
            <div>
                <h1>Your session has most likely timed out. Please log in again.</h1>
                <button onClick={() => navigate(`/login`)}>Back To Login</button>
            </div>
        );
    }
    else if (isAuthorize === -2) {
        return (
            <div>
                <h1>Loading...</h1>
            </div>
        );
    }
    else {
        return (
            <div style={{ display: 'flex', flexDirection: 'column' }}>
                <div style={{ display: 'flex', flexDirection: 'row' }}>
                    <h1 style={{ alignContent: 'center' }}>Client Page</h1>
                    <button style={{ height: '20px', marginTop: '35px', marginLeft: '20px' }} onClick={handleLogout}>Logout</button>
                    <button style={{ height: '20px', marginTop: '35px', marginLeft: '20px' }} onClick={() => navigate(`/update`)}>Update username or password</button>
                    <button style={{ height: '20px', marginTop: '35px', marginLeft: '20px' }} onClick={() => navigate(`/update/${type}`, { state: { patient } })}>Update personal informations</button>
                    <button style={{ height: '20px', marginTop: '35px', marginLeft: '20px' }} onClick={handleDesactivateAccount}>Delete account</button>
                </div>

                <div>
                    <h2>My informations</h2>
                    <div>
                        <p>CNP: {patient.cnp}</p>
                        <p>Nume: {patient.nume}</p>
                        <p>Prenume: {patient.prenume}</p>
                        <p>Email: {patient.email}</p>
                        <p>Telefon: {patient.telefon}</p>
                        <p>Data nasterii: {patient.data_nastere}</p>
                    </div>
                </div>

                <div>
                    <h2>My appointments</h2>
                    <table>
                        <thead>
                            <tr>
                                <th>Id</th>
                                <th>Physician</th>
                                <th>Specialization</th>
                                <th>Date</th>
                                <th>Status</th>
                                <th>Change Status</th>
                                <th>Consultation</th>
                            </tr>
                        </thead>
                        <tbody>
                            {appointments && appointments.map((appointment) => (
                                <tr key={appointment.id}>
                                    <td>{appointment.id + 1}</td>
                                    <td>{appointment.nume} {appointment.prenume}</td>
                                    <td>{appointment.specializare}</td>
                                    <td>{appointment.data}</td>
                                    <td>
                                        {!appointment.status ? (
                                            // Render a dropdown if appointment.data is null
                                            <select defaultValue="Choose" onChange={(e) => setSelectedStatus({ ...selectedStatus, [appointment.id]: e.target.value })}>
                                                <option defaultValue="option2"></option>
                                                <option defaultValue="option1">anulata</option>
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
                                    <td>
                                        <button onClick={() => handleViewConsultation(appointment.data, appointment.id_doctor)}>View Consultation Details</button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>

                <div> {selectedConsultation !== null && (
                    <>
                        <h3>My consultation Details</h3>
                        <p>Diagnostic: {selectedConsultation.diagnostic}</p>
                        <p>Investigatii:</p>
                        <ul>
                            {selectedConsultation.investigatii.map((investigatie) => (
                                <li key={investigatie.id}>
                                    <p>Name: {investigatie.denumire}</p>
                                    <p>Period: {investigatie.durata_de_procesare}</p>
                                    <p>Result: {investigatie.rezultat}</p>
                                </li>
                            ))}
                        </ul>
                    </>
                )}
                </div>
                <p style={{ color: "brown" }}>{errorConsultation}</p>

                <div>
                    <h2>Physicians</h2>
                    <button onClick={() => getPhysiciansPage(physiciansData.currentPage - 1)} disabled={physiciansData.currentPage === 1}>
                        Previous Page
                    </button>
                    <span>&ensp; Page {physiciansData.currentPage} of {physiciansData.totalPages} &ensp;</span>
                    <button onClick={() => getPhysiciansPage(physiciansData.currentPage + 1)} disabled={physiciansData.currentPage === physiciansData.totalPages}>
                        Next Page
                    </button>

                    <br /><br />

                    <table>
                        <thead>
                            <tr>
                                <th>Last Name</th>
                                <th>First Name</th>
                                <th>Specialization</th>
                                <th>Email</th>
                                <th>Phone Number</th>
                                <th>Appointment</th>
                            </tr>
                        </thead>
                        <tbody>
                            {physiciansData.physicians && physiciansData.physicians.map((physician) => (
                                <tr key={physician.id}>
                                    <td>{physician.nume}</td>
                                    <td>{physician.prenume}</td>
                                    <td>{physician.specializare}</td>
                                    <td>{physician.email}</td>
                                    <td>{physician.telefon}</td>
                                    <td>
                                        <button onClick={() => navigate(`/makeappointment/${physician.id}`)}>
                                            Make an appointment
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>

                </div>
            </div>
        );
    }
};

export default ClientPage;