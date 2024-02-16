import com.grpc.proto.Authentication
import com.grpc.proto.AuthenticationServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder

fun main(args: Array<String>) {
    val channel : ManagedChannel = ManagedChannelBuilder.forAddress("localhost", 50051)
        .usePlaintext()
        .build()

    val stub = AuthenticationServiceGrpc.newBlockingStub(channel)

    // test the registration function
    println("\nREGISTRATION TEST:")
    registrationTest(stub)

    // get the token for the user
    println("\nTOKEN TEST:")
    val token1 = createTokenTest(stub, "mari_a123", "parolaMariei123")
    createTokenTest(stub, "mari_a123", "parolaMariei123")
    val token3 = createTokenTest(stub, "angfggggg976", "parolaAncai976")
    val token4 = createTokenTest(stub, "ancaD_p976", "parolaAncai976")


    // test the token validation AND destruction functions
    println("\nVALIDATION AND DESTRUCTION TEST:")
    verifyTokenTest(stub, token1) // valid token
    verifyTokenTest(stub, token3) // invalid token
    destroyTokenTest(stub, token1) // destroy the valid token
    verifyTokenTest(stub, token1) // invalid token
    verifyTokenTest(stub, token4) // valid token

    Thread.sleep(5000) // wait for the token to expire

    verifyTokenTest(stub, token4) // invalid token
    destroyTokenTest(stub, token4) // destroy the invalid token

    channel.shutdown()
}

fun registrationTest(stub: AuthenticationServiceGrpc.AuthenticationServiceBlockingStub){
    // valid registration
    val response = stub.registerUser(Authentication.RegistrationRequest.newBuilder()
        .setUsername("mari_a123")
        .setPassword("parolaMariei123")
        .setRole("patient")
        .build())

    // invalid registration
    val response2 = stub.registerUser(Authentication.RegistrationRequest.newBuilder()
        .setUsername("mari_a123")
        .setPassword("parolaMarieiei")
        .setRole("physician")
        .build())

    // valid registration
    val response3 = stub.registerUser(Authentication.RegistrationRequest.newBuilder()
        .setUsername("ancaD_p976")
        .setPassword("parolaAncai976")
        .setRole("physician")
        .build())

    // valid registration
    val response4 = stub.registerUser(Authentication.RegistrationRequest.newBuilder()
        .setUsername("adm_inaD_00")
        .setPassword("parolaAdminei00")
        .setRole("admin")
        .build())

    // valid registration
    val response5 = stub.registerUser(Authentication.RegistrationRequest.newBuilder()
        .setUsername("ilincaP234")
        .setPassword("parolaIlincai981")
        .setRole("patient")
        .build())

    // invalid registration
    val response6 = stub.registerUser(Authentication.RegistrationRequest.newBuilder()
        .setUsername("ili")
        .setPassword("parolaMea1234")
        .setRole("patient")
        .build())

    // invalid registration
    val response7 = stub.registerUser(Authentication.RegistrationRequest.newBuilder()
        .setUsername("usernameBun1234")
        .setPassword("parolaincorecta")
        .setRole("patient")
        .build())

    verify(response)
    verify(response2)
    verify(response3)
    verify(response4)
    verify(response5)
    verify(response6)
    verify(response7)
}

fun verify (response: Authentication.BoolValue){
    if (response.value) {
        println("Successful registration.")
    } else {
        println("This user already exists.")
    }
}

fun createTokenTest(stub: AuthenticationServiceGrpc.AuthenticationServiceBlockingStub, username: String, password: String) : String{
    val token = stub.createToken(Authentication.AuthenticationRequest.newBuilder()
        .setUsername(username)
        .setPassword(password)
        .build())

    if (token.value == "") {
        println("This user ($username) doesn't exist or the token already exists.")
        return ""
    }

    println("Token created for $username.")
    return token.value
}

fun verifyTokenTest(stub: AuthenticationServiceGrpc.AuthenticationServiceBlockingStub, token: String){
    val response = stub.verifyToken(Authentication.Token.newBuilder()
        .setValue(token)
        .build())

    if (response.value) {
        println("Valid token.")
    } else {
        println("Invalid token.")
    }
}

fun destroyTokenTest(stub: AuthenticationServiceGrpc.AuthenticationServiceBlockingStub, token: String){
    val response = stub.destroyToken(Authentication.Token.newBuilder()
        .setValue(token)
        .build())

    if (response.value) {
        println("Token was destroyed.")
    } else {
        println("Token doesn't exist.")
    }
}