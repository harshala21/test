package clubcf.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface DAO {
	Connection openConnection();
	void close(Connection con);
	void close(ResultSet results);
	void close(PreparedStatement stmt);
	
}
