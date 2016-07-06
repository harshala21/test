package dummy;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import clubcf.algo.Stemmer;
import clubcf.dao.UserDAO;
import clubcf.dao.cluster.ClusterDAO;
import clubcf.dao.service.ServiceDAO;
import clubcf.factory.DBConnection;
import clubcf.model.ClubCF;
import clubcf.model.Cluster;
import clubcf.model.Clustering;
import clubcf.model.Rating;
import clubcf.model.ServicePair;
import clubcf.model.Services;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.IndexWordSet;
import net.didion.jwnl.data.PointerUtils;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.data.list.PointerTargetTree;
import net.didion.jwnl.data.list.PointerTargetTreeNode;
import net.didion.jwnl.dictionary.Dictionary;

public class Main {

	
	static {
		try {
			JWNL.initialize(new FileInputStream(new File("F:\\git\\test\\clubcf\\Wordnet\\jwnl14-rc2\\config\\file_properties.xml")));
			Main.dict = Dictionary.getInstance() ;
		} catch (FileNotFoundException | JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	static int limit = 50;
	static Dictionary dict;
	static int row = -1;
	static double maxValue = 0d;
	static int index =-1;
	static boolean isWordNet = false;
	public static ArrayList<Services> predictedRatings = new ArrayList<Services>(); 
	static long activeuser = 0;
	public static long activeService = 0; 
	
	private static void demonstrateTreeOperation(IndexWord word, HashSet<String> semanticWords) throws JWNLException {
		// Get all the hyponyms (children) of the first sense of <var>word</var>
		PointerTargetTree hyponyms = PointerUtils.getInstance().getHyponymTree(word.getSense(1));
		//System.out.println("Hyponyms of \"" + word.getLemma() + "\":");
	
		Iterator<PointerTargetTreeNode> itr = hyponyms.getRootNode().getChildTreeList().iterator();
		while(itr.hasNext()){
			PointerTargetTreeNode node = itr.next();
			Synset s = node.getSynset();
			for (Word ss :s.getWords()){
				semanticWords.add(ss.getLemma());
			}
		}
	}
	
	 /*public static void main(String[] args){
		 Scanner sc = new Scanner(System.in);
		 System.out.print("Please enter any word: ");
		 
	 }
	 */
	public static void main(String[] args){
		System.out.println("Club CF");
		Scanner sc = new Scanner(System.in);
		 int choice = -1;
		 do{
			 choice  = printMenu(sc);
			 switch(choice){
			 case 1:
				 	System.out.println("Choosen Clustering");
				 	int innerChoice = printClusteringMenu(sc);
				 	switch(innerChoice){
				 	case 0:
				 		break;
				 	case 2:
				 		try {
				 			ServiceDAO dao = new ServiceDAO();
				 			List<Services> allServices = dao.getAllDetails(1000);
				 			System.out.println("Performing Symantic Analysis for service: ");
				 			int serviceCount =1;
				 			HashSet<String> semanticWords = new HashSet<String>();
				 			for(Services service : allServices){
				 				System.out.println(serviceCount+". "+ service.getServiceName());
				 				String[] stemWords = service.getStemWord().split(",");
				 				for(String st: stemWords )
				 					System.out.println(st);
				 				for(String stem : stemWords){
				 					IndexWordSet synonyms = Main.dict.lookupAllIndexWords(stem);
				 					for(IndexWord s : synonyms.getIndexWordArray()){
										 //System.out.println(s.getLemma());
										 demonstrateTreeOperation(s,semanticWords);
									 }
				 				}
				 				System.out.println(semanticWords.size());
				 				dao.updateSemanticWordToDB(semanticWords,service.getServiceID());
				 				serviceCount++;
				 			}
							 //IndexWordSet synonyms = Main.dict.lookupAllIndexWords(sc.next());
						} catch (JWNLException e) {
							// TODO Auto-generated catch block	
							e.printStackTrace();
						}
				 		isWordNet = true;
				 	case 1:
				 		ServiceDAO dao = new ServiceDAO();
						List<Cluster> similarityMatrix = new ArrayList<Cluster>();
						List<Services> allServices = dao.getAllDetails(100);
						Clustering clusterService = new Clustering();
						for(int row=0 ; row < allServices.size(); row++){
							Cluster cluster = new Cluster(); 
							Services serviceX = allServices.get(row);
							cluster.setName(serviceX.getServiceName());
							ArrayList<Double> similiarity = new ArrayList<Double>();
							cluster.setValues(similiarity);
							similarityMatrix.add(cluster);
							for(int innerRow = 0; innerRow < allServices.size();innerRow++){
								similiarity.add(clusterService.calculateCharacteristicSimilarity(serviceX, allServices.get(innerRow),isWordNet));
								getMax(similiarity,row);
							}
						}
						printMatrix(similarityMatrix);
						System.out.println("Please type any character and Press return to continue...");
						sc.next();
						String nextPhase = "";
						do{
							Main.maxValue =0;
							clusterService.reductionStep(similarityMatrix, Main.row, Main.index);
							printMatrix(similarityMatrix);
							System.out.println("Please type \"stop\" to save and return to Main Menu else to continue next Phase of clustering");
							nextPhase = sc.next();
							if("Stop".equalsIgnoreCase(nextPhase)){
								new ClusterDAO().saveCluster(similarityMatrix,allServices);
							}
						}while(!"Stop".equalsIgnoreCase(nextPhase));
						break;
				 	}
				 	Main.isWordNet = false;
				 break;
			 case 2:
				 	System.out.println("Choosen Flitering");
				 	Rating rating = new Rating();
				 	int activeUserID = printAndAcceptUser(sc);
				 	Main.activeuser = activeUserID;
					//rating.diplayRatingMatrix();
					System.out.println();
					ArrayList<Long> unRatedServices = Rating.dao.findUnratedServices(0,activeUserID);
					HashMap<Long,ArrayList<ServicePair>> servicePairs = rating.calculateAndPrint(unRatedServices,true,false,0);
					System.out.print("\nPlease provide Rating similarity Threshhold value:");
					rating.getNeigbhours(sc.nextDouble(),servicePairs,activeUserID,true);
				 break;
			 case 3:
				 if(Main.predictedRatings.isEmpty())
					 System.out.println("Please other actions before this.");
				 else {
					 ServiceDAO dao = new ServiceDAO();
					 System.out.println("Recommendation for "+dao.getServiceName(Main.activeService)+ " are (Acending order):");
					 Comparator<Services> comparator = new Comparator<Services>() {	
						    public int compare(Services a, Services b) {
						        if(a.getPredictedRatingDifference() > b.getPredictedRatingDifference())
						        	return 1;
						        else if ( a.getPredictedRatingDifference() < b.getPredictedRatingDifference())
						        	return -1;
						        else 
						        	return 0;
						    }
						};
					Collections.sort(Main.predictedRatings,comparator);
					int i =1;
					for(Services service : Main.predictedRatings){
						System.out.println(i+" : "+service.getApiName());
						i++;
					}
				 }				
				 break;
			 case 4:
				 System.out.println("MAE Factor: Experimental Evaluation");
				 double mea= 0;
				 for(Services service : Main.predictedRatings){
					mea += service.getPredictedRatingDifference();
				 }
				 System.out.println("MAE: "+ mea/Main.predictedRatings.size());
				 break;
			 case 5: 
				try {
					System.out.println("Please povide path to the Excel File (2003 Format)");
					String path = sc.next();
					readExcelAndAddInToDB(path);
					DBConnection connection = new DBConnection();
					List<ClubCF> dbList = connection.getDBRecords(Main.limit);
					float alpha = (float) 0.5;
					String outPut = "";
					
					float matrix[][] = new float[Main.limit][Main.limit];
					
					for (int i = 0; i < dbList.size(); i++) {
						ClubCF t1 = (ClubCF) dbList.get(i);
						List<String> list1 = new ArrayList<String>(Arrays.asList(t1
								.getStemWord().split(",")));
						List<String> list3 = new ArrayList<String>(Arrays.asList(t1
								.getApis().split(",")));
						
						for (int j = 0; j < dbList.size(); j++) {
							outPut += "'";
							ClubCF t2 = (ClubCF) dbList.get(j);
							List<String> list2 = new ArrayList<String>(Arrays.asList(t2
									.getStemWord().split(",")));
							List<String> list4 = new ArrayList<String>(Arrays.asList(t2
									.getApis().split(",")));

							float D_sim = new Main().intersection(list1, list2)
									/ new Main().union(list1, list2);
							float F_sim = new Main().intersection(list3, list4)
									/ new Main().union(list3, list4);

							float C_Sim = (float) (alpha * D_sim + (1 - alpha) * F_sim);

							String output = String.format("%.3f", C_Sim);

							//System.out.print(" " + output + " ");
							
							matrix[i][j] = C_Sim;
							
							outPut += output + "'";
							if ((j + 1) < dbList.size()) {
								outPut += ",";
							}
						}
						// connection.insertInAverageDB(outPut);
						//System.out.print("\n");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 break;
			 case 0:
				 break;
			 default:
				 System.out.println("invalid Choice. PLease Try again");
				 break;
			 }
		 }while(choice != 0);
		 sc.close();
	}
	
	private static int printAndAcceptUser(Scanner sc) {
		int count = new UserDAO().getUsersCount();
		int input = -1;
		try{
			
			System.out.println("Enter User ID ( from 1 to "+count+" )");
			System.out.print("Your Choice: ");
			input =  sc.nextInt();
			if(input <= 0 || input > count)
				throw new RuntimeException();
		 }catch(RuntimeException e){
			 System.out.println("Input should be within provided range!");
			 return printAndAcceptUser(sc);
		}catch(Exception e){
			 System.out.println("Input should be only be Integers and !\n Halting System ");
		 }
		return input;	
	}

	private static int printMenu(Scanner sc) {
		try{
			System.out.println("\n\n\t\tMain Menu\n0. Exit\n1. Clustering\n2. Collaborative Flitering\n3. Recommendations \n4. Result (Mean Absolute Error)\n5. Update Data");
			System.out.print("Your Choice: ");
			return sc.nextInt();
		 }catch(Exception e){
			 System.out.println("Input should be only be Integers!\n Halting System ");
		 }
		
		return 0;	
	}
	
	private static int printClusteringMenu(Scanner sc) {
		try{
			System.out.println("0. Previous Menu \n1. Without Semantic Analysis\n2. With Semantic Analysis");
			System.out.print("Your Choice: ");
			return sc.nextInt();
		 }catch(Exception e){
			 System.out.println("Input should be only be Integers!\n Halting System ");
		 }
		
		return 0;	
	}
	
	

	public static void getMax(List<Double> similarity, int row) {
		int index = 0;
		for(double value : similarity ){
			if(value > Main.maxValue){
				Main.row = row;
				Main.maxValue =  value;
				Main.index = index;
			}
			index++;
		}
	}

	public static void printMatrix(List<Cluster> similarityMatrix){
		Iterator<Cluster> keys = similarityMatrix.iterator();
		while(keys.hasNext())
			System.out.print("\t"+keys.next().getName());
		System.out.println();
		keys = similarityMatrix.iterator();
		while(keys.hasNext()){
			Cluster key = keys.next();
			System.out.print(key.getName());
			for(Double similarityValue: key.getValues())
				System.out.printf( similarityValue == -1 ? "\t/" : "\t%.3f", similarityValue);
			System.out.println();
		}
		System.out.println("Max Value: "+Main.maxValue+"\nPresent At ("+Main.row+","+Main.index+") and ("+Main.index+","+Main.row+")");
	}
	
	public static void readExcelAndAddInToDB(String path) throws Exception {
		ReadExcelData readExcelData = new ReadExcelData(path);
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
	

	
	public float union(List list1, List list2) {	
		Set set = new HashSet();

		set.addAll(list1);
		set.addAll(list2);

		return set.size();
	}

	public float intersection(List list1, List list2) {
		List list = new ArrayList();

		for (String t : (ArrayList<String>) list1) {
			if (list2.contains(t)) {
				list.add(t);
			}
		}

		return list.size();
	}
}
