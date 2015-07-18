package db.mappers;

import db.DatabaseInterface;
import domain.StudentFeedback;
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
 * Helper mapper to give feedback to the student at the end of a module
 *
 * @author r0430844
 */
public class StudentFeedbackMapper {

    private final Connection dbConnection;

    public StudentFeedbackMapper(DatabaseInterface db) {
        this.dbConnection = db.getConnection();
    }

    public List<StudentFeedback> getFeedbackForModule(long moduleId, String studentNumber) {
        List<StudentFeedback> list = new ArrayList<StudentFeedback>();
        String query = "SELECT q.questiontext, q.feedback, sq.lastanswer, sq.score, q.maxpoints\n"
                + "FROM question q inner join studentquestion sq\n"
                + "	ON (sq.questionid = q.id)\n"
                + "WHERE sq.studnr = ? and q.moduleid = ?;";
        try {
            PreparedStatement statement = dbConnection.prepareStatement(query);
            statement.setString(1, studentNumber);
            statement.setLong(2, moduleId);
            ResultSet rSet = statement.executeQuery();
            while(rSet.next()){
                StudentFeedback feedback = new StudentFeedback();
                feedback.setFeedback(rSet.getString("feedback"));
                feedback.setQuestionText(rSet.getString("questiontext"));
                feedback.setStudentAnswer(rSet.getString("lastanswer"));
                feedback.setScore(rSet.getInt("score"));
                feedback.setMaxScore(rSet.getInt("maxpoints"));
                list.add(feedback);
            }            
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(StudentFeedbackMapper.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return list;
    }
}
