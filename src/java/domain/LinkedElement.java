package domain;

public class LinkedElement {

    private Element name;
    private int frequency;
    private int OG = 0;
    private LinkedElement next = null;

    public LinkedElement(Element name, int frequency) {
        this.name = name;
        this.frequency = frequency;
    }

    public LinkedElement(Element name, int frequency, int OG) {
        this.name = name;
        this.frequency = frequency;
        this.OG = OG;
    }

    public LinkedElement(Element name, int frequency, LinkedElement next) {
        this.name = name;
        this.frequency = frequency;
        this.next = next;
    }

    public LinkedElement(Element name, int frequency, LinkedElement next, int OG) {
        this.name = name;
        this.frequency = frequency;
        this.next = next;
        this.OG = OG;
    }

    public String toString() {
        String answer = name.toString();
        if (frequency > 1) {
            String times = "" + frequency;

            answer += subscript(times);

        }
        if (OG != 0) {
            String og = "";
            if (OG > 0) {
                og = "" + OG + "+";
            } else {
                og = "" + Math.abs(OG) + "-";
            }

            answer += superscript(og);
        }
        return answer;

    }

    public String subscript(String number) {
        /*number = number.replaceAll("0", "₀");
         //number = number.replaceAll("1", "₁");
         number = number.replaceAll("1", "");
         number = number.replaceAll("2", "₂");
         number = number.replaceAll("3", "₃");
         number = number.replaceAll("4", "₄");
         number = number.replaceAll("5", "₅");
         number = number.replaceAll("6", "₆");
         number = number.replaceAll("7", "₇");
         number = number.replaceAll("8", "₈");
         number = number.replaceAll("9", "₉");*/

        number = number.replaceAll("0", "<sub>0</sub>");
        number = number.replaceAll("1", "");
        number = number.replaceAll("2", "<sub>2</sub>");
        number = number.replaceAll("3", "<sub>3</sub>");
        number = number.replaceAll("4", "<sub>4</sub>");
        number = number.replaceAll("5", "<sub>5</sub>");
        number = number.replaceAll("6", "<sub>6</sub>");
        number = number.replaceAll("7", "<sub>7</sub>");
        number = number.replaceAll("8", "<sub>8</sub>");
        number = number.replaceAll("9", "<sub>9</sub>");

        return number;
    }

    public String superscript(String number) {
        /*number = number.replaceAll("0", "⁰");
         //number = number.replaceAll("1", "¹");
         number = number.replaceAll("1", "");
         number = number.replaceAll("2", "²");
         number = number.replaceAll("3", "³");
         number = number.replaceAll("4", "⁴");
         number = number.replaceAll("5", "⁵");
         number = number.replaceAll("6", "⁶");
         number = number.replaceAll("7", "⁷");
         number = number.replaceAll("8", "⁸");
         number = number.replaceAll("9", "⁹");
         number = number.replaceAll("\\+", "⁺");
         number = number.replaceAll("\\-", "⁻");*/

        number = number.replaceAll("0", "<sup>0</sup>");
        number = number.replaceAll("1", "");
        number = number.replaceAll("2", "<sup>2</sup>");
        number = number.replaceAll("3", "<sup>3</sup>");
        number = number.replaceAll("4", "<sup>4</sup>");
        number = number.replaceAll("5", "<sup>5</sup>");
        number = number.replaceAll("6", "<sup>6</sup>");
        number = number.replaceAll("7", "<sup>7</sup>");
        number = number.replaceAll("8", "<sup>8</sup>");
        number = number.replaceAll("9", "<sup>9</sup>");
        number = number.replaceAll("\\+", "<sup>+</sup>");
        number = number.replaceAll("\\-", "<sup>-</sup>");
        return number;
    }

    public Element getName() {
        return name;
    }

    public int getFrequency() {
        return frequency;
    }

    public LinkedElement getNext() {
        return next;
    }

    public LinkedElement nextElement() {
        return next;
    }

    public static String getEl(LinkedElement element) {
        String response;
        if (element instanceof Combination) {
            response = ((Combination) element).toStringFirst();
        } else {
            response = element.toString();
        }
        LinkedElement next = element.nextElement();
        while (next != null) {

            response += next;
            next = next.nextElement();
        }
        return response;
    }
//	public static void main(String[] args){
//		/*LinkedElement chloor=new LinkedElement(Element.Cl,1);
//		System.out.println(getEl(new LinkedElement(Element.Na,1,chloor)));
//		LinkedElement o=new LinkedElement(Element.O,4,-2);
//		LinkedElement s=new LinkedElement(Element.S,1,o,+6);
//		Combination comb = new Combination(3,2,s);
//		LinkedElement al=new LinkedElement(Element.Al,2,comb,3);
//		Combination comb2 = new Combination(6,2,al);
//		System.out.println(getEl(comb2));*/
//		System.out.println(getEl(Translator.translate("e_|5|Na")));
//	}

    public int getOG() {
        return OG;
    }

}
