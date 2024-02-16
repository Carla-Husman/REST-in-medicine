import React, { useState } from 'react';
import { useNavigate } from "react-router-dom";
import AuthenticationService from '../Services/AuthenticationService';

const LoginPage = () => {
    // State variables to hold username, password, and error message
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    // Hook from react-router-dom to navigate between pages
    const navigate = useNavigate();

    // Instance of the AuthenticationService class
    let auth = new AuthenticationService();

    // Function to handle the login process
    const handleLogin = async () => {
        // Call the login method from the AuthenticationService
        // If login is successful, navigate to the client page
        // Otherwise, set an error message
        const response = await auth.login(username, password);

        if (response === null) {
            setError('Incorrect username or password.');
        }
        else if (response.role === "patient") {
            navigate("/client/" + response.id);
        }
        else if (response.role === "physician") {
            navigate("/physician/" + response.id);
        }
    };

    // Function to navigate to the register page
    const handleRegister = async () => {
        navigate("/register");
    }

    return <div>
        <h2>Login Page</h2>
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
            <button type="button" onClick={handleLogin}>
                Login
            </button>
            <p>Doesn't have an account?</p>
            <button type="button" onClick={handleRegister}>
                Register
            </button>
            {error && <p style={{ color: 'red' }}>{error}</p>}
        </form>
    </div>;
};

export default LoginPage;