/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solveStrategies;

import converter.DatabaseToHTMLConverter;
import domain.Question;

/**
 *
 * @author r0261853
 */
public class NormalSolve implements SolveStrategy {

    private Question question;

    public NormalSolve(Question question) {
        this.question = question;
    }

    public double solve(String solution, String answer, double max) {
        double scored = 0;
        String fullSolutionHTML = DatabaseToHTMLConverter.convertToHTML(solution);

        String answerHTML = DatabaseToHTMLConverter.convertToHTML(answer);
        String[] solutionsText = fullSolutionHTML.split(" of ");
        double foutmarge = question.getMarge();
        if (foutmarge == 0.0) {
            for (String text : solutionsText) {
                if (text.trim().equals(answerHTML.trim())) {
                    scored = max;
                    break;
                }
            }
        } else {
            try {
                double parsedSolution = Double.parseDouble(solution);
                double[] range = getRange(parsedSolution, foutmarge);
                double ans = Double.parseDouble(answer);
                if (ans >= range[0] && ans <= range[1]) {
                    scored = max;
                }
            } catch (NumberFormatException ex) {
                for (String text : solutionsText) {
                    if (text.trim().equals(answerHTML.trim())) {
                        scored = max;
                        break;
                    }
                }
            }
        }
        return scored;
    }

    private double[] getRange(double sol, double foutmarge) {
        double[] res = {0.0, 0.0};
        double marge = (sol / 100) * foutmarge;
        res[0] = sol - marge;
        res[1] = sol + marge;
        return res;
    }
}
