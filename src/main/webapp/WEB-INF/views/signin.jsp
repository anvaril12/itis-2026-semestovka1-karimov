<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Вход</title>
</head>
<body>
<h1>Вход</h1>

<c:if test="${not empty error}">
    <p class="error"><c:out value="${error}"/></p>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/signin">

    <div class="field">
        <label for="login">Логин</label>
        <input type="text" id="login" name="login" value="<c:out value='${login}'/>" required>
    </div>

    <div class="field">
        <label for="password">Пароль</label>
        <input type="password" id="password" name="password" required>
    </div>

    <button type="submit">Войти</button>
</form>

<p>Нет аккаунта? <a href="${pageContext.request.contextPath}/signup">Зарегистрироваться</a></p>
</body>
</html>
