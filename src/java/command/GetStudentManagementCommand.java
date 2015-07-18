/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import service.FacadeInterface;

/**
 *
 * @author r0430844
 */
public class GetStudentManagementCommand implements Command{
    private FacadeInterface facade;
    private final static int level=1;
    
    public GetStudentManagementCommand(FacadeInterface facade){
        this.facade = facade;
    }
    
    
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("students", facade.getStudentQuestionResults());
        return "studentQuestionOverview.jsp";
    }
    public int getLevel(){
        return level;
    }
    
}
