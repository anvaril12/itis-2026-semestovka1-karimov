<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Анкета</title>
    <jsp:include page="/WEB-INF/views/fragments/head.jsp"/>
</head>
<body class="page-questionnaire">
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
    <jsp:param name="navSection" value="questionnaire"/>
</jsp:include>
<h1>Анкета</h1>
<p class="page-description">Расскажите о своих привычках и о том, что важно для вас.</p>
<p>Ответьте на вопросы — по ним мы подберём совместимых соседей.</p>

<c:if test="${not empty errors}">
    <p class="error">Ответы не сохранены — исправьте ошибки ниже.</p>
</c:if>

<%-- errors есть только после неудачного POST: тогда показываем то, что пользователь
     только что отправил (param), иначе — сохранённые ответы из БД (userAnswer) --%>
<c:set var="resubmit" value="${not empty errors}"/>

<form method="post" action="${pageContext.request.contextPath}/questionnaire">
    <c:forEach items="${questions}" var="view">
        <c:set var="qid" value="${view.question.id}"/>
        <c:set var="selectedValue"
               value="${resubmit ? param['value_' += qid] : view.userAnswer.value}"/>
        <c:set var="importance"
               value="${resubmit ? param['importance_' += qid] : view.userAnswer.importance}"/>
        <c:set var="dealBreaker"
               value="${resubmit ? not empty param['dealbreaker_' += qid] : view.userAnswer.dealBreaker}"/>

        <fieldset>
            <legend><c:out value="${view.question.text}"/></legend>

            <c:forEach items="${view.options}" var="opt">
                <label>
                    <input type="radio" name="value_${qid}" value="${opt.value}" required
                        ${selectedValue == opt.value ? 'checked' : ''}>
                    <c:out value="${opt.label}"/>
                </label>
            </c:forEach>
            <span class="error"><c:out value="${errors['value_' += qid]}"/></span>

            <div class="field">
                <label for="importance_${qid}">Насколько это важно для вас (0–10)</label>
                <input type="number" id="importance_${qid}" name="importance_${qid}"
                       min="0" max="10" required value="<c:out value='${importance}'/>">
                <span class="error"><c:out value="${errors['importance_' += qid]}"/></span>
            </div>

            <label>
                <input type="checkbox" name="dealbreaker_${qid}" ${dealBreaker ? 'checked' : ''}>
                Не готов жить с человеком, который ответил иначе
            </label>
        </fieldset>
    </c:forEach>

    <button type="submit">Сохранить ответы</button>
</form>

<p><a href="${pageContext.request.contextPath}/profile">Вернуться в профиль</a></p>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
</body>
</html>
