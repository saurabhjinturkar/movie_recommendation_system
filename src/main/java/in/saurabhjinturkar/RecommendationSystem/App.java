package in.saurabhjinturkar.RecommendationSystem;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class App {

	public static void main(String[] args) {
		try {
			DataModel model = new FileDataModel(new File("src/main/java/dataset2_cleaned.csv"));

			
			UserSimilarity similarity = new PearsonCorrelationSimilarity(model);

			// Step 3:- Create UserNeighbourHood object. (No Need to
	        // create ItemNeighbourHood object while creating
	        // Item based Recommendation)
			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(1, similarity, model);

			// Step 4:- Create object of UserBasedRecommender or
	        // ItemBasedRecommender
			UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
			List<RecommendedItem> recommend = recommender.recommend(0, 5);
			System.out.println(recommend);
			
			
//			ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);
//
//			
//			ItemBasedRecommender recommender = new GenericItemBasedRecommender(model, similarity);
//			List<RecommendedItem> recommendations = recommender.recommend(272, 3);
//			
//			for (RecommendedItem recommendedItem : recommendations) {
//				System.out.println(recommendedItem);
//			}

//			RecommenderBuilder builder = new RecommenderBuilder() {
//				public Recommender buildRecommender(DataModel model) throws TasteException {
//					// ItemSimilarity similarity;
//			        // similarity = new TanimotoCoefficientSimilarity(model);
//			        // ItemBasedRecommender recommender = new
//			        // GenericItemBasedRecommender(model, similarity);
//
//					UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
//
//					// Step 3:- Create UserNeighbourHood object. (No Need to
//			        // create ItemNeighbourHood object while creating
//			        // Item based Recommendation)
//					UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
//
//					// Step 4:- Create object of UserBasedRecommender or
//			        // ItemBasedRecommender
//					UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
//
//					return recommender;
//				}
//			};
//
//			RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
//			RecommenderEvaluator evaluator2 = new RMSRecommenderEvaluator();
//			double evaluation = evaluator.evaluate(builder, null, model, 0.999, 1.0);
//			System.out.println(evaluation);
//			System.out.println(evaluator2.evaluate(builder, null, model, 0.999, 1.0));
//			double count = 0;
//			for (int i = 0; i < 100; i++) {
//				// double result = evaluator2.evaluate(builder, null, model,
//				// 0.9, 1.0);
//				// count += result;
//			}
//
//			// System.out.println(count / 100);
//			// System.out.println(model.getPreferencesFromUser(616));

		} catch (IOException e) {
			e.printStackTrace();
		} catch (TasteException e) {
			e.printStackTrace();
		}
	}
}
