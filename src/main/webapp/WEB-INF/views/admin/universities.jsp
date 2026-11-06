<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Управление вузами</title>
    <jsp:include page="/WEB-INF/views/fragments/head.jsp"/>
</head>
<body class="page-admin-universities">
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
    <jsp:param name="navSection" value="admin-universities"/>
</jsp:include>
<h1>Вузы</h1>
<p class="page-description">Вузы, доступные студентам при выборе общежития.</p>

<c:if test="${not empty sessionScope.flashSuccess}">
    <p class="success"><c:out value="${sessionScope.flashSuccess}"/></p>
    <c:remove var="flashSuccess" scope="session"/>
</c:if>

<c:if test="${not empty sessionScope.flashError}">
    <p class="error"><c:out value="${sessionScope.flashError}"/></p>
    <c:remove var="flashError" scope="session"/>
</c:if>

<p>
    <a href="${pageContext.request.contextPath}/admin/universities?action=create">Добавить вуз</a>
</p>

<c:choose>
    <c:when test="${empty universities}">
        <p>Вузов пока нет.</p>
    </c:when>
    <c:otherwise>
        <div class="table-scroll" role="region" aria-label="Таблица данных" tabindex="0">
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Название</th>
                <th>Город</th>
                <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="university" items="${universities}">
                <tr>
                    <td><c:out value="${university.id}"/></td>
                    <td><c:out value="${university.name}"/></td>
                    <td><c:out value="${university.city}"/></td>
                    <td>
                        <a href="${pageContext.request.contextPath}/admin/universities?action=edit&amp;id=${university.id}">
                            Редактировать
                        </a>
                        <form method="post" action="${pageContext.request.contextPath}/admin/universities">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" value="${university.id}">
                            <button class="button-danger" type="submit">Удалить</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        </div>
    </c:otherwise>
</c:choose>

<p>
    <a href="${pageContext.request.contextPath}/profile">Вернуться в профиль</a>
</p>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
</body>
</html>
