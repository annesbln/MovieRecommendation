public class MinutesFilter implements Filter {
	private int myMinMinutes;
	private int myMaxMinutes;
	
	public MinutesFilter(int minMinutes, int maxMinutes) {
		myMinMinutes = minMinutes;
		myMaxMinutes = maxMinutes;
	}
	
	@Override
	public boolean satisfies(String id) {
		int minutes = MovieDatabase.getMinutes(id);
		if (myMinMinutes<=minutes && minutes<=myMaxMinutes) {
			return true;
		} else {
			return false;
		}
	}
}
