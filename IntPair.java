//this class is used to store a pair of integers

//in our project, it is helpful to store a pair of integers
//to indicate a connection between them.

public class IntPair {

    // instance fields
    private final int x;
    private final int y;

    // constructor
    IntPair(int x, int y) {
        this.x = x;
        this.y = y;

    }

    // methods
    public int getFirst() {

        return this.x;

    }

    public int getSecond() {

        return this.y;

    }

}
