<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="errorCode" value="403" scope="request"/>
<c:set var="errorTitle" value="Доступ закрыт" scope="request"/>
<c:set var="errorDescription" value="У вашего аккаунта нет прав для просмотра этой страницы или выполнения этого действия." scope="request"/>
<c:set var="errorHint" value="Вернитесь на свою страницу и продолжите пользоваться сервисом." scope="request"/>
<jsp:include page="/WEB-INF/views/errors/layout.jsp"/>
