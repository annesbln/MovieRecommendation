public class GenreFilter implements Filter {
	private String myGenre;
	
	public GenreFilter(String genre) {
		myGenre = genre;
	}
	
	@Override
	public boolean satisfies(String id) {
		String genres = MovieDatabase.getGenres(id);
		if (genres.contains(myGenre)) {
			return true;
		} else {
			return false;
		}
	}
}
