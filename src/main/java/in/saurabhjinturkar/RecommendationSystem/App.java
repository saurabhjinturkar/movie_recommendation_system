package in.saurabhjinturkar.RecommendationSystem;

import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;

public class App {

	private UserBasedCollaborativeFiltering user;
	private ItemBasedCollaborativeFiltering item;
	private ContentBasedCollaborativeFiltering content;

	public static void main(String[] args) {
		App app = new App();
		app.init();
	}

	private void init() {
		System.out.println("#########################");
		System.out.println("Movie Recommendation System");
		System.out.println("#########################");

		try {
			user = new UserBasedCollaborativeFiltering();
			System.out.println(user.recommend(616, 3));
			user.evaluate();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (TasteException e1) {
			e1.printStackTrace();
		}

		try {
			item = new ItemBasedCollaborativeFiltering();
			System.out.println(item.recommend(616, 3));
			item.evaluate();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (TasteException e1) {
			e1.printStackTrace();
		}
	}
}
