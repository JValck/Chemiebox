/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import converter.DatabaseToHTMLConverter;

/**
 * Helper class for the student overview
 * @author r0430844
 */
public class StudentQuestionResult {
    private String studentNuber, module, questionText, lastanswer, 
            points, maxpoints;
    private boolean correct;
    private int tries;
    
    public StudentQuestionResult(String studentNuber, String module, String questionText, String lastanswer, String points, int tries) {
        this.studentNuber = studentNuber;
        this.module = module;
        this.questionText = questionText;
        this.lastanswer = lastanswer;
        this.points = points;
        this.tries = tries;
    }

    public StudentQuestionResult() {
        
    }

    public String getStudentNuber() {
        return studentNuber;
    }

    public String getModule() {
        return module;
    }

    public String getQuestionText() {
        return DatabaseToHTMLConverter.convertToHTML(questionText);
    }

    public String getLastanswer() {
        return DatabaseToHTMLConverter.convertToHTML(lastanswer);
    }

    public String getPoints() {
        return points;
    }

    public int getTries() {
        return tries;
    }

    public void setStudentNuber(String studentNuber) {
        this.studentNuber = studentNuber;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setLastanswer(String lastanswer) {
        this.lastanswer = lastanswer;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public void setMaxPoints(String maxpoints) {
        this.maxpoints = maxpoints;
    }
    
    public String getMaxPoints(){
        return maxpoints;
    }

    
}
