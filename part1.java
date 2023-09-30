/* CSC-207-01 Spring 2023
 * Names: Connor Durkin, Timur Kasimov
 * Final AI Lab 
 * Acknowledgements: previous labs, textbook, prof. eliott
 * 
 * PART 1: 
 *  This program gives user the menu option to create 4 different types of networks
 *  Two of the networks are predefined from BARABASI examples
 *  One network lets the user pick a number of nodes and the probability
 *      of connection between any two nodes
 *  The last network takes the shape of a lattice, where each node has exactly 4 connections
 * 
 * 
 *  All networks are written to a respective text file in the following format
 *  1 3 // 1 and 3 connected
 *  1 5 //1 and 5 connected
 *  ...
 *  4 -1 //4 is not connected to anything
 * 
 */

import java.io.PrintWriter;
import java.util.*;
import java.math.*;

public class part1 {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in); // to get user input

        // To print the menu instructions
        System.out.println("\nWelcome to Network Grower");
        System.out.println("This program allows you to grow various networks.");
        System.out.println("See the menu below for options.");

        int choice = 0; // game selected by the user

        // keep printing the menu until the user exits the program
        while (choice != 5) // when choice=5 exits the program
        {
            // program menu
            System.out.println();
            System.out.println("MENU:");
            System.out.println();
            System.out.println("\t1. Network 1, Example 1 from (BARABÁSI 2014)");
            System.out.println("\t2. Network 1, Example 2 from (BARABÁSI 2014)");
            System.out.println("\t3. A random network");
            System.out.println("\t4. A 2D4N regular lattice");
            System.out.println("\t5. Exit.");
            System.out.println();
            System.out.println("Type in a number 1 through 4 to choose a type of network to generate.");
            System.out.println("Type in 5 to exit the program.");

            // reading user input
            choice = in.nextInt();

            // chossing from menu options
            switch (choice) {
                case 1: // Example 1
                    System.out.println("Network 1, Example 1 from (BARABÁSI 2014)"); // printing user's choice

                    try {
                        // creating PrintWriter object
                        PrintWriter outExample1 = new PrintWriter("Example_1.txt");

                        // hand-coding the output line by line, since the network was already created
                        outExample1.println("1 4"); // connected nodes
                        outExample1.println("1 7");
                        outExample1.println("1 10");
                        outExample1.println("2 7");
                        outExample1.println("2 9");
                        outExample1.println("2 11");
                        outExample1.println("4 5");
                        outExample1.println("8 10");
                        outExample1.println("9 10");
                        outExample1.println("9 12");

                        outExample1.println("3 -1"); // unconnected nodes go at the end
                        outExample1.println("6 -1");

                        outExample1.close();// close the file after writing to it

                        // notify the user the file was created
                        System.out.println("File Example_1 was created with appropriate information written to it.");

                    } catch (Exception e) {
                        e.getStackTrace();
                        System.out.println("Error writing to file"); // notify user if smth goes wrong
                    }

                    break;

                case 2: // Example 2
                    System.out.println("Network 1, Example 2 from (BARABÁSI 2014)");

                    try {

                        PrintWriter outExample2 = new PrintWriter("Example_2.txt");

                        outExample2.println("1 3");
                        outExample2.println("1 11");
                        outExample2.println("2 11");
                        outExample2.println("3 8");
                        outExample2.println("5 9");
                        outExample2.println("5 10");
                        outExample2.println("6 11");
                        outExample2.println("6 12");
                        outExample2.println("8 9");
                        outExample2.println("9 10");

                        outExample2.println("4 -1");
                        outExample2.println("7 -1");

                        outExample2.close();
                        System.out.println("File Example_2 was created with appropriate information written to it.");

                    } catch (Exception e) {
                        e.getStackTrace();
                        System.out.println("Erorr writing to file");
                    }

                    break;

                case 3: // random network
                    System.out.println("A random network\n");
                    // specifying the number of nodes

                    int numberOfNodes = 0;
                    // use the while loop to keep prompting the user to provide the
                    // number of nodes in the proper range
                    while (numberOfNodes < 12 || numberOfNodes > 1000) {
                        System.out.println("How many nodes the network should have? (Must be between 12 and 1000)");
                        numberOfNodes = in.nextInt();
                    }

                    // specifying the probability of connection between 1/numberOfNodes and 1
                    double probability = 0.0;
                    while (probability < (1.0 / numberOfNodes) || probability > 1.0) {
                        System.out.println(
                                "What is the probability of connection between any two nodes? (Must be a double between "
                                        + 1.0 / numberOfNodes + " and 1.0)");

                        probability = in.nextDouble();
                    }

                    double randomDouble = 0.0;

                    boolean isConnected[] = new boolean[numberOfNodes]; // create an array to keep track of connected
                                                                        // nodes. initialized to false by default

                    try {

                        PrintWriter outRandom = new PrintWriter("Random.txt");

                        for (int i = 1; i <= numberOfNodes; i++) {
                            for (int j = i + 1; j <= numberOfNodes; j++) { // double for loop to combine all pairs of
                                                                           // nodes without duplicates
                                randomDouble = Math.random(); // randomly selecting probability

                                // nodes are connected if generated number is within the probability of
                                // connection
                                if (randomDouble < probability) {
                                    outRandom.println(i + " " + j); // printing to text file
                                    isConnected[i - 1] = true; // marking both nodes as connected in the array created
                                                               // above
                                    isConnected[j - 1] = true;
                                }
                            }
                        }
                        // for any nodes that are not connected,
                        // print the number of node with -1
                        for (int i = 1; i <= numberOfNodes; i++) {
                            if (isConnected[i - 1] == false) {
                                outRandom.println(i + " -1");
                            }
                        }

                        outRandom.close();
                        // System.out.println(Arrays.toString(isConnected)); //testing

                        System.out.println("File Random.txt was created with appropriate information written to it.");

                    } catch (Exception e) {
                        e.getStackTrace();
                        System.out.println("Erorr writing to file");
                    }

                    break;

                case 4: // 2D4N lattice network
                    System.out.println("2D4N network");

                    int numberOfAgents = 0;
                    // while loop until the user provides the right number of agents
                    while (numberOfAgents < 9 || numberOfAgents > 1000) {
                        System.out.println("How many nodes the network should have? (must be between 9 and 1000)");
                        System.out.println(
                                "If the number is not divisble by 3, it will be rounded to the nearest number that is.");
                        numberOfAgents = in.nextInt();
                    }

                    // check if numberOfAgents is divisible by 3, if not, round to a number that is
                    int numberOfRows = (int) Math.round(((double) numberOfAgents) / 3);
                    // let the user know the final number of agents
                    System.out.println("Generating the 2D4N network with " + numberOfRows * 3 + " nodes.");

                    // creating the network with rows based on user input and 3 columns
                    int numberOfColumns = 3;
                    int network[][] = new int[numberOfRows][numberOfColumns];
                    for (int r = 0; r < numberOfRows; r++) { // i variable for rows
                        for (int c = 0; c < numberOfColumns; c++) { // k variable for columns
                            network[r][c] = (c + 1) + numberOfColumns * r;
                        }
                    }

                    // connecting the nodes
                    try {

                        PrintWriter out2D4N = new PrintWriter("2D4N.txt");

                        for (int r = 0; r < numberOfRows; r++) { // i variable for rows
                            for (int c = 0; c < numberOfColumns; c++) { // k variable for columns

                                // right
                                if (c != numberOfColumns - 1) { // if not in the last column
                                    out2D4N.println(network[r][c] + " " + network[r][c + 1]); // make connection to the
                                                                                              // right
                                }

                                // left
                                if (c == 0) { // ONLY if in the first column
                                    out2D4N.println(network[r][c] + " " + network[r][numberOfColumns - 1]);
                                }

                                // down
                                if (r != numberOfRows - 1) { // if not in the last row
                                    out2D4N.println(network[r][c] + " " + network[r + 1][c]); // make connection to down
                                }

                                // up
                                if (r == 0) { // ONLY if in the first row
                                    out2D4N.println(network[r][c] + " " + network[numberOfRows - 1][c]);
                                }

                            }
                        }
                        out2D4N.close();

                    } catch (Exception e) {
                        e.getStackTrace();
                        System.out.println("Erorr writing to file");
                    }

                    break;

                case 5: // exiting the program
                    System.out.println("Thank you for using this program to generate your networks");
                    break;

                default: // unknown input
                    System.out.println("Type in an integer between 1 and 5");
                    break;
            }
        }
    }
}