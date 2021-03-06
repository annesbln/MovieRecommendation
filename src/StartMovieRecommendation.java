import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class StartMovieRecommendation {
	
	
	public static void main(String[] args) {
		RecommendationRunner rr = new RecommendationRunner();
		rr.getItemsToRate();
		
		String raterfilename= "data\\ratings.csv";
		RaterDatabase.initialize(raterfilename);
		ArrayList<Rater> totalRaterList = RaterDatabase.getRaters();
		Random random = new Random();
		String randomRaterId = totalRaterList.get(random.nextInt(totalRaterList.size())).getID();
		rr.printRecommendationsFor(randomRaterId);
		
		System.out.println("----------------------------printAverageRatings()---------------------------------------");
		printAverageRatings();
		System.out.println("----------------------------printSimilarRatings()---------------------------------------");
		printSimilarRatings();
		System.out.println("----------------------------printSimilarRatingsByGenre()---------------------------------------");
		printSimilarRatingsByGenre();
		System.out.println("----------------------------printAverageRatingsByYearAfterAndGenre()---------------------------------------");
		printAverageRatingsByYearAfterAndGenre();
		System.out.println("----------------------------printSimilarRatingsByDirector()---------------------------------------");
		printSimilarRatingsByDirector();
		System.out.println("----------------------------printSimilarRatingsByGenreAndMinutes()---------------------------------------");
		printSimilarRatingsByGenreAndMinutes();
		System.out.println("----------------------------printSimilarRatingsByYearAfterAndMinutes()---------------------------------------");
		printSimilarRatingsByYearAfterAndMinutes();
	}

	// prints and counts every movie that has at least minNumOfRatings
	
	public static void printAverageRatings() {
		String raterfilename= "data\\ratings.csv";
		String moviefilename= "data\\ratedmoviesfull.csv";
		RaterDatabase.initialize(raterfilename);
		MovieDatabase.initialize(moviefilename);
		RatingCalculator rc = new RatingCalculator();
		System.out.println("Number of raters: "+RaterDatabase.size());
		System.out.println("Number of movies: "+MovieDatabase.size());
		int minNumOfRatings = 35;
		ArrayList<Rating> list = rc.getAverageRatings(minNumOfRatings);
		System.out.println("Number of movies with at least "+minNumOfRatings+" raters: "+list.size());
		Collections.sort(list);
		int count =1;
		for (Rating r: list) {
			System.out.println(count+": Rating: "+r+", movie title: "+ MovieDatabase.getTitle(r.getItem()));
			count++;
		}
	}
	
	// prints all recommended movies for the specified rater. The recommendation consists of the n (= numSimilarRaters) top movies
	// having at least minNumOfRatings.
	
	public static void printSimilarRatings() {
		String raterfilename= "data\\ratings.csv";
		String moviefilename= "data\\ratedmoviesfull.csv";
		RaterDatabase.initialize(raterfilename);
		MovieDatabase.initialize(moviefilename);
		RatingCalculator rc = new RatingCalculator();
		
		String raterID = "71";
		int minNumOfRatings = 5;
		int numSimilarRaters = 20;
		ArrayList<Rating> list = rc.getSimilarRatings(raterID, numSimilarRaters, minNumOfRatings);
		System.out.println("The recommended movies and their similarityRatings are:");
		int count = 1;
		for (Rating r: list) {
			System.out.println(count+": Rating: "+r+", year: "+MovieDatabase.getYear(r.getItem())+", genre: "+MovieDatabase.getGenres(r.getItem())+", movie title: "+ MovieDatabase.getTitle(r.getItem()));
			count++;
		}
	}
	
	// prints all recommended movies for the specified rater. The recommendation consists of the n (= numSimilarRaters) top movies
	// having at least minNumOfRatings and the specified genre.
	
	public static void printSimilarRatingsByGenre() {
		String raterfilename= "data\\ratings.csv";
		String moviefilename= "data\\ratedmoviesfull.csv";
		RaterDatabase.initialize(raterfilename);
		MovieDatabase.initialize(moviefilename);
		RatingCalculator rc = new RatingCalculator();

		
		String id = "964";
		int minNumOfRatings = 5;
		int numSimilarRaters = 20;
		String genre = "Mystery";
		GenreFilter filter = new GenreFilter(genre);
		ArrayList<Rating> list = rc.getSimilarRatingsByFilter(id, numSimilarRaters, minNumOfRatings, filter);
		System.out.println("The recommended movies and their similarityRatings are:");
		int count = 1;
		for (Rating r: list) {
			System.out.println(count+": Rating: "+r+", year: "+MovieDatabase.getYear(r.getItem())+", genre: "+MovieDatabase.getGenres(r.getItem())+", movie title: "+ MovieDatabase.getTitle(r.getItem()));
			count++;
		}
		
	}
	
	// prints all recommended movies for the specified rater. The recommendation consists of the n (= numSimilarRaters) top movies
	// having at least minNumOfRatings, the specified genre and were released in the specified year or later.
	
	private static void printAverageRatingsByYearAfterAndGenre() {
		String raterfilename= "data\\ratings.csv";
		String moviefilename= "data\\ratedmoviesfull.csv";
		RaterDatabase.initialize(raterfilename);
		MovieDatabase.initialize(moviefilename);
		RatingCalculator rc = new RatingCalculator();
		System.out.println("Number of ratings: "+RaterDatabase.size());
		System.out.println("Number of movies: "+MovieDatabase.size());
		
		int year = 1990;
		String genre = "Drama";
		int minNumOfRatings = 8;
		AllFilters filters = new AllFilters();
		GenreFilter genreFilter = new GenreFilter(genre);
		YearAfterFilter yearFilter = new YearAfterFilter(year);
		filters.addFilter(genreFilter);
		filters.addFilter(yearFilter);
		ArrayList<Rating> list = rc.getAverageRatingsByFilter(minNumOfRatings, filters);
		int count =0;
		System.out.println("There are "+list.size()+" movies having "+minNumOfRatings+" or more ratings.");
		count =1;
		for (Rating r: list) {
			System.out.println(count+": Rating: "+r+", year: "+MovieDatabase.getYear(r.getItem())+", genre: "+MovieDatabase.getGenres(r.getItem())+", movie title: "+ MovieDatabase.getTitle(r.getItem()));
			count++;
		}
	}
	
	// prints all recommended movies for the specified rater. The recommendation consists of the n (= numSimilarRaters) top movies
	// having at least minNumOfRatings and as director at least one of the specified directors.
	
	public static void printSimilarRatingsByDirector() {
		String raterfilename= "data\\ratings.csv";
		String moviefilename= "data\\ratedmoviesfull.csv";
		RaterDatabase.initialize(raterfilename);
		MovieDatabase.initialize(moviefilename);
		RatingCalculator rc = new RatingCalculator();

		
		String raterID = "120";
		int minNumOfRatings = 2;
		int numSimilarRaters = 10;
		String director = "Clint Eastwood,J.J. Abrams,Alfred Hitchcock,Sydney Pollack,David Cronenberg,Oliver Stone,Mike Leigh";
		DirectorsFilter filter = new DirectorsFilter(director);
		ArrayList<Rating> list = rc.getSimilarRatingsByFilter(raterID, numSimilarRaters, minNumOfRatings, filter);
		System.out.println("The recommended movies and their similarityRatings are:");
		if (list.isEmpty()) {
			System.out.println("There where no Movies found!");
		}
		int count = 1;
		for (Rating r: list) {
			System.out.println(count+": Rating: "+r+", year: "+MovieDatabase.getYear(r.getItem())+", director: "+MovieDatabase.getDirector(r.getItem())+", movie title: "+ MovieDatabase.getTitle(r.getItem()));
			count++;
		}
	}
	
	// prints all recommended movies for the specified rater. The recommendation consists of the n (= numSimilarRaters) top movies
	// having at least minNumOfRatings. The recommended movies are part of the specified genre and have a length between minMinutes
	// and maxMinutes.
	
	public static void printSimilarRatingsByGenreAndMinutes() {
		String raterfilename= "data\\ratings.csv";
		String moviefilename= "data\\ratedmoviesfull.csv";
		RaterDatabase.initialize(raterfilename);
		MovieDatabase.initialize(moviefilename);
		RatingCalculator rc = new RatingCalculator();

		
		String raterID = "168";
		int minNumOfRatings = 3;
		int numSimilarRaters = 10;
		String genre = "Drama";
		int minMinutes = 80;
		int maxMinutes = 160;
		GenreFilter genreFilter = new GenreFilter(genre);
		AllFilters allFilter = new AllFilters();
		MinutesFilter minutesFilter = new MinutesFilter(minMinutes,maxMinutes);	
		allFilter.addFilter(genreFilter);
		allFilter.addFilter(minutesFilter );
		ArrayList<Rating> list = rc.getSimilarRatingsByFilter(raterID, numSimilarRaters, minNumOfRatings, allFilter);
		System.out.println("The recommended movies and their similarityRatings are:");
		if (list.isEmpty()) {
			System.out.println("There where no Movies found!");
		}
		int count = 1;
		for (Rating r: list) {
			System.out.println(count+": Rating: "+r+", year: "+MovieDatabase.getYear(r.getItem())+", director: "+MovieDatabase.getDirector(r.getItem())+", movie title: "+ MovieDatabase.getTitle(r.getItem()));
			count++;
		}
	}
	
	// prints all recommended movies for the specified rater. The recommendation consists of the n (= numSimilarRaters) top movies
	// having at least minNumOfRatings. The recommended movies were released in the specified year or later and have 
	// a length between minMinutes and maxMinutes.
	
	public static void printSimilarRatingsByYearAfterAndMinutes() {
		String raterfilename= "data\\ratings.csv";
		String moviefilename= "data\\ratedmoviesfull.csv";
		RaterDatabase.initialize(raterfilename);
		MovieDatabase.initialize(moviefilename);
		RatingCalculator rc = new RatingCalculator();

		
		String raterID = "314";
		int minNumOfRatings = 5;
		int numSimilarRaters = 10;
		int minMinutes = 70;
		int maxMinutes = 200;
		int year = 1975;
		YearAfterFilter yearFilter = new YearAfterFilter(year);
		AllFilters allFilter = new AllFilters();
		MinutesFilter minutesFilter = new MinutesFilter(minMinutes,maxMinutes);	
		allFilter.addFilter(yearFilter);
		allFilter.addFilter(minutesFilter );
		ArrayList<Rating> list = rc.getSimilarRatingsByFilter(raterID, numSimilarRaters, minNumOfRatings, allFilter);
		System.out.println("The recommended movies and their similarityRatings are:");
		if (list.isEmpty()) {
			System.out.println("There where no Movies found!");
		}
		int count = 1;
		for (Rating r: list) {
			System.out.println(count+": Rating: "+r+", year: "+MovieDatabase.getYear(r.getItem())+", director: "+MovieDatabase.getDirector(r.getItem())+", movie title: "+ MovieDatabase.getTitle(r.getItem()));
			count++;
		}
	}
}
