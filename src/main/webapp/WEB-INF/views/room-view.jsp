<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="room" value="${roomView.roomEntity}"/>
<c:set var="owner" value="${roomView.owner}"/>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Комната <c:out value="${room.roomNumber}"/></title>
</head>
<body>
<h1>Комната <c:out value="${room.roomNumber}"/></h1>

<%-- flash-сообщения: показываем один раз и сразу удаляем из сессии --%>
<c:if test="${not empty sessionScope.flashSuccess}">
    <p class="flash success"><c:out value="${sessionScope.flashSuccess}"/></p>
    <c:remove var="flashSuccess" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.flashError}">
    <p class="flash error"><c:out value="${sessionScope.flashError}"/></p>
    <c:remove var="flashError" scope="session"/>
</c:if>

<p>Мест: <c:out value="${room.capacity}"/></p>
<c:if test="${not empty room.description}">
    <p><c:out value="${room.description}"/></p>
</c:if>
<p>Владелец: <c:out value="${owner.firstName}"/> <c:out value="${owner.lastName}"/>,
    <c:out value="${owner.course}"/> курс</p>

<%-- кнопки видит только владелец; право на изменение всё равно проверяет сервис --%>
<c:if test="${sessionScope.user.id == owner.id}">
    <p>
        <a href="${pageContext.request.contextPath}/rooms/edit?id=${room.id}">Редактировать</a>
    </p>
    <form method="post" action="${pageContext.request.contextPath}/rooms/delete"
          onsubmit="return confirm('Удалить комнату?');">
        <input type="hidden" name="id" value="${room.id}">
        <button type="submit">Удалить</button>
    </form>
</c:if>

<p><a href="${pageContext.request.contextPath}/profile">Профиль</a></p>
</body>
</html>
