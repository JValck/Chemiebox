/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solveStrategies;

import domain.Question;

/**
 *
 * @author r0261853
 */
public class SolverFactory {
    public static SolveStrategy getSolver(String strategy, Question question){
        SolveStrategy solver = new NormalSolve(question);
        if(strategy.equalsIgnoreCase("Drag n Drop")){
            solver=new DragDropSolve();
        }
        else if(strategy.equalsIgnoreCase("Meerkeuze")){
            solver=new MultipleSolve();
        }
        else if(strategy.equalsIgnoreCase("Niet-redox")){
            solver = new NietRedoxSolve();
        }else if(strategy.equalsIgnoreCase("Tabel")){
            solver = new TableSolver(question);
        }
        return solver;
    }
    
}
