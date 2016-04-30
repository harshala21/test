package clubcf.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Clustering {

	double alpha = 0.5;
	//calculating similarities
	//clustering using Agglomerative Hierarchical(AHC) Algo
	
	/*
	 * 
	 * 1. get Stem words and API names
	 * 2. using above data calculate Description and Functional Similarity
	 * 3. Calculate Characteristic similarity
	 * 4. Cluster data using AHC ALGO
	 * 5. Save to DB.  
	 */
	
	public double calculateCharacteristicSimilarity(double dSim,double fSim){
		return getAlpha() * dSim + (1 - getAlpha()) * fSim ;
	}
	
	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public double calculateDescriptionSimilarity(Services serviceX, Services serviceY){
		List<String> stemWordX = new ArrayList<String>(Arrays.asList(serviceX.getStemWord().split(",")));
		List<String> stemWordY = new ArrayList<String>(Arrays.asList(serviceY.getStemWord().split(",")));
		return  intersection(stemWordX, stemWordY)/ union(stemWordX, stemWordY);
	}
	
	public double calculateFunctionalitySimilarity(Services serviceX, Services serviceY){
		 List<String> stemWordX = new ArrayList<String>(Arrays.asList(serviceX.getApiName().split(",")));
		List<String> stemWordY = new ArrayList<String>(Arrays.asList(serviceY.getApiName().split(",")));
		 return intersection(stemWordX, stemWordY)/ union(stemWordX, stemWordY);
	}
	
	private float union(List<String> list1, List<String> list2) {	
		Set<String> set = new HashSet<String>();
		set.addAll(list1);
		set.addAll(list2);
		return set.size();
	}

	private float intersection(List<String> list1, List<String> list2) {
		List<String> list = new ArrayList<String>();
		for (String t : (ArrayList<String>) list1) {
			if (list2.contains(t)) {
				list.add(t);
			}
		}
		return list.size();
	}
	
	
}
