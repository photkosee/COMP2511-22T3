package splitter;

import java.util.Scanner;

/**
 * Splitter
 * @author Phot Koseekrainiramon (z5387411)
 *
 */
public class Splitter {

    public static void main(String[] args) {
        System.out.println("Enter a sentence specified by spaces only: ");
        Scanner scanner = new Scanner(System.in);
        String[] inputs = scanner.nextLine().split(" ");
        for (int i = 0; i < inputs.length; i++) {
            System.out.println(inputs[i]);
        }
        scanner.close();
    }
}
