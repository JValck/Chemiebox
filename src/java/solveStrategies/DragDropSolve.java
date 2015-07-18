package solveStrategies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author r0261853
 */
public class DragDropSolve implements SolveStrategy {

    public DragDropSolve() {

    }

    public double solve(String solution, String answer, double max) {
        double scored = 0;
        //ignore 1 as coefficient
        solution = solution.replaceAll("(1){1}", "");
        answer = answer.replaceAll("(1){1}", "");
        String[] leftRightSol = null;
        if (solution.contains("#->#")) {
            leftRightSol = solution.split("#->#");
        } else if (solution.contains("#<=>")) {
            leftRightSol = solution.split("#<=>#");
        }
        String[] leftRightAns = null;
        if (answer.contains("#->#")) {
            leftRightAns = answer.split("#->#");
        } else if (answer.contains("#<=>")) {
            leftRightAns = answer.split("#<=>#");
        }
        if (leftRightAns.length > 1) {

            String[] leftSol = leftRightSol[0].split("#&#");

            String[] rightSol = leftRightSol[1].split("#&#");

            String[] leftAns = leftRightAns[0].split("#&#");

            String[] rightAns = leftRightAns[1].split("#&#");

            List<String> listLeft = new ArrayList<String>(Arrays.asList(leftSol));
            List<String> listRight = new ArrayList<String>(Arrays.asList(rightSol));

            List<String> listLeftAns = new ArrayList<String>(Arrays.asList(leftAns));
            List<String> listRightAns = new ArrayList<String>(Arrays.asList(rightAns));

            ListIterator<String> leftIt = listLeft.listIterator();
            ListIterator<String> rightIt = listRight.listIterator();

            ListIterator<String> leftItAns = listLeftAns.listIterator();
            ListIterator<String> rightItAns = listRightAns.listIterator();

            while (leftItAns.hasNext()) {
                String leftStringAns = leftItAns.next();
                while (leftIt.hasNext()) {
                    String leftStringSol = leftIt.next();
                    if (leftStringAns.trim().equals(leftStringSol.trim())) {

                        leftIt.remove();
                        leftItAns.remove();
                        break;

                    }
                }
                leftIt = listLeft.listIterator();
            }
            while (rightItAns.hasNext()) {
                String rightStringAns = rightItAns.next();
                while (rightIt.hasNext()) {
                    String rightStringSol = rightIt.next();
                    if (rightStringAns.trim().equals(rightStringSol.trim())) {

                        rightItAns.remove();
                        rightIt.remove();
                        break;
                    }

                }
                rightIt = listRight.listIterator();
            }

            if ((listRight.size() + listLeft.size() + listLeftAns.size() + listRightAns.size()) == 0) {
                scored = max;
            }

        }
        return scored;
    }
}
