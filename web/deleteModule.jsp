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
            <div class="row"> 
                <h2 class="pull-left">Module ${name} verwijderen</h2>
                <a href="ChemieboxController?action=getModuleManagement" class="pull-right"><span><img src="images/back.png"> Annuleren</span></a>
            </div>
            <div id="row">
                <div class="well bs-component">
                    <form class="form-horizontal" action="ChemieboxController?action=deleteModule" method="POST">
                        <input type="hidden" name="moduleId" value="${id}" readonly/>
                        <div class="row">
                            <div class="col-md-6"><img alt="danger" src="images/warningBig.png"></div>
                            <div class="col-md-6">
                                <p>Bent u zeker dat u de module "${name}" uit hoofdstuk "${chapter}" wilt <strong>verwijderen</strong>?</p>
                                <p>Deze module bevat nog ${numberOfQuestions} vragen.</p>
                                <button type="submit" class="btn btn-danger">Verwijderen</button>
                            </div>
                        </div>
                </div>
                </form>                
            </div>
        </div>
    </body>
</html>
