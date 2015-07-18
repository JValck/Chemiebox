/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package command;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import service.*;

/**
 *
 * @author r0431118
 */
public class LoginCommand implements Command{
    
    FacadeInterface facade;
    private final static int level=0;
    HttpSession session;
    
    public LoginCommand(FacadeInterface facade) {
        this.facade = facade;
    }
    
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response){
        String destination = null;
        String studnr = request.getParameter("inputStudnr");
        String password = request.getParameter("inputPassword");
        if(studnr != null && password != null && !studnr.equals("") && !password.equals("")){
            if(facade.authenticate(studnr, password)){                
                destination = validLogin(studnr, request, response);
            }
            else{
                destination = "index.jsp";
                request.setAttribute("inputStudnr", studnr);
                request.setAttribute("errorMessage", "Studentennummer en/of wachtwoord zijn onjuist.");
            }
        }
        else {
            destination = "index.jsp";
            request.setAttribute("errorMessage", true);
            request.setAttribute("errorMessage", "Gelieve je studentennummer en wachtwoord in te vullen.");
        }
        return destination;
    }

    private String validLogin(String studnr, HttpServletRequest request, HttpServletResponse response) {
        String destination;
        request.setAttribute("modules", facade.getAllModules());
        destination = "overview.jsp";
        //Set Session
        session = request.getSession();
        session.setAttribute("userId", studnr);
        if((""+studnr.charAt(0)).equals("u")||studnr.equals("r0431118")||studnr.equals("r0430844") || studnr.equals("r0261853")){ 
            session.setAttribute("level", 1);
        }
        return destination;
    }
    public int getLevel(){
        return level;
    }
}
