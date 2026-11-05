<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Управление вопросами</title>
</head>
<body>
<h1>Вопросы анкеты</h1>

<c:if test="${not empty sessionScope.flashSuccess}">
    <p class="success">
        <c:out value="${sessionScope.flashSuccess}"/>
    </p>
    <c:remove var="flashSuccess" scope="session"/>
</c:if>

<c:if test="${not empty sessionScope.flashError}">
    <p class="error">
        <c:out value="${sessionScope.flashError}"/>
    </p>
    <c:remove var="flashError" scope="session"/>
</c:if>

<p>
    <a href="${pageContext.request.contextPath}/admin/questions?action=create">
        Добавить вопрос
    </a>
</p>

<c:choose>
    <c:when test="${empty questions}">
        <p>Вопросов пока нет.</p>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Текст вопроса</th>
                <th>Статус</th>
                <th>Действие</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="question" items="${questions}">
                <tr>
                    <td><c:out value="${question.id}"/></td>
                    <td><c:out value="${question.text}"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${question.active}">Активен</c:when>
                            <c:otherwise>Неактивен</c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/admin/questions?id=${question.id}">
                            Открыть
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>

<p>
    <a href="${pageContext.request.contextPath}/profile">Вернуться в профиль</a>
</p>
</body>
</html>
