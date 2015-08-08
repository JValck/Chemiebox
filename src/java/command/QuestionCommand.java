package command;

import domain.StudentFeedback;
import domain.Question;
import domain.Strategy;
import domain.StudentQuestion;
import domain.Solution;
import helper.DragAndDropHelper;
import solveStrategies.SolverFactory;
import solveStrategies.SolveStrategy;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.novell.ldap.util.Base64;

import service.FacadeInterface;
import converter.DatabaseToHTMLConverter;
import helper.ArrowChangeable;
import helper.ReactionCompleteHelper;
import helper.TableHelper;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class QuestionCommand implements Command {

	private FacadeInterface facade;
	private final static int level=0;
	private HttpSession session;

	public QuestionCommand(FacadeInterface facade) {
		this.facade = facade;
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		session = request.getSession();
		long moduleId = (long) (session.getAttribute("moduleId"));
		String studnr = (String) (session.getAttribute("userId"));
		String answer = (String) (request.getParameter("answer")).replaceAll(",$", "");//als js array laatste leeg
		Question question = facade.getQuestion(Long.parseLong("" + session.getAttribute("questionId")));       
		int score = (int) getPoints(question, answer);        
		StudentQuestion studQuestion = facade.getStudentQuestion(Long.parseLong("" + session.getAttribute("questionId")), studnr);
		try {
			if (studQuestion != null) {
				studQuestion.setLastAnswer(answer);
				studQuestion.setScore(score);
				studQuestion.setTries(studQuestion.getTries() + 1);
				facade.updateStudentQuestion(studQuestion);
			} else {
				studQuestion = new StudentQuestion(Long.parseLong("" + session.getAttribute("questionId")), studnr, score, 1, answer);
				facade.addStudentQuestion(studQuestion);
			}

		} catch (SQLException ex) {
			Logger.getLogger(QuestionCommand.class.getName()).log(Level.SEVERE, null, ex);
		}

		return showNextQuestion(moduleId, studnr, request, response);
	}

	private String showNextQuestion(long moduleId, String studnr, HttpServletRequest request, HttpServletResponse response) {

		Question question = facade.getNextQuestion(moduleId, studnr);
		if (question != null) {
			session = request.getSession();
			session.setAttribute("questionId", question.getId());
		}
		Strategy strategy = facade.getStrategy(question.getStrategyId());
		String stratName = strategy.getName();
		response.setContentType("text/xml");
		String xml = toXml(moduleId, studnr, question.getQuestionText(), stratName);
		if (stratName.equalsIgnoreCase("Niet-redox")) {
			xml = makeCompleteReaction(question, xml);
		} else if (stratName.equalsIgnoreCase("Tabel")) {
			xml = makeTableXml(question, xml);
		} else if (stratName.equalsIgnoreCase("Drag n Drop")) {
			xml = makeDragAndDropData(question, xml);
		} else if (stratName.equalsIgnoreCase("Meerkeuze")) {
			xml = makeMultipleData(generateOptionsList(question), xml);
		}
		//check if end of module
		if (facade.getInProgress(moduleId, studnr) > 100) {
			xml = endOfModule(moduleId, studnr, xml);
		}

		try {
			xml = closeXml(xml);
			response.getWriter().write(xml);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException ex) {
			Logger.getLogger(QuestionCommand.class.getName()).log(Level.SEVERE, null, ex);
		}

		return "";
	}

	protected String toXml(long moduleId, String studnr, String questionText, String strategy) {
		StringBuffer xmlDoc = new StringBuffer();
		xmlDoc.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		xmlDoc.append("<question>\n");//open root tag
		xmlDoc.append("<progress>").append(facade.getInProgress(moduleId, studnr)).append("</progress>\n");
		xmlDoc.append("<questionText>").append(questionText).append("</questionText>\n");
		xmlDoc.append("<strategy>").append(strategy).append("</strategy>\n");
		//send instructions again, as it will be flushed by each new question
		xmlDoc.append("<info>").append(
				Base64.encode(
				facade.getModule(moduleId).getInstructions())
				).append("</info>\n");
		return xmlDoc.toString();
	}

	protected String closeXml(String xml) {
		StringBuffer xmlBuffer = new StringBuffer(xml);
		xmlBuffer.append("</question>\n");
		return xmlBuffer.toString();//close root tag
	}

	private String makeDragAndDropData(Question question, String xml) {
		DragAndDropHelper helper = new DragAndDropHelper(question, facade);
		return dragAndDropToXml(xml, helper.getItems(), helper.getWrapper().getMaxItems(), helper);
	}

	private void shuffle(List<String> items) {
		long seed = System.nanoTime();
		Collections.shuffle(items, new Random(seed));
	}

	private String dragAndDropToXml(String oldXml, List<String> items, int parts, ArrowChangeable helper) {
		StringBuffer buffer = new StringBuffer(oldXml);
		buffer.append("<parts>" + parts + "</parts>\n");
		buffer.append("<items>\n");
		for (String item : items) {
			buffer.append("<item>" + item + "</item>\n");
		}
		buffer.append("</items>\n");  
		buffer.append("<isEvenwicht>"+helper.isEvenwichtsReactie()+"</isEvenwicht>\n");
		return buffer.toString();
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

	private String makeMultipleData(List<String> options, String oldXml) {
		StringBuffer buffer = new StringBuffer(oldXml);
		buffer.append("<options>\n");
		for (String option : options) {
			buffer.append("<option>" + option + "</option>\n");
		}
		buffer.append("</options>\n");

		return buffer.toString();
	}

	private String makeTableData(List<String> options, String oldXml) {
		StringBuffer buffer = new StringBuffer(oldXml);
		buffer.append("<table>\n");
		buffer.append("<colomns>\n");
		for (String option : options) {
			buffer.append("<colom>" + option + "</colom>\n");
		}
		buffer.append("</colomns>\n");
		buffer.append("<rows>\n");
		for (String option : options) {
			buffer.append("<row>" + option + "</row>\n");
		}
		buffer.append("</rows>\n");
		buffer.append("</table>\n");
		return buffer.toString();
	}

	private double getPoints(Question question, String answer) {
		double max = question.getMaxPoints();
		double scored = 0;
		String strategy = facade.getStrategy(question.getStrategyId()).getName();
		SolveStrategy solver = SolverFactory.getSolver(strategy, question);
		return solver.solve(facade.getSolutionByQuestionId(question.getId()).get(0).getNativeSolutionText(), answer, max);
	}

	private String endOfModule(long moduleId, String studnr, String xml) {
		List<StudentFeedback> feedback = facade.getStudentFeedbackForModule(moduleId, studnr);
		StringBuffer buffer = new StringBuffer(xml);
		int totalScore = 0;
		int scoreAchieved = 0;
		buffer.append("<studentFeedback>\n");
		buffer.append("<moduleFeedback>\n");
		for (StudentFeedback sf : feedback) {
			buffer.append("<answerfeedback>\n");
			buffer.append("<question>").append(sf.getQuestionText()).append("</question>\n");
			buffer.append("<studentanswer>").append(sf.getStudentAnswer()).append("</studentanswer>\n");
			buffer.append("<feedback>").append(sf.getFeedback()).append("</feedback>\n");
			buffer.append("<score>").append(sf.getScore()).append("</score>\n");
			buffer.append("<scoreForQuestion>").append(sf.getMaxScore()).append("</scoreForQuestion>\n");
			buffer.append("</answerfeedback>\n");
			scoreAchieved = scoreAchieved + sf.getScore();
			totalScore = totalScore + sf.getMaxScore();
		}
		buffer.append("</moduleFeedback>\n");
		buffer.append("<scoreAchieved>").append(scoreAchieved).append("</scoreAchieved>\n");
		buffer.append("<totalScore>").append(totalScore).append("</totalScore>\n");
		buffer.append("</studentFeedback\n>");
		return buffer.toString();
	}

	private String makeTableXml(Question question, String xml) {
		TableHelper helper = new TableHelper(facade.getSolutionByQuestionId(question.getId()).get(0).getNativeSolutionText());
		StringBuffer buffer = new StringBuffer(xml);
		buffer.append("<tableQuestion>\n"); buffer.append("<leftElements>\n");
		for(String lElement : helper.getLeftSideElements()){
			buffer.append("<leftElement>").append(lElement).append("</leftElement>\n");
		}
		buffer.append("</leftElements>\n");
		buffer.append("<rightElements>\n");
		for(String rElement : helper.getRightSideElements()){
			buffer.append("<rightElement>").append(rElement).append("</rightElement>\n");
		}
		buffer.append("</rightElements>\n");
		buffer.append("<columns>").append(helper.numberOfColumns()).append("</columns>\n");
		buffer.append("<isEvenwicht>"+helper.isEvenwichtsReactie()+"</isEvenwicht>\n");
		buffer.append("</tableQuestion>\n");
		return buffer.toString();
	}

	private String makeCompleteReaction(Question question, String xml) {
		ReactionCompleteHelper helper = new ReactionCompleteHelper(facade.getSolutionByQuestionId(question.getId()).get(0).getNativeSolutionText());
		StringBuffer buffer = new StringBuffer(xml);
		buffer.append("<reactionParts>\n");
		buffer.append("<left>"+helper.getPartsBefore()+"</left>\n");
		buffer.append("<right>"+helper.getPartsAfter()+"</right>\n");
		buffer.append("<isEvenwicht>"+helper.isEvenwichtsReactie()+"</isEvenwicht>\n");
		buffer.append("</reactionParts>\n");
		return buffer.toString();
	}

	public int getLevel(){
		return level;
	}
}
