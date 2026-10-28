<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Подбор соседей</title>
</head>
<body>
<h1>Подбор соседей</h1>

<c:if test="${not empty sessionScope.flashSuccess}">
    <p class="flash success"><c:out value="${sessionScope.flashSuccess}"/></p>
    <c:remove var="flashSuccess" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.flashError}">
    <p class="flash error"><c:out value="${sessionScope.flashError}"/></p>
    <c:remove var="flashError" scope="session"/>
</c:if>

<c:choose>
    <c:when test="${empty sessionScope.user.dormitoryId}">
        <p>Сначала выберите общежитие —
            <a href="${pageContext.request.contextPath}/profile">в профиле</a>.</p>
    </c:when>
    <c:when test="${empty matches}">
        <p>Подходящих соседей пока нет. Если вы ещё не прошли анкету —
            <a href="${pageContext.request.contextPath}/questionnaire">пройдите её</a>.</p>
    </c:when>
    <c:otherwise>
        <p>Студенты вашего общежития, отсортированные по совместимости.</p>
        <ul>
            <c:forEach items="${matches}" var="m">
                <li>
                    <c:out value="${m.user.firstName}"/> <c:out value="${m.user.lastName}"/>,
                    <c:out value="${m.user.course}"/> курс —
                    <strong><c:out value="${m.percent}"/>%</strong>
                    <%-- POST: заявка меняет данные; получатель передаётся скрытым полем --%>
                    <form method="post" action="${pageContext.request.contextPath}/request/send">
                        <input type="hidden" name="receiverId" value="${m.user.id}">
                        <button type="submit">Хочу жить вместе</button>
                    </form>
                </li>
            </c:forEach>
        </ul>
    </c:otherwise>
</c:choose>

<p><a href="${pageContext.request.contextPath}/requests">Мои заявки</a> ·
    <a href="${pageContext.request.contextPath}/profile">Вернуться в профиль</a></p>
</body>
</html>
