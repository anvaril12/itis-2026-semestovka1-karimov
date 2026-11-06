<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Подбор соседей</title>
    <jsp:include page="/WEB-INF/views/fragments/head.jsp"/>
</head>
<body class="page-matches">
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
    <jsp:param name="navSection" value="matches"/>
</jsp:include>
<span class="eyebrow page-eyebrow">Люди рядом</span>
<h1>Подбор соседей</h1>
<p class="page-description">Найдите человека, с которым совпадает ваш ритм жизни.</p>

<c:if test="${not empty sessionScope.flashSuccess}">
    <p class="flash success" role="status"><c:out value="${sessionScope.flashSuccess}"/></p>
    <c:remove var="flashSuccess" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.flashError}">
    <p class="flash error" role="alert"><c:out value="${sessionScope.flashError}"/></p>
    <c:remove var="flashError" scope="session"/>
</c:if>

<c:choose>
    <c:when test="${empty sessionScope.user.dormitoryId}">
        <section class="content-card empty-state matches-empty">
            <span class="empty-symbol" aria-hidden="true">⌂</span>
            <h2>Сначала найдём ваше общежитие</h2>
            <p>Выберите его в профиле, чтобы увидеть студентов, которые живут рядом.</p>
            <a class="button-link" href="${pageContext.request.contextPath}/profile">Перейти в профиль</a>
        </section>
    </c:when>
    <c:when test="${empty matches}">
        <section class="content-card empty-state matches-empty">
            <span class="empty-symbol" aria-hidden="true">⌂</span>
            <h2>Ваши люди ещё впереди</h2>
            <p>Подходящих соседей пока нет. Проверьте, что анкета заполнена, или загляните позже.</p>
            <a class="button-link" href="${pageContext.request.contextPath}/questionnaire">Перейти к анкете</a>
        </section>
    </c:when>
    <c:otherwise>
        <div class="matches-section-head">
            <div>
                <h2 id="matches-title">В вашем общежитии <span class="count-badge"><c:out value="${fn:length(matches)}"/></span></h2>
                <p>Сначала — самые совместимые по анкете.</p>
            </div>
            <a class="button-link button-secondary" href="${pageContext.request.contextPath}/requests">Мои заявки <span aria-hidden="true">↗</span></a>
        </div>
        <div class="matches-grid" role="list" aria-labelledby="matches-title">
            <c:forEach items="${matches}" var="m">
                <article class="match-card" role="listitem" aria-labelledby="match-name-${m.user.id}">
                    <div class="match-person">
                        <span class="match-avatar" aria-hidden="true"><c:out value="${fn:substring(m.user.firstName, 0, 1)}"/><c:out value="${fn:substring(m.user.lastName, 0, 1)}"/></span>
                        <div>
                            <h2 id="match-name-${m.user.id}"><c:out value="${m.user.firstName}"/> <c:out value="${m.user.lastName}"/></h2>
                            <p><c:out value="${m.user.course}"/> курс · Ваше общежитие</p>
                        </div>
                    </div>

                    <div class="match-compatibility">
                        <div class="compatibility-heading">
                            <span>Совместимость привычек</span>
                            <span class="compatibility-score"><c:out value="${m.percent}"/><small>%</small></span>
                        </div>
                        <progress class="match-progress" max="100" value="${m.percent}" aria-hidden="true"></progress>
                    </div>

                    <div class="match-actions">
                        <form class="inline-form" method="post" action="${pageContext.request.contextPath}/request/send">
                            <input type="hidden" name="receiverId" value="${m.user.id}">
                            <button type="submit">Хочу жить вместе <span aria-hidden="true">↗</span></button>
                        </form>
                        <a class="match-report" href="${pageContext.request.contextPath}/complaints?receiverId=${m.user.id}">Пожаловаться</a>
                    </div>
                </article>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
</body>
</html>
