package db.mappers;

import db.DatabaseInterface;
import domain.Solution;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author r0261853
 */
public class SolutionMapper {

    private Connection dbConnection;

    public SolutionMapper(DatabaseInterface db) {
        this.dbConnection = db.getConnection();
    }

    public List<Solution> getSolution(int id) {
        ArrayList<Solution> allSolutions = new ArrayList<Solution>();
        try {
            String query = "select * "
                    + "from solution " + "where id=?;";
            //Create the statement
            PreparedStatement stmt = dbConnection.prepareStatement(query);
            stmt.setInt(1, id);
            //Create the resultSet
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                Solution solution = new Solution();
                solution.setId(resultSet.getInt("id"));
                solution.setText(resultSet.getString("solution"));
                solution.setNextId(resultSet.getInt("nextsolution"));
                allSolutions.add(solution);
                while (solution.getNextId() != 0) {
                    query = "select * " + "from solution " + "where id=" + solution.getNextId() + ";";
                    stmt = dbConnection.prepareStatement(query);
                    resultSet = stmt.executeQuery();
                    if (resultSet.next()) {
                        solution.setId(resultSet.getInt("id"));
                        solution.setText(resultSet.getString("solution"));
                        solution.setNextId(resultSet.getInt("nextsolution"));
                        allSolutions.add(solution);
                    }
                }
                stmt.close();
            }
        } catch (SQLException ex) {
        }
        return allSolutions;
    }

    public Solution getSolutionByText(String text) {
        Solution foundSolution = null;
        try {
            String query = "select * "
                    + "from solution " + "where solution.solution=? LIMIT 1;";
            //Create the statement
            PreparedStatement stmt = dbConnection.prepareStatement(query);
            stmt.setString(1, text);
            //Create the resultSet
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                Solution solution = new Solution();
                solution.setId(resultSet.getInt("id"));
                solution.setText(resultSet.getString("solution"));
                solution.setNextId(0);
                foundSolution = solution;
                stmt.close();
            }
        } catch (SQLException ex) {
        }
        return foundSolution;
    }

    public void deleteSolution(Solution solution) {
        try {
            dbConnection.setAutoCommit(false);
            String query = "delete from solution " + "where id=?";
            PreparedStatement stmt = dbConnection.prepareStatement(query);
            stmt.setLong(1, solution.getId());
            stmt.executeUpdate(query);
            dbConnection.commit();
            stmt.close();
        } catch (SQLException ex) {
        }
    }

    public void addSolution(Solution solution) {
        try {
            dbConnection.setAutoCommit(false);
            String insertTableSQL = "INSERT INTO solution" + "(solution,nextsolution) VALUES" + "(?,null)";
            PreparedStatement prepared = dbConnection.prepareStatement(insertTableSQL);
            prepared.setString(1, solution.getNativeSolutionText());
           // prepared.setObject(2, solution.getNextId());
            prepared.executeUpdate();
            dbConnection.commit();
            prepared.close();
        } catch (SQLException ex) {

        }
    }

    public void updateSolution(Solution solution) {
        try {
            dbConnection.setAutoCommit(false);
            String insertTableSQL = "Update solution set solution=" + "(?)" + "where id=" + "(?)";
            PreparedStatement prepared = dbConnection.prepareStatement(insertTableSQL);
            prepared.setInt(2, solution.getId());
            prepared.setString(1, solution.getText());
            prepared.executeUpdate();
            dbConnection.commit();
            prepared.close();
        } catch (SQLException ex) {

        }
    }

    public List<Solution> getSolutionByQuestionId(long questionId) {
        List<Solution> sols = new ArrayList<Solution>();
        try {
            String query = "Select * from solution s inner join question q on (q.solutionid = s.id) where q.id = ?";
            PreparedStatement prepared = dbConnection.prepareStatement(query);
            prepared.setLong(1, questionId);
            ResultSet resultSet = prepared.executeQuery();
            while (resultSet.next()) {
                Solution sol = new Solution();
                sol.setId(resultSet.getInt("id"));
                sol.setText(resultSet.getString("solution"));
                sol.setNextId(resultSet.getInt("nextsolution"));
                sols.add(sol);
            }
            prepared.close();
        } catch (SQLException ex) {
            Logger.getLogger(SolutionMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sols;
    }
}
