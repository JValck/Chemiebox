/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package db.mappers;

import db.DatabaseInterface;
import domain.Strategy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fery
 */
public class StrategyMapper {
    
    private Connection dbConnection;
   
    public StrategyMapper(DatabaseInterface dbInterface) {
        this.dbConnection = dbInterface.getConnection();
    }
    
    /**
     * Searches the database for strategies
     *
     * @return A list of all the strategies in the database
     */
    public List<Strategy> getAllStrategies() {
        ArrayList<Strategy> strategies = new ArrayList<Strategy>();
        try {
            //Create the sql-query
            String query = "select * "
                    + "from strategy;";
            //Create the statement
            Statement stmt = dbConnection.createStatement();
            //Create the resultSet
            ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {//update cursor
                String name = resultSet.getString("name");
                long id = resultSet.getLong("id");
                Strategy strategy = new Strategy(id,name);
                strategies.add(strategy);
            }
            stmt.close();//closing the statement
        } catch (SQLException ex) {
            Logger.getLogger(StrategyMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return strategies;
    }

    /**
     * Searches the database for a strategy
     *
     * @param id
     * @return A strategy with the given name
     */
    public Strategy getStrategy(long id) {
        Strategy strategy = null;
        try {
            //Create the sql-query
            String query = "select * "
                    + "from strategy where strategy.id=? ;";
            //Create the PREPARED statement
            PreparedStatement prepStmt = dbConnection.prepareStatement(query);
            prepStmt.setLong(1, id); // Eerste vraagteken uit de query  -> waar questiontext = questiontext die we willen zoeken
            //Create the resultSet
            ResultSet resultSet = prepStmt.executeQuery();
            if(resultSet.next()){
            strategy = new Strategy(resultSet.getLong("id"), resultSet.getString("name"));
            }
            prepStmt.close();//closing the statement
        } catch (SQLException ex) {
            Logger.getLogger(StrategyMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return strategy;
    }
}
