<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>
        <c:choose>
            <c:when test="${not empty university}">Редактирование вуза</c:when>
            <c:otherwise>Добавление вуза</c:otherwise>
        </c:choose>
    </title>
    <jsp:include page="/WEB-INF/views/fragments/head.jsp"/>
</head>
<body class="page-admin-universities">
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
    <jsp:param name="navSection" value="admin-universities"/>
</jsp:include>
<h1>
    <c:choose>
        <c:when test="${not empty university}">Редактировать вуз</c:when>
        <c:otherwise>Добавить вуз</c:otherwise>
    </c:choose>
</h1>
<p class="page-description">Укажите название вуза и город.</p>

<c:if test="${not empty errors.university}">
    <p><c:out value="${errors.university}"/></p>
</c:if>

<c:if test="${not empty duplicateError}">
    <p><c:out value="${duplicateError}"/></p>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/admin/universities">
    <c:choose>
        <c:when test="${not empty university}">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="id" value="${university.id}">
        </c:when>
        <c:otherwise>
            <input type="hidden" name="action" value="create">
        </c:otherwise>
    </c:choose>

    <p>
        <label for="name">Название</label>
        <input id="name" name="name" type="text" maxlength="100"
               value="<c:out value='${name}'/>" required>
        <c:if test="${not empty errors.name}">
            <span><c:out value="${errors.name}"/></span>
        </c:if>
    </p>

    <p>
        <label for="city">Город</label>
        <input id="city" name="city" type="text" maxlength="30"
               value="<c:out value='${city}'/>" required>
        <c:if test="${not empty errors.city}">
            <span><c:out value="${errors.city}"/></span>
        </c:if>
    </p>

    <button type="submit">Сохранить</button>
</form>

<p>
    <a href="${pageContext.request.contextPath}/admin/universities">Назад к списку</a>
</p>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
</body>
</html>
