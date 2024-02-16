import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import PatientService from '../Services/PatientService';
import AuthenticationService from '../Services/AuthenticationService';

const MakeAppointmentPage = () => {
    // Extracting physicianId from the URL parameters
    const { physicianId } = useParams();

    // State variables for selected date and error messages
    const [selectedDate, setSelectedDate] = useState('');
    const [error, setError] = useState('');

    // Function to handle form submission
    const handleSubmit = async (event) => {
        // Prevent to reload the page after submitting the form
        event.preventDefault();

        const patientService = new PatientService();

        // Formatting the selected date
        const formattedDate = selectedDate.split('T').join(' ') + ':00';

        // Making an appointment and handling the response
        const response = await patientService.makeAppointment(physicianId, formattedDate);
        if (response === "Token expired") {
            const auth = new AuthenticationService();
            auth.logout();
            return window.location.href = '/login';
        } else if (response !== "") {
            setError(response);
        }
    };

    return (
        <div>
            <h1>Make an Appointment</h1>
            <br />
            <form onSubmit={handleSubmit}>
                <label>
                    Select Date:
                </label><br /><br />
                <input
                    type="datetime-local"
                    value={selectedDate}
                    onChange={(e) => setSelectedDate(e.target.value)}
                />
                <br />
                <br />
                <button type="submit">Submit Appointment</button>
                <p style={{color:"red"}}>{error}</p>
            </form>
        </div>
    );
};

export default MakeAppointmentPage;
