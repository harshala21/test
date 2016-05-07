package clubcf.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dummy.Main;

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
	
	public double calculateCharacteristicSimilarity(Services serviceX, Services serviceY,boolean isWordNet){
		double dSim = calculateDescriptionSimilarity( serviceX, serviceY,isWordNet);
		double fSim = calculateFunctionalitySimilarity( serviceX, serviceY);
		return serviceX.getServiceID() == serviceY.getServiceID() ? -1d : getAlpha() * dSim + (1 - getAlpha()) * fSim ;
	}
	
	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	private double calculateDescriptionSimilarity(Services serviceX, Services serviceY,boolean isWordNet){
		List<String> stemWordX = new ArrayList<String>(Arrays.asList(serviceX.getStemWord().split(",")));
		List<String> stemWordY = new ArrayList<String>(Arrays.asList(serviceY.getStemWord().split(",")));
		return  intersection(stemWordX, stemWordY)/ union(stemWordX, stemWordY);
	}
	
	private double calculateFunctionalitySimilarity(Services serviceX, Services serviceY){
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
	
	public void reductionStep(Map<String,List<Double>> matrix,String serviceName,int index ){
		removeRow(matrix,index);
		computeColumn(matrix,Integer.parseInt(serviceName.substring(1)),index);
	}

	private void computeColumn(Map<String, List<Double>> matrix, int row, int column) {
		Iterator<String> itr = matrix.keySet().iterator();
		int index =0;
		while(itr.hasNext()){
			String serviceName = itr.next();
			List<Double> similarityValues = matrix.get(serviceName);
			similarityValues.set(row-1, similarityValues.get(row-1) != -1 && similarityValues.get(column) != -1 ? (similarityValues.get(row-1)+ similarityValues.get(column))/2 : -1d);
			matrix.get("S"+row).set(index, similarityValues.get(row-1));
			similarityValues.remove(column);
			Main.getMax(similarityValues, serviceName);
			index++;
		}
		
	}

	private void removeRow(Map<String, List<Double>> matrix, int index) {
		matrix.remove("S"+(index+1));		
	}
	
}
