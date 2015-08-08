package command;

import domain.Question;
import domain.Solution;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import service.FacadeInterface;

/**
 *
 * @author Fery
 */
public class UpdateQuestionCommand implements Command {

    private FacadeInterface facade;
    private final static int level = 1;

    public UpdateQuestionCommand(FacadeInterface facade) {
        this.facade = facade;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String destination = null;
        try {
            int maxpoints = Integer.parseInt(request.getParameter("maxpoints"));
            long questionid = Long.parseLong(request.getParameter("questionId"));
            String questiontext = request.getParameter("questiontext");
            String feedback = request.getParameter("feedback");
            Double marge = Double.parseDouble(request.getParameter("marge"));
            String solutiontext = request.getParameter("solution");
            int solutionId = 0;
            Question question = facade.getQuestion(questionid);
            long moduleId = question.getModuleId();
            long strategyId = question.getStrategyId();
            if (strategyId == facade.getStrategy(strategyId).getId()) {
                solutiontext = solutiontext.replaceAll("#-&gt;#", "#->#");
                solutiontext = solutiontext.replaceAll("#&lt;=&gt;#", "#<=>#");
                solutiontext = solutiontext.replaceAll("#&amp;#", "#&#");
                solutiontext = solutiontext.replaceAll(",", ".");
            }
            if (questiontext != null && !questiontext.equals("") && solutiontext != null && !solutiontext.equals("") && moduleId > 0 && strategyId > 0 && maxpoints > 0) { //extra check
                Solution solution = new Solution(solutiontext, 0);
                facade.addSolution(solution);
                Solution solutionWithThisText = facade.getSolutionByText(solution.getNativeSolutionText());
                solutionId = solutionWithThisText.getId();
                question.setFeedback(feedback);
                question.setMarge(marge);
                question.setMaxPoints(maxpoints);
                question.setQuestionText(questiontext);
                question.setSolutionId(solutionId);
                facade.updateQuestion(question);
                response.getWriter().flush();
                response.getWriter().close();
                destination = "";
            }
        } catch (Exception e) {
            destination = "errorPage.jsp";
        }
        return destination;
    }

    public int getLevel() {
        return level;
    }

}
