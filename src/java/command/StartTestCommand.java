/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import service.*;

/**
 *
 * @author r0431118
 */
public class StartTestCommand implements Command{
    
    FacadeInterface facade;
    private final static int level=0;
    HttpSession session;
    
    public StartTestCommand(FacadeInterface facade) {
        this.facade = facade;
    }
    
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response){
        request.setAttribute("modules", facade.getAllModules());
        return "overview.jsp";
    }
    public int getLevel(){
        return level;
    }
}
