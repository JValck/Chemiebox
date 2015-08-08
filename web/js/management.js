
var globalCounterForLoopthroughTable;
var globalModuleId = 0;
var formWindow = null;
var leftTableSolution = "";
var rightTableSolution = "";
var finalSolForCheck;

function getModuleForm() {
    $(".choice").load('partials/createModuleForm.jsp', function () {
        refreshEnriched();
    });
}

function saveModule() { //Validatiefunctie, pas wanneer true, wordt verder gegaan naar de servlet
    var form = document.getElementById('moduleForm');
    var chapter = form.chapter.value;
    var name = form.name.value;
    var instructions = $("#instructions").html();
    $('<input>').attr({type:"hidden", id:"instructions", name:"instructions"}).appendTo($(form));
    form.instructions.value=instructions;
    var startyear = form.startyear.value;
    var startmonth = form.startmonth.value;
    var startday = form.startday.value;
    var endyear = form.endyear.value;
    var endmonth = form.endmonth.value;
    var endday = form.endday.value;
    var startdate = new Date(startyear, startmonth, startday);
    var enddate = new Date(endyear, endmonth, endday);
    if (chapter === "" || name === "" || chapter == null || name == null || !name.match(/\S/) || !chapter.match(/\S/)) {
        createErrorMessage('errorMessage', "Gelieve alle velden correct in te vullen.");
        return false;
    }
    else if (startdate > enddate) {
        createErrorMessage('errorMessage', "De deadline dient na de start datum te zijn.");
        return false;
    }
    else {
        return true;
    }
}

function getQuestionsForModule() {
    $("#loading").empty();
    if (document.getElementById("moduleDropDown").value === null || document.getElementById("moduleDropDown").value === "") {
        getModuleForm(); // We weten dat de option-tag aageduid is waar we een nieuwe module willen creeeren, aangezien er geen moduleid aan gekoppeld is
    }
    else {
        $(".choice").load('partials/questionsForModule.jsp', function () {
            var moduleId = document.getElementById("moduleDropDown").value;
            globalModuleId = moduleId;
            $.ajax({
                url: "ChemieboxController",
                async: true,
                type: "GET",
                data: {"action": "getAllQuestionsForModule", "moduleId": "" + moduleId},
                success: function (data) {
                    return getQuestionsForModuleCallback(data);
                },
                error: function (data) {
                    console.log("error:", data);
                }
            });
            return false;
        });
        return loading(); // we tonen een laadicoontje totdat de status success is. In de callback verwijderen we vervolgens het icoontje
    }
}
function loading() { // functie die het "laden" verzorgt. Wanneer bvb wordt gewacht op data uit servlet wordt deze aangeroepen tot beschikbaar
    var divcontainer = document.createElement('div');
    divcontainer.id = 'canvasloader-container';
    var parent = document.getElementById('loading');
    parent.appendChild(divcontainer);
    var cl = new CanvasLoader('canvasloader-container');
    cl.setDiameter(52);
    cl.setDensity(39);
    cl.setRange(0.9);
    cl.setFPS(20);
    cl.show(); // Hidden by default

    var loaderObj = document.getElementById("canvasLoader");
    loaderObj.style.width = "52px";
    loaderObj.style.margin = "auto";
    loaderObj.style.paddingTop = "100px";
}

function deleteHelp(evt) {
    deleteQuestion(evt.target.myParam);
}

function deleteQuestion(questionid) {
    $.ajax({
        url: "ChemieboxController",
        async: true,
        type: "POST",
        data: {"action": "deleteQuestion", "questionid": "" + questionid},
        success: function (data) {
            return getQuestionsForModule();
        },
        error: function (data) {
            console.log("error:", data);
        }
    });
    return false;
}

function getCreateQuestionForm() {
    var moduleId = globalModuleId;
    formWindow = window.open(
            'partials/createQuestionForm.jsp', "Creëer", "scrollbars = 1, height=700,width=1200"
            );
    formWindow.focus();
    formWindow.myPara = moduleId;
    formWindow.onbeforeunload = function () {
        return getQuestionsForModule();
    };
}

function loadFormForStrategy() {
    var form = document.getElementById('questionForm');
    var strategyId = form.strategy.value;
    if ($(".helpBox").children().length >= 1) {
        $(".helpBox").remove();
    }
    if (strategyId == 1) { // = Normaal, "onveilige" check als deze bvb verwijderd wordt en opnieuw toegevoegd als nieuw id
        $(".questionFormForStrategy").load('normalQuestionForm.jsp');
    }
    else if (strategyId == 5) { // = Meerkeuze, "onveilige" check als deze bvb verwijderd wordt en opnieuw toegevoegd als nieuw id
        $(".questionFormForStrategy").load('multipleChoiceQuestionForm.jsp');
    }
    else if (strategyId == 2) { // = Reacties, "onveilige" check als deze bvb verwijderd wordt en opnieuw toegevoegd als nieuw id
        $(".questionFormForStrategy").load('reactionsForm.jsp');
    }
    else if (strategyId == 4) { // = drag N drop, "onveilige" check als deze bvb verwijderd wordt en opnieuw toegevoegd als nieuw id
        $(".questionFormForStrategy").load('dragNdropForm.jsp');
    }
    else if (strategyId == 3) { // = Tabel, "onveilige" check als deze bvb verwijderd wordt en opnieuw toegevoegd als nieuw id
        $(".questionFormForStrategy").load('tableForm.jsp');
    }
}


function generateAnotherAnswer() { // functie die bij multiple choice formulier een veldje toevoegt waar een andere mogelijkheid in komt	
    var counter = $("#multipleChoice").children().length;
    if (counter < 4) {
        var p = document.createElement('p');
        p.setAttribute("class", "enriched form-control");
        p.setAttribute("name", "solution");
        p.setAttribute("type", "text");
        p.id = "solution" + counter;
        p.contentEditable = true;
        $(".multipleChoice").append(p);
        findAllEnrichedElements();
        makeEnrichedReady();
        document.getElementById("deleteLast").style.visibility = "visible";
    }
    else {
        createErrorMessage("errorMessage2", "Je kan maximaal 4 mogelijkheden opgeven");
    }
}

function removeLastAnswer() {
    var counter = $("#multipleChoice").children().length;
    var id = counter - 1;
    document.getElementById("multipleChoice").removeChild(document.getElementById("solution" + id));
    if (counter === 2) {
        document.getElementById("deleteLast").style.visibility = "hidden";
    }
}

function getCreateQuestionFormCallback() {
    var moduleId = myPara;
    $.ajax({
        url: "../ChemieboxController", // "Ander" pad, want we zitten in partials nu
        async: true,
        type: "GET",
        data: {"action": "getInformationForCreateQuestion", "moduleId": "" + moduleId},
        success: function (data) {
            return getInformationNeeded(data);
        },
        error: function (data) {
            console.log("error:", data);
        }
    });
    return false;
}

function getInformationNeeded(data) {
    var strategies = data.getElementsByTagName('strategy');
    var modules = data.getElementsByTagName('module');
    var strategySelect = document.getElementById("strategySelect");
    for (var i = 0; i < strategies.length; i++) {
        var idOfStrategyI = strategies[i].getElementsByTagName('id')[0].textContent;
        var nameOfStrategyI = strategies[i].getElementsByTagName('name')[0].textContent;
        var option = document.createElement("OPTION");
        option.value = idOfStrategyI;
        option.id = idOfStrategyI;
        var optiontext = document.createTextNode(nameOfStrategyI);
        option.appendChild(optiontext);
        strategySelect.appendChild(option);
    }
    for (var i = 0; i < modules.length; i++) {
        var idOfModuleI = modules[i].getElementsByTagName('id')[0].textContent;
        var chapterOfModuleI = modules[i].getElementsByTagName('chapter')[0].textContent;
        var nameOfModuleI = modules[i].getElementsByTagName('name')[0].textContent;
        var startyearOfModule = modules[i].getElementsByTagName('startyear')[0].textContent;
        var startmonthOfModule = modules[i].getElementsByTagName('startmonth')[0].textContent;
        var startdayOfModule = modules[i].getElementsByTagName('startday')[0].textContent;
        var endyearOfModule = modules[i].getElementsByTagName('endyear')[0].textContent;
        var endmonthOfModule = modules[i].getElementsByTagName('endmonth')[0].textContent;
        var enddayOfModule = modules[i].getElementsByTagName('endday')[0].textContent;
        var startdate = new Date(startyearOfModule, startmonthOfModule, startdayOfModule);
        var enddate = new Date(endyearOfModule, endmonthOfModule, enddayOfModule);
        var currentDate = new Date();
        if (currentDate >= startdate && currentDate <= enddate - 1) {
            document.getElementById("questionForm").style.visibility = "hidden";
            createErrorMessage("errorMessage3", "De deadline van deze module is nog niet verlopen. Je kan bijgevolg geen vragen toevoegen.");
        }
        else {
            document.getElementById("questionForm").style.visibility = "visible";
        }
        var title = document.getElementById("chapterOfModule");
        title.textContent = chapterOfModuleI;
        var ttle2 = document.getElementById("nameOfModule");
        ttle2.textContent = nameOfModuleI;
        var input = document.getElementById("idOfModule");
        input.textContent = idOfModuleI;
    }
}

function generateArrowForQuestiontext() {
    var text = $("#questiontext").html();
    clearBr_Moz();
    text = text + "→";
    $("#questiontext").html(text);
    return false;
}

function generateArrow2ForQuestiontext() {
    var text = $("#questiontext").html();
    clearBr_Moz();
    text = text + "⇔";
    $("#questiontext").html(text);
    return false;
}

function generateArrowForFeedback() {
    var text = $("#feedback").html();
    clearBr_Moz();
    text = text + "→";
    $("#feedback").html(text);
    return false;
}

function generateArrow2ForFeedback() {
    var text = $("#feedback").html();
    clearBr_Moz();
    text = text + "⇔";
    $("#feedback").html(text);
    return false;
}

function getInputsForQuestion() {
    clearBr_Moz(); // Functie die automatisch breaks verwijderd uit bepaalde divs, aangezien deze als input worden gezien
    if (document.getElementById("solutionDiv") !== null) { //Er is sprake van een solutionDiv, enkel Normal en multiple choice hebben dit
        var form = document.getElementById('questionForm');
        var questiontext = document.getElementById("questiontext").innerHTML;
        var maxpoints = form.maxpoints.value;
        var strategyId = form.strategy.value;
        var marge = 0;
        if (document.getElementById("solutionmarge") !== null) {
            if (document.getElementById("solutionmarge").value !== "") {
                marge = form.solutionmarge.value;
            }
        }
        var solution = document.getElementById("solution").innerHTML;
        var feedback = document.getElementById("feedback").innerHTML;
        solution = solution.replace(/&nbsp;/gi, '');
        questiontext = questiontext.replace(/&nbsp;/gi, '');
        feedback = feedback.replace(/&nbsp;/gi, '');
        questiontext = HtmlToDatabaseLanguage(questiontext);
        solution = HtmlToDatabaseLanguage(solution);
        feedback = HtmlToDatabaseLanguage(feedback);
        var firstSolution = (solution.trim) ? solution.trim() : solution.replace(/^\s+/, '');
        var trimmedQuestion = (questiontext.trim) ? questiontext.trim() : questiontext.replace(/^\s+/, '');
        if (firstSolution === "" || firstSolution == null || firstSolution.length <= 0 || trimmedQuestion === "" || trimmedQuestion == null || trimmedQuestion.length <= 0) {
            if (document.getElementById("errorMessage").childNodes[0] == null) {
                createErrorMessage('errorMessage', "Gelieve alle verplichtte (*) velden correct in te vullen.");
            }
            return false;
        }
        if ($("#multipleChoice").children().length > 1) { // Voor multiple choice
            for (var i = 0; i < $("#multipleChoice").children().length - 1; i++) {
                var id = i + 1;
                var solId = "solution" + id;
                var otherSol = document.getElementById(solId).innerHTML;
                otherSol = otherSol.replace(/&nbsp;/gi, '');
                var otherSolTrim = (otherSol.trim) ? otherSol.trim() : otherSol.replace(/^\s+/, '');
                otherSol = HtmlToDatabaseLanguage(otherSol);
                if (otherSol === "" || otherSol == null || otherSolTrim.length <= 0 || otherSolTrim === "" || otherSolTrim == null) {
                    createErrorMessage("errorMessage2", "Gelieve al je mogelijkheden op te vullen, of eventueel te verwijderen.");
                    return false;
                }
                else {
                    solution = solution + "##";
                    solution = solution + otherSol;
                }
            }
        }
        var moduleId = document.getElementById("idOfModule").childNodes[0].nodeValue;
        if (questiontext === "" || questiontext == null || solution == null || solution === "" || strategyId == null) {
            if (document.getElementById("errorMessage").childNodes[0] == null) {
                createErrorMessage('errorMessage', "Gelieve alle verplichtte (*) velden correct in te vullen.");
            }
            return false;
        }
        else {
            return saveQuestionToDb(questiontext, maxpoints, strategyId, solution, feedback, moduleId, marge);
        }
    }
    else if (document.getElementById("reactionSolution") !== null) { // We hebben een div genaamd reactionSolution, dus we hebben te maken met een aangepaste strategie, namelijk drag n drop / reacties
        var form = document.getElementById('questionForm');
        var questiontext = document.getElementById("questiontext").innerHTML;
        var maxpoints = form.maxpoints.value;
        var strategyId = form.strategy.value;
        var solutionL = document.getElementById("solutionL").innerHTML;
        var solutionR = document.getElementById("solutionR").innerHTML;
        var feedback = document.getElementById("feedback").innerHTML;
        var moduleId = document.getElementById("idOfModule").childNodes[0].nodeValue;
        feedback = feedback.replace(/&nbsp;/gi, '');
        solutionL = solutionL.replace(/&nbsp;/gi, '');
        solutionR = solutionR.replace(/&nbsp;/gi, '');
        questiontext = questiontext.replace(/&nbsp;/gi, '');
        questiontext = HtmlToDatabaseLanguage(questiontext);
        feedback = HtmlToDatabaseLanguage(feedback);
        solutionL = HtmlToDatabaseLanguage(solutionL);
        solutionR = HtmlToDatabaseLanguage(solutionR);
        $(".removeField").remove();
        var firstSolutionR = (solutionR.trim) ? solutionR.trim() : solutionR.replace(/^\s+/, '');
        var firstSolutionL = (solutionL.trim) ? solutionL.trim() : solutionL.replace(/^\s+/, '');
        if ($("#leftPart").children().length > 1) { // Er zijn minstens twee velden opgevuld links van de pijl
            for (var i = 0; i < $("#leftPart").children().length; i++) {
                var id = i + 1;
                var solId = "solutionL" + id;
                if (document.getElementById(solId) !== null) {
                    var otherSol = document.getElementById(solId).innerHTML;
                    otherSol = otherSol.replace(/&nbsp;/gi, '');
                    var otherSolTrim = (otherSol.trim) ? otherSol.trim() : otherSol.replace(/^\s+/, '');
                    otherSol = HtmlToDatabaseLanguage(otherSol);
                    if (otherSol === "" || otherSol === null || otherSolTrim.length <= 0 || otherSolTrim === "" || otherSolTrim === null) {
                        createErrorMessage("errorMessage2", "Gelieve al je mogelijkheden op te vullen, of eventueel te verwijderen.");
                        return false;
                    }
                    else {
                        solutionL = solutionL + "#&#" + otherSol;
                    }
                }
            }
        }
        if ($("#rightPart").children().length > 1) { // Er zijn minstens twee velden opgevuld rechts van de pijl
            for (var i = 0; i < $("#rightPart").children().length; i++) {
                var id = i + 1;
                var solId = "solutionR" + id;
                if (document.getElementById(solId) !== null) {
                    var otherSol = document.getElementById(solId).innerHTML;
                    otherSol = otherSol.replace(/&nbsp;/gi, '');
                    var otherSolTrim = (otherSol.trim) ? otherSol.trim() : otherSol.replace(/^\s+/, '');
                    otherSol = HtmlToDatabaseLanguage(otherSol);
                    if (otherSol === "" || otherSol == null || otherSolTrim.length <= 0 || otherSolTrim === "" || otherSolTrim == null) {
                        createErrorMessage("errorMessage2", "Gelieve al je mogelijkheden op te vullen, of eventueel te verwijderen.");
                        return false;
                    }
                    else {
                        solutionR = solutionR + "#&#" + otherSol;
                    }
                }
            }
        }
        if (questiontext === "" || questiontext === null || !questiontext.match(/\S/) || firstSolutionR === "" || firstSolutionR === null || firstSolutionR.length <= 0 || firstSolutionL === "" || firstSolutionL === null || firstSolutionL.length <= 0) {
            if (document.getElementById("errorMessage").childNodes[0] == null) {
                createErrorMessage('errorMessage', "Gelieve alle verplichtte (*) velden correct in te vullen.");
            }
            return false;
        }
        else {
            if (document.getElementById("selectArrow").value == 0) {
                var finalSolution = solutionL + "#->#" + solutionR;
            }
            else {
                var finalSolution = solutionL + "#<=>#" + solutionR;
            }
            finalSolution = finalSolution.replace(/\s+/g, '');
            return saveQuestionToDb(questiontext, maxpoints, strategyId, finalSolution, feedback, moduleId, 0);
        }
    }
    else if (document.getElementById("tableSolutionDiv") !== null) { // We hebben een tableSolutionDiv, en bevinden zich dus op het formulier voor tabeloefeningen
        var form = document.getElementById('questionForm');
        var questiontext = document.getElementById("questiontext").innerHTML;
        var feedback = document.getElementById("feedback").innerHTML;
        var moduleId = document.getElementById("idOfModule").childNodes[0].nodeValue;
        var solutionL = document.getElementById("solutionL").innerHTML;
        var solutionR = document.getElementById("solutionR").innerHTML;
        feedback = feedback.replace(/&nbsp;/gi, '');
        solutionL = solutionL.replace(/&nbsp;/gi, '');
        solutionR = solutionR.replace(/&nbsp;/gi, '');
        questiontext = questiontext.replace(/&nbsp;/gi, '');
        questiontext = HtmlToDatabaseLanguage(questiontext);
        feedback = HtmlToDatabaseLanguage(feedback);
        solutionL = HtmlToDatabaseLanguage(solutionL);
        solutionR = HtmlToDatabaseLanguage(solutionR);
        var strategyId = form.strategy.value;
        var maxpoints = form.maxpoints.value;
        var marge = 0;
        if (document.getElementById("solutionmarge") !== null) {
            if (document.getElementById("solutionmarge").value !== "") {
                marge = form.solutionmarge.value;
            }
        }
        var firstSolutionR = (solutionR.trim) ? solutionR.trim() : solutionR.replace(/^\s+/, '');
        var firstSolutionL = (solutionL.trim) ? solutionL.trim() : solutionL.replace(/^\s+/, '');
        if ($("#leftPart").children().length > 1) { // Er zijn minstens twee velden opgevuld links van de pijl
            for (var i = 0; i < $("#leftPart").children().length; i++) {
                var id = i + 1;
                var solId = "solutionL" + id;
                if (document.getElementById(solId) !== null) {
                    var otherSol = document.getElementById(solId).innerHTML;
                    otherSol = otherSol.replace(/&nbsp;/gi, '');
                    var otherSolTrim = (otherSol.trim) ? otherSol.trim() : otherSol.replace(/^\s+/, '');
                    otherSol = HtmlToDatabaseLanguage(otherSol);
                    if (otherSol === "" || otherSol === null || otherSolTrim.length <= 0 || otherSolTrim === "" || otherSolTrim === null) {
                        createErrorMessage("errorMessage2", "Gelieve al je mogelijkheden op te vullen, of eventueel te verwijderen.");
                        return false;
                    }
                    else {
                        solutionL = solutionL + "#&#" + otherSol;
                    }
                }
            }
        }
        if ($("#rightPart").children().length > 1) { // Er zijn minstens twee velden opgevuld rechts van de pijl
            for (var i = 0; i < $("#rightPart").children().length; i++) {
                var id = i + 1;
                var solId = "solutionR" + id;
                if (document.getElementById(solId) !== null) {
                    var otherSol = document.getElementById(solId).innerHTML;
                    otherSol = otherSol.replace(/&nbsp;/gi, '');
                    var otherSolTrim = (otherSol.trim) ? otherSol.trim() : otherSol.replace(/^\s+/, '');
                    otherSol = HtmlToDatabaseLanguage(otherSol);
                    if (otherSol === "" || otherSol == null || otherSolTrim.length <= 0 || otherSolTrim === "" || otherSolTrim == null) {
                        createErrorMessage("errorMessage2", "Gelieve al je mogelijkheden op te vullen, of eventueel te verwijderen.");
                        return false;
                    }
                    else {
                        solutionR = solutionR + "#&#" + otherSol;
                    }
                }
            }
        }
        if (questiontext === "" || questiontext === null || !questiontext.match(/\S/) || firstSolutionR === "" || firstSolutionR === null || firstSolutionR.length <= 0 || firstSolutionL === "" || firstSolutionL === null || firstSolutionL.length <= 0) {
            createErrorMessage('errorMessage', "Gelieve alle verplichtte (*) velden correct in te vullen.");
            return false;
        }
        else if ($("#table").children().length <= 0) {
            createErrorMessage('errorMessage', "Gelieve je tabel te genereren en correct in te vullen.");
            return false;
        }
        else {
            var $root = $('<XMLDocument />');
            if (document.getElementById("selectArrow").value == 0) {
                var finalSolution = leftTableSolution + "#->#" + rightTableSolution;
            }
            else {
                var finalSolution = leftTableSolution + "#<=>#" + rightTableSolution;
            }
            //var finalSolution = leftTableSolution + "#->#" + rightTableSolution;
            finalSolution = finalSolution.replace(/\s+/g, '');
            $root.append($('<tablesolution/>').append($('<reaction/>').text("" + finalSolution)).append($('<solutions>')));
            for (var r = 0; r < 4; r++) {
                for (var c = 1; c <= globalCounterForLoopthroughTable; c++) {
                    var input = "input" + r + c;
                    var value = document.getElementsByName(input)[0].value;
                    value = value.replace(/&nbsp;/gi, '');
                    if (value === "" || !value.match(/\S/) || value === null) {
                        createErrorMessage('errorMessage', "Gelieve correcte waarden in te vullen in je tabel.");
                        return false;
                    }
                    else {
                        var parent = $root.find('solutions');
                        if (r === 0) { // tag maken met coefficient-oplossingen
                            parent.append($('<coef/>').text(value));
                        }
                        else {
                            parent.append($('<amount/>').text(value));
                        }
                    }
                }
            }
            var reactionToCompare;
            if (document.getElementById("selectArrow").value == 0) {
                reactionToCompare = solutionL + "#->#" + solutionR;
            }
            else {
                reactionToCompare = solutionL + "#<=>#" + solutionR;
            }
            //var reactionToCompare = solutionL + "#->#" + solutionR;
            if (reactionToCompare != finalSolForCheck) {
                createErrorMessage('errorMessage', "Je hebt je reactievergelijking aangepast. Gelieve je tabel te genereren en in te vullen voor je nieuwe vergelijking.");
                return false;
            }
            var sol = $root.html();
        }
        return saveQuestionToDb(questiontext, maxpoints, strategyId, sol, feedback, moduleId, marge);
    }
    else {
        return false; // Kennen dit form niet
    }
}

function generateTable() {
    document.getElementById("errorMessage2").innerHTML = "";
    clearBr_Moz(); // Functie die automatisch breaks verwijderd uit bepaalde divs, aangezien deze als input worden gezien
    $(".removeField").remove();
    leftTableSolution = "";
    rightTableSolution = "";
    leftTableSolution = document.getElementById("solutionL").innerHTML;
    rightTableSolution = document.getElementById("solutionR").innerHTML;
    var leftSolutionsArray = new Array();
    var rightSolutionsArray = new Array();
    leftTableSolution = leftTableSolution.replace(/&nbsp;/gi, '');
    rightTableSolution = rightTableSolution.replace(/&nbsp;/gi, '');
    var leftCount = 1;
    var rightCount = 1;
    leftSolutionsArray[0] = leftTableSolution;
    rightSolutionsArray[0] = rightTableSolution;
    leftTableSolution = HtmlToDatabaseLanguage(leftTableSolution);
    rightTableSolution = HtmlToDatabaseLanguage(rightTableSolution);
    var leftTrim = (leftTableSolution.trim) ? leftTableSolution.trim() : leftTableSolution.replace(/^\s+/, '');
    var rightTrim = (rightTableSolution.trim) ? rightTableSolution.trim() : rightTableSolution.replace(/^\s+/, '');
    if (leftTrim === "" || leftTrim === null || leftTrim.length <= 0 || rightTrim === "" || rightTrim === null || rightTrim.length <= 0) {
        if (document.getElementById("errorMessage").childNodes[0] == null) {
            createErrorMessage('errorMessage2', "Gelieve een correcte reactievergelijking op te stellen.");
        }
        return false;
    }
    if ($("#leftPart").children().length > 1) { // Er zijn minstens twee velden opgevuld links van de pijl
        console.log($("#leftPart").children());
        for (var i = 0; i < $("#leftPart").children().length; i++) {
            var id = i + 1;
            var solId = "solutionL" + id;
            if (document.getElementById(solId) !== null) {
                var otherSol = document.getElementById(solId).innerHTML;
                otherSol = otherSol.replace(/&nbsp;/gi, '');
                var otherSolTrim = (otherSol.trim()) ? otherSol.trim() : otherSol.replace(/^\s+/, '');
                leftSolutionsArray.push(otherSol);
                otherSol = HtmlToDatabaseLanguage(otherSol);
                if (otherSol === "" || otherSol === null || otherSolTrim.length <= 0 || otherSolTrim === "" || otherSolTrim === null) {
                    createErrorMessage("errorMessage2", "Gelieve al je mogelijkheden op te vullen, of eventueel te verwijderen.");
                    return false;
                }
                else {
                    leftTableSolution = leftTableSolution + "#&#" + otherSol;

                    leftCount = leftCount + 1;
                }
            }
        }
    }
    if ($("#rightPart").children().length > 1) { // Er zijn minstens twee velden opgevuld rechts van de pijl
        console.log($("#rightPart").children());
        for (var i = 0; i < $("#rightPart").children().length; i++) {
            var id = i + 1;
            var solId = "solutionR" + id;
            if (document.getElementById(solId) !== null) {
                var otherSol = document.getElementById(solId).innerHTML;
                otherSol = otherSol.replace(/&nbsp;/gi, '');
                var otherSolTrim = (otherSol.trim()) ? otherSol.trim() : otherSol.replace(/^\s+/, '');
                rightSolutionsArray.push(otherSol);
                otherSol = HtmlToDatabaseLanguage(otherSol);
                if (otherSol === "" || otherSol == null || otherSolTrim.length <= 0 || otherSolTrim === "" || otherSolTrim == null) {
                    createErrorMessage("errorMessage2", "Gelieve al je mogelijkheden op te vullen, of eventueel te verwijderen.");
                    return false;
                }
                else {
                    rightTableSolution = rightTableSolution + "#&#" + otherSol;
                    rightCount = rightCount + 1;
                }
            }
        }
    }
    if (document.getElementById("selectArrow").value == 0) {
        finalSolForCheck = leftTableSolution + "#->#" + rightTableSolution;
    }
    else {
        finalSolForCheck = leftTableSolution + "#<=>#" + rightTableSolution;
    }
    //finalSolForCheck = leftTableSolution + "#->#" + rightTableSolution;
    globalCounterForLoopthroughTable = leftCount + rightCount;
    var coeffs = new Array();
    sliceCoefficients(leftSolutionsArray, coeffs);
    sliceCoefficients(rightSolutionsArray, coeffs);

    var cols = leftCount + rightCount + 1; // aantal kolommen komt overeen met aantal stoffen uit reactie + 1 extra kolom met "opgave" links
    var rows = 4; // rijen voor: coefficienten, voor, na en tijdens 
    var tableDiv = $("#table");
    $(tableDiv).empty();
    var tableHead = $('<thead>');
    var tableBody = $('<tbody>');

    var trHeader = $('<tr>');
    $('<th></th>').appendTo(trHeader);
    trHeader.appendTo(tableHead);
    for (var i = 0; i < leftSolutionsArray.length; i++) {
        $('<th>' + leftSolutionsArray[i] + '</th>').appendTo(trHeader);
        trHeader.appendTo(tableHead);
    }
    for (var i = 0; i < rightSolutionsArray.length; i++) {
        $('<th>' + rightSolutionsArray[i] + '</th>').appendTo(trHeader);
        trHeader.appendTo(tableHead);
    }

    for (var r = 0; r < rows; r++)
    {
        var tr = $('<tr>');
        for (var c = 0; c < cols; c++) {
            if (r === 0 && c === 0) {
                $('<td  style="font-weight:bold">Coefficient</td>').appendTo(tr);
                tr.appendTo(tableBody);
            }
            else if (c === 0 && r === 1) {
                $('<td style="font-weight:bold" ># Mol voor de reactie</td>').appendTo(tr);
                tr.appendTo(tableBody);
            }
            else if (c === 0 && r === 2) {
                $('<td  style="font-weight:bold"># Mol tijdens de reactie</td>').appendTo(tr);
                tr.appendTo(tableBody);
            }
            else if (c === 0 && r === 3) {
                $('<td  style="font-weight:bold"># Mol na de reactie</td>').appendTo(tr);
                tr.appendTo(tableBody);
            }
            else {
                if (r === 0) { // rij voor coefficienten
                    var td = $('<td>');
                    var name = "input" + r + c;
                    var input = $('<input name=' + name + '></input>');
                    input.addClass("form-control");
                    input.css("width", "70px");
                    input.css("float", "left");
                    $(input).appendTo(td);
                    $(input).val(coeffs[c - 1]);
                    $(input).prop("readonly", true);
                    $(td).appendTo(tr);
                    $(tr.appendTo(tableBody));
                }
                else if (r > 0) { // rij voor hoeveelheden mol
                    var td = $('<td>');
                    var name = "input" + r + c;
                    var input = $('<input name=' + name + ' ></input>');
                    input.addClass("form-control");
                    input.css("width", "70px");
                    input.css("float", "left");
                    $(input).appendTo(td);
                    $(td).appendTo(tr);
                    $(tr.appendTo(tableBody));
                }
            }
        }
    }
    tableHead.appendTo(tableDiv);
    tableBody.appendTo(tableDiv);
}

function clearBr_Moz() {
    $('#reactionDiv,#multipleChoice, #questionDiv, #solutionDiv, #feedbackDiv, #tableSolutionDiv, #TableReaction, #question').find('br').remove();
}


function sliceCoefficients(arrayOfSolutions, coeffs) {
    for (var i = 0; i < arrayOfSolutions.length; i++) {
        if (arrayOfSolutions[i] != null) {
            if (!parseInt(arrayOfSolutions[i].charAt(0).match(/\d+/g))) { // Checken of eerste element een integer is -> coefficient
                coeffs.push(1); //Indien dit niet zo is, zetten we die coefficient op 1
            }
            else {
                var coeff = parseInt(arrayOfSolutions[i].match(/[0-9]+/)[0], 10); //Anders halen we de opeenvolgende getallen op (coefficient)
                arrayOfSolutions[i] = arrayOfSolutions[i].slice(coeff.toString().length, coeff.length); // De coefficient ordt van het element gekapt
                coeffs.push(coeff); // en toegevoegd aan een lijst, waar we alles tenslotte uit gaan halen in generateTable() en in rij van coefficienten steken.
            }
        }
    }
}

function generateArrowForSolution(value) {
    var parent = document.getElementById("arrow");
    $(parent).empty();
    if (value == 0) {
        $(parent).html('&rarr;');
    }
    else {
        $(parent).html('&hArr;');
    }
}



function generateLeft() {
    var counter = $("#leftPart").children().length;
    var p = document.createElement('p');
    p.setAttribute("class", "enriched form-control");
    p.setAttribute("name", "solution");
    p.setAttribute("type", "text");
    p.style.width = "auto";
    p.style.float = "left";
    var id = "solutionL" + counter;
    p.id = id;
    p.contentEditable = true;
    $(p).mouseenter(
            function () {
                clearBr_Moz();
                if ($(p).is(':empty')) {
                    var aRemove = document.createElement('a');
                    aRemove.appendChild(document.createTextNode("X"));
                    aRemove.style.color = "#c9302c";
                    aRemove.style.cursor = "pointer";
                    aRemove.setAttribute("class", "removeField");
                    aRemove.onclick = (function () {
                        return function () {
                            deleteLeftBox(id + '', counter);
                        }
                    })();
                    $(p).append($(aRemove));
                }
            }
    );
    $(p).click(
            function () {
                clearBr_Moz(); // Functie die automatisch breaks verwijderd uit bepaalde divs, aangezien deze als input worden gezien
                if ($(p).find("a").length) {
                    $(p).empty();
                }
            }
    );
    var plusPara = document.createElement('p');
    plusPara.appendChild(document.createTextNode('+'));
    plusPara.style.float = "left";
    plusPara.style.paddingTop = "10px";
    plusPara.id = "plusL" + (counter);
    $(".leftPart").append(plusPara);
    $(".leftPart").append(p);
    refreshEnriched();
}

function deleteLeftBox(id, counter) {
    var paraId = "plusL" + counter;
    document.getElementById("leftPart").removeChild(document.getElementById(id));
    document.getElementById("leftPart").removeChild(document.getElementById(paraId));
}

function deleteRightBox(id, counter) {
    var paraId = "plusR" + counter;
    document.getElementById("rightPart").removeChild(document.getElementById(id));
    document.getElementById("rightPart").removeChild(document.getElementById(paraId));
}

function generateRight() {
    var counter = $("#rightPart").children().length;
    var p = document.createElement('p');
    p.setAttribute("class", "enriched form-control");
    p.setAttribute("name", "solution");
    p.setAttribute("type", "text");
    p.style.width = "auto";
    p.style.float = "left";
    var id = "solutionR" + counter;
    p.id = id;
    p.contentEditable = true;
    p.float = "left";
    $(p).mouseenter(
            function () {
                clearBr_Moz();
                if ($(p).is(':empty')) {
                    var aRemove = document.createElement('a');
                    aRemove.appendChild(document.createTextNode("X"));
                    aRemove.style.color = "#c9302c";
                    aRemove.style.cursor = "pointer";
                    aRemove.setAttribute("class", "removeField");
                    aRemove.onclick = (function () {
                        return function () {
                            deleteRightBox(id + '', counter);
                        }
                    })();
                    $(p).append($(aRemove));
                }
            }
    );
    $(p).click(
            function () {
                if ($(p).find("a").length) {
                    $(p).empty();
                }
            }
    );
    findAllEnrichedElements();
    makeEnrichedReady();
    var plusPara = document.createElement('p');
    plusPara.appendChild(document.createTextNode('+'));
    plusPara.style.float = "left";
    plusPara.style.paddingTop = "10px";
    plusPara.id = "plusR" + counter;
    $(".rightPart").append(plusPara);
    $(".rightPart").append(p);
    refreshEnriched();
}


function saveQuestionToDb(questiontext, maxpoints, strategyId, solution, feedback, moduleId, marge) {
    if (feedback === "" || !feedback.match(/\S/)) {
        feedback = "";
    }
    $.ajax({
        url: "../ChemieboxController", // "Ander" pad, want we zitten in partials nu
        async: true,
        type: "POST",
        data: {"action": "saveQuestion", "moduleId": "" + moduleId, "questiontext": "" + questiontext, "maxpoints": "" + maxpoints, "strategyId": "" + strategyId, "solution": "" + solution, "feedback": "" + feedback, "marge": "" + marge},
        success: function (data) {
            setTimeout(function () {
                window.close();
            }, 200);
            window.close(); // window wordt gesloten, wat bij onbeforeunload staat wordt snel uitgevoerd vooraleer we sluiten
        },
        error: function (data) {
            console.log("error:", data);
        }
    });
    return false;
}

function getQuestionsForModuleCallback(data) {
    $("#loading").empty();
    document.getElementById("createQuestionBtn").style.visibility = "visible";
    var questions = data.getElementsByTagName('question');
    var table = document.getElementById('tableBody');
    setModuleStatusIcon($(data).find("available").text());
    if (questions.length <= 0) {
        $("#table").html("Er werden geen vragen gevonden voor deze module");
    }
    for (var i = 0; i < questions.length; i++) {
        var questiontextOfQuestionI = (questions[i]).getElementsByTagName('questiontext')[0];
        var maxPointsOfQuestionI = questions[i].getElementsByTagName('maxpoints')[0].textContent;
        var solutionOfQuestionI = questions[i].getElementsByTagName('solution')[0];
        var strategyOfQuestionI = questions[i].getElementsByTagName('strategy')[0].textContent;

        var feedbackOfQuestionI = questions[i].getElementsByTagName('feedback')[0];
        var idOfQuestionI = questions[i].getElementsByTagName('id')[0].textContent;
        var questiontr = document.createElement('tr');
        questiontr.id = idOfQuestionI;

        var questiontexttd = document.createElement('td');
        $(questiontexttd).html(questiontextOfQuestionI);

        var solutiontexttd;

        if (strategyOfQuestionI === "Tabel") {
            var $root = $('<XMLDocument />');
            $root.append($(solutionOfQuestionI));
            solutiontexttd = showTableForSolution($root);
        }
        else {
            solutiontexttd = document.createElement('td');
            $(solutiontexttd).html(solutionOfQuestionI);
        }
        var maxpointstd = document.createElement('td');
        var maxpointstext = document.createTextNode(maxPointsOfQuestionI);
        maxpointstd.appendChild(maxpointstext);

        var strategytd = document.createElement('td');
        var strategytext = document.createTextNode(strategyOfQuestionI);
        strategytd.appendChild(strategytext);

        var feedbacktd = document.createElement('td');
        if (feedbackOfQuestionI.textContent === '') {
            feedbackOfQuestionI = '(geen)';
        }
        $(feedbacktd).html(feedbackOfQuestionI)

        var deletetd = document.createElement('td');
        var aRemove = document.createElement('img');
        aRemove.alt = "verwijder";
        aRemove.src = "images/delete.png";
        aRemove.myParam = idOfQuestionI;
        aRemove.style.cursor = "pointer";
        aRemove.addEventListener("click", deleteHelp);
        deletetd.appendChild(aRemove);

        var updatetd = document.createElement('td');
        var aUpdate = document.createElement('img');
        aUpdate.alt = "editeer";
        aUpdate.src = "images/edit.png";
        aUpdate.style.cursor = "pointer";
        aUpdate.myParam = idOfQuestionI;
        aUpdate.addEventListener("click", updateHelp);
        updatetd.appendChild(aUpdate);

        questiontr.appendChild(questiontexttd);
        questiontr.appendChild(solutiontexttd);
        questiontr.appendChild(strategytd);
        questiontr.appendChild(feedbacktd);
        questiontr.appendChild(maxpointstd);
        questiontr.appendChild(updatetd);
        questiontr.appendChild(deletetd);

        table.appendChild(questiontr);
    }
}

function showTableForSolution($root) {
    var solutiontexttd = document.createElement('td');
    var reaction = $root.find('reaction').html();
    $(solutiontexttd).html(reaction);
    return solutiontexttd;
}


function updateHelp(evt) {
    var updateFormWindow = window.open(
            'partials/editQuestionForm.jsp', "Editeer", "scrollbars = 1,  height=700,width=1200"
            );
    updateFormWindow.myParaForUpdate = evt.target.myParam;
    updateFormWindow.onbeforeunload = function () {
        return getQuestionsForModule();
    };
}

function getEditQuestionFormCallback() {
    var questionId = myParaForUpdate;
    $.ajax({
        url: "../ChemieboxController",
        async: true,
        type: "GET",
        data: {"action": "getInformationAboutQuestion", "questionId": "" + questionId},
        success: function (data) {
            return generateEditFormCallback(data);
        },
        error: function (data) {
            console.log("error:", data);
        }
    });
    return false;
}

function generateEditFormCallback(data) {
    var form = document.getElementById('questionForm');
    var questiontextOfQuestion = data.getElementsByTagName('questiontext')[0];
    var maxPointsOfQuestion = data.getElementsByTagName('maxpoints')[0].textContent;
    var solutionOfQuestion = data.getElementsByTagName('solution')[0];
    var strategyOfQuestion = data.getElementsByTagName('strategy')[0].textContent;
    var feedbackOfQuestion = data.getElementsByTagName('feedback')[0];
    var margeOfQuestion = data.getElementsByTagName('solutionmarge')[0].textContent;
    var modules = data.getElementsByTagName('module');
    var idOfQuestion = data.getElementsByTagName('id')[0].textContent;
    var startyearOfModule = modules[0].getElementsByTagName('startyear')[0].textContent;
    var startmonthOfModule = modules[0].getElementsByTagName('startmonth')[0].textContent;
    var startdayOfModule = modules[0].getElementsByTagName('startday')[0].textContent;
    var endyearOfModule = modules[0].getElementsByTagName('endyear')[0].textContent;
    var endmonthOfModule = modules[0].getElementsByTagName('endmonth')[0].textContent;
    var enddayOfModule = modules[0].getElementsByTagName('endday')[0].textContent;
    var startdate = new Date(startyearOfModule, startmonthOfModule, startdayOfModule);
    var enddate = new Date(endyearOfModule, endmonthOfModule, enddayOfModule);
    var currentDate = new Date();
    if (currentDate >= startdate && currentDate <= enddate - 1) {
        document.getElementById("questionForm").style.visibility = "hidden";
        createErrorMessage("errorMessage3", "De deadline van deze module is nog niet verlopen. Je kan bijgevolg geen vragen bewerken.");
    }
    else {
        document.getElementById("questionForm").style.visibility = "visible";
        document.getElementById("idOfQuestion").innerHTML = idOfQuestion;
        if (strategyOfQuestion === "Tabel") {
            $(".questionEditFormForStrategy").load('editTableForm.jsp', function () {
                document.getElementById("questiontext").innerHTML = $(questiontextOfQuestion).html();
                document.getElementById("feedback").innerHTML = $(feedbackOfQuestion).html();
                document.getElementById("solutionmarge").value = margeOfQuestion;
                generateTableForEditQuestion(solutionOfQuestion);
                form.maxpoints.value = maxPointsOfQuestion;
            });
        }
        else if (strategyOfQuestion === "Normaal") {
            $(".questionEditFormForStrategy").load('editNormalQuestionForm.jsp', function () {
                document.getElementById("questiontext").innerHTML = $(questiontextOfQuestion).html();
                document.getElementById("feedback").innerHTML = $(feedbackOfQuestion).html();
                document.getElementById("solution").innerHTML = $(solutionOfQuestion).html();
                document.getElementById("solutionmarge").value = margeOfQuestion;
                form.maxpoints.value = maxPointsOfQuestion;
            });
        }
        else if (strategyOfQuestion === "Meerkeuze") {
            $(".questionEditFormForStrategy").load('editMultipleChoiceQuestionForm.jsp', function () {
                document.getElementById("questiontext").innerHTML = $(questiontextOfQuestion).html();
                document.getElementById("feedback").innerHTML = $(feedbackOfQuestion).html();
                $(document.getElementById("solution")).html(solutionOfQuestion);
                fillInMultipleAnswers(solutionOfQuestion);
                form.maxpoints.value = maxPointsOfQuestion;
            });
        }
        else if (strategyOfQuestion === "Drag n Drop" || strategyOfQuestion === "Niet-redox") {
            $(".questionEditFormForStrategy").load('editDragNDropReactionsQuestionForm.jsp', function () {
                document.getElementById("questiontext").innerHTML = $(questiontextOfQuestion).html();
                document.getElementById("feedback").innerHTML = $(feedbackOfQuestion).html();
                $(document.getElementById("solution")).html(solutionOfQuestion);
                generateReaction(solutionOfQuestion);
                form.maxpoints.value = maxPointsOfQuestion;
            });
        }
    }
}

function fillInMultipleAnswers(solution) {
    var solutionHtml = $(solution).html();
    var solutionsArray = solutionHtml.split(" of ");
    $(document.getElementById("solution")).html(solutionsArray[0]);
    for (var i = 1; i < solutionsArray.length; i++) {
        generateAnotherAnswer();
        $(document.getElementById("solution" + i)).html(solutionsArray[i]);
    }
}

function generateReaction(solution) {
    var solutionHtml = $(solution).html();
    var aflopend = solutionHtml.indexOf("→");
    if (aflopend != -1) {
        var reactionParts = solutionHtml.split(" → ");
        document.getElementById("selectArrow").value = 0;
        generateArrowForSolution(document.getElementById("selectArrow").value);
    }
    else {
        var reactionParts = solutionHtml.split(" ⇔ ");
        document.getElementById("selectArrow").value = 1;
        generateArrowForSolution(document.getElementById("selectArrow").value);
    }
    leftTableSolution = reactionParts[0];
    rightTableSolution = reactionParts[1];
    var leftSolutionArray = leftTableSolution.split(" + ");
    var rightSolutionArray = rightTableSolution.split(" + ");
    $(document.getElementById("solutionL")).html(leftSolutionArray[0]);
    $(document.getElementById("solutionR")).html(rightSolutionArray[0]);

    var leftId = 1;
    var rightId = 1;
    for (var i = 1; i < leftSolutionArray.length; i++) {
        generateLeft();
        $(document.getElementById("solutionL" + leftId)).html(leftSolutionArray[i]);
        leftId = leftId + 2; // Zo zit het maken in mekaar, aangezien er een plus tussen komt
    }
    for (var i = 1; i < rightSolutionArray.length; i++) {
        generateRight();
        $(document.getElementById("solutionR" + rightId)).html(rightSolutionArray[i]);
        rightId = rightId + 2; // Zo zit het maken in mekaar, aangezien er een plus tussen komt
    }
}

function getInputsForEditQuestion() { //SORRY
    clearBr_Moz();
    if (document.getElementById("solutionDiv") !== null) { //Er is sprake van een solutionDiv, enkel Normal en multiple choice hebben dit
        var form = document.getElementById('questionForm');
        var questiontext = document.getElementById("questiontext").innerHTML;
        var maxpoints = form.maxpoints.value;
        var marge = 0;
        if (document.getElementById("solutionmarge") !== null) {
            if (document.getElementById("solutionmarge").value !== "") {
                marge = form.solutionmarge.value;
            }
        }
        var questionId = document.getElementById("idOfQuestion").childNodes[0].nodeValue;
        var solution = document.getElementById("solution").innerHTML;
        var feedback = document.getElementById("feedback").innerHTML;
        solution = solution.replace(/&nbsp;/gi, '');
        questiontext = questiontext.replace(/&nbsp;/gi, '');
        feedback = feedback.replace(/&nbsp;/gi, '');
        questiontext = HtmlToDatabaseLanguage(questiontext);
        solution = HtmlToDatabaseLanguage(solution);
        feedback = HtmlToDatabaseLanguage(feedback);
        var firstSolution = (solution.trim) ? solution.trim() : solution.replace(/^\s+/, '');
        var trimmedQuestion = (questiontext.trim) ? questiontext.trim() : questiontext.replace(/^\s+/, '');
        if (firstSolution === "" || firstSolution == null || firstSolution.length <= 0 || trimmedQuestion === "" || trimmedQuestion == null || trimmedQuestion.length <= 0) {
            if (document.getElementById("errorMessage").childNodes[0] == null) {
                createErrorMessage('errorMessage', "Gelieve alle verplichtte (*) velden correct in te vullen.");
            }
            return false;
        }
        if ($("#multipleChoice").children().length > 1) { // Voor multiple choice
            for (var i = 0; i < $("#multipleChoice").children().length - 1; i++) {
                var id = i + 1;
                var solId = "solution" + id;
                var otherSol = document.getElementById(solId).innerHTML;
                otherSol = otherSol.replace(/&nbsp;/gi, '');
                var otherSolTrim = (otherSol.trim) ? otherSol.trim() : otherSol.replace(/^\s+/, '');
                otherSol = HtmlToDatabaseLanguage(otherSol);
                if (otherSol === "" || otherSol == null || otherSolTrim.length <= 0 || otherSolTrim === "" || otherSolTrim == null) {
                    createErrorMessage("errorMessage2", "Gelieve al je mogelijkheden op te vullen, of eventueel te verwijderen.");
                    return false;
                }
                else {
                    solution = solution + "##";
                    solution = solution + otherSol;
                }
            }
        }
        if (questiontext === "" || questiontext == null || solution == null || solution === "") {
            if (document.getElementById("errorMessage").childNodes[0] == null) {
                createErrorMessage('errorMessage', "Gelieve alle verplichtte (*) velden correct in te vullen.");
            }
            return false;
        }
        else {
            return editQuestionInDb(questionId, questiontext, maxpoints, solution, feedback, marge);
        }
    }
    else if (document.getElementById("reactionSolution") !== null) { // We hebben een div genaamd reactionSolution, dus we hebben te maken met een aangepaste strategie, namelijk drag n drop / reacties
        var form = document.getElementById('questionForm');
        var questiontext = document.getElementById("questiontext").innerHTML;
        var maxpoints = form.maxpoints.value;
        var solutionL = document.getElementById("solutionL").innerHTML;
        var solutionR = document.getElementById("solutionR").innerHTML;
        var feedback = document.getElementById("feedback").innerHTML;
        var questionId = document.getElementById("idOfQuestion").childNodes[0].nodeValue;
        feedback = feedback.replace(/&nbsp;/gi, '');
        solutionL = solutionL.replace(/&nbsp;/gi, '');
        solutionR = solutionR.replace(/&nbsp;/gi, '');
        questiontext = questiontext.replace(/&nbsp;/gi, '');
        questiontext = HtmlToDatabaseLanguage(questiontext);
        feedback = HtmlToDatabaseLanguage(feedback);
        solutionL = HtmlToDatabaseLanguage(solutionL);
        solutionR = HtmlToDatabaseLanguage(solutionR);
        $(".removeField").remove();
        var firstSolutionR = (solutionR.trim) ? solutionR.trim() : solutionR.replace(/^\s+/, '');
        var firstSolutionL = (solutionL.trim) ? solutionL.trim() : solutionL.replace(/^\s+/, '');
        if ($("#leftPart").children().length > 1) { // Er zijn minstens twee velden opgevuld links van de pijl
            for (var i = 0; i < $("#leftPart").children().length; i++) {
                var id = i + 1;
                var solId = "solutionL" + id;
                if (document.getElementById(solId) !== null) {
                    var otherSol = document.getElementById(solId).innerHTML;
                    otherSol = otherSol.replace(/&nbsp;/gi, '');
                    var otherSolTrim = (otherSol.trim) ? otherSol.trim() : otherSol.replace(/^\s+/, '');
                    otherSol = HtmlToDatabaseLanguage(otherSol);
                    if (otherSol === "" || otherSol === null || otherSolTrim.length <= 0 || otherSolTrim === "" || otherSolTrim === null) {
                        createErrorMessage("errorMessage2", "Gelieve al je mogelijkheden op te vullen, of eventueel te verwijderen.");
                        return false;
                    }
                    else {
                        solutionL = solutionL + "#&#" + otherSol;
                    }
                }
            }
        }
        if ($("#rightPart").children().length > 1) { // Er zijn minstens twee velden opgevuld rechts van de pijl
            for (var i = 0; i < $("#rightPart").children().length; i++) {
                var id = i + 1;
                var solId = "solutionR" + id;
                if (document.getElementById(solId) !== null) {
                    var otherSol = document.getElementById(solId).innerHTML;
                    otherSol = otherSol.replace(/&nbsp;/gi, '');
                    var otherSolTrim = (otherSol.trim) ? otherSol.trim() : otherSol.replace(/^\s+/, '');
                    otherSol = HtmlToDatabaseLanguage(otherSol);
                    if (otherSol === "" || otherSol == null || otherSolTrim.length <= 0 || otherSolTrim === "" || otherSolTrim == null) {
                        createErrorMessage("errorMessage2", "Gelieve al je mogelijkheden op te vullen, of eventueel te verwijderen.");
                        return false;
                    }
                    else {
                        solutionR = solutionR + "#&#" + otherSol;
                    }
                }
            }
        }
        if (questiontext === "" || questiontext === null || !questiontext.match(/\S/) || firstSolutionR === "" || firstSolutionR === null || firstSolutionR.length <= 0 || firstSolutionL === "" || firstSolutionL === null || firstSolutionL.length <= 0) {
            if (document.getElementById("errorMessage").childNodes[0] == null) {
                createErrorMessage('errorMessage', "Gelieve alle verplichtte (*) velden correct in te vullen.");
            }
            return false;
        }
        else {
            if (document.getElementById("selectArrow").value == 0) {
                var finalSolution = solutionL + "#->#" + solutionR;
            }
            else {
                var finalSolution = solutionL + "#<=>#" + solutionR;
            }
            finalSolution = finalSolution.replace(/\s+/g, '');
            return editQuestionInDb(questionId, questiontext, maxpoints, finalSolution, feedback, 0);
        }
    }
    else if (document.getElementById("tableSolutionDiv") !== null) { // We hebben een tableSolutionDiv, en bevinden zich dus op het formulier voor tabeloefeningen
        var form = document.getElementById('questionForm');
        var questiontext = document.getElementById("questiontext").innerHTML;
        var feedback = document.getElementById("feedback").innerHTML;
        var questionId = document.getElementById("idOfQuestion").childNodes[0].nodeValue;
        var solutionL = document.getElementById("solutionL").innerHTML;
        var solutionR = document.getElementById("solutionR").innerHTML;
        feedback = feedback.replace(/&nbsp;/gi, '');
        solutionL = solutionL.replace(/&nbsp;/gi, '');
        solutionR = solutionR.replace(/&nbsp;/gi, '');
        questiontext = questiontext.replace(/&nbsp;/gi, '');
        questiontext = HtmlToDatabaseLanguage(questiontext);
        feedback = HtmlToDatabaseLanguage(feedback);
        solutionL = HtmlToDatabaseLanguage(solutionL);
        solutionR = HtmlToDatabaseLanguage(solutionR);
        var maxpoints = form.maxpoints.value;
        var marge = 0;
        $(".removeField").remove();
        var firstSolutionR = (solutionR.trim) ? solutionR.trim() : solutionR.replace(/^\s+/, '');
        var firstSolutionL = (solutionL.trim) ? solutionL.trim() : solutionL.replace(/^\s+/, '');
        if ($("#leftPart").children().length > 1) { // Er zijn minstens twee velden opgevuld links van de pijl
            for (var i = 0; i < $("#leftPart").children().length; i++) {
                var id = i + 1;
                var solId = "solutionL" + id;
                if (document.getElementById(solId) !== null) {
                    var otherSol = document.getElementById(solId).innerHTML;
                    otherSol = otherSol.replace(/&nbsp;/gi, '');
                    var otherSolTrim = (otherSol.trim) ? otherSol.trim() : otherSol.replace(/^\s+/, '');
                    otherSol = HtmlToDatabaseLanguage(otherSol);
                    if (otherSol === "" || otherSol === null || otherSolTrim.length <= 0 || otherSolTrim === "" || otherSolTrim === null) {
                        createErrorMessage("errorMessage2", "Gelieve al je mogelijkheden op te vullen, of eventueel te verwijderen.");
                        return false;
                    }
                    else {
                        solutionL = solutionL + "#&#" + otherSol;
                    }
                }
            }
        }
        if ($("#rightPart").children().length > 1) { // Er zijn minstens twee velden opgevuld rechts van de pijl
            for (var i = 0; i < $("#rightPart").children().length; i++) {
                var id = i + 1;
                var solId = "solutionR" + id;
                if (document.getElementById(solId) !== null) {
                    var otherSol = document.getElementById(solId).innerHTML;
                    otherSol = otherSol.replace(/&nbsp;/gi, '');
                    var otherSolTrim = (otherSol.trim) ? otherSol.trim() : otherSol.replace(/^\s+/, '');
                    otherSol = HtmlToDatabaseLanguage(otherSol);
                    if (otherSol === "" || otherSol == null || otherSolTrim.length <= 0 || otherSolTrim === "" || otherSolTrim == null) {
                        createErrorMessage("errorMessage2", "Gelieve al je mogelijkheden op te vullen, of eventueel te verwijderen.");
                        return false;
                    }
                    else {
                        solutionR = solutionR + "#&#" + otherSol;
                    }
                }
            }
        }
        if (document.getElementById("solutionmarge") !== null) {
            if (document.getElementById("solutionmarge").value !== "") {
                marge = form.solutionmarge.value;
            }
        }
        var firstSolutionR = (solutionR.trim) ? solutionR.trim() : solutionR.replace(/^\s+/, '');
        var firstSolutionL = (solutionL.trim) ? solutionL.trim() : solutionL.replace(/^\s+/, '');
        if (questiontext === "" || questiontext === null || !questiontext.match(/\S/) || firstSolutionR === "" || firstSolutionR === null || firstSolutionR.length <= 0 || firstSolutionL === "" || firstSolutionL === null || firstSolutionL.length <= 0) {
            createErrorMessage('errorMessage', "Gelieve alle verplichtte (*) velden correct in te vullen.");
            return false;
        }
        else if ($("#table").children().length <= 0) {
            createErrorMessage('errorMessage', "Gelieve je tabel te genereren en correct in te vullen.");
            return false;
        }
        else {
            var $root = $('<XMLDocument />');
            leftTableSolution = HtmlToDatabaseLanguage(leftTableSolution);
            rightTableSolution = HtmlToDatabaseLanguage(rightTableSolution);
            if (document.getElementById("selectArrow").value == 0) {
                var finalSolution = leftTableSolution + "#->#" + rightTableSolution;
            }
            else {
                var finalSolution = leftTableSolution + "#<=>#" + rightTableSolution;
            }
            //var finalSolution = leftTableSolution + "#->#" + rightTableSolution;
            finalSolution = finalSolution.replace(/\s+/g, "");
            finalSolution = finalSolution.replace(/(?!\|)\+(?!\|)/g, "#&#");
            $root.append($('<tablesolution/>').append($('<reaction/>').text("" + finalSolution)).append($('<solutions>')));
            for (var r = 0; r < 4; r++) {
                for (var c = 1; c <= globalCounterForLoopthroughTable; c++) {
                    var input = "input" + r + c;
                    var value = document.getElementsByName(input)[0].value;
                    value = value.replace(/&nbsp;/gi, '');
                    if (value === "" || !value.match(/\S/) || value === null) {
                        createErrorMessage('errorMessage', "Gelieve correcte waarden in te vullen in je tabel.");
                        return false;
                    }
                    else {
                        var parent = $root.find('solutions');
                        if (r === 0) { // tag maken met coefficient-oplossingen
                            parent.append($('<coef/>').text(value));
                        }
                        else {
                            parent.append($('<amount/>').text(value));
                        }
                    }
                }

            }
            finalSolForCheck = finalSolForCheck.replace(/\s+/g, "");
            finalSolForCheck = finalSolForCheck.replace(/(?!\|)\+(?!\|)/g, "#&#");

            var reactionToCompare;// = solutionL + "#->#" + solutionR;
            if (document.getElementById("selectArrow").value == 0) {
                reactionToCompare = solutionL + "#->#" + solutionR;
            }
            else {
                reactionToCompare = solutionL + "#<=>#" + solutionR;
            }
            reactionToCompare = reactionToCompare.replace(/\s+/g, "");
            if (reactionToCompare != finalSolForCheck) {
                createErrorMessage('errorMessage', "Je hebt je reactievergelijking aangepast. Gelieve je tabel te genereren en in te vullen voor je nieuwe vergelijking.");
                return false;
            }
            var sol = $root.html();
        }
        return editQuestionInDb(questionId, questiontext, maxpoints, sol, feedback, marge);
    }
    else {
        return false; // Kennen dit form niet
    }
}

function editQuestionInDb(questionId, questiontext, maxpoints, solution, feedback, marge) {
    if (feedback === "" || !feedback.match(/\S/)) {
        feedback = "";
    }
    $.ajax({
        url: "../ChemieboxController", // "Ander" pad, want we zitten in partials nu
        async: true,
        type: "POST",
        data: {"action": "saveUpdatedQuestion", "questionId": "" + questionId, "questiontext": "" + questiontext, "maxpoints": "" + maxpoints, "solution": "" + solution, "feedback": "" + feedback, "marge": "" + marge},
        success: function (data) {
            setTimeout(function () {
                window.close();
            }, 200);
            window.close(); // window wordt gesloten, wat bij onbeforeunload staat wordt snel uitgevoerd vooraleer we sluiten
        },
        error: function (data) {
            console.log("error:", data);
        }
    });
    return false;
}

function createErrorMessage(divId, message) {
    if (document.getElementById("errorMessage") !== null) {
        document.getElementById("errorMessage").innerHTML = "";
    }
    if (document.getElementById("errorMessage2") !== null) {
        document.getElementById("errorMessage2").innerHTML = "";
    }
    if (document.getElementById("errorMessage3") !== null) {
        document.getElementById("errorMessage3").innerHTML = "";
    }
    var errorParagraph = document.getElementById(divId);
    var newTextNode = document.createTextNode(message);
    errorParagraph.appendChild(newTextNode);
}

function generateTableForEditQuestion(solution) {
    var root = $('<XMLDocument />');
    root.append($(solution));
    var coefficientSols = root.find('coef');
    var amountSols = root.find('amount');
    var reaction = root.find('reaction').html();
    finalSolForCheck = reaction;
    finalSolForCheck = HtmlToDatabaseLanguage(finalSolForCheck);
    finalSolForCheck = finalSolForCheck.replace(/\s+/g, "");
    finalSolForCheck = finalSolForCheck.replace(/(?!\|)\+(?!\|)/g, "#&#");
    var aflopend = reaction.indexOf("→");
    if (aflopend != -1) {
        var reactionParts = reaction.split(" → ");
        document.getElementById("selectArrow").value = 0;
        generateArrowForSolution(document.getElementById("selectArrow").value);
    }
    else {
        var reactionParts = reaction.split(" ⇔ ");
        document.getElementById("selectArrow").value = 1;
        generateArrowForSolution(document.getElementById("selectArrow").value);
    }

    // var reactionParts = reaction.split(" → ");
    leftTableSolution = reactionParts[0];
    rightTableSolution = reactionParts[1];
    var leftSolutionArray = leftTableSolution.split(/\s*\+\s*/);
    var rightSolutionArray = rightTableSolution.split(/\s*\+\s*/);
    $(document.getElementById("solutionL")).html(leftSolutionArray[0]);
    $(document.getElementById("solutionR")).html(rightSolutionArray[0]);

    var leftId = 1;
    var rightId = 1;
    for (var i = 1; i < leftSolutionArray.length; i++) {
        generateLeft();
        $(document.getElementById("solutionL" + leftId)).html(leftSolutionArray[i]);
        leftId = leftId + 2; // Zo zit het maken in mekaar, aangezien er een plus tussen komt
    }
    for (var i = 1; i < rightSolutionArray.length; i++) {
        generateRight();
        $(document.getElementById("solutionR" + rightId)).html(rightSolutionArray[i]);
        rightId = rightId + 2; // Zo zit het maken in mekaar, aangezien er een plus tussen komt
    }

    globalCounterForLoopthroughTable = leftSolutionArray.length + rightSolutionArray.length;
    var coeffs = new Array();
    sliceCoefficients(leftSolutionArray, coeffs);
    sliceCoefficients(rightSolutionArray, coeffs);
    var cols = coefficientSols.length + 1; // aantal kolommen komt overeen met aantal stoffen uit reactie + 1 extra kolom met "opgave" links
    var rows = 4; // rijen voor: coefficienten, voor, na en tijdens 
    var tableDiv = $("#table");
    $(tableDiv).empty();
    var tableHead = $('<thead>');
    var tableBody = $('<tbody>');
    var trHeader = $('<tr>');
    $('<th></th>').appendTo(trHeader);
    trHeader.appendTo(tableHead);
    for (var i = 0; i < leftSolutionArray.length; i++) {
        $('<th>' + leftSolutionArray[i] + '</th>').appendTo(trHeader);
        trHeader.appendTo(tableHead);
    }
    for (var i = 0; i < rightSolutionArray.length; i++) {
        $('<th>' + rightSolutionArray[i] + '</th>').appendTo(trHeader);
        trHeader.appendTo(tableHead);
    }
    var counter = 0;
    for (var r = 0; r < rows; r++)
    {
        var tr = $('<tr>');
        for (var c = 0; c < cols; c++) {
            if (r === 0 && c === 0) {
                $('<td>Coefficient</td>').appendTo(tr);
                tr.appendTo(tableBody);
            }
            else if (c === 0 && r === 1) {
                $('<td># Mol voor de reactie</td>').appendTo(tr);
                tr.appendTo(tableBody);
            }
            else if (c === 0 && r === 2) {
                $('<td># Mol tijdens de reactie</td>').appendTo(tr);
                tr.appendTo(tableBody);
            }
            else if (c === 0 && r === 3) {
                $('<td># Mol na de reactie</td>').appendTo(tr);
                tr.appendTo(tableBody);
            }
            else {
                if (r === 0) { // rij voor coefficienten
                    var td = $('<td>');
                    var name = "input" + r + c;
                    var input = $('<input name=' + name + '></input>');
                    input.addClass("form-control");
                    input.css("width", "70px");
                    input.css("float", "left");
                    var coefficientOfElement = $(coefficientSols[c - 1]).text();
                    $(input).val(coefficientOfElement);
                    $(input).prop("readonly", true);
                    $(input).appendTo(td);
                    $(td).appendTo(tr);
                    $(tr.appendTo(tableBody));
                }
                else if (r > 0) { // rij voor hoeveelheden mol
                    var td = $('<td>');
                    var name = "input" + r + c;
                    var input = $('<input name=' + name + ' ></input>');
                    input.addClass("form-control");
                    input.css("width", "70px");
                    input.css("float", "left");
                    var amountOfMolForElement = $(amountSols[counter]).text();
                    $(input).val(amountOfMolForElement);
                    $(input).appendTo(td);
                    $(td).appendTo(tr);
                    $(tr.appendTo(tableBody));
                    counter++;
                }
            }
        }
    }
    tableHead.appendTo(tableDiv);
    tableBody.appendTo(tableDiv);
}

function setModuleStatusIcon(isAvailable) {
    var divToAppend = $("#status");
    divToAppend.empty();
    var img;
    if (isAvailable === 'true') {
        img = $('<img style="width: 70%; height: 70%" src="images/klok.png" title="Deze module is beschikbaar voor studenten"/>');
        img.click(makeUnavailable);
    } else {
        img = $('<img style="width: 70%; height: 70%" src="images/klok_inactive.png" title="Deze module is niet beschikbaar voor studenten"/>');
        img.click(makeAvailable);
    }
    img.css('cursor', 'pointer');
    divToAppend.append(img);
}

function makeUnavailable() {
    var dialogContent = $('<div id="dialog-confirm" title="Module sluiten"><p><span class="ui-icon ui-icon-alert" style="float:left; margin: 1em;"></span>Bent u zeker dat u deze module onbeschikbaar wilt maken voor studenten?</p></div>');
    $("#dialog-confirm").remove();//remove if already added
    $(document.body).append(dialogContent);//(re)apend to body to be able to show
    dialogContent.hide();//make invisible
    //show as dialog
    $("#dialog-confirm").dialog({
        resizable: false,
        height: 140,
        width: 400,
        modal: true, //Andere items disabled
        show: {effect: "clip", duration: 200},
        hide: {effect: "drop", duration: 200},
        buttons: {
            "Onbeschikbaar maken": function () {
                confirmedToMakeUnavailable();
                $(this).dialog("close");
            },
            "Annuleren": function () {
                $(this).dialog("close");
            }
        }
    });
}

function makeAvailable() {
    var dialog = $('<div id="dialog-form" title="Module openstellen"><p>Deadline:<input type="hidden" autofocus="autofocus" /><input class="form-control" type="text" id="datepicker"/><span class="ui-icon ui-icon-info"></span>De start datum kunt u instellen in het module overzicht.</p></div>');
    $("#dialog-form").remove();//remove if already added
    $(document.body).append(dialog);//(re)apend to body to be able to show
    dialog.hide();//make invisible
    $("#datepicker").datepicker({
        dateFormat: "d-m-yy",
        buttonImageOnly: true
    });
    //$("#ui-datepicker-div").css("width","100px");
    $("#dialog-form").dialog({
        resizable: false,
        height: 140,
        width: 400,
        modal: true, //Andere items disabled
        show: {effect: "clip", duration: 200},
        hide: {effect: "drop", duration: 200},
        buttons: {
            "Opslaan": function () {
                confirmedToMakeAvailable();
                $(this).dialog("close");
            },
            "Annuleren": function () {
                $(this).dialog("close");
            }
        }
    });

}

function confirmedToMakeUnavailable() {
    $.ajax({
        url: "ChemieboxController",
        async: true,
        type: "GET",
        data: {"action": "changeAvailabilityOfModule", "moduleId": "" + globalModuleId},
        success: function (data) {
            return getQuestionsForModule();//refresh
        },
        error: function (data) {
            console.log("error:", data);
        }
    });
}

function confirmedToMakeAvailable() {
    var date = $("#datepicker").val();
    if (date.length !== 0) {
        $.ajax({
            url: "ChemieboxController",
            async: true,
            type: "GET",
            data: {"action": "changeAvailabilityOfModule", "moduleId": "" + globalModuleId, "deadline": "" + date},
            success: function (data) {
                return getQuestionsForModule();//refresh
            },
            error: function (data) {
                console.log("error:", data);
            }
        });
    }
}