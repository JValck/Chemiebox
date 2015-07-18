/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.mappers;

import db.DatabaseInterface;
import domain.Question;
import domain.StudentQuestion;
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
public class StudentQuestionMapper {

    private Connection dbConnection;

    /**
     * Creates the question mapper with the given database connection
     *
     * @param dbInterface
     */
    public StudentQuestionMapper(DatabaseInterface dbInterface) {
        this.dbConnection = dbInterface.getConnection();
    }

    /**
     * Searches the database for students questions
     *
     * @return A list of all the students questions in the database
     */
    public List<StudentQuestion> getAllStudentQuestions() {
        ArrayList<StudentQuestion> questions = new ArrayList<StudentQuestion>();
        try {
            //Create the sql-query
            String query = "select * "
                    + "from studentquestion;";
            //Create the statement
            Statement stmt = dbConnection.createStatement();
            //Create the resultSet
            ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {//update cursor
                long questionId = resultSet.getLong("questionid");
                String studNr = resultSet.getString("studnr");
                int score = resultSet.getInt("score");
                int tries = resultSet.getInt("tries");
                String lastAnswer = resultSet.getString("lastAnswer");
                StudentQuestion question = new StudentQuestion(questionId, studNr, score, tries, lastAnswer);
                questions.add(question);
            }
            stmt.close();//closing the statement
        } catch (SQLException ex) {
            Logger.getLogger(StudentQuestionMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return questions;
    }


    /**
     * Searches the database for a studentquestion
     *
     * @param questionTextKey
     * @return A student question in the database with a specific question text
     */
    public StudentQuestion getStudentQuestion(long questionId, String studnr) {
        StudentQuestion question = null;
        try {
            //Create the sql-query
            String query = "select * "
                    + "from studentquestion where studentquestion.questionid=? and studentquestion.studnr=?;";
            //Create the PREPARED statement
            PreparedStatement prepStmt = dbConnection.prepareStatement(query);
            prepStmt.setLong(1, questionId); // Eerste vraagteken uit de query  -> waar questiontext = questiontext die we willen zoeken
            prepStmt.setString(2, studnr);
            //Create the resultSet
            ResultSet resultSet = prepStmt.executeQuery();
            if(resultSet.next()){
            long questionID = resultSet.getLong("questionid");
            String studNr = resultSet.getString("studnr");
            int score = resultSet.getInt("score");
            int tries = resultSet.getInt("tries");
            String lastAnswer = resultSet.getString("lastAnswer");
            question = new StudentQuestion(questionID, studNr, score, tries, lastAnswer);
            }
            prepStmt.close();//closing the statement
        } catch (SQLException ex) {
            Logger.getLogger(StudentQuestionMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return question;
    }
    
    public int getTimesTried(long moduleId, String studnr) {
        int tried = 0;
        try {
            //Create the sql-query
            String query = "SELECT  " +
"  (case when MIN(case when tries> 0 then tries else 0 end)>0 then MIN(case when tries> 0 then tries else 0 end) else 0 end) as tried " +
"FROM  " +
"  (select * from chemiedb.studentquestion where studnr=? ) as studQ " +
"  right outer join (select * from chemiedb.question where moduleid=? ) as questions " +
"  on studQ.questionid=questions.id " +
"  " +
";";
            
            //Create the PREPARED statement
            PreparedStatement prepStmt = dbConnection.prepareStatement(query);
            prepStmt.setLong(2, moduleId); 
            prepStmt.setString(1, studnr);
            //Create the resultSet
            ResultSet resultSet = prepStmt.executeQuery();
            if(resultSet.next()){
                tried=resultSet.getInt("tried");
                
            }
            prepStmt.close();//closing the statement
        } catch (SQLException ex) {
            Logger.getAnonymousLogger().log(Level.SEVERE, null, ex);
        }
        return tried;
    }
    

    /**
     * Adds a question to the database
     *
     */
    public void addStudentQuestion(StudentQuestion question) throws SQLException {
        dbConnection.setAutoCommit(false);
        try {
            String insertQuery = "INSERT INTO studentquestion"
                    + "(questionid, studnr, score, tries, lastanswer) VALUES"
                    + "(?,?,?,?, ?)";
            PreparedStatement preparedStatement = dbConnection.prepareStatement(insertQuery);
            preparedStatement.setLong(1, question.getQuestionId());
            preparedStatement.setString(2, question.getStudnr());
            preparedStatement.setInt(3, question.getScore());
            preparedStatement.setInt(4, question.getTries());
            preparedStatement.setString(5, question.getLastAnswer());
            // execute insert SQL stetement
            preparedStatement.executeUpdate();
            dbConnection.commit();
            preparedStatement.close();
        } catch (SQLException ex) {
            dbConnection.rollback();
            Logger.getLogger(StudentQuestionMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Deletes a question from the database
     *
     * @param question
     * @throws java.sql.SQLException
     */
    public void deleteStudentQuestion(StudentQuestion question) throws SQLException {
        dbConnection.setAutoCommit(false);
        try {
            String deleteQuery = "DELETE FROM studentquestion where studentquestion.questionid=? AND studentquestion.studnr=? ;";
            PreparedStatement preparedStatement = dbConnection.prepareStatement(deleteQuery);
            preparedStatement.setLong(1, question.getQuestionId());
            preparedStatement.setString(2, question.getStudnr());
            // execute insert SQL stetement
            preparedStatement.executeUpdate();
            dbConnection.commit();
            preparedStatement.close();
        } catch (SQLException ex) {
            dbConnection.rollback();
            Logger.getLogger(StudentQuestionMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Update a question from the database
     * @param question
     * @throws java.sql.SQLException
     */
    public void updateStudentQuestion(StudentQuestion question) throws SQLException {
        
        dbConnection.setAutoCommit(false);
        try {
            String updateTableSQL = "UPDATE studentquestion SET questionid = ?, studnr = ?, score = ?, tries = ?, lastanswer = ? WHERE questionid = ? AND studnr = ?;";
            PreparedStatement preparedStatement = dbConnection.prepareStatement(updateTableSQL);
            preparedStatement.setLong(1, question.getQuestionId());
            preparedStatement.setString(2, question.getStudnr());
            preparedStatement.setInt(3, question.getScore());
            preparedStatement.setInt(4, question.getTries());
            preparedStatement.setString(5, question.getLastAnswer());
            preparedStatement.setLong(6, question.getQuestionId());
            preparedStatement.setString(7, question.getStudnr());
            // execute insert SQL stetement
            preparedStatement.executeUpdate();
            dbConnection.commit();
            preparedStatement.close();
        } catch (SQLException ex) {
            dbConnection.rollback();
            Logger.getLogger(StudentQuestionMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
