/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solveStrategies;

import domain.Question;
import java.util.regex.Pattern;

/**
 *
 * @author jasper
 */
class TableSolver implements SolveStrategy {

    private final Question question;

    public TableSolver(Question q) {
        this.question = q;
    }

    @Override
    public double solve(String nativeSolutionText, String answer, double max) {
        double scored = 0.0;
        String fullSolution = nativeSolutionText;
        String userAnswer = answer.substring(answer.indexOf("<solutions>") + 11, answer.indexOf("</solutions>")).trim();
        String solution = fullSolution.substring(fullSolution.indexOf("<solutions>") + 11, fullSolution.indexOf("</solutions>")).trim();
        double foutmarge = question.getMarge();
        if (foutmarge == 0.0) {
            if (userAnswer.equalsIgnoreCase(solution)) {
                scored = max;
            }
        } else {
            //check coefficients
            int endOfCoefSolution = solution.lastIndexOf("</coef>");
            int endOfCoefAnswer = userAnswer.lastIndexOf("</coef>");
            String coefSolution = solution.substring(0, endOfCoefSolution);
            String coefAnswer = userAnswer.substring(0, endOfCoefAnswer);
            if (!coefAnswer.equalsIgnoreCase(coefSolution)) {
                return 0.0;//nul tolerance
            }
            //prepare for splitting
            userAnswer = userAnswer.replaceAll("<coef>[0-9]*</coef>", "").replaceAll("<amount>", "").trim();
            solution = solution.replaceAll("<coef>[0-9]*</coef>", "").replaceAll("<amount>", "").trim();
            String[] userAnswerParts = userAnswer.split("</amount>");
            String[] solutionParts = solution.split("</amount>");
            //both same length
            boolean ok = true;
            for (int i = 0, j = 0; i < userAnswerParts.length && j < solutionParts.length && ok == true; i++, j++) {
                if (Pattern.matches("[a-zA-Z]+", solutionParts[i]) || Pattern.matches("[a-zA-Z]+", userAnswerParts[i])) {//bevat letter, mag niet
                    if (!solutionParts[i].equals(userAnswerParts[i])) {
                        ok = false;
                    }
                }
                if (!solutionParts[j].matches(".*[a-zA-Z]+.*")) {
                    String solutionAtPlaceJ = solutionParts[j].replaceAll(",", ".").trim();

                    double[] range = getRange(solutionAtPlaceJ, foutmarge);
                    String ansAtPlaceI = userAnswerParts[i].replaceAll(",", ".").trim();
                    double ans = Double.parseDouble(ansAtPlaceI);
                    if (ans >= range[0] && ans <= range[1]) {
                        ok = true;
                    } else {
                        ok = false;
                    }
                } else {
                    String ans = userAnswerParts[i].replaceAll(",", ".").trim();
                    String solutionAtPlaceI = solutionParts[j].replaceAll(",", ".").trim();
                    if (ans.toUpperCase().equals(solutionAtPlaceI) || ans.toLowerCase().equals(solutionAtPlaceI)) {
                        ok = true;
                    } else {
                        ok = false;
                    }
                }
            }
            return ok ? max : 0.0;
        }
        return scored;
    }

    private double[] getRange(String solutionPart, double foutmarge) {
        double[] res = {0.0, 0.0};
        if (solutionPart.length() != 0) {
            double marge = 0;
            double sol = Double.parseDouble(solutionPart);
            //if (foutmarge != 0 || foutmarge != 0.0) {
                marge = (sol / 100) * foutmarge;
            //}
            res[0] = sol - marge;
            res[1] = sol + marge;
        }
        return res;
    }

}
