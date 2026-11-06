<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Управление общежитиями</title>
    <jsp:include page="/WEB-INF/views/fragments/head.jsp"/>
</head>
<body class="page-admin-dormitories">
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
    <jsp:param name="navSection" value="admin-dormitories"/>
</jsp:include>
<h1>Общежития</h1>
<p class="page-description">Общежития, в которых студенты находят соседей.</p>

<p>
    <a href="${pageContext.request.contextPath}/admin/dormitories?action=create">
        Добавить общежитие
    </a>
</p>

<c:if test="${not empty sessionScope.flashSuccess}">
    <p class="flash success" role="status"><c:out value="${sessionScope.flashSuccess}"/></p>
    <c:remove var="flashSuccess" scope="session"/>
</c:if>

<c:if test="${not empty sessionScope.flashError}">
    <p class="flash error" role="alert"><c:out value="${sessionScope.flashError}"/></p>
    <c:remove var="flashError" scope="session"/>
</c:if>

<c:choose>
    <c:when test="${empty dormitories}">
        <p>Общежитий пока нет.</p>
    </c:when>
    <c:otherwise>
        <div class="table-scroll" role="region" aria-label="Таблица данных" tabindex="0">
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
