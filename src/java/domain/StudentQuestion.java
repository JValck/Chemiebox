/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

/**
 *
 * @author Fery
 */
public class StudentQuestion {

    private String studnr;
    private int score, tries;
    private long questionId;
    private String lastAnswer;

    public StudentQuestion(long questionId, String studnr, int score, int tries, String lastAnswer) {
        this.questionId = questionId;
        this.studnr = studnr;
        this.score = score;
        this.tries = tries;
        this.lastAnswer = lastAnswer;
    }

    public StudentQuestion() {
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public String getStudnr() {
        return studnr;
    }

    public void setStudnr(String studnr) {
        this.studnr = studnr;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

    public String getLastAnswer() {
        return lastAnswer;
    }

    public void setLastAnswer(String lastAnswer) {
        if (lastAnswer.equals("Hier typen")) {
            this.lastAnswer = "";
        } else {
            this.lastAnswer = lastAnswer;
        }
    }
}
