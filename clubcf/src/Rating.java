import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Rating {
Connection con;
ResultSet results;
PreparedStatement stmt;

	/*private void getConnection(){
		try{
			Class.forName("oracle.jdbc.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","honour");
		}catch (ClassNotFoundException e){
			e.printStackTrace();
		}catch ( SQLException e){
			e.printStackTrace();
		}
	}*/
	
	public static void main(String[] args) {
		Rating rating = new Rating();
		try{
			DBConnection conne = new DBConnection(); 
			rating.con = conne.getDBConnection();
			String query = "select u.username, rm.ratings, sd.sid from rating_matrix rm inner join  users u on rm.user_id = u.user_id inner join sample_data sd on rm.service_id = sd.id";
			rating.stmt = rating.con.prepareStatement(query);
			rating.results = rating.stmt.executeQuery();
			while(rating.results.next()){
				
			}
			
		} catch(SQLException e){
			e.printStackTrace();
		}
	} 	
	
/*	private double[] getNumberOfUsers(int serviceID){
		try{
			String query = "select from "
		}
	}*/
	
	public double calculateRatingSimilarity(double[] serviceX,double[] serviceY){
		double meanX = getMean(serviceX);
		double meanY = getMean(serviceY);
		double numerator = 0;
		double denominatorX = 0, denominatorY = 0;
		
		for(int i = 0; i < serviceX.length && i < serviceY.length ; i++){
			numerator += (serviceX[i] - meanX)*( serviceY[i] - meanY);
			denominatorX += Math.pow((serviceX[i] - meanX), 2);
			denominatorY += Math.pow(( serviceY[i] - meanY), 2);
		}
		return numerator /(Math.sqrt(denominatorX) * Math.sqrt(denominatorY));
	}
	
	public double calculateEnhancedRatingSimilarity(){
		
		return 0d;
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
	
}
