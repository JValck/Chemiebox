var serverPostAnswer;
$("#question").submit(function(event) {
    event.preventDefault();
});
$(".sendButton").click(function(event) {
    event.preventDefault();
});

function getNextQuestion() {
    clearBr_Moz();// breaks na spatie eruithalen
    var merged = fetchData();
    merged = fixComma(merged);
    submitCurrent(merged);
    console.log(merged);
    return false;//else it would refresh
}

function fetchData() {
    var merged = [];
    //call values enrichedElement
    var enrichedData = sendInfo();
    if ($("#question").has("table").length <= 0) {// no table
        //fetch normal data
        var normalContent = getNormalContent();
    } else {//has tabme
        var normalContent = fetchTableContent();
    }
    //merge arrays
    merged = enrichedData.concat(normalContent);
    //fetch drag and drop
    if (typeof (sendDragAndDropInfo) == "function") {//is defined
        var dragAndDrop = sendDragAndDropInfo();
        merged = (typeof merged != 'undefined' && merged instanceof Array) ? merged : [];//if no enriched or multiple choice
        merged.push(dragAndDrop);
    }
    merged = correctedArray(merged);
    if (merged.length > 1) {
        merged = JSON.stringify(merged);
        merged = merged.replace(/\s+/g,"");
    }
    //console.log(merged);
    return merged;
}
function fixComma(answerAsArray){
	for(var idx = 0; idx < answerAsArray.length; idx++){
		answerAsArray[idx] = answerAsArray[idx].replace(new RegExp(",","g"), ".");
	}
	return answerAsArray;
}
function correctedArray(merged) {
    var correct = [];
    for (var i = 0; i < merged.length; i++) {
        if (merged[i] !== undefined && merged[i].length !== 0) {
            correct.push(merged[i].trim());
        }
    }
    return correct;
}
function fetchTableContent() {
    var teller = 1;
    var elements = $("#table thead").find("th").length - 2;
    var answer = "<tablesolution><solutions>";
    $('#question :input').not(':input[type=button], :input[type=submit], :input[type=reset]').each(function() {//alle input zonder submit
        var value = $(this).val();
        if (typeof (value) !== 'undefined') {
            if (teller <= elements) {
                answer += "<coef>" + value.replace(/ /g,'') + "</coef>";
            } else {
                answer += "<amount>" + value.replace(/ /g,'') + "</amount>";
            }
            teller++;
        }
    });
    answer += "</solutions></tablesolution>";
    return answer;
}
function getNormalContent() {
    var inputArray = new Array();
    //try search for checked elements (multiple choice)
    var value = $('input:checked').val();
    if (typeof (value) !== 'undefined' && value.length !== 0) {
        inputArray.push(value);
        return inputArray;//return
    }
    inputArray.push($('input:checked').val());
    $("#question :input:not(:radio):not(:checkbox)").each(function() {
        value = $(this).val();
        if (typeof (value) !== 'undefined' && value.length !== 0) {
            inputArray.push(value);
        }
    });
    return inputArray;
}

function submitCurrent(merged) {
    clearBr_Moz();// breaks na spatie eruithalen
    $.ajax({
        url: "ChemieboxController",
        async: true,
        cache: false,
        type: "POST",
        data: {"action": "next", "answer": "" + merged},
        success: function(data) {//update DOM
            var text = ($(data).find("questionText").text());
            var progress = $(data).find("progress").text();
            var strategy = $(data).find("strategy").text().trim();
            if (progress > 100) {
                $("#questionForm").empty();
                $("<h4 style='color:#29ABE0'>Alle vragen van deze module zijn beantwoord</h4>").appendTo("#endMsg");
                showFeedback($("#endMsg"), data);
                $("<a href=\"ChemieboxController?action=startTest\" class=\"btn btn-primary\">Terug naar het overzicht</a>").appendTo("#endMsg");
                //document.getElementById("sendButton").style.visibility = "hidden";
            }
            else {
                clearEnrichedIfExist();
                $("#questionForm").empty();
                switch (strategy) {
                    case "Niet-redox":
                        loadNietRedox(data);
                        break;
                    case "Drag n Drop":
                        loadDragNDrop(data);
                        break;
                    case "Meerkeuze":
                        loadMultiple(data);
                        break;
                    case "Tabel":
                        loadTable(data);
                        break;
                    default:
                        loadNormal(data);
                }
                $("#testProgress").css("width", progress + "%");
            }
        }
        ,
        error: function(request, status, error) {
            console.log("error:");
            console.log("   Request: ", request);
            console.log("   Status: " + status);
            console.log("   Error: " + error);
        }
    });
}
function createAndAddSubmitButton() {
    $('#question').append('<button type="submit" class="btn btn-success">Volgende</button>');
}
function clearEnrichedIfExist() {
    var form = $("#question");
    var children = form.children().each(function() {
        var className = $(this).attr('class');
        if (/enriched/i.test(className)) {
            $(this).html("");
        }

    });
}
function loadDragNDrop(data) {
    serverPostAnswer = data;
    $("#questionForm").load('partials/partialDragAndDrop.jsp', function() {
        dragAndDropComplete();
    });
}

function loadNietRedox(data) {
    serverPostAnswer = data;
    $("#questionForm").load('partials/partialReactieInvulView.jsp', function() {
        nietRedoxComplete();
    });
}

function nietRedoxComplete() {
    var divBefore = $('<div style="display: inline" ></div>');
    var left = parseInt($(serverPostAnswer).find("left").text());
    var right = parseInt($(serverPostAnswer).find("right").text());
    for (var i = 1; i <= left; i++) {
        var enriched = $('<p style="float: left; width:auto" class="enriched form-control">Hier typen</p>');
        divBefore.append(enriched);
        if (i !== left)
            $('<p style="float:left; padding-top: 1em;">+</p>').insertAfter(enriched);

    }
    $(divBefore).insertAfter("#qText");
    var divAfter = $('<div style="display: inline" ></div>');
    for (var i = 1; i <= right; i++) {
        var enriched = $('<p style="float: left; width:auto" class="enriched form-control">Hier typen</p>');
        divAfter.append(enriched);
        if (i !== right)
            $('<p style="float:left; padding-top: 1em;">+</p>').insertAfter(enriched);
    }
    $(divAfter).insertAfter("#arrowSymbol");
    $("#qText").html($(serverPostAnswer).find("questionText").html());
    //update arrow if needed
    if($(serverPostAnswer).find("isEvenwicht").text() == 'true'){
        $('#arrowSymbol').html('&hArr;');
    }
    refreshEnriched();
}

function dragAndDropComplete() {
    clearPreviousDragAndDrop();
    var itemsToDragDiv = $(".itemsToDrag");
    $(serverPostAnswer).find("item").each(function() {
        var crnt = $("<p style=\"margin-left=0.5em\">" + $(this).html() + "</p>");
        crnt.css('margin-left', '0.3em');
        itemsToDragDiv.append(crnt);
    });
    var before = $("#before");
    var after = $("#after");
    var times = parseInt($(serverPostAnswer).find("parts").text());
    for (var i = 0; i < times; i++) {
        before.append("<p class=\"dropZone\" style=\"background-color: #D9F591;\">.</p>" +
                "<p class=\"dropZone\">...</p>");
        after.append("<p class=\"dropZone\" style=\"background-color: #D9F591;\">.</p>" +
                "<p class=\"dropZone\">...</p>");
        if (i !== times - 1) {
            before.append("<p style=\"display: inline-block;\">+</p>");
            after.append("<p>+</p>");
        }
    }
    //update arrow if needed
    if($(serverPostAnswer).find("isEvenwicht").text() == 'true'){
        $('#arrowSymbol').html('&hArr;');
    }
    $("#qText").html($(serverPostAnswer).find("questionText").html());
    //recall drag and drop script
    refreshDragNDrop();
    serverPostAnswer = "";//clear answer
}

function clearPreviousDragAndDrop() {
    $("#before").empty();
    $("#after").empty();
    $(".itemsToDrag").empty();
}


function createAndAddSubmitButton() {
    $('#question').append('<button type="submit" class="btn btn-success">Volgende</button>');
}
function clearEnrichedIfExist() {
    var form = $("#question");
    var children = form.children().each(function() {
        var className = $(this).attr('class');
        if (/enriched/i.test(className)) {
            $(this).html("");
        }
    });
    //hide the helper
    hideHelpBar();//in enrichedElement script
}
function loadNormal(dataFromPost) {
    serverPostAnswer = dataFromPost,
            $("#questionForm").load('partials/questionView.jsp', function() {
        normalComplete();
    });
}

function normalComplete() {
    refreshEnriched();
    $("#qText").html($(serverPostAnswer).find("questionText").html());
}

function loadMultiple(dataFromPost) {
    serverPostAnswer = dataFromPost,
            $("#questionForm").load('partials/partialMultipleChoiceView.jsp', function() {
        multipleComplete();
    });
}

function multipleComplete() {
    var optionsHTML = $(".options");
    $(serverPostAnswer).find("options").children().each(function() {
        var option = $("<input type='radio' name='option'>" + $(this).text() + "<br>");
        option.attr("value", $(this).text());
        optionsHTML.append(option);

    });
    $("#qText").html($(serverPostAnswer).find("questionText").html());
    refreshEnriched();
}

function loadTable(dataFromPost) {
    serverPostAnswer = dataFromPost,
            $("#questionForm").load('partials/partialTableView.jsp', function() {
        tableComplete();
    });
}

function tableComplete() {
    var table = $('<table id="table" class="table table-striped table-hover"></table>');
    var thead = $('<thead></thead>');
    var row = $('<tr></tr>');
    var empty = $('<th></th>');
    row.append(empty);
    $(serverPostAnswer).find("leftElement").each(function() {
        var cell = $('<th></th>');
        cell.html($(this).html());
        row.append(cell);
    });
    var arrow = $('<th>');
    if($(serverPostAnswer).find("isEvenwicht").text() == 'true'){
        arrow.html('&hArr;');
    }else{
        arrow.html('&#8594;')
    }
    row.append(arrow);
    $(serverPostAnswer).find("rightElement").each(function() {
        var cell = $('<th></th>');
        cell.html($(this).html());
        row.append(cell);
    });
    thead.append(row);
    table.append(thead);//--end of head
    var tbody = $('<tbody></tbody>');
    for (var i = 0; i < 4; i++) {
        var bodyRow = $('<tr></tr>');
        var beschrijving = $('<td></td>');
        switch (i) {
            case 0:
                beschrijving.html("Coëfficient");
                break;
            case 1:
                beschrijving.html("# Mol voor de reactie");
                break;
            case 2:
                beschrijving.html("# Mol tijdens de reactie");
                break;
            case 3:
                beschrijving.html("# Mol na de reactie");
                break;
        }
        bodyRow.append(beschrijving);
        var colTeller = 0;
        var columns = $(serverPostAnswer).find("columns").text();
        for (var colTeller = 0; colTeller < columns; colTeller++) {
            var inputCell = $('<td></td>');
            if ($(serverPostAnswer).find("leftElement").length - 1 === colTeller - 1) {
                inputCell.append('<p></p>');
            }
            else {
                //inputCell.append('<input class="form-control" step="any" type="number"/>');
                inputCell.append('<input class="form-control"/>');
            }
            bodyRow.append(inputCell);
        }
        tbody.append(bodyRow);
    }
    table.append(tbody);
    refreshEnriched();
    $("#qText").html($(serverPostAnswer).find("questionText").html());
    $(table).insertAfter("#qText");
}


function getExcel() {
    //getting data from our div that contains the HTML table
    var data_type = 'data:application/vnd.ms-excel';
    var table = document.createElement("table");
    var d = document.getElementsByTagName("tr")[1];
    var j = 1;
    var hoofd = document.createElement("tr");
    var h1 = document.createElement("td");
    var h2 = document.createElement("td");
    var h3 = document.createElement("td");
    var h4 = document.createElement("td");
    var h5 = document.createElement("td");
    var h6 = document.createElement("td");
    var h7 = document.createElement("td");
    var h8 = document.createElement("td");
    h1.appendChild(document.createTextNode("Naam"));
    h2.appendChild(document.createTextNode("Is correct"));
    h3.appendChild(document.createTextNode("Hoofdstuk"));
    h4.appendChild(document.createTextNode("Vraag"));
    h5.appendChild(document.createTextNode("Punten"));
    h6.appendChild(document.createTextNode("Max"));
    h7.appendChild(document.createTextNode("Antwoord"));
    h8.appendChild(document.createTextNode("Keer geprobeerd"));

    hoofd.appendChild(h1);
    hoofd.appendChild(h2);
    hoofd.appendChild(h3);
    hoofd.appendChild(h4);
    hoofd.appendChild(h5);
    hoofd.appendChild(h6);
    hoofd.appendChild(h7);
    hoofd.appendChild(h8);

    table.appendChild(hoofd);
    while (d != null) {
        var tread = document.createElement("tr");
        var i = 0;
        while (d.getElementsByTagName("td")[i] != null) {
            var c1 = document.createElement("td");
            var r = d.getElementsByTagName("td")[i].cloneNode(true);
            if (i == 1) {
                if (r.innerHTML.indexOf("good") > -1) {
                    r.innerHTML = "correct";
                }
                else {
                    r.innerHTML = "onjuist";
                }
            }
            if (i == 4) {
                var behaald = r.innerHTML.split("/");
                var r2 = r.cloneNode(true);
                r2.innerHTML = behaald[0];
                tread.appendChild(r2);
                r.innerHTML = behaald[1]
            }
            if (i == 5) {
                var stringC = r.innerHTML;
                var result = "";
                for (var aSeeker = 0; aSeeker < stringC.length; aSeeker++) {
                    if (stringC.charCodeAt(aSeeker) == 8594) {

                        result = result + "->";
                    }
                    else {
                        result = result + stringC.charAt(aSeeker);
                    }
                }
                r.innerHTML = result;
            }
            tread.appendChild(r);
            i++;
        }
        table.appendChild(tread);
        j++;
        d = document.getElementsByTagName("tr")[j];
    }
    var table_html = table.outerHTML.replace(/ /g, '%20');


    var ua = window.navigator.userAgent;

    var msie = ua.indexOf("MSIE ");
    var trident = ua.indexOf('Trident/');
    var edge = ua.indexOf('Edge/');

    if (msie > 0 || trident > 0 || edge > 0) {
        window.alert("Het bestand kan niet worden gedownload in deze browser. \n Gelieve een andere browser te gebruiken.");

    }
    else {
        window.open(data_type + ',' + table_html);
    }
}

function getFilteredExcel(){
       $.ajax({
        url: "ChemieboxController", 
        async: true,
        type: "GET",
        data: {"action": "getFilteredExcel"},
        success: function(data) {
            return openExcel(data);
        },
        error: function(data) {
            console.log("error:", data);
        }
    });
    return false;
}

function openExcel(data){
    var data_type = 'data:application/vnd.ms-excel';
    var table = document.createElement('table');
    var tbody = document.createElement('tbody');
    var thead = document.createElement('thead');
    var headrow = $('<tr></tr>');
    var th = document.createElement('th');
    th.innerHTML = "Student";
    var th2 = document.createElement('th');
    th2.innerHTML = "Module";
    var th3 = document.createElement('th');
    th3.innerHTML = "Resultaat";
    headrow.append(th);
    headrow.append(th2);
    headrow.append(th3);
    $(headrow).appendTo(thead);
    
    
    var results = data.getElementsByTagName('tr');
    var tbody = $('<tbody></tbody>');
        for (var i = 0;  i < results.length; i++) {
            var row = $("<tr></tr>");
            var studnr = (results[i]).getElementsByTagName('studnr')[0].innerHTML;
            var module = (results[i]).getElementsByTagName('module')[0].innerHTML;
            var points = (results[i]).getElementsByTagName('points')[0].innerHTML;
            var maxpoints = (results[i]).getElementsByTagName('maxpoints')[0].innerHTML;
            var result = points + "\\" + maxpoints;
            var column1 = document.createElement('td');
            var column2 = document.createElement('td');
            var column3 = document.createElement('td');
            column1.innerHTML = studnr;
            column2.innerHTML = module;
            column3.innerHTML = result;
            row.append(column1);
            row.append(column2);
            row.append(column3);
            tbody.append(row);
        }
    $(thead).appendTo(table);
    $(tbody).appendTo(table);
    var table_html = table.outerHTML.replace(/ /g, '%20');
    window.open(data_type + ',' + table_html);
      
}

function showFeedback(divToAppend, data) {
    var table = $('<table></table>').addClass('table table-striped table-hover');
    table.append('<thead><tr><th>Vraag</th><th>Jouw antwoord</th><th>Feedback</th><th>Punten verdiend</th></tr></thead><tbody>');
    $(data).find("moduleFeedback").children().each(function() {
        var vraag = $(this).find("question").html();
        var feedback = $(this).find("feedback").html();
        var antwoord = $(this).find("studentanswer").html();
        var score = $(this).find("score").html();
        var maxScoreForQuestion = $(this).find("scoreForQuestion").html();
        var scoreView = score + "/" + maxScoreForQuestion;
        var row = $('<tr></tr>');
        var qCell = $('<td></td>').html(vraag);
        row.append(qCell);
        if (antwoord === "Hier typen" || antwoord === "" || antwoord == null) {
            var fbCell = $('<td></td>').html("(Blanco)");
        }
        else {
            var fbCell = $('<td></td>').html(antwoord);
        }
        if (score > 0) {
            fbCell.css("color", "#93C54B");
        }
        else {
            fbCell.css("color", "#C9302C");
        }
        row.append(fbCell);
        var fbCell = $('<td></td>').html(feedback);
        row.append(fbCell);
        var fbCell = $('<td></td>').html(scoreView);
        row.append(fbCell);
        table.append(row);
    });
    var scoreAchieved = $(data).find("scoreAchieved").html();
    var totalScore = $(data).find("totalScore").html();
    table.append("</tbody>");
    var totalScoreParagraph = $('<p style="float:right">Je behaalde een resultaat van <b style="font-size:17px;">' + scoreAchieved + ' </b> op <b style="font-size:17px;">' + totalScore + '</b> </p>');
    divToAppend.append(table);
    divToAppend.append(totalScoreParagraph);

}



