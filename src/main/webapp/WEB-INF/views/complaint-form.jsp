<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Жалоба на пользователя</title>
    <jsp:include page="/WEB-INF/views/fragments/head.jsp"/>
</head>
<body class="page-matches">
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
    <jsp:param name="navSection" value="matches"/>
</jsp:include>
<h1>Жалоба на пользователя</h1>
<p class="page-description">Опишите ситуацию — администратор рассмотрит ваше обращение.</p>

<c:if test="${not empty errors.complaint}">
    <p><c:out value="${errors.complaint}"/></p>
</c:if>
<c:if test="${not empty errors.text}">
    <p><c:out value="${errors.text}"/></p>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/complaints">
    <input type="hidden" name="receiverId" value="${receiverId}">
    <label for="text">Причина жалобы</label>
    <textarea id="text" name="text" maxlength="1000" required><c:out value="${text}"/></textarea>
    <button type="submit">Отправить жалобу</button>
</form>

<p><a href="${pageContext.request.contextPath}/matches">Вернуться к подбору соседей</a></p>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
</body>
</html>
