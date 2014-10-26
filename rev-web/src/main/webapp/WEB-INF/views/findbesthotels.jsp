<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

    <title>Find Best Hotels</title>
</head>
<body>
    <h1>Find Best Hotels</h1>

   <spring:form modelAttribute="searchRequest"
        action="findbesthotels" method="get" acceptCharset="UTF-8"  >

        <table>
            <tbody>
            <tr>
            <td>Destination Id (eg: 1644736)</td>
            <td>
                <spring:input class="inputfield" path="destinationId" />
            </td>
            </tr>
            <tr>
            <td>Query (eg: staff room)</td>
            <td>
                <spring:input class="inputfield" path="searchTerm" />
            </td>
            </tr>
            <tr>
            <td>Minimum Number Of Reviews</td>
            <td>
                <spring:input class="inputfield" path="minNumberOfReviews" />
            </td>
            </tr>
            <tr>
            <td>Max Number Of Results</td>
            <td>
                <spring:input class="inputfield" path="maxNumberOfResults" />
            </td>
            </tr>
            </tbody>
         </table>
	     <input type="submit" />
     </spring:form>

    <c:if test="${not empty searchResult}">
        <c:forEach var="hotel" items="${searchResult.hotels}">
            <h2>Hotel ID: ${hotel.hotelId}</h2>
	        <table>
	            <thead>
	                <tr>
	                    <th>Review Id</th>
                        <th>Semantic Score</th>
	                    <th>Review</th>
	                </tr>
	            </thead>
	            <tbody>
                <c:forEach var="review" items="${hotel.reviews}">
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
			</tbody>
			</table>
		</c:forEach>
    </c:if>
</body>
</html>
