<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>
        <c:choose>
            <c:when test="${not empty question}">Изменить вопрос</c:when>
            <c:otherwise>Добавить вопрос</c:otherwise>
        </c:choose>
    </title>
    <jsp:include page="/WEB-INF/views/fragments/head.jsp"/>
</head>
<body class="page-admin-questions">
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
    <jsp:param name="navSection" value="admin-questions"/>
</jsp:include>
<h1>
    <c:choose>
        <c:when test="${not empty question}">Изменить вопрос</c:when>
        <c:otherwise>Добавить вопрос</c:otherwise>
    </c:choose>
</h1>
<p class="page-description">Настройте вопрос и варианты ответа для анкеты привычек.</p>

<c:if test="${not empty errors.text}">
    <p class="error">
        <c:out value="${errors.text}"/>
    </p>
</c:if>

<form method="post"
      action="${pageContext.request.contextPath}/admin/questions">
    <c:choose>
        <c:when test="${not empty question}">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="id" value="${question.id}">
        </c:when>
        <c:otherwise>
            <input type="hidden" name="action" value="create">
        </c:otherwise>
    </c:choose>

    <div class="field">
        <label for="text">Текст вопроса</label>
        <textarea id="text" name="text" rows="4" cols="60" required><c:out value="${requestScope.text != null ? requestScope.text : question.text}"/></textarea>
    </div>

    <c:if test="${empty question}">
        <h2>Варианты ответа</h2>
        <p>Заполни минимум два варианта. Пустые строки будут пропущены.</p>

        <c:if test="${not empty errors.options}">
            <p class="error">
                <c:out value="${errors.options}"/>
            </p>
        </c:if>

        <c:forEach begin="0" end="4" var="i">
            <div class="field">
                <label for="optionValue${i}">Значение варианта ${i + 1}</label>
                <input type="number"
                       id="optionValue${i}"
                       name="optionValues"
                       min="1"
                       max="5"
                       value="<c:out value='${paramValues.optionValues[i]}'/>">

                <label for="optionLabel${i}">Текст варианта ${i + 1}</label>
                <input type="text"
                       id="optionLabel${i}"
                       name="optionLabels"
                       maxlength="100"
                       value="<c:out value='${paramValues.optionLabels[i]}'/>">
            </div>
        </c:forEach>
    </c:if>

    <c:choose>
        <c:when test="${not empty question}">
            <button type="submit">Сохранить изменения</button>
        </c:when>
        <c:otherwise>
            <button type="submit">Создать</button>
        </c:otherwise>
    </c:choose>
</form>

<p>
    <a href="${pageContext.request.contextPath}/admin/questions">
        Вернуться к списку
    </a>
</p>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
</body>
</html>
