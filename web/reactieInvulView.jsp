<%-- 
    Document   : questionView
    Created on : 26-feb-2015, 14:34:22
    Author     : Jasper
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
            <h2>Module: <c:out value="${moduleName}"/></h2>
            <div id="endMsg"></div>
            <div id="questionForm">
                <div class="radio well bs-component">
                    <p>Vervolledig onderstaande reactievergelijking. Er zijn voldoende vakjes voorzien.                        
                    </p>
                    <p>Typ de gevraagde oplossing in het vakje. 
        Voor sub- of superscript klik je in het blauwe vakje, dat verschijnt als je op het veldje hebt geklikt, op X<sub>2</sub> voor sub- en X<sup>2</sup> voor superscript.
        Daarna klik je terug in het veldje en typ je het gewenste gatel in.
        Om terug normaal te typen klik je in het blauwe vakje op hetgeen je eerder had gekozen, om het uit te zetten.
        Vervolgens klik je op het veldje waardoor je terug overschakelt naar normale tekst.<br>
        
        Indien je antwoord een kommagetal is, kan je een komma of een punt gebruiken.
        Beiden worden geaccepteerd. Eenheden worden niet herkend.
        Er wordt een foutenmarge voorzien.
    </p>
                    <form method="POST" id="question" class="form-horizontal" onsubmit="return getNextQuestion();"><!-- de eerste keer wordt dit ingevuld, de volgende keren wordt dit overschreven--> 
                        <legend id="qText">${qText}</legend>

                        <c:forEach begin="1" varStatus="loop" end="${itemsBefore}">
                            <p style="display: inline" class="enriched form-control">Hier typen</p>
                            <c:choose>                            
                                <c:when test="${loop.index != itemsBefore}">
                                    +
                                </c:when>
                            </c:choose>
                        </c:forEach>
                        <p style="text-align: center;display: inline;"><!-- pijl zetten-->
                            <c:choose>
                                <c:when test="${isEventwichtsReactie}">
                                    &hArr;
                                </c:when>
                                <c:otherwise>&rarr;</c:otherwise>
                            </c:choose>
                        </p>
                        <c:forEach begin="1" varStatus="loop" end="${itemsAfter}">
                            <p style="display: inline" class="enriched form-control">Hier typen</p>
                            <c:choose>                           
                                <c:when test="${loop.index != itemsAfter}">
                                    +
                                </c:when>
                            </c:choose>
                        </c:forEach><br><br><br>
                        <button type="submit" id="sendButton" class="btn btn-success">Volgende</button>
                    </form> 
            </div>
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
