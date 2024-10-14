<%@ page contentType="text/html;charset=UTF-8" language="java" %>\
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Match Result</title>
</head>
<body>

<h1>Match Result</h1>

<!-- Отображение информации о победителе -->
<div>
    <h2>Winner: ${winner.matchWinnerName}</h2>
    <p>Player 1: ${winner.player1Name} (Sets Score: ${winner.player1SetsScore})</p>
    <p>Player 2: ${winner.player2Name} (Sets Score: ${winner.player2SetsScore})</p>
</div>

<!-- Отображение информации о сетах -->
<div>
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
        <c:forEach var="set" items="${winner.getSets()}" varStatus="status">
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
<br>
<a href="http://localhost:8080/tenis_score/new">Начать матч</a>
<br>
<a href="http://localhost:8080/tenis_score/all">Посмотреть все матчи</a>
</body>
</html>
