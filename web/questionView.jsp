<%-- 
    Document   : questionView
    Created on : 16-feb-2015, 14:34:22
    Author     : Fery
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <my:head/>
    <body>
        <my:navBar/>
        <div class="container ajaxContainer">
            <h2>Module: <c:out value="${moduleName} - ${moduleDetailed}"/></h2>
            <div id="endMsg"></div>
            <div id="questionForm">
                <form method="POST" id="question" class="form-horizontal" onsubmit="return getNextQuestion();"><!-- de eerste keer wordt dit ingevuld, de volgende keren wordt dit overschreven--> 
                    <p>Typ de gevraagde oplossing in het vakje. 
        Voor sub- of superscript klik je in het blauwe vakje, dat verschijnt als je op het veldje hebt geklikt, op X<sub>2</sub> voor sub- en X<sup>2</sup> voor superscript.
        Daarna klik je terug in het veldje en typ je het gewenste gatel in.
        Om terug normaal te typen klik je in het blauwe vakje op hetgeen je eerder had gekozen, om het uit te zetten.
        Vervolgens klik je op het veldje waardoor je terug overschakelt naar normale tekst.<br>
        
        Indien je antwoord een kommagetal is, kan je een komma of een punt gebruiken.
        Beiden worden geaccepteerd. Eenheden worden niet herkend.
        Er wordt een foutenmarge voorzien.
    </p>
                    <legend id="qText">${qText}</legend>
                    <p class="enriched form-control">Hier typen</p>
                    <button type="submit" id="sendButton" class="btn btn-success">Volgende</button>
                </form>               
            </div>
            <br>
            <div class="progress progress-striped">
                <div id="testProgress" class="progress-bar progress-bar-success" style="width: <c:out value="${percentage}"/>%;"></div>
            </div>
        </div>
    </div>
    <script type="text/javascript" src="js/dragndrop.js"></script>
    <script src="enrichedElement/js/enrichedElement.js" type="text/javascript"></script> 
    <script src="js/processQuestions.js" type="text/javascript"></script>
</body>
    <my:footer/>                    
</html>
