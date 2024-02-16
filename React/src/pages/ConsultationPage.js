import React, { useEffect, useState } from 'react';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import ConsultationService from '../Services/ConsultationService';
import AuthenticationService from '../Services/AuthenticationService';

const ConsultationPage = () => {
    // Extracting patientId from the URL parameters
    const { patientId } = useParams();

    // Hook from react-router-dom to navigate between pages
    const navigate = useNavigate();

    // Using the useLocation hook to get information from the location (added to the state object)
    const location = useLocation();
    const { physicianId, date } = location.state || {};

    // State variables and functions to update them
    // consultation: -2 change the path, -1 - token expired, null - no consultation, object - consultation details
    const [consultation, setConsultation] = useState(null);
    const [investigations, setInvestigations] = useState([]);
    const [newInvestigation, setNewInvestigation] = useState({
        denumire: '',
        durata_de_procesare: 0,
        rezultat: '',
    });
    const [diagnostic, setDiagnostic] = useState('sanatos');
    const [error, setError] = useState('');
    const [errorC, setErrorC] = useState('');
    const [errorUpdate, setErrorUpdate] = useState('');

    // Function to handle the add investigation process
    const handleAddInvestigation = async () => {
        // Verify the investigation
        if (newInvestigation.durata_de_procesare <= 0) {
            setError('Processing time must be a positive number');
        } else if (!newInvestigation.denumire.match('^[a-zA-Z\\s.,;]*$') || !newInvestigation.rezultat.match('^[a-zA-Z\\s.,;]*$')) {
            setError('Name and result must contain only letters');
        } else {
            setInvestigations(prevInvestigations => [
                ...prevInvestigations,
                {
                    denumire: newInvestigation.denumire,
                    durata_de_procesare: newInvestigation.durata_de_procesare,
                    rezultat: newInvestigation.rezultat,
                }
            ]);
            /*setNewInvestigation({
                denumire: '',
                durata_de_procesare: 0,
                rezultat: '',
            });*/
            setError('');
            setErrorC('');
        }
    };

    // Function to handle the add consultation process
    const handleAddConsultation = async () => {
        const consultationService = new ConsultationService();

        // Verify the diagnostic and investigations
        if (!diagnostic.match('^[a-zA-Z\\s.,;]*$')) {
            setError('Diagnostic must contain only letters');
        } else if (investigations.length === 0) {
            setErrorC('You must add at least one investigation');
        } else {
            const response = await consultationService.addConsultation(patientId, date, diagnostic, investigations);

            if (response === "") {
                setErrorC('');
                setError('');
                window.location.reload();
            } else if (response === "Token expired") {
                const auth = new AuthenticationService();
                auth.logout();
                return window.location.href = '/login';
            } else if (response === -3) {
                setErrorC("");
            } else {
                setErrorC(response);
            }
        }
    };

    // Function to handle the change of an investigation field
    const handleInputChange = (index, field, value) => {
        if (consultation) {
            const updatedInvestigations = [...consultation.investigatii];
            updatedInvestigations[index] = {
                ...updatedInvestigations[index],
                [field]: value,
            };

            setConsultation({
                ...consultation,
                investigatii: updatedInvestigations,
            });
        }
    };

    // Function to handle the submit event
    const handleSubmit = async () => {
        const consultationService = new ConsultationService();
        const response = await consultationService.updateConsultation(consultation);

        if (response === "Successfully updated the consultation details") {
            setErrorUpdate('Successfully updated the consultation details');
        } else if (response === -1) {
            const auth = new AuthenticationService();
            auth.logout();
            return window.location.href = '/login';
        } else if (response === -3) {
            setErrorUpdate("");
        } else {
            setErrorUpdate(response);
        }
    };

    // Effect to load the consultation information when the page loads
    useEffect(() => async () => {
        if (date === undefined || date === null || physicianId === undefined || physicianId === null) {
            setConsultation(-2);
            return;
        }
        const consultationService = new ConsultationService();

        const response = await consultationService.getConsultationDetails(patientId, date);

        if (response) {
            setConsultation(response);
        } else if (response === -1) {
            setConsultation(-1);
        } else {
            setConsultation(null);
        }

    }, [patientId, physicianId, date]);

    if (consultation === -1) {
        localStorage.removeItem("token");
        return (
            <div>
                <h1>Your session has most likely timed out. Please log in again.</h1>
                <button onClick={() => navigate(`/login`)}>Back To Login</button>
            </div>
        );
    } else if (consultation === -2) {
        return (
            <div >
                <h2>Add Consultation</h2>
                <p>Date: {date}</p>

                <form>
                    <p>Investigation</p>
                    <label>Name:</label>
                    <input
                        type="text"
                        value={newInvestigation.denumire}
                        onChange={(e) =>
                            setNewInvestigation({
                                ...newInvestigation,
                                denumire: e.target.value,
                            })
                        }
                    /><br />

                    <label>Processing time:</label>
                    <input
                        type="number"
                        value={newInvestigation.durata_de_procesare}
                        onChange={(e) =>
                            setNewInvestigation({
                                ...newInvestigation,
                                durata_de_procesare: e.target.value,
                            })
                        }
                    /> <br />

                    <label>Result:</label>
                    <input
                        type="text"
                        value={newInvestigation.rezultat}
                        onChange={(e) =>
                            setNewInvestigation({
                                ...newInvestigation,
                                rezultat: e.target.value,
                            })
                        }
                    /><br />

                    <p style={{ color: 'red' }}>{error}</p>
                    <button type="button" onClick={handleAddInvestigation}>Add Investigation</button>
                </form><br></br>

                <form>
                    <label>Diagnostic:</label>
                    <select
                        onChange={(e) => setDiagnostic(e.target.value)}
                    >
                        <option value="sanatos">Sanatos</option>
                        <option value="bolnav">Bolnav</option>
                    </select><br />

                    <p style={{ color: 'red' }}>{errorC}</p>
                    <button type="button" onClick={handleAddConsultation}>Add Consultation</button>
                </form>
            </div>
        );
    } else if (consultation) {
        return (
            <div>
                <h1>Consultation</h1>
                <p>Date: {consultation && consultation.data}</p>
                <form>
                    <div>
                        <label>Diagnostic:</label>
                        <input
                            type="text"
                            value={consultation.diagnostic}
                            onChange={(e) => setConsultation({ ...consultation, diagnostic: e.target.value })}
                        /><br />
                        <p>Investigations:</p>
                        <div>{consultation.investigatii && consultation.investigatii.map((investigation, index) => (
                            <div key={investigation.id_react}>
                                <label>Name:</label>
                                <input
                                    type="text"
                                    value={investigation.denumire}
                                    onChange={(e) => handleInputChange(index, 'denumire', e.target.value)}
                                /><br />
                                <label>Processing time: </label>
                                <input
                                    type="text"
                                    value={investigation.durata_de_procesare}
                                    onChange={(e) => handleInputChange(index, 'durata_de_procesare', e.target.value)}
                                /><br />
                                <label>Result:</label>
                                <input
                                    type="text"
                                    value={investigation.rezultat}
                                    onChange={(e) => handleInputChange(index, 'rezultat', e.target.value)}
                                /><br /><br />
                            </div>
                        ))}
                        </div>
                        <button type="button" onClick={handleSubmit}>Save Changes</button>
                    </div>
                </form>
                <p>{errorUpdate}</p>
            </div>
        );
    }
};

export default ConsultationPage;