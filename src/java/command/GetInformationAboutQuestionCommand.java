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
public class GetInformationAboutQuestionCommand implements Command {

    private FacadeInterface facade;
    private final static int level=1;
    
    public GetInformationAboutQuestionCommand(FacadeInterface facade) {
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
        Long questionId = Long.parseLong(request.getParameter("questionId"));
        Question question = facade.getQuestion(questionId);
        List<Solution> solution = facade.getSolution(question.getSolutionId());
        return toXml(facade, question, solution);
    }
    
    protected String toXml(FacadeInterface facade, Question question,  List<Solution> solution) {
        StringBuffer xmlDoc = new StringBuffer();
        xmlDoc.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
         xmlDoc.append("<question>\n");
                xmlDoc.append("<questiontext>");
                xmlDoc.append(question.getQuestionText());
                xmlDoc.append("</questiontext>\n");
                xmlDoc.append("<maxpoints>");
                xmlDoc.append(question.getMaxPoints());
                xmlDoc.append("</maxpoints>\n");
                xmlDoc.append("<solution>");
                String solutionText = "";
                for(Solution sol : solution){
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
                 xmlDoc.append("<solutionmarge>");
                xmlDoc.append(question.getMarge());
                xmlDoc.append("</solutionmarge>\n");
                
                Module mod = facade.getModule(question.getModuleId());   
                xmlDoc.append("<module>\n");
                xmlDoc.append("<modid>");
                xmlDoc.append(mod.getId());
                xmlDoc.append("</modid>\n");
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
                xmlDoc.append("</question>\n");
        return xmlDoc.toString();           
    }
    public int getLevel(){
        return level;
    }
    
}

