package db.mappers;

import db.DatabaseInterface;
import domain.Module;
import domain.Question;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author r0430844
 */
public class ModuleMapper {

    private Connection dbConnection;

    /**
     * Creates the module mapper with the given database connection
     *
     * @param db
     */
    public ModuleMapper(DatabaseInterface db) {
        this.dbConnection = db.getConnection();
    }

    /**
     * Searches the database for modules
     *
     * @return A list of all the modules in the database
     */
    public List<Module> getAllModules() {
        ArrayList<Module> allModules = new ArrayList<Module>();
        try {
            //Create the sql-query
            String query = "select * "
                    + "from module order by chapter";
            //Create the statement
            Statement stmt = dbConnection.createStatement();
            //Create the resultSet
            ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {//update cursor
                Module module = new Module();
                module.setName(resultSet.getString("name"));
                module.setInstructions(resultSet.getString("instructies"));
                module.setId(resultSet.getLong("id"));
                module.setMaxTries(resultSet.getInt("max"));
                Calendar startCal = Calendar.getInstance();//create a calendar instance for the start date
                startCal.setTime(resultSet.getDate("start", startCal));//convert the sql to date
                module.setStart(startCal);//setting the property
                Calendar deadlineCal = Calendar.getInstance();//create a calendar for the end date
                deadlineCal.setTime(resultSet.getDate("deadline", deadlineCal));
                module.setDeadline(deadlineCal);
                module.setChapter(resultSet.getString("chapter"));
                allModules.add(module);
            }
            stmt.close();//closing the statement
        } catch (SQLException ex) {
            Logger.getLogger(ModuleMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allModules;
    }

    /**
     * Adds the new module to the database
     *
     * @param module The module to add
     * @throws SQLException see {@link Connection#rollback() }
     */
    public void addModule(Module module) throws SQLException {
        try {
            //disable auto-commit
            dbConnection.setAutoCommit(false);
            String query = "INSERT INTO module (name, max, deadline, start, chapter, instructies)"
                    + "VALUES (?, ?, ?, ?, ?,?);";
            PreparedStatement stmt = dbConnection.prepareStatement(query);
            //fill in the question marks
            stmt.setString(1, module.getName());
            stmt.setInt(2, module.getMaxTries());
            stmt.setDate(3, new java.sql.Date(module.getDeadline().getTimeInMillis()));
            stmt.setDate(4, new java.sql.Date(module.getStart().getTimeInMillis()));
            stmt.setString(5, module.getChapter());
            stmt.setString(6, module.getInstructions());
            stmt.executeUpdate();//may be an insert, update or delete            
            dbConnection.commit();
            stmt.close();
        } catch (SQLException ex) {
            dbConnection.rollback();//rollback
            Logger.getLogger(ModuleMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Deletes the module in the database
     *
     * @param module The module to delete
     * @throws SQLException see {@link Connection#rollback()}
     */
    public void deleteModule(Module module) throws SQLException {
        String query = "DELETE FROM module WHERE id = ?";
        try {
            //disable auto-commit
            dbConnection.setAutoCommit(false);
            PreparedStatement stmt = dbConnection.prepareStatement(query);
            //fill in the question marks
            stmt.setLong(1, module.getId());
            stmt.executeUpdate();//may be an insert, update or delete            
            dbConnection.commit();
            stmt.close();
        } catch (SQLException ex) {
            dbConnection.rollback();//rollback
            Logger.getLogger(ModuleMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Updates the module in the database     *
     * @param updatedModule The updated module
     * @throws SQLException see {@link Connection#rollback()}
     */
    public void updateModule(Module updatedModule) throws SQLException {
        String query = "UPDATE chemiedb.module SET (name, max, deadline, start, chapter, instructies) = (?, ?, ?, ?, ?,?) WHERE id = ? ;";
        try {
            //disable auto-commit
            dbConnection.setAutoCommit(false);
            PreparedStatement stmt = dbConnection.prepareStatement(query);
            //fill in the question marks
            stmt.setString(1, updatedModule.getName());
            stmt.setInt(2, updatedModule.getMaxTries());
            stmt.setDate(3, new java.sql.Date(updatedModule.getDeadline().getTimeInMillis()));
            stmt.setDate(4, new java.sql.Date(updatedModule.getStart().getTimeInMillis()));
            stmt.setString(5, updatedModule.getChapter());
            stmt.setString(6, updatedModule.getInstructions());
            //set the where clause
            stmt.setLong(7, updatedModule.getId());
            stmt.executeUpdate();//may be an insert, update or delete            
            dbConnection.commit();
            stmt.close();
        } catch (SQLException ex) {
            dbConnection.rollback();//rollback
            Logger.getLogger(ModuleMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Searches the database for a module
     *
     * @param moduleId
     * @return A question in the database with a specific question text
     */
    public Module getModule(Long moduleId) {
        Module module = null;
        try {
            //Create the sql-query
            String query = "select * "
                    + "from module where module.id=? ;";
            //Create the PREPARED statement
            PreparedStatement prepStmt = dbConnection.prepareStatement(query);
            prepStmt.setLong(1, moduleId); // Eerste vraagteken uit de query  -> waar questiontext = questiontext die we willen zoeken
            //Create the resultSet
            ResultSet resultSet = prepStmt.executeQuery();
            if(resultSet.next()){
            module = new Module();
            module.setName(resultSet.getString("name"));
            module.setMaxTries(resultSet.getInt("max"));
            Calendar deadlineCal = Calendar.getInstance();//create a calendar for the end date
            deadlineCal.setTime(resultSet.getDate("deadline", deadlineCal));
            module.setDeadline(deadlineCal);
            Calendar startCal = Calendar.getInstance();//create a calendar instance for the start date
            startCal.setTime(resultSet.getDate("start", startCal));//convert the sql to date
            module.setStart(startCal);//setting the property
            module.setChapter(resultSet.getString("chapter"));
            module.setInstructions(resultSet.getString("instructies"));
            module.setId(moduleId);
            }
            prepStmt.close();//closing the statement
        } catch (SQLException ex) {
            Logger.getLogger(QuestionMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return module;
    }
    
    

}
