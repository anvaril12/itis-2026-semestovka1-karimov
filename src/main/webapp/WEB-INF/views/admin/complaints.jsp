<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Модерация жалоб</title>
</head>
<body>
<h1>Модерация жалоб</h1>

<c:if test="${not empty sessionScope.flashSuccess}">
    <p role="status"><c:out value="${sessionScope.flashSuccess}"/></p>
    <c:remove var="flashSuccess" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.flashError}">
    <p role="alert"><c:out value="${sessionScope.flashError}"/></p>
    <c:remove var="flashError" scope="session"/>
</c:if>

<form method="get" action="${pageContext.request.contextPath}/admin/complaints">
    <label for="status">Статус жалобы</label>
    <select id="status" name="status">
        <option value="" ${empty selectedStatus ? 'selected' : ''}>Все жалобы</option>
        <option value="NEW" ${selectedStatus == 'NEW' ? 'selected' : ''}>Новые</option>
        <option value="RESOLVED" ${selectedStatus == 'RESOLVED' ? 'selected' : ''}>Решённые</option>
        <option value="REJECTED" ${selectedStatus == 'REJECTED' ? 'selected' : ''}>Отклонённые</option>
    </select>
    <button type="submit">Показать</button>
</form>

<c:choose>
    <c:when test="${empty complaints}">
        <p>Жалоб по выбранному фильтру нет.</p>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
            <tr>
                <th scope="col">ID</th>
                <th scope="col">ID отправителя</th>
                <th scope="col">ID пользователя, на которого жалуются</th>
                <th scope="col">Причина</th>
                <th scope="col">Статус</th>
                <th scope="col">Дата</th>
                <th scope="col">Действия</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="complaint" items="${complaints}">
                <tr>
                    <td><c:out value="${complaint.id}"/></td>
                    <td><c:out value="${complaint.senderId}"/></td>
                    <td><c:out value="${complaint.receiverId}"/></td>
                    <td><c:out value="${complaint.text}"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${complaint.status == 'NEW'}">Новая</c:when>
                            <c:when test="${complaint.status == 'RESOLVED'}">Решена</c:when>
                            <c:when test="${complaint.status == 'REJECTED'}">Отклонена</c:when>
                        </c:choose>
                    </td>
                    <td>
                        <c:if test="${not empty complaint.createdAt}">
                            <%-- fmt работает с java.util.Date: разбираем LocalDateTime до минут. --%>
                            <fmt:parseDate value="${fn:substring(complaint.createdAt, 0, 16)}"
                                           pattern="yyyy-MM-dd'T'HH:mm" var="createdAt"/>
                            <fmt:formatDate value="${createdAt}" pattern="dd.MM.yyyy HH:mm"/>
                        </c:if>
                    </td>
                    <td>
                        <form method="post" action="${pageContext.request.contextPath}/admin/complaints">
                            <input type="hidden" name="id" value="${complaint.id}">
                            <c:if test="${complaint.status != 'RESOLVED'}">
                                <button type="submit" name="status" value="RESOLVED">Решено</button>
                            </c:if>
                            <c:if test="${complaint.status != 'REJECTED'}">
                                <button type="submit" name="status" value="REJECTED">Отклонить</button>
                            </c:if>
                            <c:if test="${complaint.status != 'NEW'}">
                                <button type="submit" name="status" value="NEW">Вернуть в новые</button>
                            </c:if>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>

<p><a href="${pageContext.request.contextPath}/profile">Вернуться в профиль</a></p>
</body>
</html>
