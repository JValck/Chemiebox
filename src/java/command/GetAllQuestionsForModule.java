/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package command;

import domain.Module;
import domain.Question;
import domain.Solution;
import domain.Strategy;
import java.io.IOException;
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
public class GetAllQuestionsForModule implements Command {

    FacadeInterface facade;
    private final static int level=1;
    public GetAllQuestionsForModule(FacadeInterface facade) {
        this.facade = facade;
    }
    
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response){
        String destination = null;
        try {
            String result = createResult(request, response);
            response.setContentType("text/xml");
            response.getWriter().write(result);
            response.getWriter().flush();
            response.getWriter().close();
            destination = "";
            
        } catch (IOException ex) {
            Logger.getLogger(GetAllQuestionsForModule.class.getName()).log(Level.SEVERE, null, ex);
            destination = "errorPage.jsp";
        }
        return destination;
    }
    
     protected String createResult(HttpServletRequest request, HttpServletResponse response) {
        if(!request.getParameter("moduleId").equals("")){
        Long id = Long.parseLong(request.getParameter("moduleId")); // Anders Number.FormatException om 1 of andere reden
        return toXml(facade, id);
        }
        else{
        return "";
        }
    }
    
    protected String toXml(FacadeInterface facade, long id) {
        StringBuffer xmlDoc = new StringBuffer();
        Module module = facade.getModule(id);        
        xmlDoc.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");        
        xmlDoc.append("<questions>\n");
        xmlDoc.append("<module>\n"+"<available>").append(module.isAvailable()).append("</available>\n"+"</module>\n");
            for(Question question : facade.getAllQuestionsForModule(id)) {
                xmlDoc.append("<question>\n");
                xmlDoc.append("<questiontext>");
                xmlDoc.append(question.getQuestionText());
                xmlDoc.append("</questiontext>\n");
                xmlDoc.append("<maxpoints>");
                xmlDoc.append(question.getMaxPoints());
                xmlDoc.append("</maxpoints>\n");
                xmlDoc.append("<solution>");
                List<Solution> solutions = facade.getSolution((int) question.getSolutionId());
                String solutionText = "";
                for(Solution sol : solutions){
                solutionText = solutionText + " " + sol.getText();
                }
                xmlDoc.append(solutionText);
                xmlDoc.append("</solution>\n");
                for(Strategy strategy : facade.getAllStrategies()){
                    if(question.getStrategyId() == strategy.getId()){
                        xmlDoc.append("<strategy>");
                        xmlDoc.append(strategy.getName());
                        xmlDoc.append("</strategy>\n");
                    }
                }
                xmlDoc.append("<feedback>");
                xmlDoc.append(question.getFeedback());
                xmlDoc.append("</feedback>\n");
                 xmlDoc.append("<id>");
                xmlDoc.append(question.getId());
                xmlDoc.append("</id>\n");
                xmlDoc.append("</question>\n");
            }
        xmlDoc.append("</questions>\n");
        return xmlDoc.toString();           
    }
    public int getLevel(){
        return level;
    }
}
