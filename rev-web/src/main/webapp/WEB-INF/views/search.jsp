<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

    <title>Reviews Search</title>
</head>
<body>
    <h1>Reviews Search</h1>

   <spring:form modelAttribute="searchRequest"
        action="search" method="get" acceptCharset="UTF-8"  >

        <table>
            <tbody>
            <tr>
            <td>Search Term (eg: staff)</td>
            <td>
                <spring:input class="inputfield" path="keywords" />
            </td>
            </tr>
            <tr>
            <td>Number of Results</td>
            <td>
                <spring:input class="inputfield" path="resultsNumber" />
            </td>
            </tr>
            </tbody>
         </table>
	     <input type="submit" />
     </spring:form>

    <c:if test="${not empty searchResult}">
        ${searchResult.totalNumberOfReviewsFound} reviews found.
        <table>
            <thead>
                <tr>
                    <th>Hotel ID</th>
                    <th>Review ID</th>
                    <th>Review</th>
                </tr>
            </thead>
		<c:forEach var="review" items="${searchResult.reviews}">
			<tr>
	            <td><c:out value="${review.hotelId}" /></td>
	            <td><c:out value="${review.id}" /></td>
				<td><c:out value="${review.content}" /></td>
			</tr>
		</c:forEach>
		</table>
    </c:if>
</body>
</html>
