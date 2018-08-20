import java.util.*;

public class MovieDatabase {
    private static HashMap<String, Movie> ourMovies;

    private static void initialize() {
        if (getOurMovies() == null) {
            setOurMovies(new HashMap<String,Movie>());
            loadMovies("data/ratedmoviesfull.csv");
        }
    }
    
    public static void initialize(String moviefile) {
        if (getOurMovies() == null) {
            setOurMovies(new HashMap<String,Movie>());
            loadMovies(moviefile);
        }
    }

	
    private static void loadMovies(String filename) {
        FileLoader fl = new FileLoader();
        ArrayList<Movie> list = fl.loadMovies(filename);
        for (Movie m : list) {
            getOurMovies().put(m.getID(), m);
        }
    }

    public static boolean containsID(String id) {
        initialize();
        return getOurMovies().containsKey(id);
    }

    public static int getYear(String id) {
        initialize();
        return getOurMovies().get(id).getYear();
    }

    public static String getGenres(String id) {
        initialize();
        return getOurMovies().get(id).getGenres();
    }

    public static String getTitle(String id) {
        initialize();
        return getOurMovies().get(id).getTitle();
    }

    public static Movie getMovie(String id) {
        initialize();
        return getOurMovies().get(id);
    }

    public static String getPoster(String id) {
        initialize();
        return getOurMovies().get(id).getPoster();
    }

    public static int getMinutes(String id) {
        initialize();
        return getOurMovies().get(id).getMinutes();
    }

    public static String getCountry(String id) {
        initialize();
        return getOurMovies().get(id).getCountry();
    }

    public static String getDirector(String id) {
        initialize();
        return getOurMovies().get(id).getDirector();
    }

    public static int size() {
        return getOurMovies().size();
    }

    public static ArrayList<String> filterBy(Filter f) {
        initialize();
        ArrayList<String> list = new ArrayList<String>();
        for(String id : getOurMovies().keySet()) {
            if (f.satisfies(id)) {
                list.add(id);
            }
        }
        
        return list;
    }

	public static HashMap<String, Movie> getOurMovies() {
		return ourMovies;
	}

	public static void setOurMovies(HashMap<String, Movie> ourMovies) {
		MovieDatabase.ourMovies = ourMovies;
	}

}
