/* Class node is used to create objects that represent nodes/agents 
 * in a network of players that play prisoner's dilemma game
 */

import java.util.*;

public class node {

    // instance fields
    private int ID;
    private int initialNumberOfNeighbours;
    private int currentNumberOfNeighbours;
    private boolean isDefector;
    private boolean isDead;
    private LinkedList<node> neighbourList = new LinkedList<>();
    private double totalScore;
    private double threshold;

    // constructor
    public node(int ID) {
        this.ID = ID;
        initialNumberOfNeighbours = 0;
        isDefector = false;
        isDead = false;
        this.totalScore = 0;
    }

    // METHODS
    public static void recordNeighbours(node node1, node node2) {

        // iterates node1 initial and current # of neighbours
        node1.initialNumberOfNeighbours++;
        node1.currentNumberOfNeighbours++;

        // iterates node2 initial and current # of neighbours
        node2.initialNumberOfNeighbours++;
        node2.currentNumberOfNeighbours++;

        // updates the neighbour lists of both node1 and node2
        node1.neighbourList.add(node2);
        node2.neighbourList.add(node1);

    }

    // ID
    public int getID() {
        return ID;
    }

    // strategy: defector or cooperator
    public void setDefector(boolean defects) {
        isDefector = defects;
    }

    public boolean defects() {
        return isDefector;
    }

    // threshold of elimination
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public double getThreshold() {
        return threshold;
    }

    // scores
    public void addToScore(double score) {
        totalScore += score;
    }

    public static void resetScores(node[] nodes) {
        for (node n : nodes) {
            n.totalScore = 0;
        }
    }

    public double getTotalScore() {
        return totalScore;
    }

    // state: dead or alive
    public void setDeath() {
        isDead = true;
    }

    public boolean isDead() {
        return isDead;
    }

    // neighbours
    public int getInitialNumberOfNeighbours() {
        return initialNumberOfNeighbours;
    }

    public LinkedList<node> getNeighbours() {
        return neighbourList;
    }

    public void decrementNeighbourNumber() {
        currentNumberOfNeighbours--;
    }

}
