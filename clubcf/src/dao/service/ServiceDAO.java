package dao.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import clubcf.factory.DBConnection;
import dao.DAO;
import dummy.ServicePair;
import dummy.Services;

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
	
	public ArrayList<Long> findUnratedServices( double rating){
		ArrayList<Long> unRatedServices = new ArrayList<Long>();
		try{
			String query = "select service_id from rating_matrix  where ratings = ?";
			con = openConnection();
			stmt = con.prepareStatement(query);
			stmt.setDouble(1, rating);
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
	
	public Services getServiceDetails(long serviceID){
		String serviceSQL = "select sid from sample_data where id = ?";
		return null;
	}

	public double[] getMeanRatings(long serviceID) {
		ArrayList<Double> data = new ArrayList<Double>();
		try{
			String query = "select ratings from rating_matrix where service_id = ? and user_id in (1,2,3,4)";
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
			String query = "select ratings from rating_matrix where service_id = ? and user_id in (1,2,4)";
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
	
	public double getNonZeroRatingSize(long serviceID){
		double ratingCount = 0;
		String query = "select count(ratings) from rating_matrix where service_id =? and user_id in (1,2,3,4) and ratings != 0";
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
}
