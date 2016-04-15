package in.saurabhjinturkar.RecommendationSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class UserBasedCollaborativeFiltering extends AbstractRecommender {
	private UserBasedRecommender recommender;

	public UserBasedCollaborativeFiltering() throws IOException, TasteException {
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

	@Override
	public void buildModel() throws TasteException {
		UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
		UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
		recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
	}

	@Override
	public void evaluate() throws TasteException {
		RecommenderBuilder builder = new RecommenderBuilder() {
			public Recommender buildRecommender(DataModel model) throws TasteException {
//				UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
//				UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
//				UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
				return recommender;
			}
		};

		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		RecommenderEvaluator evaluator2 = new RMSRecommenderEvaluator();
		System.out.println("\nAverage Absolute Difference Score:");
		System.out.print(evaluator.evaluate(builder, null, model, 0.9, 1.0));
		System.out.println("\nRMS Score:");
		System.out.print(evaluator2.evaluate(builder, null, model, 0.9, 1.0));

	}

}
