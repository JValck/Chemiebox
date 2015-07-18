<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <a href="ChemieboxController?action=startTest" class="navbar-brand">Chemie-box</a>
            <button class="navbar-toggle" type="button" data-toggle="collapse" data-target="#navbar-main">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>
        <% if (session.getAttribute("userId") != null) { %>
        <div class="navbar-collapse collapse" id="navbar-main">
            <ul class="nav navbar-nav">
                <li>
                    <a href="ChemieboxController?action=startTest">Start</a>
                </li>
                <% if (session.getAttribute("level") !=null) { %>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Admin <span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="ChemieboxController?action=getQuestionsManagement">Vragen</a></li>
                        <li><a href="ChemieboxController?action=getModuleManagement">Modules</a></li>
                        <li class="divider"></li>
                        <li><a href="ChemieboxController?action=getStudentManagement">Studenten</a></li>
                    </ul>
                </li>
                <% }%>
            </ul>

            <ul class="nav navbar-nav navbar-right">
                <li><a href="ChemieboxController?action=startTest">Ingelogd als ${userId}</a></li>
                <li><a href="ChemieboxController?action=logout" >Log out</a></li>
            </ul>

        </div>
        <% }%>
    </div>
</div>