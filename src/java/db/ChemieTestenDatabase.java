
package db;

import configuration.ConfigurationReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ChemieTestenDatabase implements DatabaseInterface {
    private Connection connection;
    private ConfigurationReader configurationReader;
    public ChemieTestenDatabase() {
        configurationReader = new ConfigurationReader();
    }

    /**
     * Connects to the database
     */
    @Override
    public void connect() {
        try {
            try {//check of de jar included is
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {//todo: change
                Logger.getLogger(ChemieTestenDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }   
            Properties props = new Properties();
            props.setProperty("user", configurationReader.getPropertyValue("user")); //username
            props.setProperty("password", configurationReader.getPropertyValue("password"));  //password           
            props.setProperty("ssl", configurationReader.getPropertyValue("ssl")); //ssl
            props.setProperty("sslfactory", configurationReader.getPropertyValue("sslFactory")); //ssl
            connection = DriverManager.getConnection(
                    configurationReader.getPropertyValue("connectionString"),//todo: change
                    props);    
            updateSchemaToUse();
        } catch (SQLException ex) { //connection failed
            Logger.getLogger(ChemieTestenDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Disconnects from the database
     */
    @Override
    public void disconnect() {
        if(connection != null) try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(ChemieTestenDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateSchemaToUse() {
        try {
            String schema = configurationReader.getPropertyValue("schema");
            
            //updates the schema to use
            Statement statement = connection.createStatement();
            statement.execute("SET SEARCH_PATH TO "+schema);
            statement.close();//always close statement
        } catch (SQLException ex) {//cannot update search_path
            Logger.getLogger(ChemieTestenDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public Connection getConnection(){
        return this.connection;
    }
}
