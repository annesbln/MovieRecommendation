
/**
 * Write a description of SecondRatings here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import java.util.*;

public class RatingCalculator {
    public double getAverageByID(String movieID, int minimalRaters){
        double sumRatings = 0.0;
        int numRatings =0;
        for (Rater rater: RaterDatabase.getRaters()){
            if(rater.hasRating(movieID)){
                numRatings++;
                sumRatings += rater.getRating(movieID);
            }
        }
        if (numRatings>=minimalRaters){
            return sumRatings/numRatings;
        }else{
            return 0.0;
        }
    }
    //method finds the average rating for every movie that has been rated
    // by at least minimalRaters raters. Each such rating is stored in a Rating object
    //in which the movie ID and the average rating are used in creating the Rating object.
    //The method returns an ArrayList of all the Rating objects for movies that have at 
    //least the minimal number of raters supplying a rating.
    public ArrayList<Rating> getAverageRatings(int minimalRaters){
        ArrayList<Rating> list = new ArrayList<Rating>();
        ArrayList<String> movies = MovieDatabase.filterBy(new TrueFilter());
        for (String movieID: movies){
            double rating = getAverageByID(movieID,minimalRaters);
            if(rating!=0.0){
                list.add(new Rating(movieID,rating));
            }
        }
        return list;
    }
    
    public ArrayList<Rating> getAverageRatingsByFilter(int minimalRaters, Filter filterCriteria){
        ArrayList<Rating> list = new ArrayList<Rating>();
    	ArrayList<String> movieList = MovieDatabase.filterBy(filterCriteria);
    	for (String id : movieList) {
    		double average = getAverageByID(id, minimalRaters);
    		if (average!=0.0) {
    		    Rating rating = new Rating (id, average);
    		    list.add(rating);
    		}
    	}
    	return list;       
    }
    
    private double dotProduct(Rater me, Rater r) {  
        double dotProduct = 0.0;
        for (String movieID: r.getItemsRated()) {
            if (me.hasRating(movieID)) {
                dotProduct += (me.getRating(movieID)-5)*(r.getRating(movieID)-5);
            }
        }
        return dotProduct;
    }
    
    /*  This method returns an ArrayList of type Rating (but with raterID instead of movieID! and with the dotproduct as value!)
    sorted by ratings from highest to lowest dotproduct, with the highest dotproduct first. The list only 
    includes those raters who have a positive dotproduct since 
    those with negative values are not similar in any way.*/

    private ArrayList<Rating> getSimilarities(String raterID){ //id is raterID
        ArrayList<Rating> list = new ArrayList<Rating>();
        Rater me = RaterDatabase.getRater(raterID);
        for (Rater r: RaterDatabase.getRaters()) {
            double dotProduct = dotProduct(me,r);
            if (!me.getID().equals(r.getID())&&dotProduct>0) {
                list.add(new Rating(r.getID(), dotProduct)); 
            }
        }
        Collections.sort(list, Collections.reverseOrder());
        return list;
    }

    /*  This method returns the complete ArrayList of type Rating (with movieID not raterID!) and the movies weighted average 
    using only the top numSimilarRaters with positive dot product. There are only those movies 
    included that have at least minimalRaters.*/

    public ArrayList<Rating> getSimilarRatings(String raterID, int numSimilarRaters, int minimalRaters) {
        return getSimilarRatingsByFilter(raterID, numSimilarRaters, minimalRaters, new TrueFilter());
    }
    
    public ArrayList<Rating> getSimilarRatingsByFilter(String raterID, int numSimilarRaters, int minimalRaters, Filter filterCriteria){
        ArrayList<Rating> list = new ArrayList<Rating>();
        ArrayList<Rating> similarityList = getSimilarities(raterID);  //list with rating(raterID, dotproduct) --> only dotprocuct>0
        for (String movieID : MovieDatabase.filterBy(filterCriteria)) {
            int numRatings = 0;
            double tempSum = 0.0;
            int i = 0;
            while (i < numSimilarRaters && i < similarityList.size()) { // make sure that the loop doesn't continue when similarityList.size() is reached
                Rating similarRating = similarityList.get(i);
                double dotProduct = similarRating.getValue();
                String id = similarRating.getItem();  //id = raterID!!!
                Rater rater = RaterDatabase.getRater(id);
                if (rater.hasRating(movieID)) {
                    numRatings++;
                    tempSum += dotProduct * rater.getRating(movieID);
                }
                i++;
            }
            if (numRatings >= minimalRaters) {
                list.add(new Rating(movieID, tempSum/numRatings));
            }
        }
        Collections.sort(list, Comparator.reverseOrder());
        return list;
    }
}
