import axios from "axios";
import AuthenticationService from "./Services/AuthenticationService";

// This promise-based function takes in a token and verifies it
// Will be call in every component that needs to verify a token
const verifyToken = async () => {
    // Create an instance of the AuthenticationService
    let auth = new AuthenticationService();
    const token = auth.getCurrentToken();
    try {
        // Make a GET request to the server to verify if the token is valid
        const response = await axios.get(`http://localhost:8000/verify?token=${token}`);

        // if the token is invalid, log the user out
        if (response.data === false) {
            // Remove the token from local storage
            localStorage.removeItem('token');

            // Redirect the user to the login page
            window.location.href = "/login";
        }
    } catch (error) {
        // Log an error if there's an issue with the verification request
        console.error("Error verifying token:", error);
    }
};

export { verifyToken };