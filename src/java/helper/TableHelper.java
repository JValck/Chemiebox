package helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper for the table question
 *
 * @author r0430844
 */
public class TableHelper implements ArrowChangeable{

    private String nativeDbSolution;
    private int reactionParts;
    private List<String> leftSideElements, rightSideElements;
    private boolean evenwichtsReactie;

    public TableHelper(String nativeDbSolution) {
        this.nativeDbSolution = nativeDbSolution;
        leftSideElements = new ArrayList<String>();
        rightSideElements = new ArrayList<String>();
        initReactionParts();
    }

    public int getReactionParts() {
        return reactionParts;
    }

    public int numberOfColumns() {
        return reactionParts + 1;//+1 --> arrow
    }

    private void initReactionParts() {
        String reaction = nativeDbSolution.substring(nativeDbSolution.indexOf("<reaction>") + 10, nativeDbSolution.indexOf("</reaction>"));
        String[] sides = null;
        if (reaction.contains("#->#")) {
            sides = reaction.split("#->#");
        } else if (reaction.contains("#<=>#")) {
            evenwichtsReactie = true;
            sides = reaction.split("#<=>#");
        }

        initLeftSide(sides[0]);
        if (sides[1] != null) {
            initRightSide(sides[1]);
        }
        reactionParts = leftSideElements.size() + rightSideElements.size();
    }

    private void initLeftSide(String side) {
        String[] parts = side.split("#&#");
        for (String part : parts) {
            part = (part.replaceAll("^([0-9]+)", ""));//remove coefficients
            leftSideElements.add(converter.DatabaseToHTMLConverter.convertToHTML(part));
        }
    }

    private void initRightSide(String side) {
        String[] parts = side.split("#&#");
        for (String part : parts) {
            part = (part.replaceAll("^([0-9]+)", ""));
            rightSideElements.add(converter.DatabaseToHTMLConverter.convertToHTML(part));
        }
    }

    /**
     *
     * @return The printable elements for the table
     */
    public List<String> getLeftSideElements() {
        return leftSideElements;
    }

    /**
     * @return The printable elements for the table
     */
    public List<String> getRightSideElements() {
        return rightSideElements;
    }

    @Override
    public boolean isEvenwichtsReactie() {
        return evenwichtsReactie;
    }    
}
