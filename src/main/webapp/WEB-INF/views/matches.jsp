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
                </li>
            </c:forEach>
        </ul>
    </c:otherwise>
</c:choose>

<p><a href="${pageContext.request.contextPath}/profile">Вернуться в профиль</a></p>
</body>
</html>
