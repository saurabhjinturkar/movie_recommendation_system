package in.saurabhjinturkar.RecommendationSystem;

import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;

public class ContentBasedCollaborativeFiltering extends AbstractRecommender {

	public ContentBasedCollaborativeFiltering() throws IOException, TasteException {
		super(Util.getMoviesGenresFile());
	}

	@Override
	public List<String> recommend(int userId, int count) throws TasteException {
		return null;
	}

	@Override
	public void buildModel() throws TasteException {

	}

	@Override
	public void evaluate() throws TasteException {
		System.out.println("Not implemented..");
	}

}
