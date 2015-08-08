package command;

import domain.Question;
import domain.Module;
import domain.Solution;
import domain.Strategy;
import helper.DragAndDropHelper;
import helper.DragAndDropWrapper;
import helper.ReactionCompleteHelper;
import helper.TableHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import service.FacadeInterface;

/**
 *
 * @author Fery
 */
public class StartModuleCommand implements Command {

    private FacadeInterface facade;
    private final static int level=0;
    private HttpSession session;

    public StartModuleCommand(FacadeInterface facade) {
        this.facade = facade;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        long module = Long.parseLong(request.getParameter("moduleId"));
        return showFirstQuestion(module, request);
    }

    private String showFirstQuestion(long moduleId, HttpServletRequest request) {
        
        String next = "questionView.jsp";
        session = request.getSession();
        String studnr = (String) (session.getAttribute("userId"));
        
        Module current = facade.getModule(moduleId);
        request.setAttribute("moduleInfo", current.getInstructions());

        if (current.getMaxTries() <= facade.getTimesTried(moduleId, studnr)) {
            next = "overview.jsp";
            request.setAttribute("modules", facade.getAllModules());
            request.setAttribute("error", "Maximale keren oplossen module "+current.getName()+" overschreden");
        } else if(current.getDeadline().getTime().before(new Date())){
            next = "overview.jsp";
            request.setAttribute("modules", facade.getAllModules());
            request.setAttribute("error", "Deadline module "+current.getName()+" verlopen");
        }
        else if(current.getStart().getTime().after(new Date())){
            next = "overview.jsp";
            request.setAttribute("modules", facade.getAllModules());
            request.setAttribute("error", "Module "+current.getName()+" nog niet beschikbaar");
        }
        
        else {
            Question question = facade.getNextQuestion(moduleId, studnr);
            if(question==null){
                next = "overview.jsp";
            request.setAttribute("modules", facade.getAllModules());
            request.setAttribute("error", "Module "+current.getName()+" heeft nog geen vragen");
            }
            else{
                request.setAttribute("moduleName", moduleId);

                request.setAttribute("moduleName", current.getChapter());
                request.setAttribute("moduleDetailed", current.getName());
                session.setAttribute("moduleId", current.getId());
                request.setAttribute("percentage", facade.getBeginProgress(moduleId, studnr));

                if (question != null) {

                request.setAttribute("qText", question.getQuestionText());

                session.setAttribute("questionId", question.getId());
                }
                    Strategy strategy = facade.getStrategy(question.getStrategyId());
            String stratName = strategy.getName();
            next = "questionView.jsp";

            if (stratName.equalsIgnoreCase("Niet-redox")) {
                ReactionCompleteHelper helper = new ReactionCompleteHelper(facade.getSolutionByQuestionId(question.getId()).get(0).getNativeSolutionText());
                request.setAttribute("itemsBefore", helper.getPartsBefore());
                request.setAttribute("itemsAfter", helper.getPartsAfter());
                request.setAttribute("isEventwichtsReactie", helper.isEvenwichtsReactie());
                next = "reactieInvulView.jsp";
            } else if (stratName.equalsIgnoreCase("Tabel")) {
                TableHelper tHelper = new TableHelper(facade.getSolutionByQuestionId(question.getId()).get(0).getNativeSolutionText());
                request.setAttribute("leftSide", tHelper.getLeftSideElements());
                request.setAttribute("rightSide", tHelper.getRightSideElements());
                request.setAttribute("totalAnswerRows", 3);
                request.setAttribute("arrowIndex", tHelper.getLeftSideElements().size()+1);
                request.setAttribute("columns", tHelper.numberOfColumns());
                request.setAttribute("isEventwichtsReactie", tHelper.isEvenwichtsReactie());
                next = "tableView.jsp";
            } else if (stratName.equalsIgnoreCase("Drag n Drop")) {
                DragAndDropHelper helper = new DragAndDropHelper(question, facade);                
                request.setAttribute("items", helper.getItems());
                request.setAttribute("parts", helper.getWrapper().getMaxItems());
                request.setAttribute("isEventwichtsReactie", helper.isEvenwichtsReactie());
                next = "dragendrop.jsp";
            } else if (stratName.equalsIgnoreCase("Meerkeuze")) {
                List<String> options = generateOptionsList(question);
                request.setAttribute("options", options);
                next = "multipleChoiceView.jsp";
            }
            }
            
            
        }

        return next;
    }

    private List<String> generateOptionsList(Question question) {
        List<Solution> sols = facade.getSolutionByQuestionId(question.getId());
        List<String> options = new ArrayList<String>();
        for (Solution s : sols) {
            String solution = s.getNativeSolutionText();
            String[] parts = solution.split("##");
            for (String part : parts) {
                if (!part.isEmpty()) {                    
                    options.add(part);
                }
            }
        }
        shuffle(options);
        return options;
    }

    private void shuffle(List<String> items) {
        long seed = System.nanoTime();
        Collections.shuffle(items, new Random(seed));
    }
    public int getLevel(){
        return level;
    }
}
