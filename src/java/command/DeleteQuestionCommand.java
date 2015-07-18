package command;

import domain.Module;
import domain.Question;
import java.io.PrintWriter;
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
public class DeleteQuestionCommand implements Command{
    private FacadeInterface facade;
    private final static int level=1;
    
    public DeleteQuestionCommand(FacadeInterface facade){
        this.facade = facade;
    }    
    
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String destination = null;
        long id = Long.parseLong(request.getParameter("questionid"));
        try{
            Question questionToBeDeleted = facade.getQuestion(id);
            facade.deleteQuestion(questionToBeDeleted);
            response.getWriter().flush();
            response.getWriter().close();
            destination = "";
        }
        catch (Exception ex){
            destination = "errorPage.jsp";
        }
        return destination;
    }
    public int getLevel(){
        return level;
    }
}
