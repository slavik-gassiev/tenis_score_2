<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>All Matches</title>
</head>
<body>
<h1>All Matches</h1>
    <tbody>
    <c:forEach var="match" items="${matches}">
        <h2>Sets Information</h2>
        <table border="1">
            <thead>
            <tr>
                <th>Set #</th>
                <th>Winner</th>
                <th>Player 1 Game Score</th>
                <th>Player 2 Game Score</th>
                <th>Player 1 TieBreak Score</th>
                <th>Player 2 TieBreak Score</th>
                <th>Deuce Info</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="set" items="${match.getSets()}" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td>${set.setWinner.name}</td>
                    <td>${set.player1GameScore}</td>
                    <td>${set.player2GameScore}</td>

                    <!-- Тай-брейк: если был сыгран -->
                    <td>
                        <c:if test="${set.tieBreak != null}">
                            ${set.tieBreak.player1TieBreakScore}
                        </c:if>
                        <c:if test="${set.tieBreak == null}">
                            -
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${set.tieBreak != null}">
                            ${set.tieBreak.player2TieBreakScore}
                        </c:if>
                        <c:if test="${set.tieBreak == null}">
                            -
                        </c:if>
                    </td>

                    <!-- Отображение информации о Deuce, если есть -->
                    <td>
                        <c:forEach var="game" items="${set.games}">
                            <c:if test="${game.deuce != null}">
                                <c:if test="${game.deuce.deuceWinner != null}">
                                    Deuce Winner: ${game.deuce.deuceWinner.name},
                                    Player 1 Score: ${game.deuce.player1DeuceScore},
                                    Player 2 Score: ${game.deuce.player2DeuceScore}
                                </c:if>
                            </c:if>
                        </c:forEach>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    </c:forEach>
    </tbody>
</table>
<%--<a href="${pageContext.request.contextPath}/index.jsp">Back to Home</a>--%>
</body>
</html>
