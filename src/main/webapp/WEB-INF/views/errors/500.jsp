<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="errorCode" value="500" scope="request"/>
<c:set var="errorTitle" value="Что-то пошло не так" scope="request"/>
<c:set var="errorDescription" value="На сервере произошла ошибка. Сейчас не удалось обработать ваш запрос." scope="request"/>
<c:set var="errorHint" value="Попробуйте вернуться на свою страницу. Если ошибка повторяется, зайдите немного позже." scope="request"/>
<jsp:include page="/WEB-INF/views/errors/layout.jsp"/>
