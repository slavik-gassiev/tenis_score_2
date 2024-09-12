<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <<title>Введите имена игроков</title>
</head>
    <body style="position: relative;height: 100vh;">
    <div style="position: absolute;top: 50%;left: 50%;transform: translate(-50%, -50%);">
        <h1>Введите имена игроков</h1>
        <form action="/tenis_score/new" method="post">
            <label for="player1">Имя игрока 1:</label>
            <input type="text" id="player1" name="player1"><br>
            <label for="player2">Имя игрока 2:</label>
            <input type="text" id="player2" name="player2"><br>
            <label for="type">Выберите тип игры:</label>
            <select id="type" name="type">
                <option value="short">Короткая</option>
                <option value="long">Длинная</option>
            </select><br>
            <input type="submit" value="Отправить">
        </form>
    </div>
    </body>
</html>


