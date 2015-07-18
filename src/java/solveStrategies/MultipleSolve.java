/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solveStrategies;

/**
 *
 * @author r0261853
 */
public class MultipleSolve implements SolveStrategy {

    public MultipleSolve() {

    }

    public double solve(String solution, String answer, double max) {
        double scored = 0;
        String[] choices = solution.split("##");
        if (answer.trim().equals(choices[0].trim())) {

            scored = max;

        }
        return scored;
    }
}
