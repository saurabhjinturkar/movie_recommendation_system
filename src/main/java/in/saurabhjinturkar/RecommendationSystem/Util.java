package in.saurabhjinturkar.RecommendationSystem;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVParser;

public class Util {

	private static final String MOVIES_CSV = "src/main/java/movies.csv";
	private static final String USERS_MOVIES_CSV = "src/main/java/dataset1_cleaned_average_division.csv";
	private static final String MOVIES_GENRES_CSV = "src/main/java/dataset2_cleaned.csv";

	private List<String> movies;

	public Util() {
		movies = new ArrayList<String>();
	}

	public void loadMovies() throws IOException {
		Reader in = new FileReader(MOVIES_CSV);
		CSVParser records = new CSVParser(in);
		String[][] values = records.getAllValues();
		for (String[] record : values) {
			String lastName = record[0];
			movies.add(lastName);
		}
		System.out.println(movies.size() + " movies loaded in util...");
	}

	public String getMovieName(int index) {
		return movies.get(index);
	}

	public static String getMoviesGenresFile() {
		return MOVIES_GENRES_CSV;
	}

	public static String getUsersMoviesFile() {
		return USERS_MOVIES_CSV;
	}

	public static String getMoviesFile() {
		return MOVIES_CSV;
	}
}
