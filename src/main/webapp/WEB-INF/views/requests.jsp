<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Мои заявки</title>
</head>
<body>
<h1>Мои заявки</h1>

<%-- flash-сообщения: показываем один раз и сразу удаляем из сессии --%>
<c:if test="${not empty sessionScope.flashSuccess}">
    <p class="flash success"><c:out value="${sessionScope.flashSuccess}"/></p>
    <c:remove var="flashSuccess" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.flashError}">
    <p class="flash error"><c:out value="${sessionScope.flashError}"/></p>
    <c:remove var="flashError" scope="session"/>
</c:if>

<h2>Входящие</h2>
<c:choose>
    <c:when test="${empty incoming}">
        <p>Вам пока никто не отправлял заявок.</p>
    </c:when>
    <c:otherwise>
        <ul>
            <c:forEach items="${incoming}" var="r">
                <li>
                    <c:out value="${r.otherUser.firstName}"/> <c:out value="${r.otherUser.lastName}"/>,
                    <c:out value="${r.otherUser.course}"/> курс —
                    <c:choose>
                        <%-- кнопки только у необработанной заявки; права всё равно проверяет сервис --%>
                        <c:when test="${r.roommateRequestEntity.status == 'PENDING'}">
                            <form method="post" action="${pageContext.request.contextPath}/request/action">
                                <input type="hidden" name="requestId" value="${r.roommateRequestEntity.id}">
                                <button type="submit" name="action" value="accept">Принять</button>
                                <button type="submit" name="action" value="reject">Отклонить</button>
                            </form>
                        </c:when>
                        <c:when test="${r.roommateRequestEntity.status == 'ACCEPTED'}">
                            принята. Контакты: <c:out value="${r.otherUser.contacts}"/>
                        </c:when>
                        <c:otherwise>отклонена</c:otherwise>
                    </c:choose>
                </li>
            </c:forEach>
        </ul>
    </c:otherwise>
</c:choose>

<h2>Исходящие</h2>
<c:choose>
    <c:when test="${empty outgoing}">
        <p>Вы пока не отправляли заявок.
            <a href="${pageContext.request.contextPath}/matches">Найти соседа</a></p>
    </c:when>
    <c:otherwise>
        <ul>
            <c:forEach items="${outgoing}" var="r">
                <li>
                    <c:out value="${r.otherUser.firstName}"/> <c:out value="${r.otherUser.lastName}"/>,
                    <c:out value="${r.otherUser.course}"/> курс —
                    <c:choose>
                        <c:when test="${r.roommateRequestEntity.status == 'PENDING'}">ожидает ответа</c:when>
                        <%-- контакты открываются только после согласия второй стороны --%>
                        <c:when test="${r.roommateRequestEntity.status == 'ACCEPTED'}">
                            принята. Контакты: <c:out value="${r.otherUser.contacts}"/>
                        </c:when>
                        <c:otherwise>отклонена</c:otherwise>
                    </c:choose>
                </li>
            </c:forEach>
        </ul>
    </c:otherwise>
</c:choose>

<p><a href="${pageContext.request.contextPath}/matches">Подбор соседей</a> ·
    <a href="${pageContext.request.contextPath}/profile">Профиль</a></p>
</body>
</html>
