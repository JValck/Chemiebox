<%-- 
    Document   : studentQuestionOverview
    Created on : 25-feb-2015, 10:37:04
    Author     : r0430844
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <my:head/>
    <body>
        <my:navBar/>
        <div class="container" >
            <h1>Overzicht studenten</h1>
            <b style='float:right'>Resultaten per module: <button id="excel" class="btn btn-sm btn-success" onclick=" getFilteredExcel();">Download Excel</button></b><br><br>
            <b style='float:right'>Resultaten per vraag: <button id="excel" class="btn btn-sm btn-success" onclick="getExcel();">Download Excel</button></b>
            <br><br><br>
            <table style="cursor: url(images/cursor.png), auto;" class="table table-striped" id="tableToExcel">
                <thead>
                    <tr>
                    <th>Student</th>
                    <th>Correct</th>
                    <th>Module</th>
                    <th>Vraag</th>                    
                    <th>Behaald punt</th>
                    <th>Laatste antwoord</th>
                    <th>Aantal pogingen</th>
                    </tr>                        
                </thead>
                <tbody>
                    <c:forEach var="student" items="${students}">
                        <tr>
                            <td style="mso-number-format:\@">${student.studentNuber}</td>
                            <td align="center">
                                <c:choose>
                                    <c:when test="${student.correct == true}">
                                        <img src="images/good.png" alt="correct">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="images/bad.png" alt="correct">
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td style="mso-number-format:\@">${student.module}</td>
                            <td style="mso-number-format:\@">${student.questionText}</td>                            
                            <td style="mso-number-format:General">${student.points}</td>
                            <td style="mso-number-format:\@">${student.lastanswer}</td>
                            <td style="mso-number-format:General">${student.tries}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>              
        </div>   
        <script src="js/processQuestions.js" type="text/javascript"></script>
        <p style="padding-bottom: 1em;">Laatste software update: 26/11/2015 - 16h55</p>
    </body>
        <my:footer/>                    
</html>
