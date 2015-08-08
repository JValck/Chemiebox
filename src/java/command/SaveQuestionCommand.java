package command;

import domain.Module;
import domain.Question;
import domain.Solution;
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
public class SaveQuestionCommand implements Command {

    private FacadeInterface facade;
    private final static int level=1;

    public SaveQuestionCommand(FacadeInterface facade) {
        this.facade = facade;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String destination = null;
        try {
            int maxpoints = Integer.parseInt(request.getParameter("maxpoints"));
            long strategyId = Long.parseLong(request.getParameter("strategyId"));
            long moduleId = Long.parseLong(request.getParameter("moduleId"));
            String questiontext = request.getParameter("questiontext");
            String feedback = request.getParameter("feedback");
            Double marge = Double.parseDouble(request.getParameter("marge"));
            String solutiontext = request.getParameter("solution");
            if(strategyId == facade.getStrategy(strategyId).getId()){
                solutiontext = solutiontext.replaceAll("#-&gt;#", "#->#");
                solutiontext = solutiontext.replaceAll("#&lt;=&gt;#", "#<=>#");
                solutiontext = solutiontext.replaceAll("#&amp;#", "#&#");
                solutiontext = solutiontext.replaceAll(",", ".");
            }
            int solutionId = 0;
            if (questiontext != null && !questiontext.equals("") && solutiontext != null && !solutiontext.equals("") && moduleId > 0 && strategyId > 0 && maxpoints > 0) { //extra check
                Solution solution = new Solution(solutiontext, 0);
                facade.addSolution(solution);
                Solution solutionWithThisText = facade.getSolutionByText(solution.getNativeSolutionText());
                solutionId = solutionWithThisText.getId();                
                Question question = new Question(questiontext, moduleId, strategyId, solutionId, maxpoints, feedback, marge);
                facade.addQuestion(question);
                response.getWriter().flush();
                response.getWriter().close();
                destination = "";
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
