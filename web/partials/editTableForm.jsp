<%-- 
    Document   : normalQuestionForm
    Created on : 22-feb-2015, 12:47:12
    Author     : Fery
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../css/bootstrap.css" media="screen">
        <script src="../js/management.js"></script>  
        <script src="../enrichedElement/js/jquery-2.1.3.min.js" type="text/javascript"></script>
        <script src="../enrichedElement/js/jquery-ui.js" type="text/javascript"></script>
        <script src="../enrichedElement/js/caret.js" type="text/javascript"></script>
        <%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
        <%@taglib prefix="my" tagdir="/WEB-INF/tags" %>
        <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    </head>
    <body>
        <div class="form-group">
            <label for="questiontext" class="col-lg-2 control-label">Vraagstelling (*)</label>
            <div class="col-lg-10" id="questionDiv">
                <p type="text" class="enriched form-control" name="questiontext" id="questiontext" title="De vraag die beantwoord dient te worden"></p>
            </div>
            <a class="btn btn-info" name="action" onclick="generateArrowForQuestiontext()" style="font-size:20px; margin-left: 15px;" >&rarr;</a>
            <a class="btn btn-info" name="action" onclick="generateArrow2ForQuestiontext()" style="font-size:20px; margin-left: 15px;" >&hArr;</a>
        </div>
        <div class="form-group">
            <label for="solution" class="col-lg-2 control-label">Oplossing (*)</label>
            <div class="col-lg-10" id="tableSolutionDiv">
                <label for="solution" class="col-lg-2 control-label">Creëer de reactievergelijking</label>
                <select id="selectArrow" autocomplete="off" onchange= "generateArrowForSolution(this.value)" class="form-control" name="solution" style="margin-left:15px;width:250px">
                    <option id="default" name="aflopend" value="0">Aflopende reactie</option>
                    <option name="evenwicht" value="1">Evenwichtsreactie</option>
                </select> 
                <div id = "TableReaction" class="col-lg-10 reaction">
                    <div id="leftPart" class="leftPart pull-left">
                        <p style="width:auto; float:left;" type="text" class="enriched form-control" name="solutionL" id="solutionL"></p>
                    </div>
                    <div id="arrow" style="float:left; padding-top:10px;">&rarr;</div>
                    <div id="rightPart" class="rightPart pull-left">
                        <p style="float:left; width:auto" type="text" class="enriched form-control" name="solutionR" id="solutionR"></p>
                    </div>
                </div>
            </div>
        </div>
        <a class="col-lg-2 control-label" onclick="generateLeft()" style="cursor:pointer; text-decoration: underline">Voeg links een element toe</a>
        <br><a class="col-lg-2 control-label" onclick="generateRight()" style="cursor:pointer; text-decoration: underline">Voeg rechts een element toe</a><br>
        <p id="errorMessage2" style="color:#c9302c; padding-left:15px;"></p>
        <a class="btn btn-success" name="action" onclick="generateTable()" style="margin-left: 15px;" >Genereer nieuwe tabel</a>
        <div class="bs-component">
            <table id="table" style="cursor: url(images/cursor.png), auto;" class="table table-striped table-hover">
            </table>
        </div>
        <label for="solution" class="col-lg-2 control-label">Procentuele afwijking</label>
        <input name="solutionmarge" id="solutionmarge" step="any" type="number" class="form-control" style="margin-left:15px; width:100px;" />
        <div class="form-group">
            <label for="feedback" class="col-lg-2 control-label">Feedback</label>
            <div class="col-lg-10" id="feedbackDiv">
                <p type="text" class="enriched form-control" name="feedback" id="feedback" title="Waarop moeten de studenten letten"></p>
            </div>
            <a class="btn btn-info" name="action" onclick="generateArrowForFeedback()" style="font-size:20px; margin-left: 15px;" >&rarr;</a>
            <a class="btn btn-info" name="action" onclick="generateArrow2ForFeedback()" style="font-size:20px; margin-left: 15px;" >&hArr;</a>
        </div>
        <div class="form-group">
            <label for="maxpoints" class="col-lg-2 control-label">Maximaal aantal punten mogelijk (*)</label>
            <div class="col-lg-10">
                <select style="float:left; width: 100px;"class="form-control"name="maxpoints">
                    <% for (int i = 1; i <= 20; i++) {%>
                    <option value="<%= i%>"><%= i%></option><% }%></select>       
            </div>
        </div>
        <button class="btn btn-danger" name="action" value="saveQuestion" type="submit">Opslaan</button>
        <p id="errorMessage" style="color:#c9302c"></p>
        <script src="../enrichedElement/js/enrichedElement.js" type="text/javascript"></script>
    </body>
</html>
