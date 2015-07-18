
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="../css/bootstrap.css" media="screen">
        <script src="../js/management.js"></script>  
        <script src="../enrichedElement/js/jquery-2.1.3.min.js" type="text/javascript"></script>
        <script src="../enrichedElement/js/jquery-ui.js" type="text/javascript"></script>
        <%@page contentType="text/html" pageEncoding="UTF-8"%>
        <%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
        <%@taglib prefix="my" tagdir="/WEB-INF/tags" %>
        <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
        <script>
            function disableF5(e) { if ((e.which || e.keyCode) == 116) e.preventDefault(); };
            $(document).bind("keydown", disableF5);
            $(document).on("keydown", disableF5);
            
            
        </script>
    </head>
    <body onload="getCreateQuestionFormCallback();
          ">
        <div id="container">
            <div class="row" id="rowForForm" style="width:auto"> 
                <h3 class="titelForCreate" id="chapterOfModule"></h3><h4 class= "titelForCreate" id="nameOfModule"></h4>
                <div class="well bs-component">
                    <p id="errorMessage3" style="color:#c9302c"></p>
                    <form id="questionForm" class="form-horizontal" onsubmit="return getInputsForQuestion()" method="POST">
                        <div class="form-group">
                            <label for="strategy" class="col-lg-2 control-label">Type vraagstelling (*)</label>
                            <div class="col-lg-10">
                                <select autocomplete="off" onchange= "loadFormForStrategy()" id="strategySelect" style="float:left; width: 200px;"class="form-control"name="strategy">
                                    <option id="default" name="default" value="0" disabled selected>Kies een type vraag</option>
                                </select> 
                            </div>
                        </div>
                        <div class="questionFormForStrategy">

                        </div>   

                    </form>
                </div>    
            </div>
            <input id ="idOfModule" style="visibility:hidden"/>
        </div>
    </body>
</html>
