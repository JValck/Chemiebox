/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package command;

import domain.Module;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import service.FacadeInterface;

/**
 *
 * @author r0430844
 */
public class GetModuleManagement implements Command{
    private FacadeInterface facade;
    private final static int level=1;
    
    public GetModuleManagement(FacadeInterface facade){
        this.facade = facade;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try{
            List<Module> modules = facade.getAllModules();//fetch all modules
            request.setAttribute("modules", modules);
            return "moduleManagement.jsp";
        }catch(Exception ex){
            Logger.getLogger(GetQuestionsManagement.class.getName()).log(Level.SEVERE, null, ex);
            return "errorPage.jsp";
        }
    }
    public int getLevel(){
        return level;
    }
}
