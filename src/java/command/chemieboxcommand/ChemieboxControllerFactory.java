/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package command.chemieboxcommand;
import command.UpdateQuestionCommand;
import command.GetInformationAboutQuestionCommand;
import java.util.HashMap;
import java.util.Map;
import service.*;
import command.*;
/**
 *
 * @author r0431118
 */
public class ChemieboxControllerFactory {
    private Map<String, Command> commands = new HashMap();
    
    public ChemieboxControllerFactory(FacadeInterface facade){
        commands.put("getHomePage", new HomeCommand(facade));
        commands.put("login", new LoginCommand(facade));
        commands.put("logout", new LogoutCommand(facade));
        commands.put("startModule", new StartModuleCommand(facade));
        commands.put("getQuestionsManagement", new GetQuestionsManagement(facade));
        commands.put("saveModule", new SaveModuleCommand(facade));
        commands.put("next", new QuestionCommand(facade));
        commands.put("startTest", new StartTestCommand(facade));
        commands.put("getAllQuestionsForModule", new GetAllQuestionsForModule(facade));
        commands.put("getModuleManagement", new GetModuleManagement(facade));
        commands.put("editModule", new EditModule(facade));
        commands.put("deleteModule", new DeleteModule(facade));
        commands.put("deleteQuestion", new DeleteQuestionCommand(facade));
        commands.put("getInformationForCreateQuestion", new GetInformationForCreateQuestionCommand(facade));
        commands.put("getInformationAboutQuestion", new GetInformationAboutQuestionCommand(facade));
        commands.put("saveQuestion", new SaveQuestionCommand(facade));
        commands.put("saveUpdatedQuestion", new UpdateQuestionCommand(facade));
        commands.put("getStudentManagement", new GetStudentManagementCommand(facade));
        commands.put("getFilteredExcel", new GetFilteredExcelCommand(facade));
        commands.put("changeAvailabilityOfModule", new ModuleAvailabilityChanger(facade));
    }
    
    public Command getController(String key) {	
        Command answer = commands.get(key);
        if(answer==null){
                answer=commands.get("startTest");
                }
    return answer;	
}
}
