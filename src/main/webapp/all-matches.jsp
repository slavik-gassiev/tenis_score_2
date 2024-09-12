<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>All Matches</title>
</head>
<body>
<h1>All Matches</h1>
<table border="1">
    <thead>
    <tr>
        <th>Player 1</th>
        <th>Player 2</th>
        <th>Winner</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="match" items="${matches}">
        <tr>
            <td>${match.player1Name}</td>
            <td>${match.player1Name}</td>
            <td>${match.matchWinnerName}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<%--<a href="${pageContext.request.contextPath}/index.jsp">Back to Home</a>--%>
</body>
</html>
