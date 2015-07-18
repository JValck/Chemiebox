package domain;

import converter.DatabaseToHTMLConverter;
/**
 *
 * @author r0261853
 */
public class Solution {
    //private int id;
    private String textSolution;
    private int id;
    private int nextSolution;
    
    public Solution(){
        
    }
    
    public Solution(String textSolution,int id, int nextSolution){
        this.textSolution =textSolution;
        this.id=id;
        this.nextSolution=nextSolution;
    }
    
     public Solution(String textSolution, int nextSolution){
        this.textSolution =textSolution;
        this.nextSolution=nextSolution;
    }
    
     
    public int getId(){
        return id;
    }
    
    public void setId(int id){
        this.id=id;
    }
    
    public int getNextId(){
        return nextSolution;
    }
    
    public void setNextId(int id){
        this.nextSolution=id;
    }
    
    public String getText(){
        return DatabaseToHTMLConverter.convertToHTML(textSolution);
    }
    
    public void setText(String text){
        textSolution=text;
    }
    
    public String getNativeSolutionText(){
        return this.textSolution;
    }
}
