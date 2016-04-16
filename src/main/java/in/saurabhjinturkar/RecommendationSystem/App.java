package in.saurabhjinturkar.RecommendationSystem;

import java.io.IOException;
import java.util.Scanner;

import org.apache.mahout.cf.taste.common.TasteException;

public class App {

	private UserBasedCollaborativeFiltering user;
	private ItemBasedCollaborativeFiltering item;
	private ContentBasedCollaborativeFiltering content;
	private int userId;
	
	public static void main(String[] args) {
		App app = new App();
		app.input();
		app.recommend();
	}
	
	private void input() {
		Scanner in = new Scanner(System.in);
		System.out.println("#########################");
		System.out.println("Movie Recommendation System");
		System.out.println("#########################");
		System.out.println("Please enter user id :");
		this.userId = in.nextInt();
		in.close();
	}
	
	private void recommend() {

		try {
			user = new UserBasedCollaborativeFiltering();
			System.out.println(user.recommend(userId, 3));
			user.evaluate();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (TasteException e1) {
			e1.printStackTrace();
		}

		try {
			item = new ItemBasedCollaborativeFiltering();
			System.out.println(item.recommend(userId, 3));
			item.evaluate();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (TasteException e1) {
			e1.printStackTrace();
		}
		
		try {
			content = new ContentBasedCollaborativeFiltering();
			System.out.println(content.recommend(userId, 3));
			System.out.println("Hybrid Recommendation");
			System.out.println(content.hybridRecommend(userId, item.recommendMovieIds(userId, 10) , 3));
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (TasteException e1) {
			e1.printStackTrace();
		}
	}
}
