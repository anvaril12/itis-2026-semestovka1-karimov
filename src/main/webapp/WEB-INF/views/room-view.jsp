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
    <form method="post" action="${pageContext.request.contextPath}/rooms/edit"
          onsubmit="return confirm('Удалить комнату?');">
        <input type="hidden" name="id" value="${room.id}">
        <input type="hidden" name="action" value="delete">
        <button type="submit">Удалить</button>
    </form>
</c:if>

<%-- жильцы видны всем: владелец + принятые участники (таблица-связка room_members) --%>
<h2>Жильцы</h2>
<ul>
    <li><c:out value="${owner.firstName}"/> <c:out value="${owner.lastName}"/> (владелец)</li>
    <c:forEach var="m" items="${members}">
        <c:if test="${m.roomMemberEntity.status == 'ACCEPTED'}">
            <li><c:out value="${m.user.firstName}"/> <c:out value="${m.user.lastName}"/></li>
        </c:if>
    </c:forEach>
</ul>

<%-- новые заявки видит только владелец; кнопки шлют POST в RoomMemberActionServlet --%>
<c:if test="${sessionScope.user.id == owner.id}">
    <h2>Заявки</h2>
    <c:set var="hasPending" value="false"/>
    <c:forEach var="m" items="${members}">
        <c:if test="${m.roomMemberEntity.status == 'PENDING'}">
            <c:set var="hasPending" value="true"/>
            <p>
                <c:out value="${m.user.firstName}"/> <c:out value="${m.user.lastName}"/>,
                <c:out value="${m.user.course}"/> курс
            </p>
            <form method="post" action="${pageContext.request.contextPath}/rooms/members">
                <input type="hidden" name="roomId" value="${room.id}">
                <input type="hidden" name="memberId" value="${m.user.id}">
                <button type="submit" name="action" value="accept">Принять</button>
                <button type="submit" name="action" value="reject">Отклонить</button>
            </form>
        </c:if>
    </c:forEach>
    <c:if test="${not hasPending}">
        <p>Новых заявок нет.</p>
    </c:if>
</c:if>

<%-- не владелец: если уже подал заявку — показываем её статус, иначе кнопку «Хочу в эту комнату» --%>
<c:if test="${sessionScope.user.id != owner.id}">
    <c:set var="myStatus" value=""/>
    <c:forEach var="m" items="${members}">
        <c:if test="${m.user.id == sessionScope.user.id}">
            <c:set var="myStatus" value="${m.roomMemberEntity.status}"/>
        </c:if>
    </c:forEach>
    <c:choose>
        <c:when test="${myStatus == 'PENDING'}"><p>Ваша заявка ждёт ответа владельца.</p></c:when>
        <c:when test="${myStatus == 'ACCEPTED'}"><p>Вы живёте в этой комнате.</p></c:when>
        <c:when test="${myStatus == 'REJECTED'}"><p>Владелец отклонил вашу заявку.</p></c:when>
        <c:otherwise>
            <form method="post" action="${pageContext.request.contextPath}/rooms/join">
                <input type="hidden" name="roomId" value="${room.id}">
                <button type="submit">Хочу в эту комнату</button>
            </form>
        </c:otherwise>
    </c:choose>
</c:if>

<p><a href="${pageContext.request.contextPath}/rooms">Все комнаты</a></p>
<p><a href="${pageContext.request.contextPath}/profile">Профиль</a></p>
</body>
</html>
