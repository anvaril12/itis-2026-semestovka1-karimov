<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Соседи</title>
</head>
<body>
<h1>Подбор соседей</h1>

<%-- user кладёт в сессию SignInServlet; сюда без входа не пустит AuthFilter --%>
<p>Вы вошли как <c:out value="${sessionScope.user.login}"/></p>

<%-- Выход — POST, а не ссылка: GET не должен менять состояние на сервере --%>
<form method="post" action="${pageContext.request.contextPath}/signout">
    <button type="submit">Выйти</button>
</form>
</body>
</html>
