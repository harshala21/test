package clubcf.dao.cluster;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.omg.PortableServer.ServantRetentionPolicyValue;

import clubcf.dao.DAO;
import clubcf.dao.service.ServiceDAO;
import clubcf.factory.DBConnection;
import clubcf.model.Cluster;
import clubcf.model.Services;

public class ClusterDAO implements DAO {

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

	public void saveCluster(List<Cluster> similarityMatrix, List<Services> allServices) {
		try{
			con = openConnection();
			String deleteQuery = "delete from clusters";
			stmt =con.prepareStatement(deleteQuery);
			stmt.executeUpdate();
			stmt.close();
			String query = "insert into clusters (cluster_name,services) values (?,?)";
			stmt = con.prepareStatement(query);
			int clusterID = 1;
			for(Cluster c : similarityMatrix){
				stmt.setString(1, "c"+clusterID++);
				stmt.setString(2, getServiceIDs(c,allServices));
				stmt.addBatch();
			}
			stmt.executeBatch();
		} catch (SQLException e){
			e.printStackTrace();
		} finally {
			close(stmt);
			close(con);
		}
		
	}

	private String getServiceIDs(Cluster c, List<Services> allServices) {
		StringBuilder ids = new StringBuilder();
		for(String service : c.getName().split(",")){
			for(Services s : allServices){
				if(s.getServiceName().equals(service))
					ids.append(s.getServiceID()).append(",");
			}
		}
		System.out.println(ids.toString());
		return ids.deleteCharAt(ids.length()-1).toString();
	}

	

}
