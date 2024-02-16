package services

import com.grpc.proto.Authentication
import com.grpc.proto.AuthenticationServiceGrpc
import io.grpc.stub.StreamObserver
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.impl.TextCodec
import java.sql.Connection
import java.sql.PreparedStatement
import java.time.Instant
import java.util.*

class AuthenticationImpl(
    private val connection: Connection
) : AuthenticationServiceGrpc.AuthenticationServiceImplBase() {
    private val tokens : MutableList<String> = mutableListOf()
    private val expirationTime : Long = 3600 // in seconds
    private val secretKey : String = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E="

    override fun registerUser(
        request: Authentication.RegistrationRequest,
        responseObserver: StreamObserver<Authentication.RegistrationResponse>
    ) {
        // validate the input
        // the username must be between 7 and 14 characters long and can only contain letters, numbers and underscores
        // the password must be between 8 and 20 characters long and must contain at least one letter and one number
        // and can only contain letters, numbers and the following special characters: !@#$%^*()_+{}|"<>?`
        if (request.username == "" || request.password == "" || request.role == "" ||
            !request.username.matches(Regex("^[a-zA-Z0-9._]{7,14}\$")) ||
            !request.password.matches(Regex("^(?=[^a-z]*[a-z])(?=\\D*\\d)[^:&~\\s]{8,20}\$"))
        ) {
            responseObserver.onNext(Authentication.RegistrationResponse.newBuilder().setUid(-2).build())
            responseObserver.onCompleted()
            return
        }

        // verify if the username is already taken
        val existingUserQuery = "SELECT COUNT(*) FROM users WHERE username = ?"
        val statement = connection.prepareStatement(existingUserQuery)
        statement.setString(1, request.username)
        val resultSet = statement.executeQuery()

        if (resultSet.next()  && resultSet.getInt(1) > 0) {
            //return false, meaning the username is already taken
            responseObserver.onNext(Authentication.RegistrationResponse.newBuilder().setUid(-1).build())
            responseObserver.onCompleted()
            return
        }

        // if the username is not taken, add the user to the list
        val insertUserQuery = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)"
        val preparedStatement: PreparedStatement = connection.prepareStatement(insertUserQuery)
        preparedStatement.setString(1, request.username)
        preparedStatement.setString(2, request.password)
        preparedStatement.setString(3, request.role)
        preparedStatement.executeQuery()

        val findTheUid = "SELECT uid FROM users WHERE username = ?"
        val statementUid = connection.prepareStatement(findTheUid)
        statementUid.setString(1, request.username)
        val resultSetUid = statementUid.executeQuery()

        if(!resultSetUid.next() && resultSetUid.getInt(1) < 0){
            responseObserver.onNext(Authentication.RegistrationResponse.newBuilder().setUid(-1).build())
            responseObserver.onCompleted()
            return
        }

        // return true, meaning the registration was successful
        responseObserver.onNext(Authentication.RegistrationResponse.newBuilder().setUid(resultSetUid.getInt(1)).build())
        responseObserver.onCompleted()
    }

    override fun createToken(
        request: Authentication.AuthenticationRequest,
        responseObserver: StreamObserver<Authentication.Token>
    ) {
        // verify if the user exists
        val getUserQuery = "SELECT uid, role FROM users WHERE username = ? AND password = ?"
        val statement = connection.prepareStatement(getUserQuery)
        statement.setString(1, request.username)
        statement.setString(2, request.password)
        val user = statement.executeQuery()

        // if the user doesn't exist, return an empty token
        if (!user.next()) {
            responseObserver.onNext(Authentication.Token.newBuilder().setValue("").build())
            responseObserver.onCompleted()
            return
        }

        // if the user exists, create a token for him
        val jws: String = Jwts.builder()
            .claim("role", user.getString("role"))
            .setSubject(user.getString("uid"))
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.now().plusSeconds(expirationTime)))
            .signWith(
                SignatureAlgorithm.HS256,
                TextCodec.BASE64.decode(secretKey)
            )
            .compact()

        // if the token already exists, return an empty token
        if (tokens.contains(jws)) {
            responseObserver.onNext(Authentication.Token.newBuilder().setValue("").build())
            responseObserver.onCompleted()
            return
        }

        // if the token doesn't exist, add it to the list
        tokens.add(jws)

        responseObserver.onNext(Authentication.Token.newBuilder().setValue(jws).build())
        responseObserver.onCompleted()
    }

    override fun verifyToken(
        request: Authentication.Token,
        responseObserver: StreamObserver<Authentication.BoolValue>
    ) {
        // verify if the token is valid
        // if the token is empty, return false
        if(request.value == ""){
            responseObserver.onNext(Authentication.BoolValue.newBuilder().setValue(false).build())
            responseObserver.onCompleted()
            return
        }

        // if the token exists in list, verify if it's expired
        if (tokens.contains(request.value)) {
            val tokenOnly = request.value.substring(0, request.value.lastIndexOf('.') + 1)
            val expiration = (Jwts.parser().parse(tokenOnly).body as Claims).expiration

            if (expiration.before(Date.from(Instant.now()))) {
                // if the token is expired, remove it from the list
                tokens.remove(request.value)

                responseObserver.onNext(Authentication.BoolValue.newBuilder().setValue(false).build())
                responseObserver.onCompleted()
                return
            }
        }
        else { // if the token doesn't exist in list, return false
            responseObserver.onNext(Authentication.BoolValue.newBuilder().setValue(false).build())
            responseObserver.onCompleted()
            return
        }

        // if the token is valid, return true
        responseObserver.onNext(Authentication.BoolValue.newBuilder().setValue(true).build())
        responseObserver.onCompleted()
    }

    override fun destroyToken(
        request: Authentication.Token,
        responseObserver: StreamObserver<Authentication.BoolValue>
    ) {
        // if the token was already destroyed, return false
        if (!tokens.contains(request.value)) {
            responseObserver.onNext(Authentication.BoolValue.newBuilder().setValue(false).build())
            responseObserver.onCompleted()
            return
        }

        // otherwise, remove the token from the list
        tokens.remove(request.value)

        responseObserver.onNext(Authentication.BoolValue.newBuilder().setValue(true).build())
        responseObserver.onCompleted()
    }

    override fun updateUser(
        request: Authentication.UpdateRequest,
        responseObserver: StreamObserver<Authentication.UpdateResponse>
    ) {
        // verify if the user exists
        var getUserQuery = "SELECT * FROM users WHERE uid = ?"
        var statement = connection.prepareStatement(getUserQuery)
        statement.setInt(1, request.uid)
        val user = statement.executeQuery()

        if (!user.next()) {
            responseObserver.onNext(Authentication.UpdateResponse.newBuilder().setMessage("User not found.").build())
            responseObserver.onCompleted()
            return
        }

        // verify if the user entered the old password and at least one new field
        if (request.oldPassword == "" || (request.newPassword == "" && request.newUsername == "")){
            responseObserver.onNext(Authentication.UpdateResponse.newBuilder().setMessage("You must enter the old password and at least one new field.").build())
            responseObserver.onCompleted()
            return
        }

        // verify if the user entered the correct password for confirmation
        if (user.getString("password") != request.oldPassword ){
            responseObserver.onNext(Authentication.UpdateResponse.newBuilder().setMessage("You entered the wrong password.").build())
            responseObserver.onCompleted()
            return
        }

        // verify if the user want to change his password to the same one
        if (request.newPassword == request.oldPassword){
            responseObserver.onNext(Authentication.UpdateResponse.newBuilder().setMessage("The new password is the same as the old one.").build())
            responseObserver.onCompleted()
            return
        }

        // verify if the user want to change his username to the same one
        if (request.newUsername == user.getString("username")){
            responseObserver.onNext(Authentication.UpdateResponse.newBuilder().setMessage("The new username is the same as the old one.").build())
            responseObserver.onCompleted()
            return
        }

        // verify what the user want to change, empty string means that the user doesn't want to change that
        var updateUsername = false
        var updatePassword = false
        if (request.newUsername != ""){
            updateUsername = true
        }
        if (request.newPassword != ""){
            updatePassword = true
        }

        // verify if the new fields are in the correct format
        if (updatePassword && !request.newPassword.matches(Regex("^(?=[^a-z]*[a-z])(?=\\D*\\d)[^:&~\\s]{8,20}\$"))){
            responseObserver.onNext(Authentication.UpdateResponse.newBuilder().setMessage("Invalid format of password").build())
            responseObserver.onCompleted()
            return
        }

        if (updateUsername && !request.newUsername.matches(Regex("^[a-zA-Z0-9_.]{7,14}\$"))){
            responseObserver.onNext(Authentication.UpdateResponse.newBuilder().setMessage("Invalid format of username.").build())
            responseObserver.onCompleted()
            return
        }


        // verify if the new username is already taken
        if (updateUsername){
            getUserQuery = "SELECT * FROM users WHERE username = ?"
            statement = connection.prepareStatement(getUserQuery)
            statement.setString(1, request.newUsername)
            val userN = statement.executeQuery()

            if (userN.next()) {
                responseObserver.onNext(Authentication.UpdateResponse.newBuilder().setMessage("Username already exist.").build())
                responseObserver.onCompleted()
                return
            }
        }

        // if all above is true, update the user
        if (updatePassword){
            val updateUserQuery = "UPDATE users SET password = ? WHERE uid = ?"
            statement = connection.prepareStatement(updateUserQuery)
            statement.setString(1, request.newPassword)
            statement.setInt(2, request.uid)
            statement.executeQuery()
        }

        if (updateUsername){
            val updateUserQuery = "UPDATE users SET username = ? WHERE uid = ?"
            statement = connection.prepareStatement(updateUserQuery)
            statement.setString(1, request.newUsername)
            statement.setInt(2, request.uid)
            statement.executeQuery()
        }

        responseObserver.onNext(Authentication.UpdateResponse.newBuilder().setMessage("Successful Updated!").build())
        responseObserver.onCompleted()
        return
    }

    override fun deleteUser(
        request: Authentication.DeleteRequest,
        responseObserver: StreamObserver<Authentication.DeleteResponse>
    ) {
        deleteUser(request.uid)
        responseObserver.onNext(Authentication.DeleteResponse.newBuilder().setValue(1).build())
        responseObserver.onCompleted()
    }

    private fun deleteUser(id: Int){
        val deleteUserQuery = "DELETE FROM users WHERE uid = ?"
        val statement = connection.prepareStatement(deleteUserQuery)
        statement.setInt(1, id)
        statement.executeQuery()
    }
}