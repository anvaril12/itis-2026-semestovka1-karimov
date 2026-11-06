<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title><c:out value="${requestScope.errorCode}"/> — <c:out value="${requestScope.errorTitle}"/> — Соседи</title>
    <jsp:include page="/WEB-INF/views/fragments/head.jsp"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/errors.css">
</head>
<body class="page-error">
<jsp:include page="/WEB-INF/views/fragments/header.jsp"/>
<section class="error-layout" aria-labelledby="error-title">
    <div class="error-number-panel" aria-hidden="true">
        <span class="error-panel-label">соседи / небольшая остановка</span>
        <span class="error-number"><c:out value="${requestScope.errorCode}"/></span>
        <span class="error-panel-caption">Ваши люди всё ещё где-то рядом.</span>
    </div>
    <div class="error-content">
        <span class="eyebrow">Ошибка <c:out value="${requestScope.errorCode}"/></span>
        <h1 id="error-title"><c:out value="${requestScope.errorTitle}"/></h1>
        <p class="error-description"><c:out value="${requestScope.errorDescription}"/></p>
        <p class="error-hint"><c:out value="${requestScope.errorHint}"/></p>
        <c:choose>
            <c:when test="${empty sessionScope.user}">
                <a class="button-link" href="${pageContext.request.contextPath}/signin">На страницу входа <span aria-hidden="true">→</span></a>
            </c:when>
            <c:when test="${not sessionScope.user.confirmed}">
                <a class="button-link" href="${pageContext.request.contextPath}/confirm">Подтвердить аккаунт <span aria-hidden="true">→</span></a>
            </c:when>
            <c:otherwise>
                <a class="button-link" href="${pageContext.request.contextPath}/profile">Вернуться в профиль <span aria-hidden="true">→</span></a>
            </c:otherwise>
        </c:choose>
    </div>
</section>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
</body>
</html>
