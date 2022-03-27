import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Main {
    public static void main(String[] args) {
        try {
            int N = Integer.parseInt(args[0]);
            String path = args[1];
            generateFile(N, path);
        } catch (NumberFormatException e) {
            System.out.println("N argument is not a number");
            System.exit(3);
        }
    }

/**
 * Creates or overwrites existing file specified by the path arg.  This file contains N random numbers on separate lines.
 * 
 * @param N the amount of numbers to generate
 * @param path the path of the file to be created or overwritten
 */

    private static void generateFile(int N, String path) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(generateRandom(N));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a string containing positive random longs. This is done by creating an infinite stream of random longs.
     * This is then filtered to only contain positive numbers. To make it also use negative numbers just remove the filter in the stream. 
     * Then we limit the stream to N numbers and convert it to a string where we join all the elements with a new line.
     * 
     * @param N the amount of numbers to generate
     * @return a string containing N random numbers on seperate lines
     */    
    private static String generateRandom(int N) {
        LongStream rand = new Random().longs();
        return  rand.filter(r -> r > 0).
                limit(N).
                mapToObj(Long::toString).
                collect(Collectors.joining("\n"));
    }
}
