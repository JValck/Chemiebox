/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.mappers;

import db.DatabaseInterface;
import domain.StudentQuestionResult;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author r0430844
 */
public class StudentQuestionResultMapper {

    private final Connection dbConnection;

    public StudentQuestionResultMapper(DatabaseInterface db) {
        this.dbConnection = db.getConnection();
    }

    public List<StudentQuestionResult> getAll() {
        ArrayList<StudentQuestionResult> res = new ArrayList<StudentQuestionResult>();
        try {
            String query = "SELECT sq.studnr, m.chapter ||' '||m.name as module ,q.questiontext,\n"
                    + "	CASE WHEN (sq.score = q.maxpoints) THEN true\n"
                    + "	ELSE false END as correct,\n"
                    + "	sq.lastanswer,\n"
                    + "	CASE WHEN (sq.score < 0) THEN '0/'||q.maxpoints\n"
                    + "	ELSE sq.score||'/'||q.maxpoints END as points , sq.tries\n"
                    + "FROM question q INNER JOIN studentquestion sq \n"
                    + "	ON(q.id = sq.questionid)\n"
                    + "	INNER JOIN module m ON(q.moduleid = m.id)\n"
                    + "ORDER BY studnr, module";
            Statement stmt = (dbConnection).createStatement();
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                StudentQuestionResult sq = new StudentQuestionResult();
                sq.setStudentNuber(result.getString("studnr"));
                sq.setModule(result.getString("module"));
                sq.setQuestionText(result.getString("questiontext"));
                sq.setCorrect(result.getBoolean("correct"));
                sq.setLastanswer(result.getString("lastanswer"));
                sq.setPoints(result.getString("points"));
                sq.setTries(result.getInt("tries"));
                res.add(sq);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(StudentQuestionResultMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    
    public List<StudentQuestionResult> getPointsPerModulePerStudent() {
        ArrayList<StudentQuestionResult> res = new ArrayList<StudentQuestionResult>();
        try {
            String query = "SELECT sq.studnr, m.chapter ||' '|| m.name as module, SUM(score) as total, SUM(q.maxpoints) as noemer\n" +
"                     FROM question q INNER JOIN studentquestion sq\n" +
"                     ON(q.id = sq.questionid)\n" +
"                     INNER JOIN module m ON(q.moduleid = m.id)\n" +
"                     GROUP BY sq.studnr, module\n" +
"                     ORDER BY module, studnr";
            Statement stmt = (dbConnection).createStatement();
            ResultSet result = stmt.executeQuery(query);
            
            while (result.next()) {
                StudentQuestionResult sq = new StudentQuestionResult();
                sq.setStudentNuber(result.getString("studnr"));
                sq.setModule(result.getString("module"));
                sq.setMaxPoints(result.getString("noemer"));
                sq.setPoints(result.getString("total"));
                res.add(sq);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(StudentQuestionResultMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
}
