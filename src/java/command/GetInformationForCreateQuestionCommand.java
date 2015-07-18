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
public class GetInformationForCreateQuestionCommand implements Command {

    private FacadeInterface facade;
    private final static int level=1;
    
    public GetInformationForCreateQuestionCommand(FacadeInterface facade) {
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
            Logger.getLogger(GetInformationForCreateQuestionCommand.class.getName()).log(Level.SEVERE, null, ex);
            destination = "errorPage.jsp";
        }
        return destination;
    }
    
     protected String createResult(HttpServletRequest request, HttpServletResponse response) {
        Long id = Long.parseLong(request.getParameter("moduleId"));
        return toXml(facade, id);
    }
    
    protected String toXml(FacadeInterface facade, long id) {
        StringBuffer xmlDoc = new StringBuffer();
        xmlDoc.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        xmlDoc.append("<strategies>\n");
            for(Strategy strategy : facade.getAllStrategies()) {
                xmlDoc.append("<strategy>\n");
                xmlDoc.append("<id>");
                xmlDoc.append(strategy.getId());
                xmlDoc.append("</id>\n");
                xmlDoc.append("<name>");
                xmlDoc.append(strategy.getName());
                xmlDoc.append("</name>\n");
                xmlDoc.append("</strategy>\n");
            }
            Module mod = facade.getModule(id);   
                xmlDoc.append("<module>\n");
                xmlDoc.append("<id>");
                xmlDoc.append(mod.getId());
                xmlDoc.append("</id>\n");
                xmlDoc.append("<chapter>");
                xmlDoc.append(mod.getChapter());
                xmlDoc.append("</chapter>\n");
                xmlDoc.append("<name>");
                xmlDoc.append(mod.getName());
                xmlDoc.append("</name>\n");
                xmlDoc.append("<startyear>");
                xmlDoc.append(mod.getStart().get(Calendar.YEAR));
                xmlDoc.append("</startyear>\n");
                xmlDoc.append("<startmonth>");
                xmlDoc.append(mod.getStart().get(Calendar.MONTH));
                xmlDoc.append("</startmonth>\n");
                xmlDoc.append("<startday>");
                xmlDoc.append(mod.getStart().get(Calendar.DAY_OF_MONTH));
                xmlDoc.append("</startday>\n");
               xmlDoc.append("<endyear>");
                xmlDoc.append(mod.getDeadline().get(Calendar.YEAR));
                xmlDoc.append("</endyear>\n");
                xmlDoc.append("<endmonth>");
                xmlDoc.append(mod.getDeadline().get(Calendar.MONTH));
                xmlDoc.append("</endmonth>\n");
                xmlDoc.append("<endday>");
                xmlDoc.append(mod.getDeadline().get(Calendar.DAY_OF_MONTH));
                xmlDoc.append("</endday>\n");
                xmlDoc.append("</module>\n");
        xmlDoc.append("</strategies>\n");
        return xmlDoc.toString();           
    }
    public int getLevel(){
        return level;
    }
}

