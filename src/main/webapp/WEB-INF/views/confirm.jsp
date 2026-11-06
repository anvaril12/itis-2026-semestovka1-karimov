<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Подтверждение аккаунта — Соседи</title>
    <jsp:include page="/WEB-INF/views/fragments/head.jsp"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/confirm.css">
    <script src="${pageContext.request.contextPath}/js/confirm.js" defer></script>
</head>
<body class="page-confirm">
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
    <jsp:param name="navSection" value="confirm"/>
</jsp:include>
<div class="auth-layout">
    <aside class="auth-story" aria-label="Завершение регистрации">
        <span class="eyebrow">Остался один шаг</span>
        <h2>Подтвердите аккаунт.<br>Найдите своих людей.</h2>
        <p>Ваш профиль уже создан. Подтвердите его через Telegram, чтобы заполнить анкету и начать знакомство с соседями.</p>
        <div class="story-note"><span class="story-number">03 / 03</span><span>Регистрация завершена.<br>Впереди — хорошее соседство.</span></div>
    </aside>

    <section class="auth-card confirmation-card" aria-labelledby="confirm-title">
        <span class="confirmation-label">Подтверждение через Telegram</span>
        <h1 id="confirm-title">Это точно вы</h1>
        <p class="page-description">Откройте нашего бота и отправьте ему код с этой страницы.</p>

        <c:if test="${not empty error}">
            <p role="alert"><c:out value="${error}"/></p>
        </c:if>

        <div class="confirmation-code-box">
            <span class="confirmation-code-label">Ваш одноразовый код</span>
            <code id="confirmation-code"><c:out value="${code}"/></code>
            <button id="copy-code" class="button-quiet" type="button" hidden>Копировать код</button>
            <span id="copy-status" class="confirmation-copy-status" role="status"></span>
        </div>
        <p class="confirmation-expiry">Выдан в <c:out value="${issuedAt}"/>. Действует 10 минут. Не передавайте код другим людям.</p>

        <ol class="confirmation-steps">
            <li>Откройте бота и нажмите «Запустить».</li>
            <li>Отправьте ему шесть цифр кода.</li>
            <li>После ответа бота вернитесь сюда и нажмите «Я подтвердил».</li>
        </ol>

        <c:choose>
            <c:when test="${not empty botLink}">
                <a class="button-link confirmation-bot-link" href="<c:out value='${botLink}'/>" target="_blank" rel="noopener noreferrer">Открыть бота в Telegram <span aria-hidden="true">↗</span></a>
            </c:when>
            <c:otherwise>
                <p role="alert">Бот подтверждения пока недоступен. Попробуйте обновить страницу позже.</p>
            </c:otherwise>
        </c:choose>

        <form id="confirmation-form" method="post" action="${pageContext.request.contextPath}/confirm">
            <button class="button-secondary" type="submit">Я подтвердил</button>
        </form>
        <p class="confirmation-help">Код просрочен? <a href="${pageContext.request.contextPath}/confirm">Обновите страницу</a> — появится новый. Действующий код останется прежним.</p>
    </section>
</div>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
</body>
</html>
