<%-- 
    Document   : index
    Created on : 9-feb-2015, 14:26:09
    Author     : r0431118
--%>
<%@taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
    <my:head/>
    <body>
        <my:navBar/>
        <div class="container">
            <% if (session.getAttribute("userId") == null) { %>
            <div class="row">
                <div class="col-lg-6">
                    <h2>Log in</h2>
                    <div class="well bs-component">
                        <form class="form-horizontal" method="POST" action="ChemieboxController">
                            <div class="form-group">
                                <label for="inputStudnr" class="col-lg-2 control-label">Studentennr.</label>
                                <div class="col-lg-10">
                                    <input type="text" class="form-control" name="inputStudnr" id="inputStudnr" placeholder="studentennr.">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="inputPassword" class="col-lg-2 control-label">Wachtwoord</label>
                                <div class="col-lg-10">
                                    <input type="password" class="form-control" name="inputPassword" id="inputPassword" placeholder="********">
                                </div> 
                            </div>
                            <button class="btn btn-danger" name="action" value="login" type="submit">Log in</button>
                            <p style="color:#c9302c">${errorMessage}</p>
                        </form>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-6">
                    <div class="well bs-component">
                        <p>Log in met je UCLL studentennummer en wachtwoord.</p>
                    </div>
                </div>
            </div>
            <% }else{%> <jsp:forward page="/ChemieboxController" /> <%}%>
        </div>        
        <!-- detect browser -->
        <script type="text/javascript">
            var isChrome = false;
            var isFirefox = false;
            var agent = navigator.userAgent;

            var firefoxRegex = new RegExp("Firefox");
            isNotFirefox = !firefoxRegex.test(agent);
            var isNotChrome = (typeof (window.chrome) == 'undefined');
            if (isNotChrome && isNotFirefox) {
                var w = 400; var h = 400; 
                var left = (screen.width / 2) - (w / 2);
                var top = (screen.height / 2) - (h / 2);
                window.open("browserMessage.html", "Verander van browser...", 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width=' + w + ', height=' + h + ', top=' + top + ', left=' + left);
            }
        </script>
    </body>
            <my:footer/>                    
</html>
