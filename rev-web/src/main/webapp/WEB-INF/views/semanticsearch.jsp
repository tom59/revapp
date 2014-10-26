<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

    <title>Semantic Search</title>
</head>
<body>
    <h1>Semantic Search</h1>

   <spring:form modelAttribute="searchRequest"
        action="semanticsearch" method="get" acceptCharset="UTF-8"  >

        <table>
            <tbody>
            <tr>
            <td>Hotel Id (eg: 179615)</td>
            <td>
                <spring:input class="inputfield" path="hotelId" />
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
                    <th>Review Keyword</th>
                    <th>Count</th>
                    <th>Semantic Score</th>
                </tr>
            </thead>
		<c:forEach var="semanticGroup" items="${searchResult.semanticGroups}">
			<tr>
	            <td><c:out value="${semanticGroup.keyword}" /></td>
                <td><c:out value="${semanticGroup.keywordCount}" /></td>
	            <td><c:out value="${semanticGroup.averageSemanticScore}" /></td>
			</tr>
		</c:forEach>
		</table>
    </c:if>
</body>
</html>
