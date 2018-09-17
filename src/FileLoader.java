import java.util.*;
import java.io.*;
import org.apache.commons.csv.*;

public class FileLoader {
    private ArrayList<Movie> myMovieList;
    private ArrayList<Rater> myRaterList;

    public FileLoader(){
        myMovieList= new ArrayList<Movie>();
        myRaterList= new ArrayList<Rater>();
    }

    // * The following method loadMovies(String filename) processes every record from the CSV file whose name is filename.
    // * Filename refers to a file of movie information.
    // * The method returns an ArrayList of type Movie with all of the movie data from the file.
    // */

    public ArrayList<Movie> loadMovies(String filename){
    	try (
    			Reader reader = new FileReader(filename);
    	    	CSVParser parser = new CSVParser(reader, CSVFormat.RFC4180.withFirstRecordAsHeader());
    			){
    		for (CSVRecord rec: parser) {
    			Movie movie = new Movie(rec.get("id"),rec.get("title"),rec.get("year"),rec.get("genre"),rec.get("director"),rec.get("country"),rec.get("poster"),Integer.parseInt(rec.get("minutes")));
    			myMovieList.add(movie);
    			}
    	} catch (FileNotFoundException e) {
			System.out.println("This is a FileNotFoundException! Please enter a valid filepath!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("This is an IOException!");
			e.printStackTrace();
		}
        return myMovieList;
    }

    public ArrayList<Rater> loadRaters(String filename){
    	try (    	
    			Reader reader = new FileReader(filename);
    			CSVParser parser = new CSVParser(reader, CSVFormat.RFC4180.withFirstRecordAsHeader());
    			){
    		for (CSVRecord rec: parser) {
    			Rater currRater = new EfficientRater(rec.get("rater_id"));
    			if (myRaterList.size()==0||!listContainsRater(currRater)){
    				myRaterList.add(currRater);
    				currRater.addRating(rec.get("movie_id"),Double.parseDouble(rec.get("rating")));
    				} else {
    					for (int i = 0; i<myRaterList.size(); i++){
    						Rater rater = myRaterList.get(i);
    						if (rater.getID().equals(rec.get("rater_id"))){
    							rater.addRating(rec.get("movie_id"),Double.parseDouble(rec.get("rating")));
    							}
    						}
    					}
    			}
        }
    	catch (FileNotFoundException e) {
    		System.out.println("This is a FileNotFoundException! Please enter a valid filepath!");
    		e.printStackTrace();} 
    	catch (IOException e) {
    		System.out.println("This is an IOException!");
    		e.printStackTrace();
    		}
    	return myRaterList;
    }

    private boolean listContainsRater(Rater rater) {    // This helper method is necessary to check if the current rater is already in the list
        for (Rater r: myRaterList) {
            if (r.getID().equals(rater.getID())) {
                return true;
            }
        }
        return false;
    }

    public void testLoadMovies(){
        ArrayList<Movie> list = loadMovies("data/ratedmoviesfull.csv");
        System.out.println("There are "+list.size()+" movies in the file:");
        int i = 1;
        for (Movie movie: list){
            System.out.println(i+" "+movie.toString());
            i++;
        }        
    }

    public void testLoadRaters(){
        ArrayList<Rater> list = loadRaters("data/ratings.csv");
        int i = 1;
        for (Rater rater: list){
            ArrayList<String> ratingList = rater.getItemsRated();
            System.out.println("Size of the Ratinglist: "+ratingList.size());
            System.out.println(i+" raterID: "+rater.getID()+" num of ratings: "+rater.numRatings());
            for (String movieID: ratingList){
                System.out.println("  movieID: "+movieID+" given Rating: "+rater.getRating(movieID));
            }
            i++;
        }        
    }

}
