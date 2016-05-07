package clubcf.factory;

import java.awt.image.SampleModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import clubcf.model.ClubCF;
import clubcf.model.Services;

public class DBConnection {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	//static final String DB_URL = "jdbc:mysql://192.168.0.106/clubcf";
	static final String DB_URL = "jdbc:mysql://localhost/clubcf";
	static final String mashupService = "mashup_service";
	//static final String sampleData = "sample_data";
	static final String averageTable = "average";
	
	// Database credentials
	static final String USER = "root";
	//static final String PASS = "";
	static final String PASS = "honour";

	public Connection getDBConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return conn;
	}
	
	public void close(PreparedStatement stmt ){
		try {
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void close(ResultSet result ){
		try {
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void close(Connection con ){
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List getDBRecords(int limit) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<ClubCF> dbList = new ArrayList();
		try {
			conn = getDBConnection();
			stmt = conn.createStatement();
			String sql = "SELECT * FROM " + mashupService + " limit " + limit;
			rs = stmt.executeQuery(sql);
			int counter = 0;
			while (rs.next()) {
				counter++;
				ClubCF clubCF = new ClubCF();
				clubCF.setNo(rs.getString("sid"));
				clubCF.setName(rs.getString("sname"));
				clubCF.setApis(rs.getString("api"));
				clubCF.setTags(rs.getString("tags"));
				clubCF.setStemWord(rs.getString("stemword"));
				dbList.add(clubCF);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		
		return dbList;
	}

	public void insertInDB(ClubCF clubCF, String stemword) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = getDBConnection();
			stmt = conn.createStatement();
			String sql = "insert into " + mashupService + " (sid, sname, api, tags, stemword) "
					+ " values('"+clubCF.getNo().replaceAll("'", "\"")+"', '"+clubCF.getName().replaceAll("'", "\"")+"', '"+clubCF.getApis().replaceAll("'", "\"")+"', '"+clubCF.getTags().replaceAll("'", "\"")+"', '"+stemword.replaceAll("'", "\"")+"')";
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
	public void insertInAverageDB(String average) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = getDBConnection();
			stmt = conn.createStatement();
			String sql = "insert into " + averageTable + " (s1, s2, s3, s4, s5, s6, s7, s8, s9, s10) "
					+ " values("+average+")";
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
	/*public List get2Step(int limit) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<Services> dbList = new ArrayList();
		try {
			conn = getDBConnection();
			stmt = conn.createStatement();
			String sql = "SELECT * FROM " + averageTable + " limit " + limit;
			rs = stmt.executeQuery(sql);
			int counter = 0;
			while (rs.next()) {
				counter++;
				Services services = new Services();
				services.setS1(rs.getFloat("s1"));
				services.setS2(rs.getFloat("s2"));
				services.setS3(rs.getFloat("s3"));
				services.setS4(rs.getFloat("s4"));
				services.setS5(rs.getFloat("s5"));
				services.setS6(rs.getFloat("s6"));
				services.setS7(rs.getFloat("s7"));
				services.setS8(rs.getFloat("s8"));
				services.setS9(rs.getFloat("s9"));
				services.setS10(rs.getFloat("s10"));
				dbList.add(services);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		
		return dbList;
	}*/
}
