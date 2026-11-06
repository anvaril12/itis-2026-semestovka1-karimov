<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Модерация отзывов</title>
    <jsp:include page="/WEB-INF/views/fragments/head.jsp"/>
</head>
<body class="page-admin-reviews">
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
    <jsp:param name="navSection" value="admin-reviews"/>
</jsp:include>
<h1>Модерация отзывов</h1>
<p class="page-description">Все отзывы об общежитиях. Удаляйте публикации, нарушающие правила.</p>

<c:if test="${not empty sessionScope.flashSuccess}">
    <p class="flash success" role="status"><c:out value="${sessionScope.flashSuccess}"/></p>
    <c:remove var="flashSuccess" scope="session"/>
</c:if>

<c:choose>
    <c:when test="${empty reviews}">
        <p>Отзывов пока нет.</p>
    </c:when>
    <c:otherwise>
        <div class="table-scroll" role="region" aria-label="Таблица данных" tabindex="0">
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Автор</th>
                <th>Общежитие</th>
                <th>Оценка</th>
                <th>Текст</th>
                <th>Дата</th>
                <th>Действие</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="review" items="${reviews}">
                <tr>
                    <td><c:out value="${review.id}"/></td>
                    <td><c:out value="${review.userId}"/></td>
                    <td><c:out value="${review.dormitoryId}"/></td>
                    <td><c:out value="${review.rating}"/>/5</td>
                    <td><c:out value="${review.text}"/></td>
                    <td><c:out value="${review.formattedCreatedAt}"/></td>
                    <td>
                        <form method="post" action="${pageContext.request.contextPath}/admin/reviews">
                            <input type="hidden" name="id" value="${review.id}">
                            <button class="button-danger" type="submit">Удалить</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        </div>
    </c:otherwise>
</c:choose>

<p><a href="${pageContext.request.contextPath}/profile">Вернуться в профиль</a></p>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
</body>
</html>
