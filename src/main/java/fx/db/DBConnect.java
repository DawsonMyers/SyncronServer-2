package fx.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Narayan
 */

/**
 * Created by Dawson on 3/31/2015.
 */
public class DBConnect {
	static              String           nameId = DBConnect.class.getSimpleName();
	public final static org.slf4j.Logger log    = org.slf4j.LoggerFactory.getLogger(nameId);

	public DBConnect() {}


	private static Connection conn;
	private static String url  = "jdbc:mysql://dawsonmyers.ca";
	//	private static String url = "jdbc:mysql://localhost/company";
	private static String user = "root";
	private static String pass = "dawson";
	private static String db   = "syncron";


	public static Connection connect() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Error: " + cnfe.getMessage());
		} catch (InstantiationException ie) {
			System.err.println("Error: " + ie.getMessage());
		} catch (IllegalAccessException iae) {
			System.err.println("Error: " + iae.getMessage());
		}

		conn = DriverManager.getConnection(url, user, pass);
		return conn;
	}

	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		if (conn != null && !conn.isClosed())
			return conn;
		connect();
		return conn;

	}
}