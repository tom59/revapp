<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

    <title>Internal Analysis</title>
</head>
<body>
    <h1>Internal Analysis</h1>

	<table width="100%">
		<tr><td>
			<a href="internalanalysis?page=${analysisResult.page - 1}">${analysisResult.page - 1}</a>
		</td>
		<td>
			<a href="internalanalysis?page=${analysisResult.page + 1}">${analysisResult.page + 1}</a>
		</td></tr>
	</table>

    <c:if test="${not empty analysisResult}">
        <table border="1">
            <thead>
                <tr>
                    <th width="80%">Review</th>
                    <th width="20%">Keywords</th>
                </tr>
            </thead>
		<c:forEach var="analysedReview" items="${analysisResult.analysedReviews}">
			<tr>
				<td><c:out value="${analysedReview.review.content}" /></td>
				<td>
					<c:forEach var="keyword" items="${analysedReview.keywords}" >
						${keyword.key} : ${keyword.value} <br/>
					</c:forEach>
				</td>
			</tr>
		</c:forEach>
		</table>
    </c:if>
</body>
</html>
