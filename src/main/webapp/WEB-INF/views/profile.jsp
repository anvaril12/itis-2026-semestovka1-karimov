<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Профиль</title>
    <jsp:include page="/WEB-INF/views/fragments/head.jsp"/>
</head>
<body class="page-profile">
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
    <jsp:param name="navSection" value="profile"/>
</jsp:include>
<span class="eyebrow page-eyebrow">Личное пространство</span>
<h1>Ваш профиль</h1>
<p class="page-description">Здесь начинается хорошее соседство.</p>
<c:if test="${not empty sessionScope.flashSuccess}">
    <p class="flash success" role="status"><c:out value="${sessionScope.flashSuccess}"/></p>
    <c:remove var="flashSuccess" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.flashError}">
    <p class="flash error" role="alert"><c:out value="${sessionScope.flashError}"/></p>
    <c:remove var="flashError" scope="session"/>
</c:if>

<section class="profile-summary" aria-label="Данные профиля">
    <p><span>Логин</span><strong><c:out value="${sessionScope.user.login}"/></strong></p>
    <p><span>Имя</span><strong><c:out value="${sessionScope.user.firstName}"/> <c:out value="${sessionScope.user.lastName}"/></strong></p>
    <p><span>Курс</span><strong><c:out value="${sessionScope.user.course}"/></strong></p>
</section>

<div class="profile-grid">
    <section class="content-card">
        <div class="section-head">
            <div><span class="card-eyebrow">Моё пространство</span><h2>Общежитие</h2></div>
            <span class="section-symbol" aria-hidden="true">⌂</span>
        </div>
        <c:choose>
            <c:when test="${not empty currentDormitory}">
                <div class="current-dorm">
                    <span class="field-caption">Сейчас вы живёте здесь</span>
                    <strong><c:out value="${currentDormitory.name}"/></strong>
                    <a class="text-link" href="${pageContext.request.contextPath}/reviews?dormitoryId=${currentDormitory.id}">Отзывы об общежитии <span aria-hidden="true">↗</span></a>
                </div>
            </c:when>
            <c:otherwise><p class="muted">Выберите общежитие, чтобы находить соседей рядом.</p></c:otherwise>
        </c:choose>
        <form class="stack-form" method="post" action="${pageContext.request.contextPath}/profile">
            <div class="field">
                <label for="university">Университет</label>
                <select id="university" name="universityId" required data-url="${pageContext.request.contextPath}/api/dormitories">
                    <option value="">Выберите университет</option>
                    <c:forEach items="${universities}" var="u">
                        <option value="${u.id}"><c:out value="${u.name}"/></option>
                    </c:forEach>
                </select>
            </div>
            <div class="field">
                <label for="dormitory">Общежитие</label>
                <select id="dormitory" name="dormitoryId" required disabled>
                    <option value="">Сначала выберите университет</option>
                </select>
                <span class="error"><c:out value="${errors.dormitory}"/></span>
            </div>
            <button type="submit">Сохранить изменения</button>
        </form>
    </section>

    <section class="content-card">
        <div class="section-head">
            <div><span class="card-eyebrow">Будущее соседство</span><h2>Заявки в комнаты</h2></div>
        </div>
        <c:choose>
            <c:when test="${empty myApplications}">
                <div class="empty-state">
                    <span class="empty-symbol" aria-hidden="true">⌂</span>
                    <h3>Ваша комната ещё впереди</h3>
                    <p>Здесь появятся комнаты, в которые вы подали заявку.</p>
                    <a class="button-link button-secondary" href="${pageContext.request.contextPath}/rooms">Посмотреть комнаты</a>
                </div>
            </c:when>
            <c:otherwise>
                <ul class="row-list">
                    <c:forEach var="app" items="${myApplications}">
                        <li>
                            <a href="${pageContext.request.contextPath}/rooms/view?id=${app.roomEntity.id}">Комната <c:out value="${app.roomEntity.roomNumber}"/></a>
                            <c:choose>
                                <c:when test="${app.roomMemberEntity.status == 'PENDING'}"><span class="badge badge-waiting">Ждёт ответа</span></c:when>
                                <c:when test="${app.roomMemberEntity.status == 'ACCEPTED'}"><span class="badge">Вы живёте здесь</span></c:when>
                                <c:otherwise><span class="badge badge-neutral">Отклонена</span></c:otherwise>
                            </c:choose>
                        </li>
                    </c:forEach>
                </ul>
            </c:otherwise>
        </c:choose>
        <div class="card-footer"><a class="text-link" href="${pageContext.request.contextPath}/rooms/create">Создать свою комнату <span aria-hidden="true">↗</span></a></div>
    </section>
</div>

<nav class="shortcut-grid" aria-label="Быстрые действия">
    <a class="shortcut-card" href="${pageContext.request.contextPath}/questionnaire">
        <span class="shortcut-number">01 <span aria-hidden="true">↗</span></span>
        <strong>Мои привычки</strong><span>Заполните анкету для точного подбора.</span>
    </a>
    <a class="shortcut-card" href="${pageContext.request.contextPath}/matches">
        <span class="shortcut-number">02 <span aria-hidden="true">↗</span></span>
        <strong>Найти своих людей</strong><span>Посмотрите, с кем совпадает ваш ритм.</span>
    </a>
    <a class="shortcut-card" href="${pageContext.request.contextPath}/requests">
        <span class="shortcut-number">03 <span aria-hidden="true">↗</span></span>
        <strong>Мои заявки</strong><span>Проверьте ответы будущих соседей.</span>
    </a>
</nav>
<script src="${pageContext.request.contextPath}/js/profile.js"></script>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
</body>
</html>
