package dummy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import clubcf.algo.Stemmer;
import clubcf.dao.service.ServiceDAO;
import clubcf.factory.DBConnection;
import clubcf.model.ClubCF;
import clubcf.model.Clustering;
import clubcf.model.Services;

public class Main {

	static String serviceName = "";
	static double maxValue = 0d;
	static int index =-1;
	static boolean isWordNet = false;

	public static void main(String[] args){
		ServiceDAO dao = new ServiceDAO();
		Map<String,List<Double>> similarityMatrix = new LinkedHashMap<String,List<Double>>();
		List<Services> allServices = dao.getAllDetails(10);
		List<Services> allServicesDuplicate =allServices;
		Iterator<Services> itrOuter = allServices.iterator();
		Clustering clusterSerivice = new Clustering();
		while(itrOuter.hasNext()){
			Iterator<Services> itrInner = allServicesDuplicate.iterator();
			Services serviceX = itrOuter.next();
			ArrayList<Double> similiarity = new ArrayList<Double>();
			similarityMatrix.put(serviceX.getServiceName(), similiarity);
			while(itrInner.hasNext()){
				similiarity.add(clusterSerivice.calculateCharacteristicSimilarity(serviceX, itrInner.next(),isWordNet));
				getMax(similiarity,serviceX.getServiceName());
			}
		}
		printMatrix(similarityMatrix);
		for(int i = 0 ; i < 3; i++){
			Main.maxValue =0;
			clusterSerivice.reductionStep(similarityMatrix, Main.serviceName, Main.index);
			printMatrix(similarityMatrix);
		}
	}
	
	public static void getMax(List<Double> similarity, String serviceName) {
		int index = 0;
		for(double value : similarity ){
			if(value > Main.maxValue){
				Main.serviceName = serviceName;
				Main.maxValue =  value;
				Main.index = index;
			}
			index++;
		}
	}

	public static void printMatrix(Map<String,List<Double>> matrix){
		Iterator<String> keys = matrix.keySet().iterator();
		while(keys.hasNext())
			System.out.print("\t"+keys.next());
		System.out.println();
		keys = matrix.keySet().iterator();
		while(keys.hasNext()){
			String key = keys.next();
			System.out.print(key);
			for(Double similarityValue: matrix.get(key))
				System.out.printf( similarityValue == -1 ? "\t/" : "\t%.3f", similarityValue);
			System.out.println();
		}
		System.out.println("Max Value: "+Main.maxValue+"Present At ("+Main.serviceName+","+Main.index+") and ("+Main.index+","+Main.serviceName+")");
	}
	
	public static void readExcelAndAddInToDB() throws Exception {
		ReadExcelData readExcelData = new ReadExcelData();
		List list = readExcelData.getListFromExcel();
		Iterator iterator = list.iterator();
		int counter = 0;
		while (iterator.hasNext()) {
			ClubCF clubCF = (ClubCF) iterator.next();
			if (counter == 0) {
				counter++;
				continue;
			}
			try {
				stemmer(clubCF);
			} catch (Exception e) {
				e.printStackTrace();
			}
			counter++;
		}
	}

	public static String stemmer(ClubCF clubCF) throws Exception {
		String stemword = "";
		String[] strings = clubCF.getTags().split(",");
		for (int i = 0; i < strings.length; i++) {
			if (i != 0) {
				stemword += ",";
			}
			String word = strings[i];
			//char[] w = new char[501];
			Stemmer s = new Stemmer();
			//String u;
			char[] array = word.toCharArray();
			for (char ch : array) {
				ch = Character.toLowerCase((char) ch);
				s.add(ch);
			}
			s.stem();
			stemword += s.toString();
		}
		DBConnection connection = new DBConnection();
		connection.insertInDB(clubCF, stemword);
		return stemword;
	}
}
