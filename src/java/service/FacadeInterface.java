/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package service;
import domain.Module;
import domain.Question;
import domain.Strategy;
import domain.StudentQuestion;
import domain.Solution;
import domain.StudentFeedback;
import domain.StudentQuestionResult;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author r0431118
 */
public interface FacadeInterface {
    public boolean authenticate(String studnr, String password);
    
    /**
     * Searches the database for modules
     * @return A list of all the modules in the database
     */
    public List<Module> getAllModules();
    public void addModule(Module module);
    /**
     * Updates the module in the database
     * @param updatedModule The updated module
     */
    public void updateModule(Module updatedModule);
    public void deleteModule(Module module);
    public Module getModule(long id);
    
    public List<Question> getAllQuestions();
    public List<Question> getAllQuestionsForModule(long id);
    public Question getQuestion(long id);
    public Question getNextQuestion(long moduleid, String studnr);
    public double getBeginProgress(long moduleid, String studnr);
    public double getInProgress(long moduleid, String studnr);
    public void addQuestion(Question question);
    public void deleteQuestion(Question question);
    /**
     * @param question
     */
    public void updateQuestion(Question question);
    
    public List<Strategy> getAllStrategies();
    public Strategy getStrategy(long id);
    
    public List<StudentQuestion> getAllStudentQuestions();
    public StudentQuestion getStudentQuestion(long id, String studnr);
    public void addStudentQuestion(StudentQuestion question) throws SQLException;
    public void deleteStudentQuestion(StudentQuestion question) throws SQLException;
    public void updateStudentQuestion(StudentQuestion question) throws SQLException;

    
    public List<Solution> getSolution(int id);
    public Solution getSolutionByText(String text);
    public void addSolution(Solution solution);
    public void removeSolution(Solution solution);
    public void updateSolution(Solution solution);

    public List<String> getMonths();
    
    public int getTimesTried(long moduleid, String studnr);
    /**
     * Asks the db for the solutions of a question
     * @param id The question id
     * @return A list of solutions
     */
    public List<Solution> getSolutionByQuestionId(long id);

    /**
     * Asks the db for all the student answers and checks if ok
     * @return List of studentQuestionResults
     */
    public List<StudentQuestionResult> getStudentQuestionResults();
    public List<StudentQuestionResult> getFilteredResults();
    public List<StudentFeedback> getStudentFeedbackForModule(long moduleId, String studentNumber);
}
