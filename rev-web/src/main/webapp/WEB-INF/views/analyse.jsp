<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

    <title>Reviews Analysis</title>
</head>
<body>
    <h1>Reviews Analysis</h1>

   <spring:form modelAttribute="analyseRequest"
        action="analyse" method="get" acceptCharset="UTF-8"  >

        <table>
            <tbody>
            <tr>
            <td>Number of Keywords</td>
            <td>
                <spring:input class="inputfield" path="resultsNumber" />
            </td>
            </tr>
            </tbody>
         </table>
	     <input type="submit" />
     </spring:form>

    <c:if test="${not empty analyseResult}">
       <table>
	       <tr><td>Total Number of Reviews:</td><td><c:out value="${analyseResult.totalNumberOfReviews}" /></td></tr>
	       <tr><td>Total Number of Terms:</td><td><c:out value="${analyseResult.totalNumberOfTerms}" /></td></tr>
	       <tr><td>Number of Distinct Terms:</td><td><c:out value="${analyseResult.numberOfDistinctTerms}" /></td></tr>
        </table>

        <table>
            <thead>
                <tr>
                    <th>Keyword</th>
                    <th>Frequency</th>
                </tr>
            </thead>
            <tbody>
        <c:forEach var="keywordFrequency" items="${analyseResult.keywordsFrequency}">
	        <tr>
			    <td><c:out value="${keywordFrequency.key}" /></td>
                <td><c:out value="${keywordFrequency.value}" /></td>
            </tr>
        </c:forEach>
		</tbody>
		</table>
    </c:if>
</body>
</html>
