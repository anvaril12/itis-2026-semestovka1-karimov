<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Отзывы об общежитии</title>
    <jsp:include page="/WEB-INF/views/fragments/head.jsp"/>
</head>
<body class="page-reviews">
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
    <jsp:param name="navSection" value="reviews"/>
</jsp:include>
<a class="back-link" href="${pageContext.request.contextPath}/profile"><span aria-hidden="true">←</span> Мой профиль</a>
<h1>Как здесь живётся?</h1>
<p class="page-description">Честные отзывы студентов о своём общежитии.</p>
<c:if test="${not empty sessionScope.flashSuccess}">
    <p class="flash success" role="status"><c:out value="${sessionScope.flashSuccess}"/></p>
    <c:remove var="flashSuccess" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.flashError}">
    <p class="flash error" role="alert"><c:out value="${sessionScope.flashError}"/></p>
    <c:remove var="flashError" scope="session"/>
</c:if>
<c:if test="${not empty errors.review}"><p class="flash error" role="alert"><c:out value="${errors.review}"/></p></c:if>

<div class="reviews-layout">
    <section class="content-card review-compose">
        <span class="card-eyebrow">Ваш опыт важен</span>
        <h2>Оставить отзыв</h2>
        <p class="muted">Что стоит знать будущим жильцам?</p>
        <form class="stack-form" method="post" action="${pageContext.request.contextPath}/reviews">
            <input type="hidden" name="action" value="create">
            <input type="hidden" name="dormitoryId" value="${dormitoryId}">
            <div class="field">
                <label for="text">Ваше впечатление</label>
                <textarea id="text" name="text" maxlength="1000" rows="5" placeholder="О быте, атмосфере и соседях…" required><c:out value="${text}"/></textarea>
                <span class="error"><c:out value="${errors.text}"/></span>
            </div>
            <div class="field">
                <label for="rating">Оценка общежития</label>
                <select id="rating" name="rating" required>
                    <option value="">Выберите оценку</option>
                    <c:forEach var="value" begin="1" end="5">
                        <option value="${value}" ${param.action == 'create' and param.rating == value ? 'selected' : ''}><c:out value="${value}"/> из 5</option>
                    </c:forEach>
                </select>
                <span class="error"><c:out value="${errors.rating}"/></span>
            </div>
            <button type="submit">Опубликовать отзыв</button>
        </form>
    </section>

    <section class="review-list" aria-labelledby="reviews-title">
        <div class="section-head"><h2 id="reviews-title">Все отзывы <span class="count-badge"><c:out value="${fn:length(reviews)}"/></span></h2></div>
        <c:choose>
            <c:when test="${empty reviews}">
                <div class="content-card empty-state"><h3>Пока тихо</h3><p>Станьте первым, кто расскажет об этом общежитии.</p></div>
            </c:when>
            <c:otherwise>
                <c:forEach var="review" items="${reviews}">
                    <article class="review-card">
                        <div class="review-heading">
                            <div class="rating-group">
                                <span class="rating-score"><c:out value="${review.rating}"/><small>/ 5</small></span>
                                <span class="rating-stars" aria-hidden="true"><c:forEach var="star" begin="1" end="5"><span class="${star <= review.rating ? 'star-filled' : 'star-empty'}">★</span></c:forEach></span>
                            </div>
                            <c:if test="${sessionScope.user.id == review.userId}"><span class="badge badge-neutral">Ваш отзыв</span></c:if>
                        </div>
                        <p class="review-text"><c:out value="${review.text}"/></p>
                        <p class="review-date"><c:out value="${review.formattedCreatedAt}"/></p>
                        <c:if test="${sessionScope.user.id == review.userId}">
                            <div class="review-actions">
                                <details>
                                    <summary>Редактировать</summary>
                                    <form class="stack-form" method="post" action="${pageContext.request.contextPath}/reviews">
                                        <input type="hidden" name="action" value="update">
                                        <input type="hidden" name="id" value="${review.id}">
                                        <input type="hidden" name="dormitoryId" value="${dormitoryId}">
                                        <div class="field">
                                            <label for="text-${review.id}">Текст отзыва</label>
                                            <textarea id="text-${review.id}" name="text" maxlength="1000" required><c:out value="${review.text}"/></textarea>
                                        </div>
                                        <div class="field">
                                            <label for="rating-${review.id}">Оценка</label>
                                            <select id="rating-${review.id}" name="rating" required>
                                                <c:forEach var="value" begin="1" end="5">
                                                    <option value="${value}" ${value == review.rating ? 'selected' : ''}><c:out value="${value}"/> из 5</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <button type="submit">Сохранить</button>
                                    </form>
                                </details>
                                <form class="inline-form" method="post" action="${pageContext.request.contextPath}/reviews">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="id" value="${review.id}">
                                    <input type="hidden" name="dormitoryId" value="${dormitoryId}">
                                    <button class="button-danger button-small" type="submit">Удалить</button>
                                </form>
                            </div>
                        </c:if>
                    </article>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </section>
</div>
<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
</body>
</html>
