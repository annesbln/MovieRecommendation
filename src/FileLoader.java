import java.util.*;
import java.io.*;
import org.apache.commons.csv.*;

public class FileLoader {
	private ArrayList<Movie> myMovieList;
	private ArrayList<Rater> myRaterList;
	private ArrayList<Rating> myRatingList;
	
	public FileLoader(){
		myMovieList= new ArrayList<Movie>();
		myRaterList= new ArrayList<Rater>();
		myRatingList= new ArrayList<Rating>();
	}
	
	public ArrayList<Rating> getRatingList(){
		return myRatingList;
	}
	
	/**
	 * The method loadMovies(String filename) processes every record from the CSV file whose name is filename.
	 * Filename refers to a file of movie information.
	 * The method returns an ArrayList of type Movie with all of the movie data from the file.
	 */
	public ArrayList<Movie> loadMovies(String filename) {
		try(
				Reader reader = new FileReader(filename);
				CSVParser parser = new CSVParser(reader, CSVFormat.RFC4180.withFirstRecordAsHeader());
				){
			for (CSVRecord rec: parser) {
				Movie movie = new Movie(rec.get("id"),rec.get("title"),rec.get("year"),rec.get("genre"),rec.get("director"),rec.get("country"),Integer.parseInt(rec.get("minutes")),rec.get("poster"));
				myMovieList.add(movie);
			}
		} catch (FileNotFoundException e) {
			System.out.println("This is a FileNotFoundException! Please enter a valid filepath!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("This is a IOException!");
			e.printStackTrace();
		}
		return myMovieList;
	}
	
	/**
	 * The method loadRaters (String filename) processes every record from the CSV file whose name is filename.
	 * Filename refers to a file of raters information.
	 * The method returns an ArrayList of type Rater with all of the rater data from the file.
	 */
	
	public ArrayList<Rater> loadRaters(String filename){
			try(
					Reader reader = new FileReader(filename);
					CSVParser parser = new CSVParser(reader, CSVFormat.RFC4180.withFirstRecordAsHeader());
					){
				for (CSVRecord rec: parser) {
					Rater rater = new EfficientRater(rec.get("rater_id"));
					rater.addRating(rec.get("movie_id"), Double.parseDouble(rec.get("rating")));
					Rating rating = new Rating (rec.get("movie_id"), Double.parseDouble(rec.get("rating")));
					myRatingList.add(rating);
					if (containsRater(myRaterList, rater)==null) {
						myRaterList.add(rater);
					}
					else {
						rater = containsRater(myRaterList, rater);
						rater.addRating(rec.get("movie_id"), Double.parseDouble(rec.get("rating")));
					}
				}
		} catch (FileNotFoundException e) {
			System.out.println("This is a FileNotFoundException! Please enter a valid filepath!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("This is a IOException!");
			e.printStackTrace();
		}
		return myRaterList;
	}

	private Rater containsRater(ArrayList<Rater> list, Rater rater) {
		for (Rater r: list) {
			if (r.getID().equals(rater.getID())) {
				return r;
			}
		}
		return null;
	}
}
