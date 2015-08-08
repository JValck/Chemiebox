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
				<div class="itemsToDrag">
					<c:forEach var="item" items="${items}">
						<p>${item}</p>
					</c:forEach>
				</div>
				<br> <strong>Oplossing:</strong> <br>
				<div class="solutionZone">
					<c:forEach begin="1" end="${parts}" varStatus="loop">
						<!-- before arrow -->
						<p class="dropZone" style="background-color: #D9F591;">.</p>
						<p class="dropZone">...</p>
						<c:choose>
							<c:when test="${loop.index != parts}">
								<p>+</p>
							</c:when>
						</c:choose>
					</c:forEach>
					<c:choose>
						<c:when test="${evenwicht}">
							<p>&hArr;</p>
						</c:when>
						<c:otherwise>
							<p>&rarr;</p>
						</c:otherwise>
					</c:choose>
					<c:forEach begin="1" end="${parts}" varStatus="loop">
						<!-- after arrow -->
						<p class="dropZone" style="background-color: #D9F591;">.</p>
						<p class="dropZone">...</p>
						<c:choose>
							<c:when test="${loop.index != parts}">
								<p>+</p>
							</c:when>
						</c:choose>
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
