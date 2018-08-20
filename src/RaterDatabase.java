import java.util.*;

public class RaterDatabase {
    private static HashMap<String,Rater> ourRaters; //String is raterID!
    
    public static void initialize(String raterFile) {
    	ourRaters = new HashMap<String,Rater>();
        FileLoader fl = new FileLoader();
        ArrayList<Rater> raterList = fl.loadRaters(raterFile);
        for(Rater r : raterList) {
        	ourRaters.put(r.getID(), r);
        }
    }

	public static Rater getRater(String id) {
		return ourRaters.get(id);
	}
	
	public static ArrayList<Rater> getRaters() {
		ArrayList<Rater> list = new ArrayList<Rater>();
		for (String id: ourRaters.keySet()) {
			list.add(ourRaters.get(id));
		}
		return list;
	}

	public static int size() {
		return ourRaters.size();
	}
}
