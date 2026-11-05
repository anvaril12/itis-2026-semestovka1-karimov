<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Отзывы об общежитии</title>
</head>
<body>
<h1>Отзывы об общежитии</h1>

<c:if test="${not empty sessionScope.flashSuccess}">
    <p><c:out value="${sessionScope.flashSuccess}"/></p>
    <c:remove var="flashSuccess" scope="session"/>
</c:if>

<c:if test="${not empty errors.review}">
    <p><c:out value="${errors.review}"/></p>
</c:if>

<c:if test="${not empty errors.text}">
    <p><c:out value="${errors.text}"/></p>
</c:if>

<c:if test="${not empty errors.rating}">
    <p><c:out value="${errors.rating}"/></p>
</c:if>

<h2>Оставить отзыв</h2>
<form method="post" action="${pageContext.request.contextPath}/reviews">
    <input type="hidden" name="action" value="create">
    <input type="hidden" name="dormitoryId" value="${dormitoryId}">

    <p>
        <label for="text">Текст</label>
        <textarea id="text" name="text" maxlength="1000" required><c:out value="${text}"/></textarea>
    </p>

    <p>
        <label for="rating">Оценка</label>
        <select id="rating" name="rating" required>
            <option value="">Выберите оценку</option>
            <c:forEach var="value" begin="1" end="5">
                <option value="${value}">
                    <c:out value="${value}"/>
                </option>
            </c:forEach>
        </select>
    </p>

    <button type="submit">Добавить отзыв</button>
</form>

<h2>Все отзывы</h2>

<c:choose>
    <c:when test="${empty reviews}">
        <p>Отзывов пока нет.</p>
    </c:when>
    <c:otherwise>
        <c:forEach var="review" items="${reviews}">
            <article>
                <p>
                    Оценка:
                    <c:out value="${review.rating}"/>/5
                </p>
                <p><c:out value="${review.text}"/></p>

                <c:if test="${sessionScope.user.id == review.userId}">
                    <details>
                        <summary>Редактировать</summary>
                        <form method="post" action="${pageContext.request.contextPath}/reviews">
                            <input type="hidden" name="action" value="update">
                            <input type="hidden" name="id" value="${review.id}">
                            <input type="hidden" name="dormitoryId" value="${dormitoryId}">

                            <p>
                                <label for="text-${review.id}">Текст</label>
                                <textarea id="text-${review.id}" name="text" maxlength="1000" required><c:out value="${review.text}"/></textarea>
                            </p>

                            <p>
                                <label for="rating-${review.id}">Оценка</label>
                                <select id="rating-${review.id}" name="rating" required>
                                    <c:forEach var="value" begin="1" end="5">
                                        <option value="${value}"
                                                <c:if test="${value == review.rating}">selected</c:if>>
                                            <c:out value="${value}"/>
                                        </option>
                                    </c:forEach>
                                </select>
                            </p>

                            <button type="submit">Сохранить</button>
                        </form>
                    </details>

                    <form method="post" action="${pageContext.request.contextPath}/reviews">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${review.id}">
                        <input type="hidden" name="dormitoryId" value="${dormitoryId}">
                        <button type="submit">Удалить</button>
                    </form>
                </c:if>
            </article>
        </c:forEach>
    </c:otherwise>
</c:choose>

<p>
    <a href="${pageContext.request.contextPath}/profile">Вернуться в профиль</a>
</p>
</body>
</html>
