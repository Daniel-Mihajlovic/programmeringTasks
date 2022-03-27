import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        String order;
        String path = args[0];
        if(args.length < 2)
            order = "asc";
        else
            order = checkOrderArg(args[1]);

        if(order.equals("")) {
            System.out.println("Order argument is not asc or desc");
            return;
        }

        sort(path, order);
    }


    /**
     * This method takes a path and a sorting order. 
     * It then prints the content of the file in the correct order ignoring lines that are not numbers.
     * This is done by opening the file as a stream of strings, filter out all lines that are not numbers 
     * and then sort after desired comparator
     * 
     * @param path is the path to the file to open
     * @param order decieds if the method should sort in ascending or descending order
     */
    private static void sort(String path, String order) {
        try {
            Stream<String> lines = Files.lines(Path.of(path));
            lines.filter(s -> isNumber(s)).sorted(comp(order)).forEach(System.out::println);
            lines.close();
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }

    /**
     * Creates comparator with desired ordering
     * 
     * @param order is the order to sort the file after.
     * @return a comparator fulfilling the sorting order
     */
    private static Comparator<String> comp(String order) {
        if (order.equalsIgnoreCase("asc"))
            return Comparator.naturalOrder();

        return Comparator.reverseOrder();
    }

    /**
     * Checks if the string is a number
     * 
     * @param s is the string we want to check
     * @return true if number, else false
     */
    private static boolean isNumber(String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the order arg provided is correct.
     * 
     * @param order the order to sort after
     * @return string with correct order or empty string if param is invalid
     */
    private static String checkOrderArg(String order) {
        if(order.equalsIgnoreCase("asc"))
            return order;
        if(order.equalsIgnoreCase("desc"))
            return order;

        return "";
    }
}
