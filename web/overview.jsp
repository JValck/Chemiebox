<%-- 
    Document   : index
    Created on : 9-feb-2015, 14:26:09
--%>
<%@taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <my:head/>
    <body>
        <my:navBar/>
        <p id="Error">${error}</p>
        <div class="container">
            <div class="row">
                <div class="col-lg-6">
                    <h2>Opgaven & testen</h2>
                    <div class="well bs-component">
                        <form class="form-horizontal" method="GET" action="ChemieboxController">
                            <div class="form-group">                      
                                <c:forEach items="${modules}" var="current">
                                    <div style="cursor: pointer" class="testcategory" style="text-align: left;">
                                        <jsp:useBean id="today" class="java.util.Date" scope="page" />
                                        <c:choose>
                                            <c:when test="${today.after(current.deadline.time) == true}">
                                                    <p>
                                                    <span class="supercategory"><c:out value="${current.chapter}"/></span>
                                                    <span class="subcategory"><c:out value="${current.name}"/></span><br/>
                                                    <span class="subcategory">Deadline verlopen</span>  
                                                    </p>
                                                </c:when>
                                            
                                            
                                            <c:when test="${today.after(current.start.time) == true}">
                                                    <a href="ChemieboxController?action=startModule&moduleId=${current.id}" class="categoryblock">
                                                    <span class="supercategory"><c:out value="${current.chapter}"/></span>
                                                    <span class="subcategory"><c:out value="${current.name}"/></span><br/>
                                                    <span class="subcategory">Beschikbaar sinds <fmt:formatDate type="date" dateStyle="long" value="${current.start.time}" /></span>  
                                                    </a>
                                            </c:when>
                                            
                                            <c:otherwise>
                                                
                                                    <p>
                                                    <span class="supercategory"><c:out value="${current.chapter}"/></span>
                                                    <span class="subcategory"><c:out value="${current.name}"/></span><br/>
                                                    <span class="subcategory">Beschikbaar vanaf <fmt:formatDate type="date" dateStyle="long" value="${current.start.time}" /></span><br/>  
                                                    <span class="subcategory">Nog niet beschikbaar</span>
                                                    </p>
                                                    
                                            </c:otherwise>
                                        </c:choose>
                                        
                                        
                                        
                                    </div>
                                    <hr>
                                </c:forEach>
                                
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>  
        <my:footer/>                     
    </body>
</html>
