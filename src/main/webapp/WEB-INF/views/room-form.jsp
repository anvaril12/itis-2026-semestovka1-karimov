<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- одна форма на два режима: если сервлет передал roomId — это редактирование, иначе создание --%>
<c:set var="isEdit" value="${not empty roomId}"/>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${isEdit ? 'Редактирование комнаты' : 'Новая комната'}</title>
    <jsp:include page="/WEB-INF/views/fragments/head.jsp"/>
</head>
<body class="page-rooms">
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
    <jsp:param name="navSection" value="rooms"/>
</jsp:include>
<h1>${isEdit ? 'Редактирование комнаты' : 'Новая комната'}</h1>
<p class="page-description">Расскажите о комнате и о том, каких соседей вы ищете.</p>

<%-- общая ошибка, не привязанная к полю (например, не выбрано общежитие) --%>
<c:if test="${not empty errors.room}">
    <p class="flash error"><c:out value="${errors.room}"/></p>
</c:if>

<form method="post" action="${pageContext.request.contextPath}${isEdit ? '/rooms/edit' : '/rooms/create'}">
    <c:if test="${isEdit}">
        <input type="hidden" name="id" value="${roomId}">
        <input type="hidden" name="action" value="update">
    </c:if>

    <p>
        <label for="roomNumber">Номер комнаты</label><br>
        <input type="text" id="roomNumber" name="roomNumber" maxlength="20" required
               value="<c:out value='${roomForm.roomNumber}'/>">
        <c:if test="${not empty errors.roomNumber}">
            <br><span class="error"><c:out value="${errors.roomNumber}"/></span>
        </c:if>
    </p>

    <p>
        <label for="capacity">Число мест</label><br>
        <input type="number" id="capacity" name="capacity" min="2" max="4" required
               value="<c:out value='${roomForm.capacity}'/>">
        <c:if test="${not empty errors.capacity}">
            <br><span class="error"><c:out value="${errors.capacity}"/></span>
        </c:if>
    </p>

    <p>
        <label for="description">Описание (необязательно)</label><br>
        <textarea id="description" name="description" maxlength="255" rows="4"><c:out value="${roomForm.description}"/></textarea>
        <c:if test="${not empty errors.description}">
            <br><span class="error"><c:out value="${errors.description}"/></span>
        </c:if>
    </p>

    <button type="submit">${isEdit ? 'Сохранить' : 'Создать'}</button>
</form>

<c:choose>
    <c:when test="${isEdit}">
        <p><a href="${pageContext.request.contextPath}/rooms/view?id=${roomId}">Отмена</a></p>
    </c:when>
    <c:otherwise>
        <p><a href="${pageContext.request.contextPath}/profile">Профиль</a></p>
    </c:otherwise>
</c:choose>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
</body>
</html>
