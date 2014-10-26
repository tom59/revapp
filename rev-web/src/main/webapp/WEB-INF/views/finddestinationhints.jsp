<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

    <title>Find Destination Hints</title>
</head>
<body>
    <h1>Find Destination Hints</h1>

   <spring:form modelAttribute="searchRequest"
        action="finddestinationhints" method="get" acceptCharset="UTF-8"  >

        <table>
            <tbody>
            <tr>
            <td>Destination Id (eg: 712491)</td>
            <td>
                <spring:input class="inputfield" path="destinationId" />
            </td>
            </tr>
            <tr>
            <td>Minimum Number of Reviews</td>
            <td>
                <spring:input class="inputfield" path="minimumNumberOfReviews" />
            </td>
            </tr>
            <tr>
            <td>Minimum Score</td>
            <td>
                <spring:input class="inputfield" path="minimumScore" />
            </td>
            </tr>

            </tbody>
         </table>
	     <input type="submit" />
     </spring:form>

    <c:if test="${not empty searchResult}">
        <table>
                <thead>
                    <tr>
                        <th>Keyword</th>
                        <th>Number of Reviews</th>
                        <th>Score</th>
                    </tr>
                </thead>
            <tbody>
	        <c:forEach var="hint" items="${searchResult.hints}">
	            <tr>
		            <td>${hint.keyword}</td>
	                <td>${hint.numberOfReviews}</td>
	                <td>${hint.averageScore}</td>
	            </tr>
	        </c:forEach>
            </tbody>
         </table>
    </c:if>
</body>
</html>
