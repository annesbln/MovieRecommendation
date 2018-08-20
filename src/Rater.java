import java.util.*;

public interface Rater {
    public int numRatings();
    public void addRating(String item, double rating);
    public double getRating(String item);
    public boolean hasRating(String item);
    public String getID();
    public ArrayList<String> getItemsRated();
	public ArrayList<Rating> getMyRatings();
}
