<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="errorCode" value="404" scope="request"/>
<c:set var="errorTitle" value="Страница не найдена" scope="request"/>
<c:set var="errorDescription" value="Возможно, в адресе опечатка, ссылка устарела или страница больше не существует." scope="request"/>
<c:set var="errorHint" value="Проверьте адрес или вернитесь к знакомству с будущими соседями." scope="request"/>
<jsp:include page="/WEB-INF/views/errors/layout.jsp"/>
