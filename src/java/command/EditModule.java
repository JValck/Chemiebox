package command;

import domain.Module;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import service.FacadeInterface;

/**
 *
 * @author r0430844
 */
public class EditModule implements Command {

    private FacadeInterface facade;
    private final static int level=1;

    public EditModule(FacadeInterface facade) {
        this.facade = facade;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String modId = request.getParameter("moduleId");
        if (modId != null) {//parameter exists
            long id = Long.parseLong(modId);
            if (request.getMethod().equalsIgnoreCase("GET")) {
                return showModuleToEdit(request, response, id);
            } else if (request.getMethod().equalsIgnoreCase("POST")) {
                return submitEditedModule(request, response, id);
            }
        }
        return "errorPage.jsp";
    }

    private String showModuleToEdit(HttpServletRequest request, HttpServletResponse response, long id) {
        Module module = facade.getModule(id);
        request.setAttribute("module", module);
        return "editModule.jsp";
    }

    private String submitEditedModule(HttpServletRequest request, HttpServletResponse response, long id) {
        //get module
        Module module = facade.getModule(id);
        module.setChapter(request.getParameter("chapter"));
        module.setName(request.getParameter("name"));
        module.setInstructions(request.getParameter("instructions"));
        module.setMaxTries(Integer.parseInt(request.getParameter("max")));
        Calendar start = Calendar.getInstance();
        start.set(Integer.parseInt(request.getParameter("startyear")), Integer.parseInt(request.getParameter("startmonth")), Integer.parseInt(request.getParameter("startday")));
        module.setStart(start);
        Calendar end = Calendar.getInstance();
        end.set(Integer.parseInt(request.getParameter("endyear")), Integer.parseInt(request.getParameter("endmonth")), Integer.parseInt(request.getParameter("endday")));
        module.setDeadline(end);
        if (end.before(start)) {
            request.setAttribute("errorMessage", "De deadline dient na de start datum te zijn.");
            request.setAttribute("module", module);
            return "editModule.jsp";
        } else {
            //update
            facade.updateModule(module);
        }
        return "ChemieboxController?action=getModuleManagement";
    }
    public int getLevel(){
        return level;
    }
}
