<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Комнаты в моём общежитии</title>
</head>
<body>
<h1>Комнаты в моём общежитии</h1>

<%-- flash-сообщения: показываем один раз и сразу удаляем из сессии --%>
<c:if test="${not empty sessionScope.flashSuccess}">
    <p class="flash success"><c:out value="${sessionScope.flashSuccess}"/></p>
    <c:remove var="flashSuccess" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.flashError}">
    <p class="flash error"><c:out value="${sessionScope.flashError}"/></p>
    <c:remove var="flashError" scope="session"/>
</c:if>

<c:choose>
    <c:when test="${empty rooms}">
        <p>Комнат пока нет. Если вы ещё не выбрали общежитие — сделайте это в
            <a href="${pageContext.request.contextPath}/profile">профиле</a>.</p>
    </c:when>
    <c:otherwise>
        <table>
            <tr>
                <th>Комната</th>
                <th>Мест</th>
                <th>Владелец</th>
                <th></th>
            </tr>
            <%-- rooms — список RoomView: в каждом элементе комната и её владелец --%>
            <c:forEach var="roomView" items="${rooms}">
                <tr>
                    <td><c:out value="${roomView.roomEntity.roomNumber}"/></td>
                    <td><c:out value="${roomView.roomEntity.capacity}"/></td>
                    <td><c:out value="${roomView.owner.firstName}"/> <c:out value="${roomView.owner.lastName}"/></td>
                    <td><a href="${pageContext.request.contextPath}/rooms/view?id=${roomView.roomEntity.id}">Открыть</a></td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>

<p><a href="${pageContext.request.contextPath}/rooms/create">Создать комнату</a></p>
<p><a href="${pageContext.request.contextPath}/profile">Профиль</a></p>
</body>
</html>
