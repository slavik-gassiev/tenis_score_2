<%@ page import="com.slava.dto.MatchDto" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    MatchDto match = (MatchDto) request.getAttribute("matchJson");
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Match score</title>
</head>
<body style="position: relative;height: 100vh;">


<div style="position: absolute;top: 50%;left: 50%;transform: translate(-50%, -50%);">

    <table border="1">
        <thead>
        <tr>
            <th>Players</th>
            <th>Score</th>
            <th>Deuce</th>
            <th>Game</th>
            <th>Tie-break</th>
            <th>Set</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <th>${match.player1}
                <hr>
                ${match.player2}</th>

            <td>${match.player1Score}
                <hr>
                ${match.player2Score}</td>

            <td>${match.player1Deuce}
                <hr>
                ${match.player2Deuce}</td>

            <td>${match.player1Game}
                <hr>
                ${match.player2Game}</td>

            <td>${match.player1TB}
                <hr>
                ${match.player2TB}</td>

            <td>${match.plater1Set}
                <hr>
                ${match.player2Set}</td>

        </tr>
        </tbody>
    </table>

    <div style="margin-top: 2vh;">
        <form action="match-score?uuid=${uuid}" method="post" name="scoredform">
            <button  name ="point_winner" value="player1">${match.player1} scored!</button>
            <button name="point_winner" value="player2">${match.player2} scored!</button>
        </form>
    </div>
</div>



</body>
</html>





