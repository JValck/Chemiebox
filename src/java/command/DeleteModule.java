package command;

import domain.Module;
import domain.Question;
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
public class DeleteModule implements Command{
    private FacadeInterface facade;
    private final static int level=1;
    
    public DeleteModule(FacadeInterface facade){
        this.facade = facade;
    }    
    
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String modId = request.getParameter("moduleId");
        if(modId != null){//parameter exists
            long id = Long.parseLong(modId);        
            if(request.getMethod().equalsIgnoreCase("GET")){
                return showModuleToDelete(request, response, id);
            }else if(request.getMethod().equalsIgnoreCase("POST")){
                return submitDeleteModule(request, response, id);
            }    
        }
        return "errorPage.jsp";
    }

    private String showModuleToDelete(HttpServletRequest request, HttpServletResponse response, long id) {
        List<Question> questions = facade.getAllQuestionsForModule(id);
        Module module = facade.getModule(id);
        request.setAttribute("numberOfQuestions", questions.size());
        request.setAttribute("id", id);
        request.setAttribute("name", module.getName());
        request.setAttribute("chapter", module.getChapter());
        return "deleteModule.jsp";
    }

    private String submitDeleteModule(HttpServletRequest request, HttpServletResponse response, long id) {
        facade.deleteModule(facade.getModule(id));
        return "ChemieboxController?action=getModuleManagement";
    }
    public int getLevel(){
        return level;
    }
}
