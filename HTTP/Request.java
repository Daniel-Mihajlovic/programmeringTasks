import java.io.*;
import java.net.Socket;

public class Request implements Runnable {
    private Socket cfd;
    private String rootDir;

    public Request(Socket cfd, String rootDir) {
        this.cfd = cfd;
        this.rootDir = rootDir;
    }


    /**
     * How the thread should handle the client connection/request
     */
    public void run() {
        try {
            PrintWriter out = new PrintWriter(cfd.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(cfd.getInputStream()));

            String request = getRequest(in).split(" ")[1].substring(1);
            cfd.shutdownInput();

            sendResponse(out, request);

            out.close();
            in.close();
            cfd.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Creates a simple http header
     * 
     * @param statusCode the code of the header
     * @param statusMessage the status message of the header
     * @param msgLength the length of the message the the server is sending
     * @return
     */
    private String createHTTPHeader(int statusCode, String statusMessage, long msgLength) {
        StringBuilder http = new StringBuilder();
        http.append("HTTP/1.1 " + statusCode + " " + statusMessage + "\n");
        http.append("Server: localhost\n");
        http.append("Content-Type: text/html;charset=UTF-8\n");
        http.append("Content-Length: " + msgLength + "\r\n");
        return http.toString();
    }

    /**
     * Reads the first line of the GET request
     * 
     * @param in the clients input stream
     * @return requested file path
     */
    private String getRequest(BufferedReader in) {
        try {
            return in.readLine();
        } catch (java.io.IOException e) {
            System.out.println("could not read request");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Repsonses to the clients request. Will return the html file if it exists, else 404 will be sent
     * 
     * @param out the clients output strea,
     * @param file the file to send
     */
    private void sendResponse(PrintWriter out, String file) {
        String line;
        File index = new File(rootDir + file);
        System.out.println(rootDir + file);
        try {
            BufferedReader html = new BufferedReader(new FileReader(index));
            out.println(createHTTPHeader(200, "OK", index.length()));
            while ((line = html.readLine()) != null) {
                out.println(line);
                out.flush();
            }
            html.close();
        } catch (java.io.FileNotFoundException e) {
            String error = "404 FILE NOT FOUND";
            out.println(createHTTPHeader(404, "File not found", error.length()));
            out.println(error);
            out.flush();
        } catch (IOException e) {
            System.out.println("error while sending page");
            e.printStackTrace();
        }
    }
}
