<%-- 
    Document   : moduleManagement
    Created on : 18-feb-2015, 10:35:35
    Author     : r0430844
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <my:head/>
    <body>
    <my:navBar/>
    <% int idx = 1; %>    
    <div class="container">
        <table style="cursor: url(images/cursor.png), auto;" class="table table-striped">
            <thead>
                <tr>
                    <th><!--Number --></th>
                    <th>Hoofdstuk</th>
                    <th>Naam</th>
                    <th>Max. aantal invullen</th>
                    <th>Beschikbaar vanaf</th>
                    <th>Deadline</th>         
                    <th>Bewerk</th>
                    <th>Verwijder</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="module" items="${modules}">
                    <tr>
                        <td><%= idx %></td>
                        <td><c:out value="${module.chapter}"/></td>
                        <td><c:out value="${module.name}"/></td>
                        <td><c:out value="${module.maxTries}"/></td>
                        <td><fmt:formatDate type="date" dateStyle="long" value="${module.start.time}" /></td>
                        <td><fmt:formatDate type="date" dateStyle="long" value="${module.deadline.time}" /></td>
                        <td><a href="ChemieboxController?action=editModule&moduleId=${module.id}"><img style="cursor: pointer" src="images/edit.png" alt="bewerk"/></a></td>
                        <td><a href="ChemieboxController?action=deleteModule&moduleId=${module.id}"><img style="cursor: pointer" src="images/delete.png" alt="verwijder"/></a></td>
                    </tr>
                    <% idx++;%>
                </c:forEach>
            </tbody>
        </table>
    </div>
    </body>
        <my:footer/>                    
</html>
