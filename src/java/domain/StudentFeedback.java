package domain;

/**
 * Helper class to give feedback to the student at the end of a module
 * @author r0430844
 */
public class StudentFeedback {
    private String questionText, feedback, studentAnswer;
    private int score, maxScore;
    
    public StudentFeedback(){}

    public StudentFeedback(String questionText, String feedback, String studentAnswer, int score, int maxScore) {
        this.questionText = questionText;
        this.feedback = feedback;
        this.studentAnswer = studentAnswer;
        this.score = score;
        this.maxScore = maxScore;
    }
    

    public String getStudentAnswer() {
        return converter.DatabaseToHTMLConverter.convertToHTML(studentAnswer);
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public String getQuestionText() {
        return converter.DatabaseToHTMLConverter.convertToHTML(questionText);
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getFeedback() {
        if(feedback == null) return "Geen feedback voor deze vraag";
        if(feedback != null && feedback.isEmpty()) return "Geen feedback voor deze vraag";
        return converter.DatabaseToHTMLConverter.convertToHTML(feedback);
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
     public int getScore() {
         return score;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }
    
    public int getMaxScore(){
        return maxScore;
    }
    
}
