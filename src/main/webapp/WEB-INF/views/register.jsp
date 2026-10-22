<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Регистрация</title>
</head>
<body>
<h1>Регистрация</h1>

<%-- name каждого поля совпадает с request.getParameter(...) в RegistrationServlet.
     value берём из form (введённое ранее), ошибку — из errors; оба через c:out против XSS --%>
<form method="post" action="${pageContext.request.contextPath}/register">

    <div class="field">
        <label for="login">Логин</label>
        <input type="text" id="login" name="login" value="<c:out value='${form.login}'/>"
               required minlength="3" maxlength="12" pattern="[a-zA-Z0-9_]+">
        <span class="error"><c:out value="${errors.login}"/></span>
    </div>

    <div class="field">
        <label for="firstName">Имя</label>
        <input type="text" id="firstName" name="firstName" value="<c:out value='${form.firstName}'/>"
               required maxlength="50">
        <span class="error"><c:out value="${errors.firstName}"/></span>
    </div>

    <div class="field">
        <label for="lastName">Фамилия</label>
        <input type="text" id="lastName" name="lastName" value="<c:out value='${form.lastName}'/>"
               required maxlength="50">
        <span class="error"><c:out value="${errors.lastName}"/></span>
    </div>

    <div class="field">
        <label for="email">Email</label>
        <input type="email" id="email" name="email" value="<c:out value='${form.email}'/>"
               required maxlength="50">
        <span class="error"><c:out value="${errors.email}"/></span>
    </div>

    <%-- Пароли обратно в форму не подставляем: не возвращаем пароль в HTML-ответе --%>
    <div class="field">
        <label for="password">Пароль</label>
        <input type="password" id="password" name="password" required minlength="8" maxlength="72">
        <span class="error"><c:out value="${errors.password}"/></span>
    </div>

    <div class="field">
        <label for="passwordConfirm">Повторите пароль</label>
        <input type="password" id="passwordConfirm" name="passwordConfirm" required>
        <span class="error"><c:out value="${errors.passwordConfirm}"/></span>
    </div>

    <div class="field">
        <label for="gender">Пол</label>
        <select id="gender" name="gender" required>
            <option value="">— выберите —</option>
            <option value="MALE" ${form.gender == 'MALE' ? 'selected' : ''}>Мужской</option>
            <option value="FEMALE" ${form.gender == 'FEMALE' ? 'selected' : ''}>Женский</option>
        </select>
        <span class="error"><c:out value="${errors.gender}"/></span>
    </div>

    <div class="field">
        <label for="contacts">Телефон</label>
        <input type="tel" id="contacts" name="contacts" value="<c:out value='${form.contacts}'/>"
               required pattern="89\d{9}|\+79\d{9}" placeholder="89991234567">
        <span class="error"><c:out value="${errors.contacts}"/></span>
    </div>

    <div class="field">
        <label for="course">Курс</label>
        <input type="number" id="course" name="course" value="<c:out value='${form.course}'/>"
               required min="1" max="6">
        <span class="error"><c:out value="${errors.course}"/></span>
    </div>

    <div class="field">
        <label for="birthYear">Год рождения</label>
        <input type="number" id="birthYear" name="birthYear" value="<c:out value='${form.birthYear}'/>"
               required min="1990">
        <span class="error"><c:out value="${errors.birthYear}"/></span>
    </div>

    <button type="submit">Зарегистрироваться</button>
</form>

<p>Уже есть аккаунт? <a href="${pageContext.request.contextPath}/login">Войти</a></p>
</body>
</html>
