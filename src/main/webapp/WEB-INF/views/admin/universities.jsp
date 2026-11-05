<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Управление вузами</title>
</head>
<body>
<h1>Вузы</h1>

<c:choose>
    <c:when test="${empty universities}">
        <p>Вузов пока нет.</p>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Название</th>
                <th>Город</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="university" items="${universities}">
                <tr>
                    <td><c:out value="${university.id}"/></td>
                    <td><c:out value="${university.name}"/></td>
                    <td><c:out value="${university.city}"/></td>
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
