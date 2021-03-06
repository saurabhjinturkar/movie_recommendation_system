package in.saurabhjinturkar.RecommendationSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVStrategy;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class ContentBasedCollaborativeFiltering extends AbstractRecommender {

	public ContentBasedCollaborativeFiltering() throws IOException, TasteException {
		super(Util.getMoviesGenresFile());
		super.util.loadMovies();
	}

	@Override
	public List<String> recommend(int userId, int count) throws TasteException {

		List<String> output = new ArrayList<String>();
		long[] mostSimilarUserIDs = null;

		String csvFile = "src/main/java/Dataset1_With_UserRatings.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		String rating[] = new String[20];
		int id = userId;

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				// use comma as separator
				rating = line.split(cvsSplitBy);
				if (rating[0].equals("" + id)) {
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (line == null) {
			System.out.println("User not present!");
			return new ArrayList<String>();
		}
		
		UserProfile user = calculateUserVector(id, rating);
		System.out.println("Generated profile of the User Id:" + id);
		for (int j = 0; j < user.userPreference.length; j++) {
			System.out.print(user.userPreference[j] + "\t");
		}

		try {

			copyPreviousDataToNewCSVFile();
			inputUserDataToNewCSVFile(user);

			DataModel dm = new FileDataModel(new File("src/main/java/dataset3_cleaned.csv"));

			UserSimilarity similarity = new UncenteredCosineSimilarity(dm);

			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, dm);

			UserBasedRecommender recommender = new GenericUserBasedRecommender(dm, neighborhood, similarity);

			mostSimilarUserIDs = recommender.mostSimilarUserIDs(id, count);
			for (long itemId : mostSimilarUserIDs) {
				//System.out.println(util.getMovieName((int) itemId));
				output.add(util.getMovieName((int) itemId));
			}
		} catch (Exception e) {
			System.out.println("There was an error.");
			e.printStackTrace();
		}

		return output;
	}

	/**
	 * Method which returns movies based on hybrid recommendation. 
	 * 
	 * @param userId
	 * @param movieIDs
	 * @param count
	 * @return
	 * @throws TasteException
	 */
	public List<String> hybridRecommend(int userId, List<Integer> movieIDs, int count) throws TasteException {
		System.out.println("Generating hybrid recommendations on items:" + movieIDs);
		List<String> output = new ArrayList<String>();
		long[] mostSimilarUserIDs = null;

		String csvFile = "src/main/java/Dataset1_With_UserRatings.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		String rating[] = new String[20];
		int id = userId;

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				// use comma as separator
				rating = line.split(cvsSplitBy);
				if (rating[0].equals("" + id)) {
					break;
				}
			}
			boolean present = false;
			for (int i = 1; i < rating.length; i++) {
				present = false;
				for (int j = 0; j < movieIDs.size(); j++) {
					if (i == movieIDs.get(j)) {
						present = true;
					}
				}
				if (!present) {
					rating[i] = "0";
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		UserProfile user = calculateUserVector(id, rating);

		System.out.println("Generated profile of the User Id:" + id);
		for (int j = 0; j < user.userPreference.length; j++) {
			System.out.print(user.userPreference[j] + "\t");
		}
		System.out.println();

		try {

			copyPreviousDataToNewCSVFile();
			inputUserDataToNewCSVFile(user);

			DataModel dm = new FileDataModel(new File("src/main/java/dataset3_cleaned.csv"));

			UserSimilarity similarity = new UncenteredCosineSimilarity(dm);

			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, dm);

			UserBasedRecommender recommender = new GenericUserBasedRecommender(dm, neighborhood, similarity);

			mostSimilarUserIDs = recommender.mostSimilarUserIDs(id, count);
			for (long itemId : mostSimilarUserIDs) {
				output.add(util.getMovieName((int) itemId));
			}
		} catch (Exception e) {
			System.out.println("There was an error.");
			e.printStackTrace();
		}

		return output;
	}

	@Override
	public void buildModel() throws TasteException {

	}

	@Override
	public void evaluate() throws TasteException {
		System.out.println("Not implemented..");
	}

	/**
	 * Method which creates a copy of data set.
	 * @throws IOException
	 */
	private void copyPreviousDataToNewCSVFile() throws IOException {

		FileWriter dataset3 = new FileWriter("src/main/java/dataset3_cleaned.csv", false);
		FileReader dataset2 = new FileReader(Util.getMoviesGenresFile());

		try {
			int count = dataset2.read();
			while (count != -1) {
				dataset3.write(count);
				count = dataset2.read();
			}
			dataset3.close();
			dataset2.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * Method to append user profile to copy of dataset.
	 * 
	 * @param user
	 * @throws IOException
	 */
	public void inputUserDataToNewCSVFile(UserProfile user) throws IOException {

		FileWriter dataset3 = new FileWriter("src/main/java/dataset3_cleaned.csv", true);
		CSVPrinter csvFilePrinter = null;

		try {
			csvFilePrinter = new CSVPrinter(dataset3, new CSVStrategy(',', '\n', ':'));

			for (int i = 1; i < user.userPreference.length; i++) {
				csvFilePrinter.print(String.valueOf((int) user.userPreference[0]));
				csvFilePrinter.print(i + "");
				csvFilePrinter.print(String.valueOf(user.userPreference[i]));
				csvFilePrinter.println();
			}
			dataset3.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * Calculate the user profile.
	 * @param id
	 * @param rating
	 * @return
	 */
	public UserProfile calculateUserVector(int id, String[] rating) {
		UserProfile user = new UserProfile(id);
		int moviesRated = 0;

		for (int j = 1; j < user.userPreference.length; j++) {
			user.userPreference[j] = 0;
		}

		String csvFile = "src/main/java/Dataset2.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		String movieGenre[] = new String[8];

		try {
			int i = 0;

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				movieGenre = line.split(cvsSplitBy);
				if (i != 0 && i < rating.length) {
					for (int j = 1; j < user.userPreference.length; j++) {
						user.userPreference[j] = user.userPreference[j]
						    + (Integer.parseInt(movieGenre[j].trim()) * Integer.parseInt(rating[i].trim()));
					}
				}
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		for (int l = 1; l < rating.length; l++) {
			if (Integer.parseInt(rating[l].trim()) != 0) {
				moviesRated++;
			}
		}
		for (int j = 1; j < user.userPreference.length; j++) {
			user.userPreference[j] = user.userPreference[j] / moviesRated;
		}
		float sum = 0;
		for (int i = 1; i < user.userPreference.length; i++) {
			sum = sum + user.userPreference[i];
		}
		float avg = sum / (user.userPreference.length - 1);
		for (int j = 1; j < user.userPreference.length; j++) {
			if (user.userPreference[j] > avg || user.userPreference[j] == avg) {
				user.userPreference[j] = 1;
			} else {
				user.userPreference[j] = 0;
			}
		}
		return user;
	}

}
