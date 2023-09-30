/* CSC-207-01 Spring 2023
 * Names: Connor Durkin, Timur Kasimov
 * Final AI Lab 
 * Acknowledgements: previous labs, textbook, prof. eliott
 * 
 * PART 2: 
 *  This program runs a modified version of the prisoner's dilemma game. 
 * There is a user menu available to choose 6 pre-defined and 7th custom modes of
 * the game to play/simulate. */

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.tools.StandardJavaFileManager;

public class part2 {
    public static void main(String[] args) throws IOException {

        Scanner in = new Scanner(System.in); // to get user input

        // To print the menu instructions
        System.out.println("\nWelcome to Prisoner Dilemma Game Simulator");
        System.out.println("This program allows you to use networks to play the game.");
        System.out.println("See the menu below for options.");

        int choice = 0; // game selected by the user

        // to store user provided parameters in case 7
        int userDefector;
        double userThreshold;
        double userImitationProbability;

        // keep printing the menu until the user exits the program
        while (choice != 8) // when choice=8 exits the program
        {
            // program menu
            System.out.println();
            System.out.println("MENU:");
            System.out.println();
            System.out.println("H = the ID of the initial defector agent");
            System.out.println("T = agent elimination threshold ");
            System.out.println("M = the probability of imitating a neighor's strategy");
            System.out.println("k = initial number of neighbours of an agent");
            System.out.println();
            System.out.println("\t1. Network 1, H = agent 5, T = 1/k, M = 5%.");
            System.out.println("\t2. Network 2, H = agent 11, T = 1/k, M = 10%.");
            System.out.println("\t3. A random network with N = 1000, p = 0.15, H = 500, T = 1/k, M = 3%.");
            System.out.println("\t4. A random network with N = 1000, p = 0.3, H = 500, T = 1/k, M = 10%.");
            System.out.println("\t5. A 2D4N lattice with N = 999, H = 500, T = 0.5, M = 3%.");
            System.out.println("\t6. A 2D4N lattice with N = 999, H = 500, T = 0.75, M = 3%.");
            System.out
                    .println("\t7. A 2D4N lattice with N = 999. The user may define H [1, N], T [0, 1], and M [0, 1].");
            System.out.println("\t8. Exit.");
            System.out.println();
            System.out.println("Type in a number 1 through 7 to choose parameters for the game.");
            System.out.println("Type in 8 to exit the program.");

            // reading user input
            choice = in.nextInt();

            // chossing from menu options
            switch (choice) {
                case 1:
                    // create new environment object
                    Environment game1 = new Environment(5, 0.05, "Example_1.txt");

                    // setup the game
                    node[] nodes1 = game1.setupGame();

                    // play the game
                    game1.playGame(nodes1);

                    break;
                case 2:
                    Environment game2 = new Environment(11, 0.10, "Example_2.txt");
                    node[] nodes2 = game2.setupGame();
                    game2.playGame(nodes2);
                    break;

                case 3:
                    Environment game3 = new Environment(500, 0.03, "Random1.txt");
                    node[] nodes3 = game3.setupGame();
                    game3.playGame(nodes3);
                    break;

                case 4:
                    Environment game4 = new Environment(500, 0.10, "Random2.txt");
                    node[] nodes4 = game4.setupGame();
                    game4.playGame(nodes4);
                    break;

                case 5:
                    // use of different constructor with a shared threshold
                    Environment game5 = new Environment(500, 0.03, "2D4N.txt", 0.5);
                    node[] nodes5 = game5.setupGame();
                    game5.playGame(nodes5);
                    break;

                case 6:
                    Environment game6 = new Environment(500, 0.03, "2D4N.txt", 0.75);
                    node[] nodes6 = game6.setupGame();
                    game6.playGame(nodes6);
                    break;

                case 7: // custom case

                    // setup scanner for user input
                    Scanner value = new Scanner(System.in);

                    // obtain user input and check for range
                    // initial defector
                    do {
                        System.out.println("Provide initial defector agent ID as an integer [1, 999]");
                        userDefector = value.nextInt();
                    } while ((userDefector < 1) || (userDefector > 999));

                    // custom threshold of elimination
                    do {
                        System.out.println("Provide threshold as a double [0, 1]");
                        userThreshold = value.nextDouble();
                    } while ((userThreshold > 1.0) || (userThreshold < 0.0));

                    // custom probability of imitating strategies
                    do {
                        System.out.println("Provide the imitation probability chance as a double [0, 1]");
                        userImitationProbability = value.nextDouble();
                    } while ((userImitationProbability > 1.0) || (userImitationProbability < 0.0));

                    Environment game7 = new Environment(userDefector, userImitationProbability, "2D4N.txt",
                            userThreshold);
                    node[] nodes7 = game7.setupGame();
                    game7.playGame(nodes7);
                    break;

                case 8: // exit the program
                    System.out.println("Thank you for using the program!");
                    in.close(); // close the scanner meant for menu options
                    break;

                default: // unknown input, prompt menu again
                    System.out.println("Type in an integer between 1 and 8");
                    break;
            }
        }
    }
}
