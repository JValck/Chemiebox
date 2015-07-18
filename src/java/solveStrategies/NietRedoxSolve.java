package solveStrategies;

import helper.ReactionCompleteHelper;

/**
 *
 * @author r0430844
 */
public class NietRedoxSolve implements SolveStrategy {

    public double solve(String solution, String answer, double max) {
        if (answer.length() < 2) {
            return 0.0;
        }
        String answersStudent = answer.substring(2, answer.length() - 2);
        answersStudent = answersStudent.replaceAll(",", ".");
        String[] answers = answersStudent.split("\"\\.\"");
        ReactionCompleteHelper helper = new ReactionCompleteHelper(solution);
        int i = 1;
        String toValidate = "";
        for (String part : answers) {
            toValidate += part;
            if (i == helper.getPartsBefore()) {
                if (solution.contains("#->#")) {
                    toValidate += "#->#";
                } else if (solution.contains("<=>")) {
                    toValidate += "#<=>#";
                }
            } else {
                toValidate += "#&#";
            }
            i++;
        }
        SolveStrategy strat = SolverFactory.getSolver("Drag n Drop", null);
        return strat.solve(solution, toValidate.substring(0, toValidate.length() - 3), max);
    }
}
