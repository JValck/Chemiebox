/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import service.*;

/**
 *
 * @author r0431118
 */
public class HomeCommand implements Command{
    private final static int level=0;
    FacadeInterface facade;
    public HomeCommand(FacadeInterface facade) {
        this.facade = facade;
    }
    
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response){
        return "index.jsp";
    }
    
    public int getLevel(){
        return level;
    }
}
