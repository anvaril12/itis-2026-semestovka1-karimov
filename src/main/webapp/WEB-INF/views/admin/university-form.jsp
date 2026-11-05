<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Добавление вуза</title>
</head>
<body>
<h1>Добавить вуз</h1>

<c:if test="${not empty errors.university}">
    <p><c:out value="${errors.university}"/></p>
</c:if>

<c:if test="${not empty duplicateError}">
    <p><c:out value="${duplicateError}"/></p>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/admin/universities">
    <input type="hidden" name="action" value="create">

    <p>
        <label for="name">Название</label>
        <input id="name" name="name" type="text" maxlength="100"
               value="<c:out value='${name}'/>" required>
        <c:if test="${not empty errors.name}">
            <span><c:out value="${errors.name}"/></span>
        </c:if>
    </p>

    <p>
        <label for="city">Город</label>
        <input id="city" name="city" type="text" maxlength="30"
               value="<c:out value='${city}'/>" required>
        <c:if test="${not empty errors.city}">
            <span><c:out value="${errors.city}"/></span>
        </c:if>
    </p>

    <button type="submit">Сохранить</button>
</form>

<p>
    <a href="${pageContext.request.contextPath}/admin/universities">Назад к списку</a>
</p>
</body>
</html>
