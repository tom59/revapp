<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

    <title>Find Hotel Reviews</title>
</head>
<body>
    <h1>Find Hotel Reviews</h1>

   <spring:form modelAttribute="searchRequest"
        action="findreviews" method="get" acceptCharset="UTF-8"  >

        <table>
            <tbody>
            <tr>
            <td>Hotel Id (eg: 179615)</td>
            <td>
                <spring:input class="inputfield" path="hotelId" />
            </td>
            </tr>
            <tr>
            <td>Query (eg: breakfast)</td>
            <td>
                <spring:input class="inputfield" path="keyword" />
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
                    <th>Review Id</th>
                    <th>Review</th>
                    <th>Semantic Score</th>
                </tr>
            </thead>
		<c:forEach var="review" items="${searchResult.reviews}">
			<tr>
	            <td width="80px"><c:out value="${review.id}" /></td>
                <td width="150px">
                   <c:forEach var="semanticScore" items="${review.semanticScore}">
                    <c:set var="attributeLength" value="${fn:length(semanticScore.key)}"/>
                    <c:out value="${fn:substring(semanticScore.key, 15, attributeLength)}" /> : <c:out value="${semanticScore.value}" /><br/>
                   </c:forEach>
                </td>
                <td><c:out value="${review.content}" /></td>
			</tr>
		</c:forEach>
		</table>
    </c:if>
</body>
</html>
