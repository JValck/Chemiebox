<%-- 
    Document   : index
    Created on : 9-feb-2015, 14:26:09
    Author     : r0431118
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE html>
<html>
    <my:head/>
    <my:navBar/>
    <div class="container">
        <div class="row">
            <div class="col-lg-6">
                <select autocomplete="off" id="moduleDropDown" onchange="getQuestionsForModule()" class="form-control" name="module">
                    <option id="default" name="default" value="0" disabled selected>Kies een module</option>
                    <c:forEach var="module" items="${modules}" varStatus="loopCounter">
                        <option id="${module.id}" name="moduleId" value="${module.id}">${module.chapter} - ${module.name}</option>
                    </c:forEach>
                    <option id="create" style="color:#29abe0; font-style: italic" name="createModule" value="">Creëer een nieuwe module</option>
                </select>
            </div>            
        </div>

        <div class="choice" id="choice">
            
        </div>
        <div id="loading"> </div>
    </div>           
    <my:footer/>                    
</body>
</html>
