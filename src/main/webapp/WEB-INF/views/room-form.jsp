<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Новая комната</title>
</head>
<body>
<h1>Новая комната</h1>

<%-- общая ошибка, не привязанная к полю (например, не выбрано общежитие) --%>
<c:if test="${not empty errors.room}">
    <p class="flash error"><c:out value="${errors.room}"/></p>
</c:if>

<%-- novalidate не ставим: HTML5-проверки работают в браузере, а сервер всё равно проверяет сам --%>
<form method="post" action="${pageContext.request.contextPath}/rooms/create">
    <p>
        <label for="roomNumber">Номер комнаты</label><br>
        <input type="text" id="roomNumber" name="roomNumber" maxlength="20" required
               value="<c:out value='${roomForm.roomNumber}'/>">
        <c:if test="${not empty errors.roomNumber}">
            <br><span class="error"><c:out value="${errors.roomNumber}"/></span>
        </c:if>
    </p>

    <p>
        <label for="capacity">Число мест</label><br>
        <input type="number" id="capacity" name="capacity" min="2" max="4" required
               value="<c:out value='${roomForm.capacity}'/>">
        <c:if test="${not empty errors.capacity}">
            <br><span class="error"><c:out value="${errors.capacity}"/></span>
        </c:if>
    </p>

    <p>
        <label for="description">Описание (необязательно)</label><br>
        <textarea id="description" name="description" maxlength="255" rows="4"><c:out value="${roomForm.description}"/></textarea>
        <c:if test="${not empty errors.description}">
            <br><span class="error"><c:out value="${errors.description}"/></span>
        </c:if>
    </p>

    <button type="submit">Создать</button>
</form>

<p><a href="${pageContext.request.contextPath}/profile">Профиль</a></p>
</body>
</html>
