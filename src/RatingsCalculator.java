
/**
 * The RatingsCalculator provides methods to process the ratings.
 * Furthermore the number of ratings is tracked in the hashMap movieCountMap.
 * 
 * @author (annewbie)
 * @version (20.08.2018)
 */
import java.util.*;

public class RatingsCalculator {
    HashMap<String, Integer> movieCountMap;
    
    public RatingsCalculator() {
    	movieCountMap = getMovieCountMap();
    }
    
    public HashMap<String, Integer> getMovieCountMap() {
    	HashMap<String, Integer> movieList = new HashMap<String,Integer>();
    	int count = 0;
    	for (Rater r: RaterDatabase.getRaters()) {
    		ArrayList<Rating> ratingList = r.getMyRatings();
    		for (Rating rating: ratingList) {
    			String id = rating.getID();
    			if (!movieList.containsKey(id)) {
    				movieList.put(id, 1);
    			}
    			else {
    				count = movieList.get(id);
    				movieList.put(id, count+1);
    			}
    		}
    	}
    	return movieList;
    }
    
    public ArrayList<Rating> getAverageRatings(int minNumOfRaters) {
    	ArrayList<Rating> returnList = new ArrayList<Rating>();
    	for (String movieID: MovieDatabase.filterBy(new TrueFilter())) {
    		Double average = getAverageByID(movieID, minNumOfRaters);
    		if (average!=0.0&&movieCountMap.get(movieID)>=minNumOfRaters) {
    			Rating rating = new Rating (movieID, average);
    			returnList.add(rating);
    		}    		
    	}
    	Collections.sort(returnList,Collections.reverseOrder());
		return returnList;
    }
    
    private Double getAverageByID(String movieID, int minNumOfRaters) {
    	Double average = 0.0;
    	int count = 0;
    	for (String s: movieCountMap.keySet()) {
    		if (movieCountMap.get(s)>=minNumOfRaters) {
    			for (Rater r: RaterDatabase.getRaters()) {
        			if (r.hasRating(movieID)) {
        				average += r.getRating(movieID);
        				count++;
        			}
        		}
       		}
    	}
    	if(count!=0) {
			return average/count;
		} else {
			return 0.0;
		}
    }
    
    public ArrayList<Rating> getAverageRatingsByFilter(int minNumOfRaters, Filter filterCriteria) {
    	ArrayList<Rating> list = new ArrayList<Rating>();
    	ArrayList<String> movieList = MovieDatabase.filterBy(filterCriteria);
    	for (String id : movieList) {
    		double average = getAverageByID(id, minNumOfRaters);
    		if (average!=0.0&&movieCountMap.get(id)>=minNumOfRaters) {
    			Rating rating = new Rating (id, average);
    			list.add(rating);
    		}
    	}
    	return list;
    }
    
/*   This method translates a rating from the scale 0 to 10 to the scale -5 to 5 
     and returns the dot product of the ratings of movies that both raters rated. 
     This method is called by getSimilarities.*/
    
    private double dotProduct(Rater me, Rater r) {	
    	double dotProduct = 0.0;
    	for (String movieID: me.getItemsRated()) {
    		if (r.hasRating(movieID)) {
    			dotProduct += (me.getRating(movieID)-5)*(r.getRating(movieID)-5);
    		}
    	}
    	return dotProduct;
    }
	
/*	This method returns an ArrayList of type Rating (but with raterID instead of movieID!)
 	sorted by ratings from highest to lowest dot product, with the highest rating first. The list only 
	includes those raters who have a positive dot product since 
	those with negative values are not similar in any way.*/
	
	private ArrayList<Rating> getSimilarities(String id){ //id is raterID
		ArrayList<Rating> list = new ArrayList<Rating>();
		Rater me = RaterDatabase.getRater(id);
		for (Rater r: RaterDatabase.getRaters()) {
			double dotProduct = dotProduct(me,r);
			if (!me.getID().equals(r.getID())&&dotProduct>=0) {
				list.add(new Rating(r.getID(), dotProduct)); 
			}
		}
		Collections.sort(list, Collections.reverseOrder());
		return list;
	}
	
/*	This method returns an ArrayList of type Rating (with movieID not raterID!) and the movies weighted average 
 	using only the top numSimilarRaters with positive dot product. There are only those movies 
 	includes that have at least minNumOfRaters.*/
	
	public ArrayList<Rating> getSimilarRatings(String id, int numSimilarRaters, int minNumOfRaters) {
        return getSimilarRatingsByFilter(id, numSimilarRaters, minNumOfRaters, new TrueFilter());
    }

    public ArrayList<Rating> getSimilarRatingsByFilter(String id, int numSimilarRaters, int minNumOfRaters, Filter filterCriteria) {
        ArrayList<Rating> ratingList = new ArrayList<>();
        ArrayList<Rating> similaritiesList = getSimilarities(id);
        for (String movieID : MovieDatabase.filterBy(filterCriteria)) {
            int numRatings = 0;
            double tempSum = 0.0;
            for (int i = 0; i < numSimilarRaters; i++) {
                Rating similarRating = similaritiesList.get(i);
                double dotProduct = similarRating.getValue();
                String raterID = similarRating.getID();
                Rater rater = RaterDatabase.getRater(raterID);
                if (rater.hasRating(movieID)) {
                    numRatings++;
                    tempSum += dotProduct * rater.getRating(movieID);
                }
            }
            if (numRatings >= minNumOfRaters) {
                ratingList.add(new Rating(movieID, tempSum/numRatings));
            }
        }
        Collections.sort(ratingList, Comparator.reverseOrder());
        return ratingList;
    }
}
