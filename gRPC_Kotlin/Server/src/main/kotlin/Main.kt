import io.grpc.Server
import io.grpc.ServerBuilder
import services.AuthenticationImpl
import java.io.IOException
import java.sql.Connection
import java.sql.DriverManager

fun main(args: Array<String>) {
    val url = "jdbc:mariadb://localhost:3306/pos"
    val user = "root"
    val password = "password"
    val connection: Connection = DriverManager.getConnection(url, user, password)

    try{
       val server : Server = ServerBuilder.forPort(50051)
            .addService(AuthenticationImpl(connection))
            .build()
        server.start()
        println("Server started at ${server.port}")
        server.awaitTermination()
    }catch (e: IOException){
        println("Exception: " + e.message)
    }catch (e: InterruptedException){
        println("Exception: " + e.message)
    }
    catch (e: Exception){
        println("Exception: " + e.message)
    }
    connection.close()
}