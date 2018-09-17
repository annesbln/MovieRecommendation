import java.util.*;

/**
 * Write a description of RecommendationRunner here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RecommendationRunner implements Recommender{
    public ArrayList<String> getItemsToRate(){
        int n = 3; //number of movies that have to be rated by the user
        String raterfilename= "ratings.csv";
        String moviefilename= "ratedmoviesfull.csv";
        RaterDatabase.initialize(raterfilename);
        MovieDatabase.initialize(moviefilename);
        ArrayList<String> allMovies = MovieDatabase.filterBy(new YearAfterFilter(2000));
        ArrayList<String> selectedMovies = new ArrayList<String>();
        Random random = new Random();
        int i = 0;
        while (i<n && allMovies.size()>=n) {// check if the moviedatabase is big enough for random search as while loop would be endless otherwise
            int randomIndex = random.nextInt(allMovies.size());
            String id = allMovies.get(randomIndex);
            if (!selectedMovies.contains(id)) { // check if the movie is already in the list to create a list without duplicates
                selectedMovies.add(id);
                i++;
            }
        }
        return selectedMovies;
    }

    @Override
    // This method prints out an HTML table of movies recommended by the program for the user based on the movies they rated.
    public void printRecommendationsFor(String webRaterID) {
        RatingCalculator rc = new RatingCalculator();
        String raterfilename= "ratings.csv";
        String moviefilename= "ratedmoviesfull.csv";
        RaterDatabase.initialize(raterfilename);
        MovieDatabase.initialize(moviefilename);
        int numSimilarRaters = 20; // the number of top similar raters that are selected for the recommendation process
        int minNumOfRatings = 3;	// minimum number of Ratings the recommended movies should have
        int maxNumPrintMovies = 20; // maximum number of recommended movies that are going to be printed on the webpage
        ArrayList<Rating> list = rc.getSimilarRatings(webRaterID, numSimilarRaters, minNumOfRatings); // list of all recommended movies that meet the above requirements
        if (list.size()==0) {
            System.out.println("Sorry, with the current setup there are no movies we can recommend to you. Please try once more.");
        }
        else {
            System.out.println("<html>\r\n" +
                "<head>\r\n" +
                "<meta charset=\"utf-8\" />\r\n"+
                "<title>Movie Recommendation</title>\r\n"+
                "<meta name=\"viewport\" content=\"initial-scale=1.0; maximum-scale=1.0; width=device-width;\">\r\n"+
                "<link href=\"https://fonts.googleapis.com/css?family=Ubuntu\" rel=\"stylesheet\" type=\"text/css\">\r\n"+
                "<style>\r\n" + 
                "body {\r\n" +
                "	background-color: black;\r\n" +
                "	font-family: \"Ubuntu\", sans-serif;\r\n" +
                "	text-align=\"center\";\r\n" +
                "}\r\n" +
                "div.table-title {\r\n" +
                "	max-width: 600px;\r\n" +
                "	padding: 10px;\r\n" +
                "	margin: auto;\r\n" +
                "}\r\n" +
                ".table-title h3 {\r\n" +
                "	color: #fafafa;\r\n" +
                "	font-size: 200%;\r\n" +
                "	font-weight: 400;\r\n" +
                "	text-align: center;\r\n" +
                "}\r\n" +
                "/*** Table Style **/\r\n" +
                ".table-body {\r\n" +
                "	border-collapse: collapse;\r\n" +
                "	margin: auto;\r\n" +
                "	max-width: 600px;\r\n" +
                "	width: 100%;\r\n" +
                "}\r\n" +
                "th {\r\n" +
                "	color:#EBEBEB;\r\n" +
                "	background:#4E5066;\r\n" +
                "	border-bottom:4px solid #9ea7af;\r\n" +
                "	border-right: 1px solid #9ea7af;\r\n" +
                "	font-size:18px;\r\n" +
                "	font-weight: 100;\r\n" +
                "	padding:10px;\r\n" +
                "	text-align: center;\r\n" +
                "}\r\n" +
                "th:first-child {\r\n" +
                "	border-top-left-radius:5px;\r\n" +
                "}\r\n" +
                "th:last-child {\r\n" +
                "	border-top-right-radius:5px;\r\n" +
                "	border-right:none;\r\n" +
                "}\r\n" +
                "tr {\r\n" +
                "	color:#666B85;\r\n" +
                "	font-size:16px;\r\n" +
                "}\r\n" +
                "tr:hover td {\r\n" +
                "	background:#4E5066;\r\n" +
                "	color:#FFFFFF;\r\n" +
                "}\r\n" +
                "tr:nth-child(odd) td {\r\n" +
                "	background:#EBEBEB;\r\n" +
                "}\r\n" +
                "tr:nth-child(odd):hover td {\r\n" +
                "	background:#4E5066;\r\n" +
                "}\r\n" +
                "tr:last-child td:first-child {\r\n" +
                "	border-bottom-left-radius:5px;\r\n" +
                "}\r\n" +
                "tr:last-child td:last-child {\r\n" +
                "	border-bottom-right-radius:5px;\r\n" +
                "}\r\n" +
                "td {\r\n" +
                "	background:#FFFFFF;\r\n" +
                "	padding:10px;\r\n" +
                "	font-size:16px;\r\n" +
                "	border-right: 1px solid #9ea7af;\r\n" +
                "	text-align: center;\r\n" +
                "}\r\n" +
                "td:last-child {\r\n" +
                "	border-right: none;\r\n" +
                "}\r\n" + 
                "</style>\r\n" + 
                "</head>\r\n" + 
                "<body>\r\n" +
                "	<div class=\"table-title\">\r\n" +
                "		<h3>Your recommended movies</h3>\r\n" +
                "	</div>\r\n" +
                "	<table class=\"table-body\">\r\n" +
                "		<thead>\r\n" +
                "			<tr>\r\n" +
                "				<th>Ranking</th>\r\n" +
                "				<th>Movie Title</th>\r\n" +
                "			</tr>\r\n" +
                "		</thead>\r\n"+
                "	<tbody>\r\n");
            int i = 0;
            while (i<maxNumPrintMovies && i<list.size()) { // make sure at that at least all movies in the list are printed in case list.size is smaller then maxNumPrintMovies
                Rating rating = list.get(i);
                String movieID = rating.getItem();
                System.out.println(
                    "	<tr>\r\n" + 
                    "		<td>"+(i+1)+"</td>\r\n" + 
                    "		<td>"+MovieDatabase.getMovie(movieID).getTitle()+"</td>\r\n" +
                    "	</tr>\r\n");
                i++;
            }
            System.out.println(
                "		</tbody>\r\n"+
                "</table>\r\n" + 
                "\r\n" +
                "</body>\r\n" +
                "</html>");
        }
    }

}
