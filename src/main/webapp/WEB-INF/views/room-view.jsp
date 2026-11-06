<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="room" value="${roomView.roomEntity}"/>
<c:set var="owner" value="${roomView.owner}"/>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Комната <c:out value="${room.roomNumber}"/></title>
    <jsp:include page="/WEB-INF/views/fragments/head.jsp"/>
</head>
<body class="page-rooms">
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
    <jsp:param name="navSection" value="rooms"/>
</jsp:include>
<a class="back-link" href="${pageContext.request.contextPath}/rooms"><span aria-hidden="true">←</span> Все комнаты</a>
<h1>Комната <c:out value="${room.roomNumber}"/></h1>
<p class="page-description">Общее пространство для вашей компании.</p>
<c:if test="${not empty sessionScope.flashSuccess}">
    <p class="flash success" role="status"><c:out value="${sessionScope.flashSuccess}"/></p>
    <c:remove var="flashSuccess" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.flashError}">
    <p class="flash error" role="alert"><c:out value="${sessionScope.flashError}"/></p>
    <c:remove var="flashError" scope="session"/>
</c:if>

<div class="room-layout">
    <div class="room-main">
        <section class="content-card">
            <div class="section-head"><span class="card-eyebrow">О комнате</span><span class="badge badge-neutral"><c:out value="${room.capacity}"/> места</span></div>
            <c:choose>
                <c:when test="${not empty room.description}"><p class="room-description"><c:out value="${room.description}"/></p></c:when>
                <c:otherwise><p class="muted">Владелец пока не добавил описание.</p></c:otherwise>
            </c:choose>
            <div class="person-row">
                <span class="person-avatar" aria-hidden="true"><c:out value="${fn:substring(owner.firstName, 0, 1)}"/><c:out value="${fn:substring(owner.lastName, 0, 1)}"/></span>
                <div><strong><c:out value="${owner.firstName}"/> <c:out value="${owner.lastName}"/></strong><span class="person-caption">Владелец · <c:out value="${owner.course}"/> курс</span></div>
            </div>

            <c:if test="${sessionScope.user.id == owner.id}">
                <div class="action-row card-footer">
                    <a class="button-link button-secondary" href="${pageContext.request.contextPath}/rooms/edit?id=${room.id}">Редактировать комнату</a>
                    <form class="inline-form" method="post" action="${pageContext.request.contextPath}/rooms/edit" onsubmit="return confirm('Удалить комнату?');">
                        <input type="hidden" name="id" value="${room.id}">
                        <input type="hidden" name="action" value="delete">
                        <button class="button-danger" type="submit">Удалить</button>
                    </form>
                </div>
            </c:if>

            <c:if test="${sessionScope.user.id != owner.id}">
                <c:set var="myStatus" value=""/>
                <c:forEach var="m" items="${members}">
                    <c:if test="${m.user.id == sessionScope.user.id}"><c:set var="myStatus" value="${m.roomMemberEntity.status}"/></c:if>
                </c:forEach>
                <div class="card-footer">
                    <c:choose>
                        <c:when test="${myStatus == 'PENDING'}"><p class="status-note">Ваша заявка ждёт ответа владельца.</p></c:when>
                        <c:when test="${myStatus == 'ACCEPTED'}"><p class="status-note">Вы живёте в этой комнате.</p></c:when>
                        <c:when test="${myStatus == 'REJECTED'}"><p class="status-note">Владелец отклонил вашу заявку.</p></c:when>
                        <c:otherwise>
                            <form class="inline-form" method="post" action="${pageContext.request.contextPath}/rooms/join">
                                <input type="hidden" name="roomId" value="${room.id}">
                                <button type="submit">Хочу в эту комнату</button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
        </section>

        <c:if test="${sessionScope.user.id == owner.id}">
            <section class="content-card">
                <div class="section-head"><div><span class="card-eyebrow">Знакомимся ближе</span><h2>Заявки в комнату</h2></div></div>
                <c:set var="hasPending" value="false"/>
                <c:forEach var="m" items="${members}">
                    <c:if test="${m.roomMemberEntity.status == 'PENDING'}">
                        <c:set var="hasPending" value="true"/>
                        <div class="member-request">
                            <div class="person-row">
                                <span class="person-avatar" aria-hidden="true"><c:out value="${fn:substring(m.user.firstName, 0, 1)}"/><c:out value="${fn:substring(m.user.lastName, 0, 1)}"/></span>
                                <div><strong><c:out value="${m.user.firstName}"/> <c:out value="${m.user.lastName}"/></strong><span class="person-caption"><c:out value="${m.user.course}"/> курс</span></div>
                            </div>
                            <ul class="compatibility-list">
                                <c:forEach var="entry" items="${m.compatibility}">
                                    <li><span>С <c:out value="${entry.key}"/></span>
                                        <c:choose>
                                            <c:when test="${empty entry.value}"><span class="muted">Несовместимы или нет анкеты</span></c:when>
                                            <c:otherwise><span class="badge"><c:out value="${entry.value}"/>%</span></c:otherwise>
                                        </c:choose>
                                    </li>
                                </c:forEach>
                            </ul>
                            <form class="inline-form" method="post" action="${pageContext.request.contextPath}/rooms/members">
                                <input type="hidden" name="roomId" value="${room.id}">
                                <input type="hidden" name="memberId" value="${m.user.id}">
                                <button type="submit" name="action" value="accept">Принять</button>
                                <button type="submit" name="action" value="reject">Отклонить</button>
                            </form>
                        </div>
                    </c:if>
                </c:forEach>
                <c:if test="${not hasPending}"><div class="empty-state empty-state-compact"><h3>Все заявки рассмотрены</h3><p>Новые обращения будущих соседей появятся здесь.</p></div></c:if>
            </section>
        </c:if>
    </div>

    <aside class="content-card residents-card">
        <span class="card-eyebrow">Ваша компания</span>
        <h2>Жильцы</h2>
        <ul class="residents-list">
            <li class="person-row">
                <span class="person-avatar" aria-hidden="true"><c:out value="${fn:substring(owner.firstName, 0, 1)}"/><c:out value="${fn:substring(owner.lastName, 0, 1)}"/></span>
                <div><strong><c:out value="${owner.firstName}"/> <c:out value="${owner.lastName}"/></strong><span class="person-caption">Владелец</span></div>
            </li>
            <c:forEach var="m" items="${members}">
                <c:if test="${m.roomMemberEntity.status == 'ACCEPTED'}">
                    <li class="person-row">
                        <span class="person-avatar" aria-hidden="true"><c:out value="${fn:substring(m.user.firstName, 0, 1)}"/><c:out value="${fn:substring(m.user.lastName, 0, 1)}"/></span>
                        <div><strong><c:out value="${m.user.firstName}"/> <c:out value="${m.user.lastName}"/></strong><span class="person-caption">Жилец</span></div>
                    </li>
                </c:if>
            </c:forEach>
        </ul>
        <p class="residents-note">Вместимость комнаты — <c:out value="${room.capacity}"/> чел.</p>
    </aside>
</div>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
</body>
</html>
