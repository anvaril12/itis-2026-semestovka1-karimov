<%@ tag pageEncoding="UTF-8" %>
<%@ attribute name="requestView" required="true" type="ru.itis.karimov.dto.RequestView" %>
<%@ attribute name="incoming" required="true" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<article class="request-card">
    <div class="request-card-top">
        <div class="person-row">
            <span class="person-avatar" aria-hidden="true"><c:out value="${fn:substring(requestView.otherUser.firstName, 0, 1)}"/><c:out value="${fn:substring(requestView.otherUser.lastName, 0, 1)}"/></span>
            <div>
                <h3><c:out value="${requestView.otherUser.firstName}"/> <c:out value="${requestView.otherUser.lastName}"/></h3>
                <span class="person-caption"><c:out value="${requestView.otherUser.course}"/> курс</span>
            </div>
        </div>
        <c:choose>
            <c:when test="${requestView.roommateRequestEntity.status == 'PENDING'}">
                <span class="badge badge-waiting"><span class="status-dot" aria-hidden="true"></span>${incoming ? 'Новая заявка' : 'Ждёт ответа'}</span>
            </c:when>
            <c:when test="${requestView.roommateRequestEntity.status == 'ACCEPTED'}">
                <span class="badge"><span class="status-dot" aria-hidden="true"></span>Принята</span>
            </c:when>
            <c:otherwise><span class="badge badge-neutral">Отклонена</span></c:otherwise>
        </c:choose>
    </div>

    <c:choose>
        <c:when test="${requestView.roommateRequestEntity.status == 'PENDING'}">
            <p class="request-message">${incoming ? 'Хочет жить вместе с вами.' : 'Вы предложили жить вместе. Ждём ответа.'}</p>
            <c:if test="${incoming}">
                <form class="inline-form request-response" method="post" action="${pageContext.request.contextPath}/request/action">
                    <input type="hidden" name="requestId" value="${requestView.roommateRequestEntity.id}">
                    <button type="submit" name="action" value="accept">Принять заявку</button>
                    <button type="submit" name="action" value="reject">Отклонить</button>
                </form>
            </c:if>
        </c:when>
        <c:when test="${requestView.roommateRequestEntity.status == 'ACCEPTED'}">
            <%-- Контакты показываем только после взаимного согласия. --%>
            <div class="request-contact">
                <span class="contact-symbol" aria-hidden="true">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none"><path d="M8 3H5a2 2 0 0 0-2 2c0 8.8 7.2 16 16 16a2 2 0 0 0 2-2v-3l-5-2-2 2a14 14 0 0 1-6-6l2-2-2-5Z" stroke="currentColor" stroke-width="1.5" stroke-linejoin="round"/></svg>
                </span>
                <div>
                    <span class="contact-label">Можно связаться</span>
                    <c:choose>
                        <c:when test="${not empty requestView.otherUser.contacts}"><span class="contact-value"><c:out value="${requestView.otherUser.contacts}"/></span></c:when>
                        <c:otherwise><span class="contact-value">Контакты не указаны</span></c:otherwise>
                    </c:choose>
                </div>
            </div>
        </c:when>
        <c:otherwise><p class="request-message">${incoming ? 'Вы отклонили эту заявку.' : 'Пользователь отклонил вашу заявку.'}</p></c:otherwise>
    </c:choose>
</article>
