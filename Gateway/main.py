import base64
import json
import grpc
import jwt
import requests
import uvicorn as uvicorn

from fastapi import FastAPI, Body, Query, Header, HTTPException, Request
from fastapi.middleware.cors import CORSMiddleware

import authentication_pb2
from authentication_pb2_grpc import AuthenticationServiceStub

# Create a FastAPI app
app = FastAPI()

# Add CORS middleware to allow cross-origin requests
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Create a gRPC channel to communicate with the gRPC server
channel = grpc.insecure_channel('localhost:50051')

# Create a gRPC stub to make requests
stub = AuthenticationServiceStub(channel)

# A secret key for signing JWTs
secret = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E="


# A function for verifying the authorization header and the token
# Returns the decoded data from the JWT token
# This function is used for all the API endpoints that require authorization
def header_and_token_verification(authorization):
    # Check if authorization header is missing
    if authorization is None:
        raise HTTPException(status_code=401, detail="Authentication header missing")

    # Extract token from authorization header
    token = authorization.split(" ")[1] if authorization.startswith("Bearer ") else None

    # Check if Bearer token is missing
    if token is None:
        raise HTTPException(status_code=401, detail="Bearer token missing")

    # Check if token is expired
    result = stub.VerifyToken(authentication_pb2.Token(value=token))

    # Return an error if the token has expired
    if result.value is False:
        raise HTTPException(status_code=401, detail="Token expired")
        return

    # Decode the JWT token using the provided secret key
    secretKey_bytes = base64.b64decode(secret)
    return jwt.decode(token,
                      secretKey_bytes,
                      ["HS256", ])


# Define an API endpoint for user login
@app.api_route("/login", methods=["POST"])
def login(username: str = Body(...), password: str = Body(...)):
    # Call the gRPC CreateToken method with the provided username and password
    clientToken = stub.CreateToken(authentication_pb2.AuthenticationRequest(username=username, password=password))

    # Check if the token is empty
    if clientToken.value == "":
        return {
            "token": "",
            "role": "",
            "id": ""
        }

    # Decode the JWT token using the provided secret key
    secretKey_bytes = base64.b64decode(secret)
    decoded_data = jwt.decode(
        clientToken.value,
        secretKey_bytes,
        ["HS256", ]
    )

    if decoded_data['role'] == "patient":
        complete_path = "http://localhost:8080/api/medical_office/patients/" + str(decoded_data['sub'])
        response = requests.request("GET", complete_path)

        # Verify if the patient account is active
        if json.loads(response.content.decode('utf-8'))['is_active'] is True or \
                json.loads(response.content.decode('utf-8'))['is_active'] == 1:
            return {
                "token": clientToken.value,
                "role": decoded_data['role'],
                "id": decoded_data['sub']
            }
        else:
            return {
                "token": "",
                "role": "",
                "id": ""
            }
    elif decoded_data['role'] == "physician":
        return {
            "token": clientToken.value,
            "role": decoded_data['role'],
            "id": decoded_data['sub']
        }


# Define an API endpoint for updating a user's password or username
@app.api_route("/update", methods=["PUT"])
def update(username: str = Body(None), password: str = Body(None), oldPassword: str = Body(None), authorization: str = Header()):
    # Call the function for verifying the authorization header and the token
    decoded_data = header_and_token_verification(authorization)

    if decoded_data['role'] == "patient" or decoded_data['role'] == "physician":
        # Call the gRPC UpdateUser method
        result = stub.UpdateUser(authentication_pb2.UpdateRequest(newUsername=username, newPassword=password,
                                                                  oldPassword=oldPassword, uid=int(decoded_data['sub'])))

        # Return the result message
        return result.message

    return "Unauthorized"


# Define an API endpoint for verifying a user's token
@app.api_route("/verify", methods=["GET"])
def verify(token: str = Query(..., title="Tokenul de autentificare")):
    # Call the gRPC VerifyToken method with the provided token
    result = stub.VerifyToken(authentication_pb2.Token(value=token))

    # Return the result value
    return result.value


# Define an API endpoint for user logout
@app.post("/logout")
def logout(authorization: str = Header()):
    # Check if the Authorization header is present
    if authorization is None:
        raise HTTPException(status_code=401, detail="Authentication header missing")

    # Extract the token from the Authorization header
    token = authorization.split(" ")[1] if authorization.startswith("Bearer ") else None

    # Check if the token is present
    if token is None:
        raise HTTPException(status_code=401, detail="Bearer token missing")

    # Call the gRPC DestroyToken method with the provided token
    result = stub.DestroyToken(authentication_pb2.Token(value=token))

    # Return the result value
    return result.value


# Define an API endpoint for user registration
@app.api_route(path="/register", methods=["PUT"])
async def register(request: Request):
    # Obtain the request body
    body = await request.json()

    # Call the gRPC CreateToken method with the provided username and password
    registrationResponse = stub.RegisterUser(
        authentication_pb2.RegistrationRequest(username=body['username'], password=body['password'], role="patient"))

    # Check the response status code and handle accordingly
    if registrationResponse.uid == -2:
        raise HTTPException(status_code=422, detail="Invalid username or password")
    elif registrationResponse.uid == -1:
        raise HTTPException(status_code=422, detail="Username already exists")

    # Send a PUT request to create a new patient
    response = requests.request("PUT", "http://localhost:8080/api/medical_office/patients/" + str(body['cnp']),
                                json={
                                    "cnp": body['cnp'],
                                    "id_user": registrationResponse.uid,
                                    "nume": body['last_name'],
                                    "prenume": body['first_name'],
                                    "email": body['email'],
                                    "telefon": body['phone_number'],
                                    "data_nasterii": body['date_of_birth'],
                                    "is_active": 1
                                })

    # Check the response status code and handle accordingly
    # Delete the user from the grpc server if the request fails
    if response.status_code == 201:
        return registrationResponse.uid
    elif response.status_code in {500, 409, 422}:
        stub.DeleteUser(authentication_pb2.DeleteRequest(uid=registrationResponse.uid))
        raise HTTPException(status_code=response.status_code, detail=str(response.content.decode('utf-8')))
    else:
        stub.DeleteUser(authentication_pb2.DeleteRequest(uid=registrationResponse.uid))
        raise Exception(f"Unexpected error: {response.status_code}")


# Define an API endpoint for patient
@app.api_route(path="/patients/{path:path}", methods=["GET", "DELETE", "PUT"])
async def patients(path: str, request: Request, authorization: str = Header(...)):
    # Call the function for verifying the authorization header and the token
    decoded_data = header_and_token_verification(authorization)

    # A patient can only see his information and can deactivate his account
    if decoded_data['role'] != "patient":
        # Raise exception for unauthorized role
        raise HTTPException(status_code=401, detail="Unauthorized")
    else:
        complete_path = "http://localhost:8080/api/medical_office/patients/"

        if request.method == "GET":
            # If the patient ID from the token does not match the patient ID from the path
            if decoded_data['sub'] != path:
                raise HTTPException(status_code=403, detail="Forbidden")

            # Send a GET request to retrieve patient information
            response = requests.request("GET", complete_path + path)

            # Check the response status code and handle accordingly
            if response.status_code == 200:
                return response.content
            elif response.status_code == 404:
                raise HTTPException(status_code=404, detail="Patient not found")
            elif response.status_code == 500:
                raise HTTPException(status_code=500, detail="Internal server error")
            else:
                raise Exception(f"Unexpected error: {response.status_code}")
        elif request.method == "DELETE":
            # Deactivate the patient account
            response = requests.request("DELETE", complete_path + str(decoded_data['sub']))

            # Check the response status code and handle accordingly
            if response.status_code == 200:
                return "success"
            elif response.status_code == 404:
                raise HTTPException(status_code=404, detail="Patient not found")
            elif response.status_code == 500:
                raise HTTPException(status_code=500, detail="Internal server error")
            else:
                raise Exception(f"Unexpected error: {response.status_code}")
        elif request.method == "PUT":
            # Extract patient data from the request
            patient_data = await request.json()

            # Send a PUT request to update patient information
            response = requests.request("PUT", complete_path + str(patient_data['cnp']),
                                        json={
                                            "cnp": patient_data['cnp'],
                                            "id_user": decoded_data['sub'],
                                            "nume": patient_data['nume'],
                                            "prenume": patient_data['prenume'],
                                            "email": patient_data['email'],
                                            "telefon": patient_data['telefon'],
                                            "data_nasterii": patient_data['data_nasterii'],
                                            "is_active": 'true'
                                        })

            # Check the response status code and handle accordingly
            if response.status_code == 200:
                return "Successfull update!"
            elif response.status_code == 404:
                raise HTTPException(status_code=404, detail="Patient not found")
            elif response.status_code in {409, 422}:
                raise HTTPException(status_code=response.status_code, detail=str(response.content.decode('utf-8')))
            elif response.status_code == 500:
                raise HTTPException(status_code=500, detail="Internal server error")
            else:
                raise Exception(f"Unexpected error: {response.status_code}")
        else:
            # Raise exception for unauthorized method
            raise HTTPException(status_code=405, detail="Method not allowed")


@app.api_route(path="/physicians/{path:path}", methods=["GET", "PUT"])
async def physicians(path: str, request: Request, authorization: str = Header(...)):
    # Call the function for verifying the authorization header and the token
    decoded_data = header_and_token_verification(authorization)

    # Path for backend request
    complete_path = "http://localhost:8080/api/medical_office/physicians/"

    # If the role is physician, him can see his patients and his user infos
    if decoded_data['role'] == "physician":
        if request.method == "GET" and ("patients" in path):
            complete_path += str(path)

            # Send a GET request to retrieve patient information
            response = requests.request("GET", complete_path)

            # Check the response status code and handle accordingly
            if response.status_code == 200:
                jsonResponse = json.loads(response.content.decode("utf-8"))
                try:
                    print(jsonResponse['_embedded']['patientList'])
                except KeyError:
                    return "0"
                return jsonResponse['_embedded']['patientList']
            elif response.status_code == 404:
                raise HTTPException(status_code=404, detail="Physician not found")
            elif response.status_code == 500:
                raise HTTPException(status_code=500, detail="Internal server error")
            else:
                raise Exception(f"Unexpected error: {response.status_code}")
        elif request.method == "GET":
            # If the patient ID from the token does not match the physician ID from the path
            if decoded_data['sub'] != path:
                raise HTTPException(status_code=403, detail="Forbidden")

            # Send a GET request to retrieve physician information
            # Append the physician ID to the path for retrieving physician information
            response = requests.request("GET", complete_path + str(path))

            # Check the response status code and handle accordingly
            if response.status_code == 200:
                jsonResponse = json.loads(response.content.decode("utf-8"))
                return {
                    "nume": jsonResponse['nume'],
                    "prenume": jsonResponse['prenume'],
                    "email": jsonResponse['email'],
                    "telefon": jsonResponse['telefon'],
                    "specializare": jsonResponse['specializare']
                }
            elif response.status_code == 404:
                raise HTTPException(status_code=404, detail="Physician not found")
            elif response.status_code == 500:
                raise HTTPException(status_code=500, detail="Internal server error")
            else:
                raise Exception(f"Unexpected error: {response.status_code}")
        elif request.method == "PUT":
            # Extract physician data from the request
            physician_data = await request.json()

            # Send a PUT request to update physician information
            response = requests.request("PUT", complete_path + str(decoded_data['sub']),
                                        json={
                                            "nume": physician_data['nume'],
                                            "prenume": physician_data['prenume'],
                                            "email": physician_data['email'],
                                            "telefon": physician_data['telefon'],
                                            "specializare": physician_data['specializare']
                                        })

            # Check the response status code and handle accordingly
            if response.status_code == 200:
                return "Successfull update!"
            elif response.status_code == 404:
                raise HTTPException(status_code=404, detail="Physician not found")
            elif response.status_code in {409, 422}:
                raise HTTPException(status_code=response.status_code, detail=str(response.content.decode('utf-8')))
            elif response.status_code == 500:
                raise HTTPException(status_code=500, detail="Internal server error")
            else:
                raise Exception(f"Unexpected error: {response.status_code}")
        else:
            # Raise exception for unauthorized method
            raise HTTPException(status_code=405, detail="Method not allowed")
    # If the role is patient, him can see all physicians paginated or not
    elif decoded_data['role'] == "patient":
        # If the patient ID from the token does not match the patient ID from the path
        if path.isdigit() and decoded_data['sub'] != path:
            raise HTTPException(status_code=403, detail="Forbidden")

        if request.method == "GET" and request.query_params.get("page") is not None:
            response = requests.request("GET", "http://localhost:8080/api/medical_office/physicians",
                                        params={
                                            "page": request.query_params.get("page")
                                        })

            if response.status_code == 200:
                jsonResponse = json.loads(response.content.decode("utf-8"))

                # Check if the response contains the physicians list
                try:
                    print(jsonResponse['physicians'])
                except KeyError:
                    return "0"

                return {
                    "physicians": jsonResponse['physicians'],
                    "totalPages": jsonResponse['totalPages'],
                    "currentPage": jsonResponse['currentPage']
                }
            elif response.status_code == 500:
                raise HTTPException(status_code=500, detail="Internal server error")
            else:
                raise Exception(f"Unexpected error: {response.status_code}")
        elif request.method == "GET":
            # Send a GET request to retrieve physicians list
            response = requests.request("GET", "http://localhost:8080/api/medical_office/physicians")

            # Check the response status code and handle accordingly
            if response.status_code == 200:
                return response.content
            elif response.status_code == 500:
                raise HTTPException(status_code=500, detail="Internal server error")
            else:
                raise Exception(f"Unexpected error: {response.status_code}")
    else:
        raise HTTPException(status_code=401, detail="Unauthorized")


# Define an API endpoint for appointments
@app.api_route(path="/appointments/{path:path}", methods=["GET", "POST", "PUT"])
async def appointments(path: str, request: Request, authorization: str = Header(...)):
    # Call the function for verifying the authorization header and the token
    decoded_data = header_and_token_verification(authorization)

    # A patient can only see his appointments, can create new ones and can update them
    if decoded_data['role'] == "patient":
        if request.method == "GET":
            response = requests.request("GET", "http://localhost:8080/api/medical_office/appointments/" + str(path))
        elif request.method == "POST":
            # Extract appointment data from the request
            appointment_data = await request.json()
            response = requests.request("POST", "http://localhost:8080/api/medical_office/appointments",
                                        json={
                                            "id_pacient": int(decoded_data['sub']),
                                            "id_doctor": int(appointment_data['appointment']['id_doctor']),
                                            "status": "",
                                            "data": appointment_data['appointment']['data']
                                        })
        elif request.method == "PUT" and path == "":
            # Extract appointment data from the request
            appointment_data = await request.json()
            response = requests.put("http://localhost:8080/api/medical_office/appointments",
                                    json={
                                        "id_pacient": int(decoded_data['sub']),
                                        "id_doctor": int(appointment_data['appointment']['id_doctor']),
                                        "status": appointment_data['appointment']['status'],
                                        "data": appointment_data['appointment']['data']
                                    })

        # Check the response status code and handle accordingly
        if response.status_code == 200 or response.status_code == 201:
            return response.content
        elif response.status_code == 500:
            raise HTTPException(status_code=500, detail="Internal server error")
        elif response.status_code == 404:
            raise HTTPException(status_code=404, detail="Appointment not found")
        elif response.status_code in {409, 422}:
            raise HTTPException(status_code=response.status_code, detail=str(response.content.decode('utf-8')))
        else:
            raise Exception(f"Unexpected error: {response.status_code}")

    # A physician can only see his appointments and can update them
    elif decoded_data['role'] == "physician":
        if request.method == "GET":
            response = requests.request("GET", "http://localhost:8080/api/medical_office/appointments/" + str(path))
        elif request.method == "PUT":
            appointment_data = await request.json()
            response = requests.put("http://localhost:8080/api/medical_office/appointments",
                                    json={
                                        "id_pacient": int(appointment_data['appointment']['id_doctor']),
                                        "id_doctor": int(decoded_data['sub']),
                                        "status": appointment_data['appointment']['status'],
                                        "data": appointment_data['appointment']['data']
                                    })
        # Check the response status code and handle accordingly
        if response.status_code == 200 or response.status_code == 201:
            # Check if the response contains the appointments list
            try:
                jsonR = json.loads(response.content)['_embedded']['appointmentList']
                return jsonR
            except KeyError:
                return {}
        elif response.status_code == 500:
            raise HTTPException(status_code=500, detail="Internal server error")
        elif response.status_code == 404:
            raise HTTPException(status_code=404, detail="Appointment not found")
        elif response.status_code == 409:
            raise HTTPException(status_code=409, detail="Appointment already exists")
        elif response.status_code == 422:
            raise HTTPException(status_code=422, detail=str(response.content.decode('utf-8')))
        else:
            raise Exception(f"Unexpected error: {response.status_code}")
    else:
        # Raise exception for unauthorized role
        raise HTTPException(status_code=401, detail="Unauthorized")


# Define an API endpoint for consultations
@app.api_route(path="/consultation/{path:path}", methods=["GET", "POST", "PUT"])
async def consultations(path: str, request: Request, authorization: str = Header(...)):
    # Call the function for verifying the authorization header and the token
    decoded_data = header_and_token_verification(authorization)

    # A patient can only see his consultations
    # A physician can only see his consultations and can create new ones
    if decoded_data['role'] == "patient" or decoded_data['role'] == "physician":
        if request.method == "GET" and path == "":
            if decoded_data['role'] == "patient":
                params = {"data": request.query_params.get("data"),
                          "id_doctor": request.query_params.get("id_doctor"),
                          "id_pacient": decoded_data['sub']}
            else:
                params = {"data": request.query_params.get("data"),
                          "id_doctor": decoded_data['sub'],
                          "id_pacient": request.query_params.get("id_doctor")}

            response = requests.request("GET", "http://localhost:8081/api/medical_office/consultation",
                                        params=params)
        elif request.method == "POST" and decoded_data['role'] == "physician":
            # Extract consultation data from the request
            consultation_data = await request.json()
            response = requests.request("POST", "http://localhost:8081/api/medical_office/consultation",
                                        json={
                                            "id_pacient": int(consultation_data['id_pacient']),
                                            "id_doctor": int(decoded_data['sub']),
                                            "data": consultation_data['data'],
                                            "diagnostic": consultation_data['diagnostic'],
                                            "investigatii": consultation_data['investigatii'],
                                        })
        elif request.method == "PUT" and decoded_data['role'] == "physician":
            consultation_data = await request.json()
            response = requests.request("PUT", "http://localhost:8081/api/medical_office/consultation",
                                        json={
                                            "id_pacient": int(consultation_data['id_pacient']),
                                            "id_doctor": int(decoded_data['sub']),
                                            "data": consultation_data['data'],
                                            "diagnostic": consultation_data['diagnostic'],
                                            "investigatii": consultation_data['investigatii'],
                                        })
            if response.status_code == 200:
                return "Consultation updated successfully"
            elif response.status_code in {404, 409, 422}:
                raise HTTPException(status_code=response.status_code, detail=str(response.content.decode('utf-8')))
            elif response.status_code == 500:
                raise HTTPException(status_code=500, detail="Internal server error")
            else:
                raise Exception(f"Unexpected error: {response.status_code}")
        else:
            raise HTTPException(status_code=405, detail="Method not allowed")

        # Check the response status code and handle accordingly
        if response.status_code == 201 or response.status_code == 200:
            return response.content
        elif response.status_code == 404:
            raise HTTPException(status_code=404, detail="Consultation not found")
        elif response.status_code == 500:
            raise HTTPException(status_code=500, detail="Internal server error")
        elif response.status_code in {409, 422}:
            raise HTTPException(status_code=response.status_code, detail=str(response.content.decode('utf-8')))
        else:
            raise Exception(f"Unexpected error: {response.status_code}")
    else:
        # Raise exception for unauthorized role
        raise HTTPException(status_code=401, detail="Unauthorized")


# Run the FastAPI app using UVicorn when this script is executed
if __name__ == '__main__':
    uvicorn.run(app, host="127.0.0.1", port=8000)
