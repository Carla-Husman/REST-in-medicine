import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import PatientService from '../Services/PatientService';

const UpdateInfoPage = () => {
    // Hook from react-router-dom to navigate between pages
    const navigate = useNavigate();

    // State variables to track form input and error messages
    const [updatedInfo, setUpdatedInfo] = useState({
        username: '',
        password: '',
        oldPassword: '',
    });
    const [error, setError] = useState('');
    const [isAuthorize, setisAuthorize] = useState(-2);

    // Function to handle form submission
    // PatientService will be called to update the username and password by a 
    // patient or a physician. This is not a good practice, but this logic would 
    // be the same for both types of users.
    const handleUpdate = async () => {
        const patientService = new PatientService();
        const response = await patientService.updateInfo(updatedInfo);
        if (response === "Token expired") {
            setisAuthorize(-1);
        } else if (response === "Unauthorized") {
            setisAuthorize(0);
        } else {
            setError(response);
        }
    };

    if (isAuthorize === -1) {
        if (isAuthorize === -1) {
            localStorage.removeItem("token");
            return (
                <div>
                    <h1>Your session has most likely timed out. Please log in again.</h1>
                    <button onClick={() => navigate(`/login`)}>Back To Login</button>
                </div>
            );
        }
    } else if (isAuthorize === 0) {
        return (
            <div>
                <h1>You are not authorized to access this page.</h1>
            </div>
        );
    } else {
        return (
            <div>
                <h1>Update my informations</h1>
                <form>
                    <label>New username: </label>
                    <input
                        type="text"
                        value={updatedInfo.username}
                        onChange={(e) => setUpdatedInfo({
                            ...updatedInfo,
                            username: e.target.value,
                        })}
                    /> <br />
                    <label>New password: </label>
                    <input
                        type="password"
                        value={updatedInfo.password}
                        onChange={(e) => setUpdatedInfo({
                            ...updatedInfo,
                            password: e.target.value,
                        })}
                    /> <br /><br />
                    <label>Introduce your current password for confirmation: </label><br />
                    <input
                        type="password"
                        value={updatedInfo.oldPassword}
                        onChange={(e) => setUpdatedInfo({
                            ...updatedInfo,
                            oldPassword: e.target.value,
                        })}
                    /> <br /><br />
                    <p style={{ color: error === "Successful Updated!" ? 'green' : 'red' }}>{error}</p>
                    <button type="button" onClick={handleUpdate}>Save changes</button>
                </form>
                <br />
            </div>
        );
    }
}

export default UpdateInfoPage;