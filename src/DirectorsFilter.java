public class DirectorsFilter implements Filter {
	private String[] myDirector;
	
	public DirectorsFilter(String director) {
		myDirector = director.split(",");
	}
	
	@Override
	public boolean satisfies(String id) {
		String[] currDirector = MovieDatabase.getDirector(id).split(",");
		for (String d: myDirector) {
			for (String s: currDirector) {
				if (s.equals(d)) {
					return true;
				}
			}
		}
		return false;
	}
}
