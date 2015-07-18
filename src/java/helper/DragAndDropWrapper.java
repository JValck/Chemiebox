package helper;

import converter.DatabaseToHTMLConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * This class is a wrapper for the drag and drop functionality
 *
 * @author r0430844
 */
public class DragAndDropWrapper implements ArrowChangeable{

    private int itemsBefore, itemsAfter;
    private String nativeDbSolution;
    private List<String> randomElements, leftSide, rightSide;
    private boolean evenwichtsReactie;

    public DragAndDropWrapper(String nativeDbSolution) {
        this.nativeDbSolution = nativeDbSolution;
        processString();
    }

    /**
     * Get number of elements before the arrow
     *
     * @return number of elements before arrow
     */
    public int getItemsBefore() {
        return itemsBefore;
    }

    /**
     * Gets the number of elements after the reaction arrow
     *
     * @return number of elements after arrow
     */
    public int getItemsAfter() {
        return itemsAfter;
    }

    private void processString() {
        String [] parts = null;
        if (nativeDbSolution.contains("#->#")) {
            parts = nativeDbSolution.split("#->#");
        } else if (nativeDbSolution.contains("#<=>#")) {
            evenwichtsReactie = true;
            parts = nativeDbSolution.split("#<=>#");
        }
        String[] itemsBefore = parts[0].split("#&#");
        leftSide = Arrays.asList(itemsBefore);
        String[] itemsAfter = parts[1].split("#&#");
        rightSide = Arrays.asList(itemsAfter);
        this.itemsAfter = itemsAfter.length;
        this.itemsBefore = itemsBefore.length;
    }

    /**
     * Checks both parts of the reaction, checks which has the most items
     *
     * @return The highest number of elements
     */
    public int getMaxItems() {
        return (itemsBefore >= itemsAfter) ? itemsBefore : itemsAfter;
    }

    /**
     * A -> B + C gives 1
     *
     * @return The size difference
     */
    public int getSizeDifference() {
        return (itemsBefore >= itemsAfter) ? itemsBefore - itemsAfter : itemsAfter - itemsBefore;
    }

    public boolean leftIsSmaller() {
        return (itemsBefore >= itemsAfter);
    }

    public boolean rightIsSmaller() {
        return (itemsAfter >= itemsBefore);
    }

    public boolean bothSidesSameLength() {
        return itemsAfter == itemsBefore;
    }

    public String getRandomElement() {
        Random r = new Random();
        return randomElements.get(r.nextInt(randomElements.size()));
    }

    public List<String> getLeftSide() {
        List<String> readable = new ArrayList<String>();
        for (String element : leftSide) {
            element = element.replaceAll("^[0-9]+", "");
            readable.add(DatabaseToHTMLConverter.convertToHTML(element));
        }
        HashSet<String> left = new HashSet<String>(readable);
        return new ArrayList<String>(left);
    }

    public List<String> getRightSide() {
        List<String> readable = new ArrayList<String>();
        for (String element : rightSide) {
            element = element.replaceAll("^[0-9]+", "");
            readable.add(DatabaseToHTMLConverter.convertToHTML(element));
        }
        HashSet<String> right = new HashSet<String>(readable);
        return new ArrayList<String>(right);
    }

    public boolean isEvenwichtsReactie() {
        return evenwichtsReactie;
    }
    
}
