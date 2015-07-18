package db;

import java.sql.Connection;
/**
 *
 * @author r0431118
 */
public interface DatabaseInterface {
 //Methodes
     /**
     * Connects to the database
     */
    void connect();
    /**
     * Disconnects from the database
     */
    void disconnect();
    /**
     * 
     * @return The connection to the database
     */
    Connection getConnection();    
}
