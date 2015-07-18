package command;

import domain.Module;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import service.FacadeInterface;

/**
 *
 * @author jasper
 */
public class ModuleAvailabilityChanger implements Command {

    private final FacadeInterface facade;
    
    public ModuleAvailabilityChanger(FacadeInterface facade) {
        this.facade = facade;
    }
    
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("moduleId");
        try {
            long parsedId = Long.parseLong(id);
            Module module = facade.getModule(parsedId);
            if (module.isAvailable()) {//make unavailable
                module.setDeadline(module.getStart());                
            } else {
                String deadline = request.getParameter("deadline");
                String[] parts = deadline.split("-");
                Calendar newDeadline = Calendar.getInstance();
                newDeadline.set(Integer.parseInt(parts[2]), Integer.parseInt(parts[1])-1, Integer.parseInt(parts[0]));
                module.setDeadline(newDeadline);
            }
            facade.updateModule(module);
            return "";
        } catch (NumberFormatException e) {
            return "errorPage.jsp";
        }
    }
    
    @Override
    public int getLevel() {
        return 1;
    }
    
}
