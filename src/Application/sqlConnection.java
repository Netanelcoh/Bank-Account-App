package Application;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mysql.cj.jdbc.MysqlDataSource;

//class handles sql connection
public class sqlConnection {

    private static String servername = "localhost";
    private static String username = "root";
    private static String dbname  = "users_schema";
    private static Integer portnumber  = 3306;
    private static String password = "";
    
	public static Connection getConnection() {
        Connection cnx = null;
        
        MysqlDataSource datasource = new MysqlDataSource();
        
        datasource.setServerName(servername);
        datasource.setUser(username);
        datasource.setPassword(password);
        datasource.setDatabaseName(dbname);
        datasource.setPortNumber(portnumber);
        
        try {
            cnx = datasource.getConnection();
        } catch (SQLException ex) {
        	System.out.println("failed to connect to db");
            Logger.getLogger(" Get Connection -> " + sqlConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return cnx;
    }
	
}
