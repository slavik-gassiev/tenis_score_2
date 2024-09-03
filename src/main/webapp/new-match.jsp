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
        <form action="/tenis_score_war_exploded/new" method="post">
            <label for="p1name">Имя игрока 1:</label>
            <input type="text" id="p1name" name="p1name"><br>
            <label for="p2name">Имя игрока 2:</label>
            <input type="text" id="p2name" name="p2name"><br>
            <input type="submit" value="Отправить">
        </form>
    </div>
    </body>
</html>


