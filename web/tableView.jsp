<%-- 
    Document   : questionView
    Created on : 25-feb-2015, 17:34:22
    Author     : Jasper
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="my" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
				<table id="table" class="table table-striped table-hover">
					<thead>
						<tr>
							<th>
								<!--empty-->
							</th>
							<c:forEach var="element" items="${leftSide}">
								<th align="center">${element}</th>
							</c:forEach>
							<th align="center"><c:choose>
									<c:when test="${isEventwichtsReactie}">&hArr;</c:when>
									<c:otherwise>&rarr;</c:otherwise>
								</c:choose></th>
							<c:forEach var="element" items="${rightSide}">
								<th align="center">${element}</th>
							</c:forEach>
						</tr>
					</thead>
					<tbody>
						<c:forEach varStatus="rows" begin="0" end="${totalAnswerRows}">
							<tr>
								<c:forEach varStatus="loop" begin="0" end="${columns}">
									<td><c:choose>
											<c:when test="${loop.index == 0}">
												<c:choose>
													<c:when test="${rows.index == 0}">Coëfficient</c:when>
													<c:when test="${rows.index == 1}"># Mol voor de reactie</c:when>
													<c:when test="${rows.index == 2}"># Mol tijdens de reactie</c:when>
													<c:when test="${rows.index == 3}"># Mol na de reactie</c:when>
												</c:choose>
											</c:when>
											<c:when test="${loop.index == arrowIndex}">
												<!-- Nothing-->
											</c:when>
											<c:otherwise>
												<input class="form-control" />
											</c:otherwise>
										</c:choose></td>
								</c:forEach>
							<tr>
						</c:forEach>
					</tbody>
				</table>
				<button type="submit" id="sendButton" class="btn btn-success">Volgende</button>
			</form>
		</div>
		<br>
		<div class="progress progress-striped">
			<div id="testProgress" class="progress-bar progress-bar-success"
				style="width: <c:out value="${percentage}"/>%;"></div>
		</div>
	</div>
	</div>
	<script type="text/javascript" src="js/dragndrop.js"></script>
	<script src="enrichedElement/js/enrichedElement.js"
		type="text/javascript"></script>
	<script src="js/processQuestions.js" type="text/javascript"></script>
</body>
<my:footer />
</html>
