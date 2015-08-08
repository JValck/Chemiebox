<%-- 
    Document   : createModuleForm
    Created on : 17-feb-2015, 15:49:51
    Author     : Fery
--%>
<%@page import="java.util.List"%>
<%@page import="domain.Months"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">       
    </head>
    <body>
        <% Months months = new Months(); List<String> maanden = months.getMonths(); %>
        <div class="row" style='width:60%'>
            <h2>Creëer module</h2>
            <div class="well bs-component">
                <form id="moduleForm" class="form-horizontal" action="ChemieboxController?action=saveModule" method="POST" onsubmit="return saveModule()">
                  <div class="form-group">
                    <label for="chapter" class="col-lg-2 control-label">Hoofdstuk</label>
                    <div class="col-lg-10">
                      <input type="text" class="form-control" name="chapter" id="chapter" title="Hoofdstuk van de module">
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="name" class="col-lg-2 control-label">Modulenaam</label>
                    <div class="col-lg-10">
                      <input class="form-control" name="name" id="name" title="Naam van de module">
                    </div> 
                  </div>
                  <div class="form-group">
                    <label for="instructions" class="col-lg-2 control-label">Instructies</label>
                    <div class="col-lg-10">
                        <input type="text" class="enriched form-control" name="instructions" id="name" title="Instructies bij het invullen van de bijbehorende vragen"/>                    </div> 
                  </div>
                    <div class="form-group">
                    <label for="max" class="col-lg-2 control-label"># pogingen</label>
                    <div class="col-lg-10">
                        <select style="float:left; width: 100px;"class="form-control"name="max" title="Maximum aantal keren dat de student de module mag invullen">
                        <% for (int i = 1; i <= 100; i++) {%>
                        <option value="<%= i%>"><%= i%></option><% }%></select> 
                    </div> 
                  </div>
                    <div class="form-group">
                    <label for="start" class="col-lg-2 control-label">Open vanaf</label>
                    <div class="col-lg-10">
                        <select style="float:left; width: auto;"class="form-control"name="startday" title="Dag van de maand">
                        <% for (int i = 1; i <= 31; i++) {%>
                        <option value="<%= i%>"><%= i%></option><% }%>
                        </select> 
                    <select style="float:left; width: auto; margin-left: 0.75em;"class="form-control"name="startmonth" title="Maand">
                        <% for (int i = 0; i <= 11; i++) {%>
                        <option value="<%= i%>"><%= maanden.get(i) %></option><% }%>
                    </select> 

                    <select style="float:left; width: auto;  margin-left: 0.75em;" class="form-control"name="startyear" title="Jaar">
                        <% for (int j = 2015; j <= 2100; j++) {%>
                        <option value="<%= j%>"><%= j%></option><% }%></select> 
                    </div> 
                  </div>
                    <div class="form-group">
                    <label for="start" class="col-lg-2 control-label">Deadline</label>
                    <div class="col-lg-10">
                        <select style="float:left; width: auto;"class="form-control"name="endday" title="Dag van de maand">
                        <% for (int i = 1; i <= 31; i++) {%>
                        <option value="<%= i%>"><%= i%></option><% }%></select> 
                    <select style="float:left; width: auto; margin-left: 0.75em; "class="form-control"name="endmonth" title="Maand">
                        <% for (int i = 0; i <= 11; i++) {%>
                        <option value="<%= i%>"><%= maanden.get(i) %></option><% }%>
                    </select> 

                    <select style="float:left; width: auto;  margin-left: 0.75em;" class="form-control"name="endyear" title="Jaar">
                        <% for (int j = 2015; j <= 2100; j++) {%>
                        <option value="<%= j%>"><%= j%></option><% }%></select> 
                    </div> 
                  </div>
                    <button class="btn btn-danger" name="action" value="saveModule" type="submit">Opslaan</button>
                    <p id="errorMessage" style="color:#c9302c"></p>
                </form>
            </div>
          </div>
    </body>
        <my:footer/>                    
</html>
