<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Добавить вопрос</title>
</head>
<body>
<h1>Добавить вопрос</h1>

<c:if test="${not empty errors.text}">
    <p class="error">
        <c:out value="${errors.text}"/>
    </p>
</c:if>

<form method="post"
      action="${pageContext.request.contextPath}/admin/questions">
    <input type="hidden" name="action" value="create">

    <div class="field">
        <label for="text">Текст вопроса</label>
        <textarea id="text" name="text" rows="4" cols="60" required><c:out value="${text}"/></textarea>
    </div>

    <h2>Варианты ответа</h2>
    <p>Заполни минимум два варианта. Пустые строки будут пропущены.</p>

    <c:forEach begin="0" end="4" var="i">
        <div class="field">
            <label for="optionValue${i}">Значение варианта ${i + 1}</label>
            <input type="number"
                   id="optionValue${i}"
                   name="optionValues"
                   min="1"
                   max="5"
                   value="${paramValues.optionValues[i]}">

            <label for="optionLabel${i}">Текст варианта ${i + 1}</label>
            <input type="text"
                   id="optionLabel${i}"
                   name="optionLabels"
                   maxlength="100"
                   value="<c:out value='${paramValues.optionLabels[i]}'/>">
        </div>
    </c:forEach>

    <button type="submit">Создать</button>
</form>

<p>
    <a href="${pageContext.request.contextPath}/admin/questions">
        Вернуться к списку
    </a>
</p>
</body>
</html>
