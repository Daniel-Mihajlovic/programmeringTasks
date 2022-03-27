public class Main {
    public static void main(String[] args) {
        if(args.length < 2) {
            System.out.println("not enough args provided");
            return;
        }

        int port = convertToInt(args[0]);
        String root = args[1];

        Server server = new Server(port, root);
        server.run();
    }

    /**
     * Converts provided port to int. Exits program if port provided is not a number.
     * 
     * @param s string to convert
     * @return converted string.
     */
    private static int convertToInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("port is not a number");
            System.exit(0);
        }

        return 0;
    }
}
