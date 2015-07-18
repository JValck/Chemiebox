/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.mappers;

import db.DatabaseInterface;
import domain.Question;
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
public class QuestionMapper {

    private Connection dbConnection;

    /**
     * Creates the question mapper with the given database connection
     *
     * @param dbInterface
     */
    public QuestionMapper(DatabaseInterface dbInterface) {
        this.dbConnection = dbInterface.getConnection();
    }

    /**
     * Searches the database for questions
     *
     * @return A list of all the questions in the database
     */
    public List<Question> getAllQuestions() {
        ArrayList<Question> questions = new ArrayList<Question>();
        try {
            //Create the sql-query
            String query = "select * "
                    + "from question;";
            //Create the statement
            Statement stmt = dbConnection.createStatement();
            //Create the resultSet
            ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {//update cursor
                String questionText = resultSet.getString("questiontext");
                Long id = resultSet.getLong("id");
                long moduleId = resultSet.getLong("moduleid");
                long strategyId = resultSet.getLong("strategyid");
                int solutionId = resultSet.getInt("solutionid");
                int maxPoints = resultSet.getInt("maxpoints");
                String feedback = resultSet.getString("feedback");
                double marge = resultSet.getDouble("foutmarge");
                Question question = new Question(id, questionText, moduleId, strategyId, solutionId, maxPoints, feedback, marge);

                questions.add(question);
            }
            stmt.close();//closing the statement
        } catch (SQLException ex) {
            Logger.getLogger(QuestionMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return questions;
    }

    /**
     * Searches the database for questions
     *
     * @param moduleId
     * @return A list of all the questions in the database for a specific Module
     */
    public List<Question> getAllQuestionsForModule(long moduleId) {
        ArrayList<Question> questionsForModule = new ArrayList<Question>();
        try {
            //Create the sql-query
            String query = "select * "
                    + "from question where question.moduleid=? Order By question.id ;";
            //Create the PREPARED statement
            PreparedStatement prepStmt = dbConnection.prepareStatement(query);
            prepStmt.setLong(1, moduleId); // Eerste vraagteken uit de query  -> waar modulenaam = module die binnengekomen is 
            //Create the resultSet
            ResultSet resultSet = prepStmt.executeQuery();
            while (resultSet.next()) {//update cursor
                String questionText = resultSet.getString("questiontext");
                long moduleID = resultSet.getLong("moduleid");
                long strategyId = resultSet.getLong("strategyid");
                int solutionId = resultSet.getInt("solutionid");
                int maxPoints = resultSet.getInt("maxpoints");
                long id = resultSet.getLong("id");
                String feedback = resultSet.getString("feedback");
                double marge = resultSet.getDouble("foutmarge");
                Question question = new Question(id, questionText, moduleId, strategyId, solutionId, maxPoints, feedback, marge);
                questionsForModule.add(question);
            }
            prepStmt.close();//closing the statement
        } catch (SQLException ex) {
            Logger.getLogger(QuestionMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return questionsForModule;
    }

    /**
     * Searches the database for a question
     *
     * @param questionTextKey
     * @return A question in the database with a specific question text
     */
    public Question getQuestion(long id) {
        Question question = null;
        try {
            //Create the sql-query
            String query = "select * "
                    + "from question where question.id=? ;";
            //Create the PREPARED statement
            PreparedStatement prepStmt = dbConnection.prepareStatement(query);
            prepStmt.setLong(1, id); // Eerste vraagteken uit de query  -> waar questiontext = questiontext die we willen zoeken
            //Create the resultSet
            ResultSet resultSet = prepStmt.executeQuery();
            if (resultSet.next()) {
                String questionText = resultSet.getString("questiontext");
                long moduleId = resultSet.getLong("moduleid");
                long strategyId = resultSet.getLong("strategyid");
                int solutionId = resultSet.getInt("solutionid");
                int maxPoints = resultSet.getInt("maxpoints");
                String feedback = resultSet.getString("feedback");
                double marge = resultSet.getDouble("foutmarge");
                question = new Question(id, questionText, moduleId, strategyId, solutionId, maxPoints, feedback, marge);

            }
            prepStmt.close();//closing the statement
        } catch (SQLException ex) {
            Logger.getLogger(QuestionMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return question;
    }

    /**
     * Adds a question to the database
     *
     * @param question
     */
    public void addQuestion(Question question) throws SQLException {
        dbConnection.setAutoCommit(false);
        PreparedStatement preparedStatement;
        try {
            if (question.getNativeFeedback().length() > 0) {
                if (question.getMarge() > 0) {
                    String insertQuery = "INSERT INTO question"
                            + "(questiontext, moduleid, strategyid, solutionid, maxpoints, feedback, foutmarge) VALUES"
                            + "(?,?,?,?,?,?,?)";
                    preparedStatement = dbConnection.prepareStatement(insertQuery);
                    preparedStatement.setString(1, question.getNativeQuestionText());
                    preparedStatement.setLong(2, question.getModuleId());
                    preparedStatement.setLong(3, question.getStrategyId());
                    preparedStatement.setInt(4, question.getSolutionId());
                    preparedStatement.setInt(5, question.getMaxPoints());
                    preparedStatement.setString(6, question.getNativeFeedback());
                    preparedStatement.setDouble(7, question.getMarge());
                } else {
                    String insertQuery = "INSERT INTO question"
                            + "(questiontext, moduleid, strategyid, solutionid, maxpoints, feedback, foutmarge) VALUES"
                            + "(?,?,?,?,?,?,Null)";
                    preparedStatement = dbConnection.prepareStatement(insertQuery);
                    preparedStatement.setString(1, question.getNativeQuestionText());
                    preparedStatement.setLong(2, question.getModuleId());
                    preparedStatement.setLong(3, question.getStrategyId());
                    preparedStatement.setInt(4, question.getSolutionId());
                    preparedStatement.setInt(5, question.getMaxPoints());
                    preparedStatement.setString(6, question.getNativeFeedback());
                }
            } else {
                if (question.getMarge() > 0) {
                    String insertQuery = "INSERT INTO question"
                            + "(questiontext, moduleid, strategyid, solutionid, maxpoints, feedback, foutmarge) VALUES"
                            + "(?,?,?,?,?,Null, ?)";
                    preparedStatement = dbConnection.prepareStatement(insertQuery);
                    preparedStatement.setString(1, question.getQuestionText());
                    preparedStatement.setLong(2, question.getModuleId());
                    preparedStatement.setLong(3, question.getStrategyId());
                    preparedStatement.setInt(4, question.getSolutionId());
                    preparedStatement.setInt(5, question.getMaxPoints());
                    preparedStatement.setDouble(6, question.getMarge());
                } else {
                    String insertQuery = "INSERT INTO question"
                            + "(questiontext, moduleid, strategyid, solutionid, maxpoints, feedback, foutmarge) VALUES"
                            + "(?,?,?,?,?,Null, Null)";
                    preparedStatement = dbConnection.prepareStatement(insertQuery);
                    preparedStatement.setString(1, question.getQuestionText());
                    preparedStatement.setLong(2, question.getModuleId());
                    preparedStatement.setLong(3, question.getStrategyId());
                    preparedStatement.setInt(4, question.getSolutionId());
                    preparedStatement.setInt(5, question.getMaxPoints());
                }
            }
            // execute insert SQL stetement
            preparedStatement.execute();
            dbConnection.commit();
            preparedStatement.close();
        } catch (SQLException ex) {
            dbConnection.rollback();
            Logger.getLogger(QuestionMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Deletes a question from the database
     *
     * @param question
     * @throws java.sql.SQLException
     */
    public void deleteQuestion(Question question) throws SQLException {
        dbConnection.setAutoCommit(false);
        try {
            String deleteQuery = "DELETE FROM question where question.id=? ;";
            PreparedStatement preparedStatement = dbConnection.prepareStatement(deleteQuery);
            preparedStatement.setLong(1, question.getId());
            // execute insert SQL stetement
            preparedStatement.executeUpdate();
            dbConnection.commit();
            preparedStatement.close();
        } catch (SQLException ex) {
            dbConnection.rollback();
            Logger.getLogger(QuestionMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Update a question from the database
     *
     * @param guestion
     * @throws java.sql.SQLException
     */
    public void updateQuestion(Question question) throws SQLException {
        dbConnection.setAutoCommit(false);
        try {
            String updateTableSQL = "UPDATE question SET questiontext = ?, moduleid = ?, strategyid = ?, solutionid = ?, maxpoints = ?, feedback = ?, foutmarge = ? WHERE id = ? ;";
            PreparedStatement preparedStatement = dbConnection.prepareStatement(updateTableSQL);
            preparedStatement.setString(1, question.getQuestionText());
            preparedStatement.setLong(2, question.getModuleId());
            preparedStatement.setLong(3, question.getStrategyId());
            preparedStatement.setInt(4, question.getSolutionId());
            preparedStatement.setInt(5, question.getMaxPoints());
            preparedStatement.setString(6, question.getFeedback());
            preparedStatement.setDouble(7, question.getMarge());
            preparedStatement.setLong(8, question.getId());
            // execute insert SQL stetement
            preparedStatement.executeUpdate();
            dbConnection.commit();
            preparedStatement.close();
        } catch (SQLException ex) {
            dbConnection.rollback();
            Logger.getLogger(QuestionMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Question getNextQuestion(long moduleId, String studnr) {
        Question question = null;
        try {

            String query = "SELECT "
                    + "  * ,(case when tries>0 then tries else 0 end)as newTries "
                    + " FROM "
                    + "  (select * from chemiedb.studentquestion where studnr=?)as student right outer join (select * from chemiedb.question where moduleid=?) as question"
                    + "  on student.questionid=question.id "
                    + " Order by newTries,id"
                    + " limit 1"
                    + " ;";
            PreparedStatement prepStmt = dbConnection.prepareStatement(query);
            prepStmt.setLong(2, moduleId); // Eerste vraagteken uit de query  -> waar questiontext = questiontext die we willen zoeken
            prepStmt.setString(1, studnr);
            //Create the resultSet
            ResultSet resultSet = prepStmt.executeQuery();
            if (resultSet.next()) {
                String questionText = resultSet.getString("questiontext");
                long id = resultSet.getLong("id");
                long strategyId = resultSet.getLong("strategyid");
                int solutionId = resultSet.getInt("solutionid");
                int maxPoints = resultSet.getInt("maxpoints");
                String feedback = resultSet.getString("feedback");
                double marge = resultSet.getDouble("foutmarge");
                question = new Question(id, questionText, moduleId, strategyId, solutionId, maxPoints, feedback, marge);
            }
            prepStmt.close();//closing the statement
        } catch (SQLException ex) {
            Logger.getLogger(QuestionMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return question;
    }

    private Progress getProgress(long moduleId, String studnr) {

        Progress progress = null;
        try {
            //SELECT SUM(case when tries=(select MIN(tries) from chemiedb.studentquestion where chemiedb.studentquestion.studnr='r0261853') then 0 else 1 end),COUNT(*) 
            //FROM  (chemiedb.question left outer join chemiedb.studentquestion on  chemiedb.question.id=chemiedb.studentquestion.questionid 
            //and chemiedb.studentquestion.studnr='r0261853') as studQ where studQ.moduleid  = 4;
            //Create the sql-query
            String query = "SELECT SUM(case when COALESCE(answer.studnr,'')= '' then 0 else 1 end),COUNT(*)"
                    + " FROM  ((select * from chemiedb.question where moduleid  =?) as question"
                    + "                       left outer join (select * from"
                    + "                       chemiedb.studentquestion "
                    + "                       where studnr=?"
                    + "			and"
                    + "			tries="
                    + "		("
                    + "		select MIN((case when tries>0 then tries else 0 end))+1"
                    + "		from (select tries, questionid from chemiedb.studentquestion where studnr=?) as studQ"
                    + "		right outer join "
                    + "		(select id from chemiedb.question where moduleid=?) as question"
                    + "		on studQ.questionid= question.id"
                    + "		)"
                    + "	)as studQ"
                    + "	on  question.id=studQ.questionid "
                    + "	and studQ.studnr=?) as answer"
                    + "	;";
            //String query = "SELECT SUM(case when COALESCE(studQ.studnr,'')= '' then 0 else 1 end),COUNT(*) FROM  (question left outer join studentquestion on  question.id=studentquestion.questionid and studentquestion.studnr=?) as studQ where studQ.moduleid  = ?;";
            //Create the PREPARED statement
            PreparedStatement prepStmt = dbConnection.prepareStatement(query);
            prepStmt.setLong(1, moduleId); // Eerste vraagteken uit de query  -> waar questiontext = questiontext die we willen zoeken
            prepStmt.setString(2, studnr);
            prepStmt.setString(3, studnr);
            prepStmt.setLong(4, moduleId);
            prepStmt.setString(5, studnr);
            //Create the resultSet
            ResultSet resultSet = prepStmt.executeQuery();

            if (resultSet.next()) {

                progress = new Progress(resultSet.getInt("sum"), resultSet.getInt("count"));

            }

            prepStmt.close();//closing the statement
        } catch (SQLException ex) {
        }
        return progress;
    }

    public class Progress {

        private int sum;
        private int count;

        public Progress(int sum, int count) {
            this.sum = sum;
            this.count = count;
        }

        public int getDone() {
            return sum;
        }

        public int getTotal() {
            return count;
        }
    }

    public double getBeginProgress(long moduleId, String studnr) {
        double progressAmount = 0;
        Progress progress = getProgress(moduleId, studnr);
        if (progress != null) {
            progressAmount = (progress.getDone() + 1);
            progressAmount /= progress.getTotal();
            progressAmount *= 100;
        }
        return progressAmount;
    }

    public double getInProgress(long moduleId, String studnr) {
        double progressAmount = 0;
        Progress progress = getProgress(moduleId, studnr);
        if (progress != null) {
            if (progress.getDone() == 0) {
                progressAmount = 101;
            } else {
                progressAmount = (progress.getDone() + 1);
                progressAmount /= progress.getTotal();
                progressAmount *= 100;
            }
        }
        return progressAmount;
    }

}
