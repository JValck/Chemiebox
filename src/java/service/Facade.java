package service;

import db.DatabaseInterface;

import db.mappers.*;
import domain.Module;
import domain.Months;
import domain.Question;
import domain.Solution;
import domain.Strategy;
import domain.StudentQuestion;
import domain.StudentQuestionResult;
import domain.StudentFeedback;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ldap.Authenticator;

public class Facade implements FacadeInterface {

    private final DatabaseInterface chemieTestenDb;
    private Authenticator authenticator;
    private QuestionMapper questionMapper;
    private ModuleMapper moduleMapper;
    private StrategyMapper strategyMapper;
    private StudentQuestionMapper studentQuestionMapper;
    private SolutionMapper solutionMapper;
    private Months months;
    private StudentQuestionResultMapper studentQuestionResultMapper;
    private StudentFeedbackMapper studentFeedbackMapper;

    public Facade(DatabaseInterface db) {
        chemieTestenDb = db;
        authenticator = new Authenticator();
        questionMapper = new QuestionMapper(chemieTestenDb);
        moduleMapper = new ModuleMapper(chemieTestenDb);
        strategyMapper = new StrategyMapper(chemieTestenDb);
        studentQuestionMapper = new StudentQuestionMapper(chemieTestenDb);
        solutionMapper= new SolutionMapper(chemieTestenDb);
        months = new Months();
        studentQuestionResultMapper = new StudentQuestionResultMapper(chemieTestenDb);
        studentFeedbackMapper = new StudentFeedbackMapper(chemieTestenDb);
    }

    @Override
    public boolean authenticate(String studnr, String password) {
        
        return authenticator.authenticate(studnr, password);
    }

    /**
     * Searches the database for modules
     *
     * @return A list of all the modules in the database
     */
    @Override
    public List<Module> getAllModules() {
        moduleMapper = new ModuleMapper(chemieTestenDb);
        return moduleMapper.getAllModules();
    }

    @Override
    public Module getModule(long id) {
        return moduleMapper.getModule(id);
    }

    @Override
    public void addModule(Module module) {
        try {
            moduleMapper.addModule(module);
        } catch (SQLException ex) {
            Logger.getLogger(Facade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void updateModule(Module updatedModule) {
        try {
            moduleMapper.updateModule(updatedModule);
        } catch (SQLException ex) {
            Logger.getLogger(Facade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteModule(Module module) {
        try {
            moduleMapper.deleteModule(module);
        } catch (SQLException ex) {
            Logger.getLogger(Facade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Searches the database for questions
     *
     * @return A list of all the questions in the database
     */
    @Override
    public List<Question> getAllQuestions() {
        return questionMapper.getAllQuestions();
    }

    /**
     * Searches the database for questions
     *
     * @param module
     * @return A list of all the questions in the database for a specific Module
     */
    @Override
    public List<Question> getAllQuestionsForModule(long id) {
        return questionMapper.getAllQuestionsForModule(id);
    }

    /**
     * Searches the database for a question
     *
     * @param questionText
     * @return A question in the database with a specific question text
     */
    @Override
    public Question getQuestion(long id) {
        return questionMapper.getQuestion(id);
    }

    /**
     * Adds a question to the database
     *
     * @param question
     */
    @Override
    public void addQuestion(Question question) {
        try {
            questionMapper.addQuestion(question);
        } catch (SQLException ex) {
            Logger.getLogger(Facade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Delete a question from the database
     *
     * @param question
     */
    @Override
    public void deleteQuestion(Question question) {
        try {
            questionMapper.deleteQuestion(question);
        } catch (SQLException ex) {
            Logger.getLogger(Facade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Update a question from the database
     *
     * @param question
     * @throws java.sql.SQLException
     */
    @Override
    public void updateQuestion(Question question) {
        try {
            questionMapper.updateQuestion(question);
        } catch (SQLException ex) {
            Logger.getLogger(Facade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public Question getNextQuestion(long moduleid, String studnr){
        return questionMapper.getNextQuestion(moduleid, studnr);
    }
    
    @Override
    public double getBeginProgress(long moduleid, String studnr){
        return questionMapper.getBeginProgress(moduleid, studnr);
    }
    
    @Override
    public double getInProgress(long moduleid, String studnr){
        return questionMapper.getInProgress(moduleid, studnr);
    }

    @Override
    public List<Strategy> getAllStrategies() {
        return strategyMapper.getAllStrategies();
    }

    @Override
    public Strategy getStrategy(long id) {
        return strategyMapper.getStrategy(id);
    }

    @Override
    public List<StudentQuestion> getAllStudentQuestions() {
        return studentQuestionMapper.getAllStudentQuestions();
    }

    @Override
    public StudentQuestion getStudentQuestion(long id, String studnr) {
        return studentQuestionMapper.getStudentQuestion(id,studnr);
    }
    
    

    @Override
    public void addStudentQuestion(StudentQuestion question) throws SQLException {
        studentQuestionMapper.addStudentQuestion(question);
    }

    @Override
    public void deleteStudentQuestion(StudentQuestion question) throws SQLException {
        studentQuestionMapper.deleteStudentQuestion(question);
    }

    @Override
    public void updateStudentQuestion(StudentQuestion question) throws SQLException {
        studentQuestionMapper.updateStudentQuestion(question);
    }
    
    @Override
    public List<Solution> getSolution(int id){
        return solutionMapper.getSolution(id);
    }
    
    @Override
    public void addSolution(Solution solution){
        solutionMapper.addSolution(solution);
    }
    
    @Override
    public void removeSolution(Solution solution){
        solutionMapper.deleteSolution(solution);
    }
    
    @Override
    public void updateSolution(Solution solution){
        solutionMapper.updateSolution(solution);
    }
    
    public List<String> getMonths(){
        return months.getMonths();
    }
    
    @Override
    public int getTimesTried(long moduleid, String studnr){
        return studentQuestionMapper.getTimesTried(moduleid, studnr);
    }

    @Override
    public List<Solution> getSolutionByQuestionId(long id) {
        return solutionMapper.getSolutionByQuestionId(id);
    }
    @Override
    public Solution getSolutionByText(String text) {
        return solutionMapper.getSolutionByText(text);
    }
    
    /**
     * Asks the db for all the student answers and checks if ok
     * @return List of studentQuestionResults
     */
    public List<StudentQuestionResult> getStudentQuestionResults(){
        return studentQuestionResultMapper.getAll();
    }
    
    public List<StudentFeedback> getStudentFeedbackForModule(long moduleId, String studentNumber){
        return studentFeedbackMapper.getFeedbackForModule(moduleId, studentNumber);
    }
    
    public List <StudentQuestionResult> getFilteredResults(){
        return studentQuestionResultMapper.getPointsPerModulePerStudent();
    }
}
