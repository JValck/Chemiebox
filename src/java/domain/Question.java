package domain;

import converter.DatabaseToHTMLConverter;

public class Question {
    
     private String questionText, feedback;
     private int solutionId, maxPoints;
     private long id, moduleId, strategyId;
     private double marge;
     
     public Question(long id, String questionText, long moduleId, long strategyId, int solutionId, int maxPoints, String feedback, double marge){
         this.questionText = questionText;
         this.moduleId = moduleId;
         this.strategyId = strategyId;
         this.solutionId = solutionId;
         this.maxPoints = maxPoints;
         this.id = id;
         this.feedback = feedback;
         this.marge=marge;
     }
     
     public Question(String questionText, long moduleId, long strategyId, int solutionId, int maxPoints, String feedback, double marge){
         this.questionText = questionText;
         this.moduleId = moduleId;
         this.strategyId = strategyId;
         this.solutionId = solutionId;
         this.maxPoints = maxPoints;
         this.feedback = feedback;
         this.marge = marge;
     }
     
     public Question(){}

    public String getQuestionText() {   
        return DatabaseToHTMLConverter.convertToHTML(questionText);
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public long getModuleId() {
        return moduleId;
    }

    public void setModuleId(long moduleId) {
        this.moduleId = moduleId;
    }

    public long getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(long strategyId) {
        this.strategyId = strategyId;
    }

    public int getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(int solutionId) {
        this.solutionId = solutionId;
    }
    
     public double getMarge() {
        return marge;
    }

    public void setMarge(double marge) {
        this.marge = marge;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFeedback() {
        if(feedback != null){
        return DatabaseToHTMLConverter.convertToHTML(feedback);
        }
        return "";
    }
    
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
    
    /**
     * Gets the native question text, this is the plain text from the database
     * @return The plain database question text
     */
    public String getNativeQuestionText(){
        return this.questionText;
    }
    
    public String getNativeFeedback(){
        return this.feedback;
    }
      
    
}
