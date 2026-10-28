<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Профиль</title>
</head>
<body>
<h1>Профиль</h1>

<%-- flash-сообщения: показываем один раз и сразу удаляем из сессии --%>
<c:if test="${not empty sessionScope.flashSuccess}">
    <p class="flash success"><c:out value="${sessionScope.flashSuccess}"/></p>
    <c:remove var="flashSuccess" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.flashError}">
    <p class="flash error"><c:out value="${sessionScope.flashError}"/></p>
    <c:remove var="flashError" scope="session"/>
</c:if>

<p>Логин: <c:out value="${sessionScope.user.login}"/></p>
<p>Имя: <c:out value="${sessionScope.user.firstName}"/> <c:out value="${sessionScope.user.lastName}"/></p>
<p>Курс: <c:out value="${sessionScope.user.course}"/></p>

<h2>Общежитие</h2>
<c:choose>
    <c:when test="${not empty currentDormitory}">
        <p>Сейчас: <c:out value="${currentDormitory.name}"/></p>
    </c:when>
    <c:otherwise>
        <p>Общежитие ещё не выбрано</p>
    </c:otherwise>
</c:choose>

<form method="post" action="${pageContext.request.contextPath}/profile">

    <div class="field">
        <label for="university">Университет</label>
        <select id="university" name="universityId" required
                data-url="${pageContext.request.contextPath}/api/dormitories">
            <option value="">— выберите университет —</option>
            <c:forEach items="${universities}" var="u">
                <option value="${u.id}"><c:out value="${u.name}"/></option>
            </c:forEach>
        </select>
    </div>

    <div class="field">
        <label for="dormitory">Общежитие</label>
        <select id="dormitory" name="dormitoryId" required disabled>
            <option value="">— сначала выберите университет —</option>
        </select>
        <span class="error"><c:out value="${errors.dormitory}"/></span>
    </div>

    <button type="submit">Сохранить</button>
</form>

<%-- вторая сторона M2M room_members: все комнаты, куда пользователь подавал заявки --%>
<h2>Мои заявки в комнаты</h2>
<c:choose>
    <c:when test="${empty myApplications}">
        <p>Вы ещё не подавали заявок. <a href="${pageContext.request.contextPath}/rooms">Посмотреть комнаты</a></p>
    </c:when>
    <c:otherwise>
        <ul>
            <c:forEach var="app" items="${myApplications}">
                <li>
                    <a href="${pageContext.request.contextPath}/rooms/view?id=${app.roomEntity.id}">
                        Комната <c:out value="${app.roomEntity.roomNumber}"/></a> —
                    <c:choose>
                        <c:when test="${app.roomMemberEntity.status == 'PENDING'}">ждёт ответа</c:when>
                        <c:when test="${app.roomMemberEntity.status == 'ACCEPTED'}">вы живёте здесь</c:when>
                        <c:otherwise>отклонена</c:otherwise>
                    </c:choose>
                </li>
            </c:forEach>
        </ul>
    </c:otherwise>
</c:choose>

<p><a href="${pageContext.request.contextPath}/questionnaire">Пройти анкету</a></p>
<p><a href="${pageContext.request.contextPath}/matches">Найти соседей</a></p>
<p><a href="${pageContext.request.contextPath}/requests">Мои заявки</a></p>
<p><a href="${pageContext.request.contextPath}/rooms">Комнаты в общежитии</a></p>
<p><a href="${pageContext.request.contextPath}/rooms/create">Создать комнату</a></p>

<form method="post" action="${pageContext.request.contextPath}/signout">
    <button type="submit">Выйти</button>
</form>

<script src="${pageContext.request.contextPath}/js/profile.js"></script>
</body>
</html>
