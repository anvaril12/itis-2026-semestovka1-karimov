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

<form method="post" action="${pageContext.request.contextPath}/signout">
    <button type="submit">Выйти</button>
</form>

<script src="${pageContext.request.contextPath}/js/profile.js"></script>
</body>
</html>
