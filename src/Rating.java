// An immutable passive data object (PDO) to represent the rating data

public class Rating implements Comparable<Rating> {
    private String myID;
    private double value;

    public Rating (String id, double aValue) {
    	myID = id;
        value = aValue;
    }

    // Returns the id of the item being rated
    public String getID () {
        return myID;
    }
	
    // Returns the value of this rating (as a number so it can be used in calculations)
    public double getValue () {
        return value;
    }

    // Returns a string of all the rating information
    public String toString () {
        return "[" + getID() + ", " + getValue() + "]";
    }

    public int compareTo(Rating other) {
        if (value < other.value) return -1;
        if (value > other.value) return 1;
        
        return 0;
    }


}
