<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Соседи</title>
    <jsp:include page="/WEB-INF/views/fragments/head.jsp"/>
</head>
<body class="page-profile">
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
    <jsp:param name="navSection" value="profile"/>
</jsp:include>
<h1>Подбор соседей</h1>
<p class="page-description">Найдите людей, с которыми комфортно жить в одном пространстве.</p>

<%-- user кладёт в сессию SignInServlet; сюда без входа не пустит AuthFilter --%>
<p>Вы вошли как <c:out value="${sessionScope.user.login}"/></p>

<p><a class="button-link" href="${pageContext.request.contextPath}/matches">Найти соседей</a></p>
<p><a href="${pageContext.request.contextPath}/questionnaire">Заполнить анкету привычек</a></p>
<p><a href="${pageContext.request.contextPath}/rooms">Посмотреть комнаты</a></p>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
</body>
</html>
