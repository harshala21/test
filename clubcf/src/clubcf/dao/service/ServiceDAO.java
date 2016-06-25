package clubcf.dao.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import clubcf.dao.DAO;
import clubcf.factory.DBConnection;
import clubcf.model.ServicePair;
import clubcf.model.Services;

public class ServiceDAO implements DAO {
	Connection con = null;
	ResultSet results = null;
	PreparedStatement stmt = null;
	DBConnection connection = new DBConnection();
	@Override
	public Connection openConnection() {
		return connection.getDBConnection();
	}

	@Override
	public void close(Connection con) {
		connection.close(con);

	}

	@Override
	public void close(ResultSet results) {
		connection.close(results);

	}

	@Override
	public void close(PreparedStatement stmt) {
		connection.close(stmt);
	}
	
	public ArrayList<Long> findUnratedServices( double rating, int activeUserID){
		ArrayList<Long> unRatedServices = new ArrayList<Long>();
		try{
			String query = "select service_id from rating_matrix  where ratings = ? and user_id = ?";
			con = openConnection();
			stmt = con.prepareStatement(query);
			stmt.setDouble(1, rating);
			stmt.setInt(2, activeUserID);
			results = stmt.executeQuery();
			while(results.next()){
				unRatedServices.add(results.getLong(1));
			}
		} catch (SQLException e){
			e.printStackTrace();
		} finally {
			close(stmt);
			close(results);
			close(con);
		}
		return unRatedServices.size() > 0 ? unRatedServices : null;
	}
	
	public String getServiceName(long serviceID){
		String serviceName = null;
		try{
			String query = "select sid from sample_data where id = ?";
			con = openConnection();
			stmt = con.prepareStatement(query);
			stmt.setLong(1, serviceID);
			results = stmt.executeQuery();
			if(results.next()){
				serviceName = results.getString(1);
			}
		}catch (SQLException w){
			w.printStackTrace();
		}finally {
			close(stmt);
			close(results);
			close(con);
		}
		return serviceName;
	}

	public double[] getMeanRatings(long serviceID) {
		ArrayList<Double> data = new ArrayList<Double>();
		try{
			String query = "select ratings from rating_matrix where service_id = ?";
			con = openConnection();
			stmt = con.prepareStatement(query);
			stmt.setLong(1, serviceID);
			results = stmt.executeQuery();
			while(results.next()){
				data.add(results.getDouble(1));
			}
		}catch (SQLException w){
			w.printStackTrace();
		}finally {
			close(stmt);
			close(results);
			close(con);
		}
		return convertToArray(data);
	}

	public double[] getSimlarityRatings(long serviceID) {
		
		ArrayList<Double> dataDB = new ArrayList<Double>();
		try{
			String query = "select ratings from rating_matrix where service_id = ? and ratings != 0";
			con = openConnection();
			stmt = con.prepareStatement(query);
			stmt.setLong(1, serviceID);
			results = stmt.executeQuery();
			int count = 0;
			while(results.next()){
				dataDB.add(results.getDouble(1));
				count++;
			}
		}catch (SQLException w){
			w.printStackTrace();
		}finally {
			close(stmt);
			close(results);
			close(con);
		}
		return convertToArray(dataDB);
	}
	
public double[] getSimlarityRatingsWithOutZero(long serviceID,long clusterID) {
		
		ArrayList<Double> dataDB = new ArrayList<Double>();
		try{
			String query = "select ratings from rating_matrix where service_id ="+serviceID+" and user_id not in  (select user_id from rating_matrix where service_id in ("+getClusterServices(clusterID)+")  and ratings = 0)";
			//System.out.println(query);
			con = openConnection();
			Statement stmt = con.createStatement();			
			results = stmt.executeQuery(query);
			int count = 0;
			while(results.next()){
				dataDB.add(results.getDouble(1));
				count++;
			}
		}catch (SQLException w){
			w.printStackTrace();
		}finally {
			close(stmt);
			close(results);
			close(con);
		}
		
		return convertToArray(dataDB);
	}
	
	public double getNonZeroRatingSize(long serviceID){
		double ratingCount = 0;
		String query = "select count(ratings) from rating_matrix where service_id =? and ratings != 0";
		try {
			con = openConnection();
			stmt = con.prepareStatement(query);
			stmt.setLong(1, serviceID);
			results = stmt.executeQuery();
			results.next();
			ratingCount = results.getDouble(1);
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			close(stmt);
			close(results);
			close(con);
		}
		return ratingCount;
	}

	private double[] convertToArray(ArrayList<Double> dataDB) {
		double[] data = new double[dataDB.size()];
		int count =0;
		for(double d : dataDB){
			data[count] = d;
			count++;
		}
		return data;
	}

	public double getPairIntesection(ServicePair pair) {
		double ratingCount = 0;
		String query = "select count(ratings) from rating_matrix where service_id = ? and user_id not in (select user_id from rating_matrix where ratings = 0 and service_id = ?);";
		try {
			
			con = openConnection();
			stmt = con.prepareStatement(query);
			stmt.setLong(1, pair.getOtherServiceID());
			stmt.setLong(2, pair.getUnRatedServiceID());
			results = stmt.executeQuery();
			results.next();
			ratingCount = results.getDouble(1);
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			close(stmt);
			close(results);
			close(con);
		}
		return ratingCount;
	}
	
	public List<Services> getAllDetails(int limit){
		List<Services> allServices = new ArrayList<Services>();
		String query = "select id,sid,api,stemword from sample_data order by id limit ?";
		try {
			con = openConnection();
			stmt = con.prepareStatement(query);
			stmt.setInt(1, limit);
			results = stmt.executeQuery();
			while(results.next()){
				allServices.add(new Services(results.getLong(1), results.getString(2), results.getString(3), results.getString(4)));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			close(stmt);
			close(results);
			close(con);
		}
		return allServices;
	}
	
	public String getClusterServices( long clusterID){
		String services = "";
		try{
			String query  = "select services from clusters where cluster_id = ?";
			con = connection.getDBConnection();
			stmt = con.prepareStatement(query);
			stmt.setLong(1, clusterID);
			results = stmt.executeQuery();
			if(results.next()){
				String[] service = results.getString(1).split(",");
				for(String s: service)
					services += s + ",";
				services = services.substring(0, services.length()-1);
			}
			//System.out.println(services);
		}catch (SQLException e){
			e.printStackTrace();
		}finally {
			connection.close(stmt);
			connection.close(results);
			connection.close(con);
		}
		return services;
	}

	public String getClusterName(long id) {
		String clusterName = null;
		try{
			String query = "select cluster_name from clusters where cluster_id = ?";
			con = openConnection();
			stmt = con.prepareStatement(query);
			stmt.setLong(1, id);
			results = stmt.executeQuery();
			if(results.next()){
				clusterName = results.getString(1);
			}
		}catch (SQLException w){
			w.printStackTrace();
		}finally {
			close(stmt);
			close(results);
			close(con);
		}
		return clusterName;
		
	}
	
	public double getMean(long unRatedServiceID) {
		double mean = 0;
		try{
			String query = "select avg(ratings) from rating_matrix where service_id = ?";
			con = openConnection();
			stmt = con.prepareStatement(query);
			stmt.setLong(1, unRatedServiceID);
			results = stmt.executeQuery();
			if(results.next()){
				mean =  results.getDouble(1);
			}
		}catch (SQLException w){
			w.printStackTrace();
		}finally {
			close(stmt);
			close(results);
			close(con);
		}
		return mean;
	}

	public double getRating(long serviceID, long activeUserID) {
		double rating = 0;
		try{
			String query = "select ratings from rating_matrix where service_id = ? and user_id = ?";
			con = openConnection();
			stmt = con.prepareStatement(query);
			stmt.setLong(1, serviceID);
			stmt.setLong(2, activeUserID);
			results = stmt.executeQuery();
			if(results.next()){
				rating =  results.getDouble(1);
			}
		}catch (SQLException w){
			w.printStackTrace();
		}finally {
			close(stmt);
			close(results);
			close(con);
		}
		return rating;
	}


	public String getAPIName(long serviceID) {
		String rating = null;
		try{
			String query = "select api from sample_data where id = ?";
			con = openConnection();
			stmt = con.prepareStatement(query);
			stmt.setLong(1, serviceID);
			
			results = stmt.executeQuery();
			if(results.next()){
				rating =  results.getString(1);
			}
		}catch (SQLException w){
			w.printStackTrace();
		}finally {
			close(stmt);
			close(results);
			close(con);
		}
		return rating;
	}

	public void updateSemanticWordToDB(HashSet<String> semanticWords, long serviceID) {
		String semantics = prepareSemantics(semanticWords);
		try{
			String cleanUpQuery = "update sample_data set semantics = null where id = ?";
			String query = "update sample_data set semantics = ? where id =? ";
			con = openConnection();
			stmt = con.prepareStatement(cleanUpQuery);
			stmt.setLong(1, serviceID);
			stmt.executeUpdate();
			stmt = con.prepareStatement(query);
			stmt.setString(1, semantics);
			stmt.setLong(2, serviceID);
			
			stmt.executeUpdate();
			
		}catch (SQLException w){
			System.out.println(semantics.length());
			System.out.println(semantics);
			w.printStackTrace();
		}finally {
			close(stmt);
			close(results);
			close(con);
			semanticWords.clear();
		}
	}

	private String prepareSemantics(HashSet<String> semanticWords) {
		String semantics= "";
		Iterator<String> itr = semanticWords.iterator();
		while (itr.hasNext())
			semantics += itr.next()+",";
		return semantics.isEmpty() ? "" : semantics.substring(0, semantics.length()-1);
	}

}
