/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package command;

import command.Command;
import domain.Module;
import domain.Question;
import domain.Solution;
import domain.Strategy;
import domain.StudentQuestionResult;
import java.io.IOException;
import java.util.Calendar;
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
public class GetFilteredExcelCommand implements Command {

    FacadeInterface facade;
    private final static int level=1;
    public GetFilteredExcelCommand(FacadeInterface facade) {
        this.facade = facade;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String destination = null;
        try {
            String result = createResult(request, response);
            response.setContentType("text/xml");
            response.getWriter().write(result);
            response.getWriter().flush();
            response.getWriter().close();
            destination = "";
            
        } catch (IOException ex) {
            Logger.getLogger(GetInformationAboutQuestionCommand.class.getName()).log(Level.SEVERE, null, ex);
            destination = "errorPage.jsp";
        }
        return destination;
    }
    
     protected String createResult(HttpServletRequest request, HttpServletResponse response) {
        return toXml(facade);
    }
    
    protected String toXml(FacadeInterface facade) {
        List<StudentQuestionResult> filteredStudents = facade.getFilteredResults();
        StringBuffer xmlDoc = new StringBuffer();
        xmlDoc.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        xmlDoc.append("<table>\n");
            for(StudentQuestionResult result : filteredStudents) {
                
                xmlDoc.append("<tr>\n");
                xmlDoc.append("<studnr>");
                xmlDoc.append(result.getStudentNuber());
                xmlDoc.append("</studnr>\n");
                xmlDoc.append("<module>");
                xmlDoc.append(result.getModule());
                xmlDoc.append("</module>\n");
                xmlDoc.append("<points>");
                xmlDoc.append(result.getPoints());
                xmlDoc.append("</points>\n");
                xmlDoc.append("<maxpoints>");
                xmlDoc.append(result.getMaxPoints());
                xmlDoc.append("</maxpoints>\n");
                xmlDoc.append("</tr>\n");
            }
        xmlDoc.append("</table>\n");
        return xmlDoc.toString();
    }
    public int getLevel(){
        return level;
    }  
    
}
