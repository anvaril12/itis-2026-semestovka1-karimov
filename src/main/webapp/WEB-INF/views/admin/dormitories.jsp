<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Управление общежитиями</title>
</head>
<body>
<h1>Общежития</h1>

<p>
    <a href="${pageContext.request.contextPath}/admin/dormitories?action=create">
        Добавить общежитие
    </a>
</p>

<c:if test="${not empty sessionScope.flashSuccess}">
    <p><c:out value="${sessionScope.flashSuccess}"/></p>
    <c:remove var="flashSuccess" scope="session"/>
</c:if>

<c:if test="${not empty sessionScope.flashError}">
    <p><c:out value="${sessionScope.flashError}"/></p>
    <c:remove var="flashError" scope="session"/>
</c:if>

<c:choose>
    <c:when test="${empty dormitories}">
        <p>Общежитий пока нет.</p>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>ID вуза</th>
                <th>Название</th>
                <th>Адрес</th>
                <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="dormitory" items="${dormitories}">
                <tr>
                    <td><c:out value="${dormitory.id}"/></td>
                    <td><c:out value="${dormitory.universityId}"/></td>
                    <td><c:out value="${dormitory.name}"/></td>
                    <td><c:out value="${dormitory.address}"/></td>
                    <td>
                        <a href="${pageContext.request.contextPath}/admin/dormitories?action=edit&amp;id=${dormitory.id}">
                            Редактировать
                        </a>
                        <form method="post" action="${pageContext.request.contextPath}/admin/dormitories">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" value="${dormitory.id}">
                            <button type="submit">Удалить</button>
                        </form>
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
