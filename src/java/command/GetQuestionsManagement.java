/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package command;

import domain.Module;
import domain.Months;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import service.FacadeInterface;

/**
 *
 * @author Fery
 */
public class GetQuestionsManagement implements Command {

    FacadeInterface facade;
    private final static int level=1;
    public GetQuestionsManagement(FacadeInterface facade) {
        this.facade = facade;
    }
    
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response){
        String destination = null;
        try{
        List<Module> modules = facade.getAllModules();
        request.setAttribute("modules", modules);
        destination = "questionManagement.jsp";
        }
        catch(Exception ex){
        Logger.getLogger(GetQuestionsManagement.class.getName()).log(Level.SEVERE, null, ex);
        destination = "errorPage.jsp";
        }
        return destination;
    }
    public int getLevel(){
        return level;
    }
    
}
