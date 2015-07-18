<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.util.List"%>
<%@page import="domain.Months"%>
<!DOCTYPE html>
<html>
    <my:head/>
    <body>
        <my:navBar/>
        <div id="container">
            <% Months months = new Months();
                List<String> maanden = months.getMonths(); %>     
            <div class="row"> 
                <h2 class="pull-left">Module bewerken</h2>
                <a href="ChemieboxController?action=getModuleManagement" class="pull-right"><span><img src="images/back.png"> Annuleren</span></a>
            </div>
            <div class="row">                
                <div class="well bs-component">
                    <form id="moduleForm" class="form-horizontal" onsubmit="return saveModule()" action="ChemieboxController?action=editModule&moduleId=${module.id}" method="POST">
                        <div class="form-group">
                            <label for="chapter" class="col-lg-2 control-label">Hoofdstuk</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control" name="chapter" id="chapter" value="${module.chapter}"  title="Hoofdstuk van de module">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="name" class="col-lg-2 control-label">Modulenaam</label>
                            <div class="col-lg-10">
                                <input class="form-control" name="name" id="name" value="${module.name}" title="Naam van de module">
                            </div> 
                        </div>
                        <div class="form-group">
                            <label for="max" class="col-lg-2 control-label"># pogingen</label>
                            <div class="col-lg-10">
                                <select style="float:left; width: 100px;"class="form-control"name="max" title="Maximum aantal keren dat de student de module mag invullen">
                                    <option value="${module.maxTries}" selected>${module.maxTries}</option>
                                    <% for (int i = 1; i <= 100; i++) {%>
                                        <c:if test="${i != module.maxTries}">
                                        <option value="<%= i%>"><%= i%></option>
                                        </c:if>
                                    <% }%>
                                </select> 
                            </div> 
                        </div>
                        <div class="form-group">
                            <label for="start" class="col-lg-2 control-label">Open vanaf</label>
                            <div class="col-lg-10">
                                <select style="float:left; width: auto;"class="form-control"name="startday" title="Dag van de maand">
                                    <option value="${module.startWrapper.day}" selected>${module.startWrapper.day}</option>
                                    <% for (int i = 1; i <= 31; i++) {%>
                                        <c:if test="${i ne module.startWrapper.day}">
                                        <option value="<%= i%>"><%= i%></option>
                                        </c:if>
                                    <% }%>
                                </select> 
                                <select style="float:left; width: auto; margin-left: 0.75em;" class="form-control"name="startmonth" title="Maand">
                                    <option value="${module.startWrapper.month}" selected><fmt:formatDate pattern="MMMM" value="${module.start.time}" /></option>
                                    <% for (int i = 0; i <= 11; i++) {%>                                    
                                    <c:if test="${i != module.startWrapper.month}">
                                        <option value="<%= i%>"><%= maanden.get(i)%></option>
                                    </c:if>
                                    <% }%>
                                </select> 

                                <select style="float:left; width: auto;  margin-left: 0.75em;" class="form-control"name="startyear" title="Jaar">
                                    <option value="${module.startWrapper.year}" selected>${module.startWrapper.year}</option>
                                    <% for (int j = 2015; j <= 2100; j++) {%>
                                    <c:if test="${j != module.startWrapper.year}">
                                        <option value="<%= j%>"><%= (j)%></option>
                                    </c:if>
                                    <% }%>
                                </select> 
                            </div> 
                        </div>
                        <div class="form-group">
                            <label for="start" class="col-lg-2 control-label">Deadline</label>
                            <div class="col-lg-10 ">
                                <select style="float:left; width: auto;"class="form-control"name="endday" title="Dag van de maand">
                                    <option value="${module.deadlineWrapper.day}" selected>${module.deadlineWrapper.day}</option>
                                    <% for (int i = 1; i <= 31; i++) {%>
                                        <c:if test="${i ne module.deadlineWrapper.day}">
                                        <option value="<%= i%>"><%= i%></option>
                                        </c:if>
                                    <% }%>
                                </select> 
                                
                                <select style="float:left; width: auto; margin-left: 0.75em;" class="form-control"name="endmonth" title="Maand">
                                    <option value="${module.deadlineWrapper.month}" selected><fmt:formatDate pattern="MMMM" value="${module.deadline.time}" /></option>
                                    <% for (int i = 0; i <= 11; i++) {%>                                    
                                    <c:if test="${i != module.deadlineWrapper.month}">
                                        <option value="<%= i%>"><%= maanden.get(i)%></option>
                                    </c:if>
                                    <% }%>
                                </select> 

                                <select style="float:left; width: auto; margin-left: 0.75em;" class="form-control" name="endyear" title="Jaar">
                                    <option value="${module.deadlineWrapper.year}" selected>${module.deadlineWrapper.year}</option>
                                    <% for (int j = 2015; j <= 2100; j++) {%>
                                    <c:if test="${j != module.deadlineWrapper.year}">
                                        <option value="<%= j%>"><%= (j)%></option>
                                    </c:if>
                                    <% }%>
                                </select> 
                            </div> 
                        </div>
                        <button class="btn btn-danger" name="action" value="saveModule" type="submit">Opslaan</button>
                        <p id="errorMessage" style="color:#c9302c">${errorMessage}</p>
                    </form>
                </div>    
            </div>
        </div>
    </body>
        <my:footer/>                    
</html>
