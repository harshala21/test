package clubcf.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import clubcf.factory.DBConnection;

public class UserDAO implements DAO {
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
	
	public int getUsersCount(){
		int count = -1;
		try{
			String query = "select count(user_id) from users";
			con = openConnection();
			stmt = con.prepareStatement(query);
			results = stmt.executeQuery();
			if(results.next())
				count = results.getInt(1);
		} catch (SQLException e){
			e.printStackTrace();
		} finally {
			close(stmt);
			close(results);
			close(con);
		}
		return count;
	}

}
