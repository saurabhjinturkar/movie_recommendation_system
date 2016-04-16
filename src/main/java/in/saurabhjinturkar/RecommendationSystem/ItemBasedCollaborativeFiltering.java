package in.saurabhjinturkar.RecommendationSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

public class ItemBasedCollaborativeFiltering extends AbstractRecommender {

	private ItemBasedRecommender recommender;

	public ItemBasedCollaborativeFiltering() throws IOException, TasteException {
		super(Util.getUsersMoviesFile());
		super.util.loadMovies();
	}

	@Override
	public List<String> recommend(int userId, int count) throws TasteException {
		List<String> output = new ArrayList<String>();

		List<RecommendedItem> recommend = recommender.recommend(userId, count);
		for (RecommendedItem item : recommend) {
			System.out.println(item);
			output.add(util.getMovieName((int) item.getItemID()));
		}
		return output;
	}

	public List<Integer> recommendMovieIds(int userId, int count) throws TasteException {
		List<Integer> output = new ArrayList<Integer>();

		List<RecommendedItem> recommend = recommender.recommend(userId, count);
		for (RecommendedItem item : recommend) {
			System.out.println(item);
			output.add((int) item.getItemID());
		}
		return output;
	}
	
	@Override
	public void buildModel() throws TasteException {
		ItemSimilarity similarity = new UncenteredCosineSimilarity(model);
		recommender = new GenericItemBasedRecommender(model, similarity);
	}

	@Override
	public void evaluate() throws TasteException {

		RecommenderBuilder builder = new RecommenderBuilder() {
			public Recommender buildRecommender(DataModel model) throws TasteException {
				ItemSimilarity similarity = new UncenteredCosineSimilarity(model);
				ItemBasedRecommender recommender = new GenericItemBasedRecommender(model, similarity);
				return recommender;
			}
		};

		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		RecommenderEvaluator evaluator2 = new RMSRecommenderEvaluator();
		System.out.println("Average Absolute Difference Score:");
		System.out.print(evaluator.evaluate(builder, null, model, 0.9, 1.0));
		System.out.println("RMS Score:");
		System.out.print(evaluator2.evaluate(builder, null, model, 0.9, 1.0));

	}

}
