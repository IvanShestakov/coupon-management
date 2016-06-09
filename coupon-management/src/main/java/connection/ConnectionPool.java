package connection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import webapi.LoginAPI;

public class ConnectionPool {
	private static final Logger log = LogManager.getLogger(LoginAPI.class);
	
	private static ConnectionPool INSTANCE = new ConnectionPool();
	private int POOLSIZE = 5;
	private static ArrayList<Connection> connections;
	String url = "";
	String user = "";
	String password = "";
	
	public static synchronized Connection getConnection(){
		Connection connection = null;

        //Check if there is a connection available. There are times when all the connections in the pool may be used up
        if(ConnectionPool.connections.size() > 0)
        {
            connection = (Connection) ConnectionPool.connections.get(0);
            ConnectionPool.connections.remove(0);
            //System.out.println("Pool has " + connections.size() + " connections available.");
        }
        //Giving away the connection from the connection pool
        return connection;
	}
	public static void returnConnection(Connection conn){
		//add the connection back to pool
		connections.add(conn);
		//System.out.println("Returning connection to the pool. Pool size is " + connections.size());
		
	}
	
	public static void closeAllConnections() throws SQLException{
		// Go over the connections list, and close each one of them
		log.debug("Closing all connections");
		for (Connection c : connections) {
			c.close();
		}
		
	}
	
	private ConnectionPool(){
		PropertiesLoader loader = new PropertiesLoader();
		try {
		Map<String, String> params = loader.getPropValues();
		this.url = params.get("url");
		this.user = params.get("user");
		this.password = params.get("password");
		}
		catch (IOException e){
			e.printStackTrace();
			//System.out.println("Failed to load properties file. Loading defaults.");
			this.url = "jdbc:mysql://localhost:3306/coupons?useSSL=false";
			this.user = "Admin";
			this.password = "1234";
		}
		initializeConnectionPool();
	}
	
	private void initializeConnectionPool() {
		ConnectionPool.connections = new ArrayList<Connection>();
		while(!isConnectionPoolFull()){
			//System.out.println("Connection Pool is NOT full. Proceeding with adding new connections");
			//Adding new connection instance until the pool is full
            connections.add(createNewConnectionForPool());
		}
		//System.out.println("Connection Pool is full.");
	}
	private Connection createNewConnectionForPool() {
		 Connection connection = null;
	        try
	        {
	            Class.forName("com.mysql.jdbc.Driver");
	            connection = DriverManager.getConnection(url, user, password);
	            //System.out.println("Connection: "+connection);
	        }
	        catch(SQLException sqle)
	        {
	            System.err.println("SQLException: "+sqle);
	            return null;
	        }
	        catch(ClassNotFoundException cnfe)
	        {
	            System.err.println("ClassNotFoundException: "+cnfe);
	            return null;
	        }

	        return connection;
	}
	public synchronized boolean isConnectionPoolFull(){
		//Check if connection pool already full
		if (connections.size()<POOLSIZE){
			return false;
		}
		return true;
	}
	
	public static ConnectionPool getInstance(){
		if (INSTANCE == null){
			return new ConnectionPool();
		}
		return INSTANCE;
	}
}
