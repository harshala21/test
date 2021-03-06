package clubcf.model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import clubcf.dao.service.ServiceDAO;
import clubcf.factory.DBConnection;
import dummy.Main;


public class Rating {
Connection con;
ResultSet results;
PreparedStatement stmt;
DBConnection conne = new DBConnection();
public static ServiceDAO dao = new ServiceDAO();		
String serviceTable = "sample_data",ratingTable="rating_matrix";	

public static void main(String[] args) {
		dao.getClusterServices(2);
	} 	
	
	public void calculateRatingSimialrity(HashMap<Long, ArrayList<ServicePair>> servicePairs) {
		ArrayList<ServicePair> pairList = null;
		Iterator<Long> itr = servicePairs.keySet().iterator();
		while(itr.hasNext()){
			pairList = servicePairs.get(itr.next());
			calculateRatingSimialrity(pairList);	
		}
	}

	private void calculateRatingSimialrity(ArrayList<ServicePair> pairList) {
		double meanUnRated = 0;
		double meanOther = 0;
		
		for(ServicePair pair : pairList){
			meanUnRated = getMean(dao.getSimlarityRatings(pair.getUnRatedServiceID()));
			meanOther = getMean(dao.getSimlarityRatings(pair.getOtherServiceID()));
			pair.setRatingSimilarity(calculateRatingSimilarity(dao.getSimlarityRatingsWithOutZero(pair.getUnRatedServiceID(),pair.getClusterID()), dao.getSimlarityRatingsWithOutZero(pair.getOtherServiceID(),pair.getClusterID()), meanUnRated, meanOther));
			pair.setEnhancedRatingSimilarity(calculateEnhancedRatingSimilarity(pair.getRatingSimilarity(),dao.getNonZeroRatingSize(pair.getUnRatedServiceID()),dao.getNonZeroRatingSize(pair.getOtherServiceID()),dao.getPairIntesection(pair)));
		}
	}

	public ArrayList<ServicePair> createServiceGroups(long id, long clusterID) {
		ArrayList<ServicePair>  servicePair = new ArrayList<ServicePair>();		
		String clusterSQL = "select cluster_name, services from clusters where cluster_id = ?";
		String services = null;
		String clusterName = null;
		try{
			con = conne.getDBConnection();
			stmt= con.prepareStatement(clusterSQL);
			stmt.setLong(1, clusterID);
			results = stmt.executeQuery();
			while( results.next() ){
				services = results.getString(2);
				clusterName = results.getString(1);
			}
			String[] serviceList = services.split(",");
			for(String serviceID : serviceList){
				if(id == Long.parseLong(serviceID))
					continue;
				else{
					servicePair.add(new ServicePair(id,Long.parseLong(serviceID),clusterID));
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			conne.close(stmt);
			conne.close(results);
			conne.close(con);
		}
		return servicePair.size() > 0 ? servicePair : null;
	}
	
	public void diplayRatingMatrix(){
		try{
			System.out.println("Initial Rating Data:");
			con = conne.getDBConnection();
			String usersQuery = "select user_id, username from users";
			stmt = con.prepareStatement(usersQuery);
			results = stmt.executeQuery();
			Map<Long,String> usersCollection = new HashMap<Long,String>();
			while (results.next()){
				usersCollection.put(results.getLong(1), results.getString(2));
			}
			System.out.print("users\\Servies\t");
			String servicesQuery = "select sid from "+serviceTable; 
			stmt = con.prepareStatement(servicesQuery);
			results = stmt.executeQuery();
			while (results.next()){
				System.out.print(results.getString(1)+"\t");
			}
			System.out.println();
			String ratingData = "select ratings from "+ratingTable+" where user_id = ? order by service_id" ;
			stmt = con.prepareStatement(ratingData);
			Iterator<Long> mapItr = usersCollection.keySet().iterator();
			while (mapItr.hasNext()){
				long id = mapItr.next();
				stmt.setLong(1, id);
				results = stmt.executeQuery();
				System.out.print(usersCollection.get(id)+"\t\t");
				while(results.next()){
					System.out.print(results.getDouble(1)+"\t");
				}
				System.out.println();
			}
		} catch(SQLException e){
			e.printStackTrace();
		}finally {
			conne.close(stmt);
			conne.close(results);
			conne.close(con);
		}
	}
	
	public int getClusterSize( long clusterID){
		int size = 0;
		try{
			String query  = "select services from clusters where cluster_id = ?";
			con = conne.getDBConnection();
			stmt = con.prepareStatement(query);
			stmt.setLong(1, clusterID);
			results = stmt.executeQuery();
			while(results.next()){
				size = results.getString(1).split(",").length;
			}
		}catch (SQLException e){
			e.printStackTrace();
		}finally {
			conne.close(stmt);
			conne.close(results);
			conne.close(con);
		}
		return size > 0 ? size : null;
	}
	
		
	public long findClusters(long serviceID){
		long clusterID = 0;
		try {
			String query = "select  cluster_id from clusters where services like ?";
			con = conne.getDBConnection();
			stmt = con.prepareStatement(query);
			stmt.setString(1, "%"+serviceID+"%");
			results = stmt.executeQuery();
			while(results.next()){
				clusterID = results.getLong(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			conne.close(stmt);
			conne.close(results);
			conne.close(con);
		}
		return clusterID > 0 ? clusterID : null ;
	}
	
	public double calculateRatingSimilarity(double[] serviceX,double[] serviceY,double meanX, double meanY){
		double numerator = 0;
		double denominatorX = 0, denominatorY = 0;
		for(int i = 0; i < serviceX.length && i < serviceY.length ; i++){
			numerator += (serviceX[i] - meanX)*( serviceY[i] - meanY);
			denominatorX += Math.pow((serviceX[i] - meanX), 2);
			denominatorY += Math.pow(( serviceY[i] - meanY), 2);
		}
		return numerator /(Math.sqrt(denominatorX) * Math.sqrt(denominatorY));
	}
	
	public double calculateEnhancedRatingSimilarity(double ratingSimilarity, double xLength, double yLength, double xyIntersection){
		return ((2 * (xyIntersection))/(xLength+yLength))* ratingSimilarity;
	}
	
	
	private double getMean(double[] service){
		double mean = 0;
		double sum = 0;
		for(double value : service ){
			sum += value;
		}
		mean = sum/service.length;
		return mean;
	}

	public void getNeigbhours(double threshHold, HashMap<Long, ArrayList<ServicePair>> servicePairs,long activeUserID,boolean neighbour) {
		ArrayList<ServicePair> neighbours = new ArrayList<ServicePair>();
		Iterator<Long> itr = servicePairs.keySet().iterator();
		while(itr.hasNext()){
			boolean unrated = true;
			String neighbourName = "";
			long clusterID = itr.next();
			if(neighbour)
				System.out.println("For Cluster:"+dao.getClusterName(clusterID));
			ArrayList<ServicePair> pairs = servicePairs.get(clusterID);
			for(ServicePair pair : pairs){
				if(pair.getEnhancedRatingSimilarity() >= threshHold || pair.getRatingSimilarity() >= threshHold){
					if(unrated){
						if(neighbour)
							System.out.print("Neighbours for "+pair.getUnRatedServiceName()+" are ");
						unrated = false;
					}
					neighbours.add(pair);
					neighbourName += pair.getOtherServiceName()+",";
				}
			}
			if(neighbour)
				System.out.println(neighbourName.substring(0,neighbourName.length()-1)+"\n");
			calculatePredictedRatings(neighbours,activeUserID);
			if(neighbour)
				calculateNeighbourPredictedRatings(neighbours, activeUserID,threshHold);
		}
	}
	
	private void calculateNeighbourPredictedRatings(ArrayList<ServicePair> neighbours, long activeUserID, double threshHold) {
		Main.activeService = neighbours.get(0).getUnRatedServiceID();
		Main.predictedRatings.clear();
		for(ServicePair pair : neighbours){
			getNeigbhours(threshHold,calculateAndPrint(null, false, true, pair.getOtherServiceID()),activeUserID,false);
		}
	}

	public void calculatePredictedRatings(List<ServicePair> neighbours,long activeUserID){
		double unRatedMean = dao.getMean(neighbours.get(0).getUnRatedServiceID());
		double numerator = 0, denominator = 0;
		for(ServicePair pair : neighbours){
			numerator += (dao.getRating(pair.getOtherServiceID(),activeUserID) - dao.getMean(pair.getOtherServiceID()))* pair.getEnhancedRatingSimilarity();
			denominator += pair.getEnhancedRatingSimilarity();
		}
		DecimalFormat nf = new DecimalFormat("#.##");
		double rating = (unRatedMean + (numerator/denominator));
		Main.predictedRatings.add(new Services(neighbours.get(0).getUnRatedServiceID(),neighbours.get(0).getUnRatedServiceName(),
				Math.abs(dao.getRating(neighbours.get(0).getUnRatedServiceID(), activeUserID)- rating),
				dao.getAPIName(neighbours.get(0).getUnRatedServiceID())));
		System.out.println("predicted rating for "+neighbours.get(0).getUnRatedServiceName()+": "+nf.format(rating));
	}
	
	public void print(HashMap<Long, ArrayList<ServicePair>> servicePairs) {
		Iterator< Long> itr = servicePairs.keySet().iterator();
		System.out.println("Rating Matrix");
		while(itr.hasNext()){
			ArrayList<ServicePair> pairs = servicePairs.get(itr.next());
			for(ServicePair pair : pairs){
				System.out.print("("+pair.getUnRatedServiceID()+","+pair.getOtherServiceID()+")\t");
				System.out.println(pair.getRatingSimilarity()+"\t"+pair.getEnhancedRatingSimilarity());
			}
		}
	}

	public HashMap<Long, ArrayList<ServicePair>> calculateAndPrint(ArrayList<Long> unRatedServices,boolean print, boolean singleMode,long serviceID) {
		HashMap<Long,ArrayList<ServicePair>> servicePairs = new HashMap<Long,ArrayList<ServicePair>>();
		if(!singleMode){
			for(long id : unRatedServices){
				long clusterID = findClusters(id);
				if(getClusterSize(clusterID) == 1){
					continue;
				}
				servicePairs.put(id, createServiceGroups(id,clusterID));
			}
		}else{
			servicePairs.put(serviceID, createServiceGroups(serviceID,findClusters(serviceID)));
		}
		calculateRatingSimialrity(servicePairs);
		if(print)
			print(servicePairs);
		return servicePairs;
	}
}
