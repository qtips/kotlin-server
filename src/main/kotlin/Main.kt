import java.io.InputStream
import java.net.ServerSocket

fun main() {
    setupServer(8080)
}

fun setupServer(port: Int) {
    val serverSocket = ServerSocket(port)
    while (true) {
        serverSocket.accept().use { socket ->
            println("accept!")
            val request: String = parseRequest(socket.getInputStream())
            println("request $request")

            //OUTPUT
            val out = socket.getOutputStream()
            val responsBody = "and some late stuff".toByteArray()
            val response = "HTTP/1.1 200 OK \r\n" +
                    "Content-Length: ${responsBody.size} \r\n" +
                    "\r\n"
            //end of header
            println("Responding: $response")
            out.write(response.toByteArray())
            println("responding with rest: ${String(responsBody)}")
            //responding with partial body
            out.write(responsBody.copyOfRange(0, 2))
            out.flush()
            Thread.sleep(5000)    // slow server
            out.write(responsBody.copyOfRange(2, responsBody.size))
            println("done")
        }
    }
}

private fun parseRequest(inputStream: InputStream): String {
    val it = inputStream.bufferedReader()
    val iterator = it.lines().iterator()
    val stringBuilder = StringBuilder()
    while (iterator.hasNext()) {
        val line = iterator.next()
        stringBuilder.appendLine(line)
        if (line.isNullOrEmpty()) break
    }
    return stringBuilder.toString()


}



