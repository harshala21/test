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
			String query = "select users,s2,s5,s1,s3,s4,s7,s6 from rating_matrix";
			rating.stmt = rating.con.prepareStatement(query);
			rating.results = rating.stmt.executeQuery();
			System.out.println("\t|\ts2\ts5\t|\ts1\ts3\ts4\ts7\t|\ts6");
			while(rating.results.next()){
				
			}
			
		} catch(SQLException e){
			e.printStackTrace();
		}
	} 	
	
	public void calculateRatingSimilarity(double[] serviceX,double[] serviceY){
		double meanX = getMean(serviceX);
		double meanY = getMean(serviceY);
		double numerator = 0;
		double denominatorX = 0, denominatorY = 0;
		
		for(int i = 0; i < serviceX.length && i < serviceY.length ; i++){
			numerator += (serviceX[i] - meanX)*( serviceY[i] - meanY);
			denominatorX += Math.pow((serviceX[i] - meanX), 2);
			denominatorY += Math.pow(( serviceY[i] - meanY), 2);
		}
		double ratingSimilarity = numerator /(Math.sqrt(denominatorX) * Math.sqrt(denominatorY));
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
