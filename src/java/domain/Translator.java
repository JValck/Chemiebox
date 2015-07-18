package domain;

import java.util.LinkedList;

public class Translator {

    private static LinkedList<Integer> oxidaties = new LinkedList<Integer>();
    private static LinkedList<Integer> sizes = new LinkedList<Integer>();
    private static LinkedList<Integer> quantities = new LinkedList<Integer>();

    public static LinkedElement translate(String elements) {
        LinkedElement last = null;
        int number = 1;
        int OG = 0;
        int saveNumber = 0;
        int place = elements.length() - 1;
        String next = "" + elements.charAt(place);
        int size = 0;
        while (place >= 0) {

            //controleren of element
            Element name = isElement(next);
            if (name != null) {

                if (place > 0) {//voor tiritium
                    Element ti = isElement((elements.charAt(place - 1)) + next);
                    if (ti != null) {
                        name = ti;
                        place--;
                    }

                }
                next = "";
                size++;//voor combinatie, weet dan uit hoeveel elementen het zal bestaan
                //al het voorgaande linken
                if (last != null) {
                    last = new LinkedElement(name, number, last, OG);

                } else {

                    last = new LinkedElement(name, number, OG);
                }
                //hoeveelheid standaardiseren
                number = 1;
                OG = 0;
                if (place == 0) {
                    if (size != 1) {
                        last = new Combination(1, size, last);
                    }
                    place--;
                }

            } else {

                //Als het een getal zal zijn
                if (next.equals("|")) {
                    if (place != 0) {
                        next = "" + (elements.charAt(place - 1));
                    }
                    place--;
                    if (place != 0) {
                        //alle getallen opslaan tot einde
                        while (place != 0 && !("" + elements.charAt(place - 1)).equals("|")) {
                            next = (elements.charAt(place - 1)) + next;
                            place--;
                        }
                    }
                    saveNumber = Integer.parseInt(next);
                    next = "";
                    if (place != 0) {
                        place--;
                    }

                } else if (next.equals("_")) {
                    number = saveNumber;//beneden
                    saveNumber = 0;
                    next = "";
                } else if (next.equals("^")) {
                    OG = saveNumber;//boven
                    saveNumber = 0;
                    next = "";
                } else if (next.equals(")")) {
                    sizes.add(size);
                    size = 0;
                    quantities.add(number);
                    number = 1;
                    oxidaties.add(OG);
                    OG = 0;
                    next = "";
                } else if (next.equals("(")) {

                    number = quantities.getLast();

                    quantities.removeLast();
                    OG = oxidaties.getLast();
                    oxidaties.removeLast();
                    if (size != 1) {
                        last = new Combination(number, size, last, OG);
                        size = 1;

                    }

                    size += sizes.getLast();
                    sizes.removeLast();
                    number = 1;
                    OG = 0;
                    next = "";

                }
                if (place == 0) {
                    try {
                        last = new Combination(Integer.parseInt(next), size, last);
                        next = "";
                    } catch (Exception e) {
                        last = new Combination(1, size, last);
                        next = "";
                    }
                    place--;

                }

                if (place > 0) {
                    next = (elements.charAt(place - 1)) + next;
                }
                place--;
            }

        }
        return last;
    }

    private static Element isElement(String element) {
        Element correct = null;
        try {
            correct = Element.valueOf(element);
        } catch (Exception e) {
        } finally {
            return correct;
        }
    }
}
