import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MySQLDatabase {
	private static final String DRIVER = "com.mysql.jdbc.Driver";

	public Connection DBConnect() {
//		 Properties connectionProps = new Properties();
//		 connectionProps.put("root", this.userName);
//		 connectionProps.put("", this.password);
		try {
			Class.forName(DRIVER).newInstance();
			String connectionString = "jdbc:mysql://127.0.0.1:3306/";

			Connection myConnection = DriverManager
					.getConnection(connectionString, "root", "");

			return myConnection;
		} catch (SQLException e) {			
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void Dispose(Connection myConnection) throws SQLException {
		myConnection.close();
	}
}
