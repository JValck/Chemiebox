package domain;

public class Combination extends LinkedElement {

    private int size;

    public Combination(int frequency, int size, LinkedElement next) {
        super(Element.COMBINATION, frequency, next);
        this.size = size;

    }

    public Combination(int frequency, int size, LinkedElement next, int OG) {
        super(Element.COMBINATION, frequency, next, OG);
        this.size = size;
    }

    public String toString() {
        LinkedElement next = this;
        String answer = "(";
        int amount = size;
        while (amount > 0 && next.getNext() != null) {
            answer += next.getNext();

            next = next.getNext();
            amount--;
        }
        answer = answer + ")";
        if (getFrequency() > 1) {

            answer += "" + subscript("" + getFrequency());
        }
        if (getOG() != 0) {
            String og = "";
            if (getOG() > 0) {
                og = "" + getOG() + "+";
            } else {
                og = "" + Math.abs(getOG()) + "-";
            }

            answer += superscript(og);
        }

        return answer;
    }

    public String toStringFirst() {//getal of element ervoor
        LinkedElement next = this;
        String answer = "";
        if (getNext() != null) {
            answer += getNext();

            next = next.getNext();

            int amount = size;
            while (amount > 0 && next.nextElement() != null) {
                answer += next.nextElement();

                next = next.nextElement();
                amount--;
            }
            if (getFrequency() > 1) {
                answer = "" + getFrequency() + answer;
            }

        }
        return answer;
    }

    public LinkedElement nextElement() {
        LinkedElement next = this.getNext();
        int amount = size;
        //System.out.println("size:"+size);

        while (amount > 0) {
            if (next == null) {

                amount = 0;
                next = null;
            } else {
                //System.out.println("next:"+next);
                next = next.nextElement();
                amount--;
            }

        }

        return next;
    }

}
