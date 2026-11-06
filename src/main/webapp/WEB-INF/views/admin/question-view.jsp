<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вопрос анкеты</title>
    <jsp:include page="/WEB-INF/views/fragments/head.jsp"/>
</head>
<body class="page-admin-questions">
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
    <jsp:param name="navSection" value="admin-questions"/>
</jsp:include>
<a class="back-link" href="${pageContext.request.contextPath}/admin/questions"><span aria-hidden="true">←</span> Все вопросы</a>
<h1>Вопрос анкеты</h1>
<p class="page-description">Настройки вопроса и его доступность для студентов.</p>

<section class="content-card detail-card">
    <div class="section-head">
        <span class="card-eyebrow">Вопрос № <c:out value="${question.id}"/></span>
        <c:choose>
            <c:when test="${question.active}"><span class="badge"><span class="status-dot"></span>Активен</span></c:when>
            <c:otherwise><span class="badge badge-neutral"><span class="status-dot"></span>Неактивен</span></c:otherwise>
        </c:choose>
    </div>
    <h2 class="question-title"><c:out value="${question.text}"/></h2>
    <p class="muted">
        <c:choose>
            <c:when test="${question.active}">Студенты видят этот вопрос при заполнении анкеты.</c:when>
            <c:otherwise>Этот вопрос скрыт из анкеты. Его можно активировать снова.</c:otherwise>
        </c:choose>
    </p>
    <div class="action-row card-footer">
        <a class="button-link" href="${pageContext.request.contextPath}/admin/questions?action=edit&amp;id=${question.id}">Изменить вопрос</a>
        <c:choose>
            <c:when test="${question.active}">
                <form class="inline-form" method="post" action="${pageContext.request.contextPath}/admin/questions" onsubmit="return confirm('Отключить этот вопрос?');">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="${question.id}">
                    <button class="button-secondary" type="submit">Отключить</button>
                </form>
            </c:when>
            <c:otherwise>
                <form class="inline-form" method="post" action="${pageContext.request.contextPath}/admin/questions">
                    <input type="hidden" name="action" value="activate">
                    <input type="hidden" name="id" value="${question.id}">
                    <button class="button-secondary" type="submit">Активировать</button>
                </form>
            </c:otherwise>
        </c:choose>
    </div>
</section>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
</body>
</html>
