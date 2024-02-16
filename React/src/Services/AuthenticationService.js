import axios from "axios";

class AuthenticationService {
  // Method to handle user login
  async login(username, password) {
    try {
      // Send a POST request to the login endpoint with provided username and password
      const response = await axios.post("http://localhost:8000/login", {
        username: username,
        password: password,
      },
        {
          headers: {
            'Content-Type': 'application/json', // Specify the content type of the request
          },
        });

      // If the response data is not empty, save the token in local storage
      if (response.data.token !== "") {
        localStorage.setItem('token', response.data.token);
        return { "role": response.data.role, "id": response.data.id };
      }

      return null;
    } catch (error) {
      // Log an error if there's an issue with the login request
      //console.error("Error logging in:", error);
      return null;
    }
  }

  // Method to handle user logout
  async logout() {
    // Get the token from local storage
    const token = localStorage.getItem('token');

    try {
      // Send a POST request to the logout endpoint with the token
      const response = await axios.post("http://localhost:8000/logout", {}, {
        headers: {
          'Content-Type': 'application/json', // Specify the content type of the request
          Authorization: `Bearer ${token}`, // Inject the token into the Authorization header
        },
      });

      // Remove the token from local storage
      localStorage.removeItem('token');

      return response.data;
    } catch (error) {
      // Log an error if there's an issue with the logout request
      // console.error("Error logging out:", error);
      return false;
    }
  }

  // Method to handle user registration
  async register(username, password, email, firstName, lastName, cnp, phoneNumber, dateOfBirth) {
    try {
      // Send a POST request to the register endpoint with provided username and password
      await axios.put("http://localhost:8000/register", {
        username: username,
        password: password,
        email: email,
        first_name: firstName,
        last_name: lastName,
        cnp: cnp,
        phone_number: phoneNumber,
        date_of_birth: dateOfBirth,
      },
        {
          headers: {
            'Content-Type': 'application/json', // Specify the content type of the request
          },
        });

      return "success";
    } catch (error) {
      // Log an error if there's an issue with the register request
      //console.error("Error logging in:", error);
      if (error.response.status === 409 || error.response.status === 422) {
        return error.response.data.detail;
      }
      return "not success";
    }
  }

  // Method to get the current token from local storage
  getCurrentToken() {
    return localStorage.getItem('token');
  }
}

export default AuthenticationService;