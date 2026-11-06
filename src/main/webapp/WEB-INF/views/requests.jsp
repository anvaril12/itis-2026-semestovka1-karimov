<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Мои заявки</title>
    <jsp:include page="/WEB-INF/views/fragments/head.jsp"/>
</head>
<body class="page-requests">
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
    <jsp:param name="navSection" value="requests"/>
</jsp:include>
<div class="requests-page-head">
    <div>
        <span class="eyebrow page-eyebrow">Будущие соседи</span>
        <h1>Мои заявки</h1>
        <p class="page-description">Первый шаг к хорошему соседству — познакомиться.</p>
    </div>
    <a class="button-link button-secondary" href="${pageContext.request.contextPath}/matches">Найти соседей <span aria-hidden="true">↗</span></a>
</div>

<%-- flash-сообщения: показываем один раз и сразу удаляем из сессии --%>
<c:if test="${not empty sessionScope.flashSuccess}">
    <p class="flash success" role="status"><c:out value="${sessionScope.flashSuccess}"/></p>
    <c:remove var="flashSuccess" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.flashError}">
    <p class="flash error" role="alert"><c:out value="${sessionScope.flashError}"/></p>
    <c:remove var="flashError" scope="session"/>
</c:if>

<p class="requests-privacy">
    <svg aria-hidden="true" width="15" height="15" viewBox="0 0 20 20" fill="none"><rect x="4.5" y="8.5" width="11" height="8" rx="2" stroke="currentColor" stroke-width="1.3"/><path d="M7 8.5V6a3 3 0 0 1 6 0v2.5" stroke="currentColor" stroke-width="1.3"/></svg>
    Контакты откроются после принятия заявки.
</p>

<div class="requests-layout">
    <section class="request-column" aria-labelledby="incoming-title">
        <div class="request-column-head">
            <span class="request-direction" aria-hidden="true">↓</span>
            <div><h2 id="incoming-title">Входящие <span class="count-badge"><c:out value="${fn:length(incoming)}"/></span></h2><p>Кто хочет жить с вами</p></div>
        </div>
        <div class="request-list">
            <c:choose>
                <c:when test="${empty incoming}">
                    <div class="content-card empty-state requests-empty">
                        <span class="requests-empty-icon" aria-hidden="true">↓</span>
                        <h3>Пока тихо</h3>
                        <p>Когда кто-нибудь предложит жить вместе, заявка появится здесь.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${incoming}" var="r"><ui:requestCard requestView="${r}" incoming="${true}"/></c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </section>

    <section class="request-column" aria-labelledby="outgoing-title">
        <div class="request-column-head">
            <span class="request-direction request-direction-out" aria-hidden="true">↑</span>
            <div><h2 id="outgoing-title">Исходящие <span class="count-badge"><c:out value="${fn:length(outgoing)}"/></span></h2><p>Кому вы предложили соседство</p></div>
        </div>
        <div class="request-list">
            <c:choose>
                <c:when test="${empty outgoing}">
                    <div class="content-card empty-state requests-empty">
                        <span class="requests-empty-icon" aria-hidden="true">↑</span>
                        <h3>Сделайте первый шаг</h3>
                        <p>Найдите человека с похожими привычками и предложите жить вместе.</p>
                        <a class="text-link" href="${pageContext.request.contextPath}/matches">Посмотреть соседей <span aria-hidden="true">↗</span></a>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${outgoing}" var="r"><ui:requestCard requestView="${r}" incoming="${false}"/></c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </section>
</div>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
</body>
</html>
