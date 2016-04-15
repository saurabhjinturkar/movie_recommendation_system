package in.saurabhjinturkar.RecommendationSystem;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;

public abstract class AbstractRecommender {

	protected DataModel model;
	protected Util util;

	public AbstractRecommender(String fileName) throws IOException, TasteException {
		this.util = new Util();
		init(fileName);
		buildModel();
	}

	public void init(String fileName) throws IOException {
		model = new FileDataModel(new File(fileName));
	}

	abstract public List<String> recommend(int userId, int count) throws TasteException;

	abstract public void buildModel() throws TasteException;
	
	abstract public void evaluate() throws TasteException;

}
