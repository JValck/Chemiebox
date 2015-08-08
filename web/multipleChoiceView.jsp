<%@taglib prefix="my" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<my:head />
<body>
	<my:navBar />
	<div class="container ajaxContainer">
		<h2>
			Module:
			<c:out value="${moduleName}" />
		</h2>
		<div id="endMsg"></div>
		<!-- Module info -->
		<div class="moduleInfo">
			<h4>Info</h4>
			<c:choose>
				<c:when test="${not empty moduleInfo}">
					<p>${moduleInfo}</p>
				</c:when>
				<c:otherwise>
					<p id="noInfo" style="font-style: italic;">Geen module info...</p>
				</c:otherwise>
			</c:choose>
		</div>
		<!-- End of module info -->

		<div id="questionForm">
			<form method="POST" id="question" class="form-horizontal"
				onsubmit="return getNextQuestion();">
				<!-- de eerste keer wordt dit ingevuld, de volgende keren wordt dit overschreven-->

				<legend id="qText">${qText}</legend>
				<div style="padding-left: 3em;" class="radio well bs-component">
					<c:forEach items="${options}" var="o">
						<input type="radio" name="option" value="${o}">${o}<br>
					</c:forEach>
				</div>
				<br>
				<button type="submit" id="sendButton" class="btn btn-success">Volgende</button>
			</form>
		</div>
		<br>
		<div class="progress progress-striped">
			<div id="testProgress" class="progress-bar progress-bar-success"
				style="width: <c:out value="${percentage}"/>%;"></div>
		</div>
	</div>
	<script type="text/javascript" src="js/dragndrop.js"></script>
	<script src="enrichedElement/js/enrichedElement.js"
		type="text/javascript"></script>
	<script src="js/processQuestions.js" type="text/javascript"></script>
</body>
<my:footer />
</html>
