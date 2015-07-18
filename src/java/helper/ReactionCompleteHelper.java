package helper;

/**
 * Helper class for the wrapper complete helper
 *
 * @author r0430844
 */
public class ReactionCompleteHelper implements ArrowChangeable{

    private String nativeDbSolution;
    private int partsBefore, partsAfter;
    private boolean evenwichtsReactie;

    public ReactionCompleteHelper(String nativeDbSolution) {
        this.nativeDbSolution = nativeDbSolution;
        initParts();
    }

    private void initParts() {
        String[] arrowSplitted;
        if (nativeDbSolution.contains("#->#")) {
            arrowSplitted = nativeDbSolution.split("#->#");
        } else {
            this.evenwichtsReactie = true;
            arrowSplitted = nativeDbSolution.split("#<=>#");
        }
        partsBefore = arrowSplitted[0].split("#&#").length;
        if (arrowSplitted[1] != null) {
            partsAfter = arrowSplitted[1].split("#&#").length;
        }
    }

    public int getPartsBefore() {
        return partsBefore;
    }

    public int getPartsAfter() {
        return partsAfter;
    }

    public static String parseAnswer(String answer, String solution) {
        if (answer.length() < 2) {
            return "";
        }
        String answersStudent = answer.substring(2, answer.length() - 2);
        answersStudent = answer.replaceAll(",", ".");
        String[] answers = answersStudent.split("\"\\.\"");
        ReactionCompleteHelper helper = new ReactionCompleteHelper(solution);
        int i = 1;
        int j = 1;
        String toValidate = "";
        for (String part : answers) {
            toValidate += part;
            if (i != helper.getPartsBefore()) {
                if (solution.contains("#->#")) {
                    toValidate += "#->#";
                } else if (solution.contains("#<=>#")) {
                    toValidate += "#<=>#";
                }
            } else if (helper.getPartsBefore() != j) {
                toValidate += "#&#";
            }
            i++;
            j++;
        }
        return toValidate;
    }

    public boolean isEvenwichtsReactie() {
        return evenwichtsReactie;
    }    
}
