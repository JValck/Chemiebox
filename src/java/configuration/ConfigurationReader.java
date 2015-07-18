package configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reads the configuration file
 */
public class ConfigurationReader {
    private HashMap<String, String> properties;
    
    /**
     * Reads the config file and creates a map from it
     */
    public ConfigurationReader(){
        properties = new HashMap<String, String>();
        processConfigurationFile();
    }    

    /**
     * Reads the config file
     */
    private void processConfigurationFile() {
        try {            
            File file = new File(this.getClass().getResource("config.conf").getFile());            
            Scanner fileScanner = new Scanner(file);
            while(fileScanner.hasNextLine()){
                Scanner lineScanner = new Scanner(fileScanner.next());
                lineScanner.useDelimiter("-->");
                String property = lineScanner.next().toLowerCase().trim();
                String value = lineScanner.next().trim();
                properties.put(property, value);
                lineScanner.close();
            }
            fileScanner.close();
        } catch (FileNotFoundException ex) {//todo: clean
            Logger.getLogger(ConfigurationReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Searches for the property in the property map
     * @param key - Case Insensitive. The property to search for
     * @return The value of the corresponding property. Returns null if invalid property
     */
    public String getPropertyValue(String key){
        return properties.get(key.toLowerCase().trim());
    }
    
}
