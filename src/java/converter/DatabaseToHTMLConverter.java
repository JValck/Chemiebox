package converter;

import domain.LinkedElement;
import domain.Translator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converts a database entry to readable text
 *
 * @author r0430844
 */
public class DatabaseToHTMLConverter {

    /**
     * Converts a database entry to readable text
     *
     * @param entry - The string to convert. For example: Wat is
     * C_|6|H_|12|O_|6|?
     * @return The html to show: example above: Wat is
     * C<sub>6</sub>H<sub>12</sub>O<sub>6</sub>?
     */
    public static String convertToHTML(String entry) {
        entry = entry.trim();
        entry = processDbLegend(entry);
        entry = processSubAndSuper(entry);
        return entry;
    }

    public static void main(String[] args) {
        //System.out.println(DatabaseToHTMLConverter.convertToHTML("Vervolledig: Bi_|2|O_|3|#&#6OH^|-|#->#2BiO_|3|^|-|#&#4e^|1-|#&#3H_|2|O en A_|1234|OL"));
        System.out.println(DatabaseToHTMLConverter.convertToHTML("A_|1x234|OL"));
    }

    private static String processDbLegend(String word) {
        word = word.replaceAll("##", " of ");
        word = word.replaceAll("#&#", " + ");
        word = word.replaceAll("#->#", " &#8594; ");
        word = word.replaceAll("#<=>#", " &#8660; ");
        word = word.replaceAll("-([0-9]+)", "$1-");
        return word;
    }

    private static String processSubAndSuper(String entry) {
        entry = entry.replaceAll("_\\|([\\+-])?([0-9xyzXYZ]*)([\\+-])?\\|", "<sub>$1$2$3</sub>");
        entry = entry.replaceAll("\\^\\|([\\+-])?([0-9xyzXYZ]*)([\\+-])?\\|", "<sup>$1$2$3</sup>");
        entry = entry.replaceAll("(<su[pb]>)1-(</su[pb])", "$1-$2");//replaces a sub or super 1- with a single -
        return entry;
    }
}
