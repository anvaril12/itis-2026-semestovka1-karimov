<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вход</title>
    <jsp:include page="/WEB-INF/views/fragments/head.jsp"/>
</head>
<body class="page-signin">
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
    <jsp:param name="navSection" value="signin"/>
</jsp:include>
<div class="auth-layout">
    <aside class="auth-story" aria-label="О сервисе">
        <span class="eyebrow">Хорошее соседство начинается здесь</span>
        <h2>Общее пространство.<br>Близкие привычки.</h2>
        <p>Найдите соседей, с которыми комфортно учиться, отдыхать и просто быть собой.</p>
        <div class="story-note"><span class="story-number">01—03</span><span>Расскажите о себе.<br>Найдите своих людей.<br>Планируйте жить вместе.</span></div>
    </aside>
    <section class="auth-card" aria-labelledby="auth-title">
<h1 id="auth-title">Вход</h1>
<p class="page-description">С возвращением! Войдите, чтобы продолжить.</p>

<c:if test="${not empty error}">
    <p class="error"><c:out value="${error}"/></p>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/signin">

    <div class="field">
        <label for="login">Логин</label>
        <input type="text" id="login" name="login" value="<c:out value='${login}'/>" autocomplete="username" required>
    </div>

    <div class="field">
        <label for="password">Пароль</label>
        <input type="password" id="password" name="password" autocomplete="current-password" required>
    </div>

    <button type="submit">Войти</button>
</form>

<p>Нет аккаунта? <a href="${pageContext.request.contextPath}/signup">Зарегистрироваться</a></p>
</section>
</div>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
</body>
</html>
