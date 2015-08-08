<%-- 
    Document   : questionView
    Created on : 26-feb-2015, 14:34:22
    Author     : Jasper
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="my" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <my:head />
    <body>
        <my:navBar />
        <div class="container ajaxContainer">
            <h2>
                Module:
                <c:out value="${moduleName}" />
            </h2>
            <div id="endMsg"></div>
            <!-- Module info -->
            <div class="moduleInfo">
                <h4>Info</h4>
                <c:choose>
                    <c:when test="${not empty moduleInfo}">
                        <p>${moduleInfo}</p>
                    </c:when>
                    <c:otherwise>
                        <p id="noInfo" style="font-style: italic;">Geen module info...</p>
                    </c:otherwise>
                </c:choose>
            </div>
            <!-- End of module info -->
            <div id="questionForm">
                <div class="radio well bs-component">
                    <form method="POST" id="question" class="form-horizontal"
                          onsubmit="return getNextQuestion();">
                        <!-- de eerste keer wordt dit ingevuld, de volgende keren wordt dit overschreven-->


                        <legend id="qText">${qText}</legend>

                        <c:forEach begin="1" varStatus="loop" end="${itemsBefore}">
                            <p style="float:left; width:auto" class="enriched form-control">Hier
                                typen</p>
                                <c:choose>
                                    <c:when test="${loop.index != itemsBefore}">
                                    <p style="text-align: center;float:left; padding-top:1em;">+</p>
                                </c:when>
                            </c:choose>
                        </c:forEach>
                        <p style="text-align: center;float:left; padding-top:1em;">
                            <!-- pijl zetten-->
                            <c:choose>
                                <c:when test="${isEventwichtsReactie}">
                                    &hArr;
                                </c:when>
                                <c:otherwise>&rarr;</c:otherwise>
                            </c:choose>
                        </p>
                        <c:forEach begin="1" varStatus="loop" end="${itemsAfter}">
                            <p style="float:left; width:auto" class="enriched form-control">Hier
                                typen</p>
                                <c:choose>
                                    <c:when test="${loop.index != itemsAfter}">
                                    <p style="text-align: center;float:left; padding-top:1em;">+</p>
                                </c:when>
                            </c:choose>
                        </c:forEach>
                        <br> <br> <br>
                        <button type="submit" id="sendButton" class="btn btn-success">Volgende</button>
                    </form>
                </div>
            </div>
            <br>
            <div class="progress progress-striped">
                <div id="testProgress" class="progress-bar progress-bar-success"
                     style="width: <c:out value="${percentage}"/>%;"></div>
            </div>
        </div>
    </div>
    <script type="text/javascript" src="js/dragndrop.js"></script>
    <script src="enrichedElement/js/enrichedElement.js"
    type="text/javascript"></script>
    <script src="js/processQuestions.js" type="text/javascript"></script>
</body>
<my:footer />
</html>
