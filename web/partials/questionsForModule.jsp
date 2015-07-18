<%-- 
    Document   : questionsForModule
    Created on : 18-feb-2015, 10:33:17
    Author     : Fery
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
     <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
     
        <div class="bs-docs-section">
            <p id="moduleId"></p>
            <button onclick="getCreateQuestionForm()" id="createQuestionBtn" style="visibility:hidden;" class="btn btn-danger" name="action" type="submit">Creëer vraag</button>
            <div class="pull-right" id="status"></div>
            <br>
                    <div class="bs-component">
                        <table id="table" style="cursor: url(images/cursor.png), auto;" class="table table-striped table-hover ">
                            <thead>
                                <tr>
                                    <th>Vraagstelling</th>
                                    <th>Oplossing</th>
                                    <th>Strategie</th>
                                    <th>Feedback</th>
                                    <th>Punten te behalen</th>
                                    <th>Bewerk</th>
                                    <th>Verwijder</th>
                                </tr>
                            </thead>
                            <tbody id="tableBody">
                            </tbody>
                        </table> 
                    </div>
                </div>
    </body>
</html>
