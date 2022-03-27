import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;
    private String rootDir;
    private InetAddress ip;
    private ServerSocket sfd;
    private int backlog = 10;
    private ExecutorService executorService;

    public Server(int port, String rootDir) {
        this.port = port;
        this.rootDir = rootDir;
        executorService = Executors.newFixedThreadPool(10);
        try {
            ip = InetAddress.getLocalHost();
            sfd = new ServerSocket(port, backlog, ip);
        } catch(java.net.UnknownHostException e) {
            error("local host name could not be resolved into an address", e);
        } catch (java.io.IOException e) {
            error("I/O error occurred when opening the socket", e);
        } catch (IllegalArgumentException e) {
            error("Port number out of range, must be between 0 and 65535, inclusive", e);
        }
    }

    /**
     * Main thread listens for new connections. When a new connection is detected it creates a runnable that is then queued in the thread pool.
     * This allows us to handle multiple clients at the same time.
     * 
     */
    public void run() {
        System.out.println("Listening for connections on: " + ip.getHostAddress() + ":" + port);
        try {
            while (true) {
                Socket cfd = sfd.accept();
                executorService.execute(new Request(cfd, rootDir));
            }
        } catch (java.io.IOException e) {
            System.out.println("I/O error occurred when waiting for a connection");
        }
    }

    /**
     * Helper method to handle execptions. Prints the msg provided and the stack trace of the exception.
     * Then it exits the program.
     * 
     * @param msg is the error message 
     * @param e is the execption
     */
    private void error(String msg, Exception e) {
        System.out.println(msg);
        e.printStackTrace();
        System.exit(3);
    }
}
