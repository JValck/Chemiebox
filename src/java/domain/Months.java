/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package domain;

//import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Fery
 */
public class Months {
    
    private List <String> months;
    public Months(){
        months = new ArrayList<String>();
        initialise();
    }
    
    private void initialise() {
        months.add("Januari");
        months.add("Februari");
        months.add("Maart");
        months.add("April");
        months.add("Mei");
        months.add("Juni");
        months.add("Juli");
        months.add("Augustus");
        months.add("September");
        months.add("Oktober");
        months.add("November");
        months.add("December");
    }
    
    public List<String> getMonths(){
        return months;
    }
        
}
