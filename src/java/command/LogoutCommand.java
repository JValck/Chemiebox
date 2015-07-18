/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import service.*;

/**
 *
 * @author r0431118
 */
public class LogoutCommand implements Command{
    
    FacadeInterface facade;
    private final static int level=0;
    public LogoutCommand(FacadeInterface facade) {
        this.facade = facade;
    }
    
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        return "index.jsp";
    }
    public int getLevel(){
        return level;
    }
}
