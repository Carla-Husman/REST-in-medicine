import React, { useState } from 'react';
import { useNavigate } from "react-router-dom";
import AuthenticationService from '../Services/AuthenticationService';

const RegisterPage = () => {
    // State variables to track form input and error messages
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [cnp, setCNP] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [dateOfBirth, setDateOfBirth] = useState('');
    const [error, setError] = useState('');

    // Hook from react-router-dom to navigate between pages
    const navigate = useNavigate();

    // Instance of the AuthenticationService class
    const auth = new AuthenticationService();

    // Function to handle the registration process
    const handleRegister = async () => {
        try {
            // Call the register method from the AuthenticationService
            // If registration is successful, navigate to the login page
            // Otherwise, set an error message
            const isSuccess = await auth.register(username, password, email, firstName, lastName, cnp, phoneNumber, dateOfBirth);
            if (isSuccess === "success") {
                navigate("/login");
            } else {
                setError(isSuccess);
            }
        } catch (error) {
            // Handle unexpected errors during registration
            console.error('Error during registration:', error);
            setError(error.response.detail);
        }
    };

    return (
        <div>
            <h2>Register Page</h2>
            <form>
                <div>
                    <label>Username:</label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </div>
                <div>
                    <label>Password:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                <div>
                    <label>Email:</label>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                </div>

                <div>
                    <label>First Name:</label>
                    <input
                        type="text"
                        value={firstName}
                        onChange={(e) => setFirstName(e.target.value)}
                    />
                </div>
                <div>
                    <label>Last Name:</label>
                    <input
                        type="text"
                        value={lastName}
                        onChange={(e) => setLastName(e.target.value)}
                    />
                </div>
                <div>
                    <label>CNP:</label>
                    <input
                        type="text"
                        value={cnp}
                        onChange={(e) => setCNP(e.target.value)}
                    />
                </div>
                <div>
                    <label>Phone Number:</label>
                    <input
                        type="text"
                        value={phoneNumber}
                        onChange={(e) => setPhoneNumber(e.target.value)}
                    />
                </div>
                <div>
                    <label>Date of Birth:</label>
                    <input
                        type="date"
                        value={dateOfBirth}
                        onChange={(e) => setDateOfBirth(e.target.value)}
                    />
                </div>

                <button type="button" onClick={handleRegister}>
                    Register
                </button>

                <p>Already have an account?</p>

                <button type="button" onClick={() => navigate("/login")}>
                    Login
                </button>

                {error && <p style={{ color: 'red' }}>{error}</p>}
            </form>
        </div>
    );
};

export default RegisterPage;
