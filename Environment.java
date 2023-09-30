import java.io.*;
import java.util.*;

public class Environment {

    // INSTANCE FIELDS
    private int H; // initial defector agent ID
    private double M; // probability of imitation
    private String directory; // file directory
    private boolean isThresholdShared;
    private double sharedThreshold;
    // linked list for keeping track of existing connections
    private LinkedList<IntPair> pairs = new LinkedList<>();
    private static int finalNumberOfNodes;

    // CONSTRUCTOR 1
    public Environment(int H, double M, String fileName) {
        this.H = H;
        this.M = M;
        this.directory = (System.getProperty("user.dir") + "/" + fileName);
        this.isThresholdShared = false;
    }

    // CONSTRUCTOR 2
    public Environment(int H, double M, String fileName, double sharedThreshold) {
        this.H = H;
        this.M = M;
        this.directory = (System.getProperty("user.dir") + "/" + fileName);
        this.isThresholdShared = true;
        this.sharedThreshold = sharedThreshold;

    }

    // METHODS

    // setupGame creates initial data structures to store all information needed to
    // run the prisoner's dilemma game
    // Pre-cond: none
    // Post-cond: returns an array of nodes
    public node[] setupGame() throws IOException {
        // creating a file object
        File fileReader = new File(directory);

        // System.out.println((fileReader)); //testing

        // creating a scanner from the file object to read the file
        Scanner fileIn = new Scanner(fileReader);

        int firstNode; // to read through the file
        int secondNode;

        int numberOfNodes = 0; // to evaluate the total number of nodes

        // to store pairs of integers from text file into linked list
        // and to find out the total number of nodes
        while (fileIn.hasNextInt()) {

            firstNode = fileIn.nextInt();
            secondNode = fileIn.nextInt();

            if (secondNode != -1) { // only record the connected pairs, ignore unconnected
                pairs.add(new IntPair(firstNode, secondNode));
            }

            // finding the total number of nodes
            if (numberOfNodes < firstNode)
                numberOfNodes = firstNode;

            if (numberOfNodes < secondNode)
                numberOfNodes = secondNode;

        }
        // assign initial value to final # of nodes; will be decremented as nodes are
        // eliminated
        finalNumberOfNodes = numberOfNodes;

        fileIn.close(); // close the scanner object

        // testing
        /*
         * System.out.println(pairs);
         * System.out.println(numberOfNodes);
         * System.out.println("Testing END");
         */

        // creating and populating the array of nodes of size equal to the total number
        // of nodes
        node[] nodes = new node[numberOfNodes];

        for (int i = 0; i < numberOfNodes; i++) {
            nodes[i] = new node(i + 1);
        }

        // recording each node's neighbours and number of neighbours
        for (IntPair pair : pairs) {
            node.recordNeighbours(nodes[pair.getFirst() - 1], nodes[pair.getSecond() - 1]);
        }

        // set unconnected agents to dead
        for (node n : nodes) {
            if (n.getInitialNumberOfNeighbours() == 0) {
                n.setDeath(); // set dead
                finalNumberOfNodes--; // decrement final number of nodes as nodes are marked dead
            }
        }

        // set up a defector,
        nodes[H - 1].setDefector(true);// initial defector set

        // set up the shared or individual thresholds of each agent/node
        if (isThresholdShared) {// custom specifified case
            for (node n : nodes) {
                n.setThreshold(sharedThreshold);
            }
        } else { // 1/k case
            for (node n : nodes) {
                if (n.getInitialNumberOfNeighbours() != 0) {
                    n.setThreshold(1.0 / (n.getInitialNumberOfNeighbours()));
                }
            }
        }

        return nodes; // end setupGame() method
    }

    // playGame() actually runs the game repeatedly
    // until it reaches equilibrium or all agents die
    // prints all the results of the game
    // pre-cond: takes an array of nodes
    // post-cond: doesn't return anything
    public void playGame(node[] nodes) {

        int numberOfRounds = 0; // to keep track of total rounds run
        node node1; // initializing variables to use later
        node node2;

        int stabilityCounter = 0; // how many consecutive times the rounds had no change
        boolean stable = true; // checked at the end of each round
        int sufficientStability = 10; // how many consecutive times to run with no change before we establish stable
                                      // equilibrium

        // keep playing until all dead or equilibrium
        while (!pairs.isEmpty() && stabilityCounter < sufficientStability) {

            stable = true;

            for (IntPair pair : pairs) {

                node1 = nodes[pair.getFirst() - 1]; // take a single connection
                node2 = nodes[pair.getSecond() - 1];

                // decide on scores from each pair's cooperation
                // if both cooperate
                if (!node1.defects() && !node2.defects()) {
                    node1.addToScore(1.0 / node1.getInitialNumberOfNeighbours());
                    node2.addToScore(1.0 / node2.getInitialNumberOfNeighbours());
                }
                // if only first defects
                else if (node1.defects() && !node2.defects()) {
                    node1.addToScore(2.0 / node1.getInitialNumberOfNeighbours());

                }
                // if only second defects
                else if (!node1.defects() && node2.defects()) {
                    node2.addToScore(2.0 / node2.getInitialNumberOfNeighbours());
                }
                // if both defect, both get zero
            }

            // imitating strategies
            for (node n : nodes) { // for each node, check each of its alive neighbours's scores and strategies,
                                   // also run
                                   // the probability of imitation. if
                                   // neighbour's alive, its score is higher and strategy is different and it
                                   // passes the
                                   // chance of imitation ...
                for (node neighbour : n.getNeighbours()) {
                    if ((!n.isDead()) && n.getTotalScore() < neighbour.getTotalScore()
                            && (n.defects() != neighbour.defects())
                            && Math.random() < M) {
                        n.setDefector(neighbour.defects()); // ... set the neighbour's strategy to the current node.
                        stable = false; // change stability marker
                        stabilityCounter = 0; // reset stability counter
                    }
                }

            }

            // mark agents as dead and delete connections with dead agents
            for (node n : nodes) {
                if ((!n.isDead()) && n.getTotalScore() < n.getThreshold()) { // if agent alive and current score below
                                                                             // threshold
                    n.setDeath(); // eliminate
                    finalNumberOfNodes--; // decrement final number of alive agents
                    stable = false; // change stability marker to false
                    stabilityCounter = 0; // reset stability counter

                    // remove all the connections with the newly eliminated node from linked list
                    // that stores connections as pairs of integers
                    for (Iterator<IntPair> pairIterator = pairs.iterator(); pairIterator.hasNext();) {
                        IntPair currentPair = pairIterator.next();
                        if (currentPair.getFirst() == n.getID() || currentPair.getSecond() == n.getID()) {
                            pairIterator.remove();
                        }

                    }

                }

            }

            // remove all dead nodes from each node's linked list of neighbours
            for (node n : nodes) {

                for (Iterator<node> neighbourIterator = n.getNeighbours().iterator(); neighbourIterator.hasNext();) {
                    node currentNeighbour = neighbourIterator.next();
                    if (currentNeighbour.isDead()) {
                        neighbourIterator.remove();
                        n.decrementNeighbourNumber(); // decrement neighbour number
                    }

                }
            }

            /*
             * // testing each round
             * for (node n : nodes) {
             * System.out.
             * printf("ID: %4d ||  Score: %.5f || Threshold: %.5f || defects: %5B || dead?: %B\n"
             * ,
             * n.getID(), n.getTotalScore(), n.getThreshold(), n.defects(), n.isDead());
             * }
             * // end testing each round
             */
            // uncomment to look through the results of each round

            if (stable) {
                stabilityCounter++; // increment stability counter if there was no change in this round
            }
            node.resetScores(nodes); // reset all scores at the end of the round.
            numberOfRounds++; // increment total number of rounds run.

            /*
             * // testing each round's stability
             * System.out.println("Stable? " + stable);
             * System.out.println("Stability counter: " + stabilityCounter);
             * System.out.println();
             * // end testing each round's stability
             */

        }

        // output:

        // declaring some final variables for output
        int largestNumberOfNeighbours = 0;
        int smallestNumberOfNeighbours = nodes.length;
        int finalNumberOfDefectors = 0;
        int finalNumberOfCooperators = 0;

        // calculating final numbers of alive defectors and cooperators
        for (node n : nodes) {
            if (n.defects() && !n.isDead()) {
                finalNumberOfDefectors++;
            } else if (!n.defects() && !n.isDead()) {
                finalNumberOfCooperators++;
            }
        }

        // calculating largest and smallest initial numbers of neighbours
        for (node n : nodes) {
            if (largestNumberOfNeighbours < n.getInitialNumberOfNeighbours()) {
                largestNumberOfNeighbours = n.getInitialNumberOfNeighbours();
            }
        }

        for (node n : nodes) {
            if (smallestNumberOfNeighbours > n.getInitialNumberOfNeighbours()) {
                smallestNumberOfNeighbours = n.getInitialNumberOfNeighbours();
            }
        }

        // printing
        System.out.println();
        System.out.println("\t\tResults of the game:");
        System.out.println();

        // neighbour information
        System.out.println("Initial number of agents from the txt file: " + nodes.length);
        System.out.println();

        if (smallestNumberOfNeighbours == largestNumberOfNeighbours) { // for 2D4N lattice that all have 4 connections
            System.out.println("All agents have exactly " + smallestNumberOfNeighbours + " initial neighbours");
        } else { // for all other networks

            // printing IDs and largest/smallest k
            System.out.print("The ID(s) of the agent(s) with the largest initial number of neighbours: ");
            for (node n : nodes) {
                if (n.getInitialNumberOfNeighbours() == largestNumberOfNeighbours) {
                    System.out.print(n.getID() + "  ");
                }
            }
            System.out.println();
            System.out.println("The largest initial number of neighbours: " + largestNumberOfNeighbours);
            System.out.println();

            System.out.print("The ID(s) of the agent(s) with the smallest initial number of neighbours: ");
            for (node n : nodes) {
                if (n.getInitialNumberOfNeighbours() == smallestNumberOfNeighbours) {
                    System.out.print(n.getID() + "  ");
                }
            }
            System.out.println();
            System.out.println("The smallest initial number of neightbours: " + smallestNumberOfNeighbours);
        }

        // parameters information
        System.out.println();
        System.out.println("Parameters used in the game:");
        System.out.println();
        System.out.println("Initial Defector ID:   " + H);
        System.out.print("Elimination Threshold: ");
        if (isThresholdShared)
            System.out.println(sharedThreshold);
        else
            System.out.println("1/k");
        System.out.println("Imitation Probability: " + M);
        System.out.println();

        // actual results of the game
        System.out.println("The number of rounds the program executed: " + numberOfRounds);
        if (stabilityCounter == sufficientStability) {
            System.out.println("The environment reached the equilibrium.");
            System.out.println(
                    "The program ran additional " + stabilityCounter + " rounds to ensure the stable equilibrium");
            System.out.println("Final number of agents: " + finalNumberOfNodes);
            System.out.println("Among those:");
            System.out.println("\tNumber of cooperators: " + finalNumberOfCooperators);
            System.out.println("\tNumber of defectors:   " + finalNumberOfDefectors);

        } else {
            System.out.println("All agents were eliminated");
        }
        System.out.println();

    } // end playGame()
}
