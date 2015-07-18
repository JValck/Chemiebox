/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import domain.Question;
import domain.Solution;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import service.FacadeInterface;

/**
 * Makes all the needed info for the response
 * @author r0430844
 */
public class DragAndDropHelper implements ArrowChangeable{
    private Question question;
    private List<String> items;
    private DragAndDropWrapper wrapper;
    private FacadeInterface facade;
    
    public DragAndDropHelper(Question question, FacadeInterface facade) {
        this.question = question;
        this.facade = facade;
        createDefaultItems();
        createItemsFromDatabase();        
    }

    private void createDefaultItems() {
        items = new ArrayList<String>() {
            {
                add("1");
                add("2");
                add("3");
                add("4");
                add("5");
                add("6");
                add("7");
                add("8");
                add("9");
                add("10");
                add("11");
                add("12");
                add("13");
                add("14");
                add("15");
                add("e<sup>-</sup>");
                add("H<sup>+</sup>");
                add("H<sub>2</sub>O");
                add("OH<sup>-</sup>");
                add("(leeg)");
            }
        };
    }

    private void createItemsFromDatabase() {
      List<Solution> nativeSolution = facade.getSolutionByQuestionId(question.getId());
        wrapper = new DragAndDropWrapper(nativeSolution.get(0).getNativeSolutionText());
        addLeftSide(wrapper.getLeftSide());
        addRightSide(wrapper.getRightSide());
        shuffle(items);
    }
    
    private void shuffle(List<String> items) {
        long seed = System.nanoTime();
        Collections.shuffle(items, new Random(seed));
    }

    /**
     * 
     * @return A list of items for the page
     */
    public List<String> getItems() {
        return new ArrayList<String>(new HashSet<String>(items));
    }

    public DragAndDropWrapper getWrapper() {
        return wrapper;
    }

    private void addLeftSide(List<String> leftSide) {
        for(String element: leftSide){
            items.add(element);
        }
    }      

    private void addRightSide(List<String> rightSide) {
        for(String element: rightSide){
            items.add(element);
        }
    }

    @Override
    public boolean isEvenwichtsReactie() {
        return wrapper.isEvenwichtsReactie();
    }
    
}
