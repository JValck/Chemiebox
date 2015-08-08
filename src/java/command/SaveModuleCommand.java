package command;

import domain.Module;
import domain.Question;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import service.FacadeInterface;

/**
 *
 * @author Fery
 */
public class SaveModuleCommand implements Command {

    private FacadeInterface facade;
    private final static int level=1;

    public SaveModuleCommand(FacadeInterface facade) {
        this.facade = facade;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String destination = null;
        try {
            int startyear = Integer.parseInt(request.getParameter("startyear"));
            int startmonth = Integer.parseInt(request.getParameter("startmonth"));
            int startday = Integer.parseInt(request.getParameter("startday"));
            int endyear = Integer.parseInt(request.getParameter("endyear"));
            int endmonth = Integer.parseInt(request.getParameter("endmonth"));
            int endday = Integer.parseInt(request.getParameter("endday"));
            int max = Integer.parseInt(request.getParameter("max"));
            String chapter = request.getParameter("chapter");
            String name = request.getParameter("name");
            String instructions = request.getParameter("instructions");
            if (chapter != null && !chapter.equals("") && name != null && !name.equals("")) { //extra check
                Calendar startcalendar = Calendar.getInstance();
                startcalendar.set(Calendar.YEAR, startyear);
                startcalendar.set(Calendar.MONTH, startmonth);
                startcalendar.set(Calendar.DAY_OF_MONTH, startday);
                Calendar endcalendar = Calendar.getInstance();
                endcalendar.set(Calendar.YEAR, endyear);
                endcalendar.set(Calendar.MONTH, endmonth);
                endcalendar.set(Calendar.DAY_OF_MONTH, endday);
                Module module = new Module(name, chapter, max, endcalendar, startcalendar, instructions);
                facade.addModule(module);
                List<Module> modules = facade.getAllModules();
                request.setAttribute("modules", modules);
                destination = "questionManagement.jsp";
            }
        } catch (Exception e) {
            destination = "errorPage.jsp";
        }
        return destination;
    }
    public int getLevel(){
        return level;
    }

}
