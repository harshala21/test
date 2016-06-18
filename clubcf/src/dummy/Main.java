package dummy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.omg.PortableServer.ServantRetentionPolicyValue;

import clubcf.algo.Stemmer;
import clubcf.dao.UserDAO;
import clubcf.dao.service.ServiceDAO;
import clubcf.factory.DBConnection;
import clubcf.model.ClubCF;
import clubcf.model.Cluster;
import clubcf.model.Clustering;
import clubcf.model.Rating;
import clubcf.model.ServicePair;
import clubcf.model.Services;

public class Main {

	static int row = -1;
	static double maxValue = 0d;
	static int index =-1;
	static boolean isWordNet = false;
	public static ArrayList<Services> predictedRatings = new ArrayList<Services>(); 
	static long activeuser = 0;
	 public static long activeService = 0; 

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
				 		System.out.println("Not Implemented");
				 		break;
				 	case 1:
				 		ServiceDAO dao = new ServiceDAO();
						List<Cluster> similarityMatrix = new ArrayList<Cluster>();
						List<Services> allServices = dao.getAllDetails(10);
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
						sc.next();
						for(int i = 0 ; i < 4; i++){
							Main.maxValue =0;
							clusterService.reductionStep(similarityMatrix, Main.row, Main.index);
							printMatrix(similarityMatrix);
							sc.next();
						}
						break;
				 	case 3:
				 		System.out.println("Not Implemented");
				 		break;
				 	}
				 	
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
				 System.out.println("MEA: "+mea/Main.predictedRatings.size());
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
			System.out.println("\n\n\t\tMain Menu\n0. Exit\n1. Phase I Clustering\n2. Flitering\n3. Recommendation\n4. Result");
			System.out.print("Your Choice: ");
			return sc.nextInt();
		 }catch(Exception e){
			 System.out.println("Input should be only be Integers!\n Halting System ");
		 }
		
		return 0;	
	}
	
	private static int printClusteringMenu(Scanner sc) {
		try{
			System.out.println("0. Previous Menu \n1. Without WordNet\n2. With WordNet");
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
