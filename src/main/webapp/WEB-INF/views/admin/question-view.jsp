<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Вопрос анкеты</title>
</head>
<body>
<h1>Вопрос анкеты</h1>

<p>ID: <c:out value="${question.id}"/></p>
<p>Текст: <c:out value="${question.text}"/></p>
<p>
    Статус:
    <c:choose>
        <c:when test="${question.active}">активен</c:when>
        <c:otherwise>неактивен</c:otherwise>
    </c:choose>
</p>

<p>
    <a href="${pageContext.request.contextPath}/admin/questions?action=edit&id=${question.id}">
        Изменить
    </a>
</p>

<c:choose>
    <c:when test="${question.active}">
        <form method="post"
              action="${pageContext.request.contextPath}/admin/questions"
              onsubmit="return confirm('Отключить этот вопрос?');">
            <input type="hidden" name="action" value="delete">
            <input type="hidden" name="id" value="${question.id}">
            <button type="submit">Отключить</button>
        </form>
    </c:when>

    <c:otherwise>
        <form method="post"
              action="${pageContext.request.contextPath}/admin/questions">
            <input type="hidden" name="action" value="activate">
            <input type="hidden" name="id" value="${question.id}">
            <button type="submit">Активировать</button>
        </form>
    </c:otherwise>
</c:choose>

<p>
    <a href="${pageContext.request.contextPath}/admin/questions">
        Вернуться к списку
    </a>
</p>
</body>
</html>
