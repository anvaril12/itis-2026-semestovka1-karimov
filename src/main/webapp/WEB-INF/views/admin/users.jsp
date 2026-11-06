<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Пользователи</title>
    <jsp:include page="/WEB-INF/views/fragments/head.jsp"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-users.css">
</head>
<body class="page-admin-users">
<jsp:include page="/WEB-INF/views/fragments/header.jsp">
    <jsp:param name="navSection" value="admin-users"/>
</jsp:include>

<span class="eyebrow page-eyebrow">Администрирование</span>
<h1>Пользователи</h1>
<p class="page-description">Учётные записи студентов и управление блокировкой.</p>

<c:if test="${not empty sessionScope.flashSuccess}">
    <p class="flash success" role="status"><c:out value="${sessionScope.flashSuccess}"/></p>
    <c:remove var="flashSuccess" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.flashError}">
    <p class="flash error" role="alert"><c:out value="${sessionScope.flashError}"/></p>
    <c:remove var="flashError" scope="session"/>
</c:if>

<section class="users-panel" aria-labelledby="users-list-title">
    <div class="users-toolbar">
        <div class="users-list-heading">
            <h2 id="users-list-title">Учётные записи <span class="count-badge"><c:out value="${fn:length(users)}"/></span></h2>
            <p>Роли и статусы зарегистрированных пользователей.</p>
        </div>
        <form class="users-filter" method="get" action="${pageContext.request.contextPath}/admin/users">
            <label for="blocked">Статус</label>
            <select id="blocked" name="blocked">
                <option value="" ${empty selectedBlocked ? 'selected' : ''}>Все пользователи</option>
                <option value="false" ${selectedBlocked == 'false' ? 'selected' : ''}>Без блокировки</option>
                <option value="true" ${selectedBlocked == 'true' ? 'selected' : ''}>Заблокированные</option>
            </select>
            <button class="button-secondary" type="submit">Показать</button>
        </form>
    </div>

    <c:choose>
        <c:when test="${empty users}">
            <div class="empty-state users-empty">
                <span class="empty-symbol" aria-hidden="true">○</span>
                <h3>Здесь пока никого</h3>
                <p>Пользователей с выбранным статусом нет.</p>
                <c:if test="${not empty selectedBlocked}">
                    <a class="text-link" href="${pageContext.request.contextPath}/admin/users">Показать всех пользователей <span aria-hidden="true">↗</span></a>
                </c:if>
            </div>
        </c:when>
        <c:otherwise>
            <p class="users-scroll-hint">Таблицу можно прокрутить вправо.</p>
            <div class="users-table-scroll" role="region" aria-label="Список пользователей" tabindex="0">
                <table class="users-table">
                    <thead>
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Пользователь</th>
                        <th scope="col">Роль</th>
                        <th scope="col">Статус</th>
                        <th scope="col">Действия</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="account" items="${users}">
                        <tr>
                            <td class="user-id">#<c:out value="${account.id}"/></td>
                            <td>
                                <div class="user-table-person">
                                    <span class="user-table-avatar" aria-hidden="true"><c:out value="${fn:substring(account.firstName, 0, 1)}"/><c:out value="${fn:substring(account.lastName, 0, 1)}"/></span>
                                    <div>
                                        <span class="user-fullname"><c:out value="${account.firstName}"/> <c:out value="${account.lastName}"/></span>
                                        <span class="user-login"><c:out value="${account.login}"/> · <c:out value="${account.course}"/> курс</span>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${account.role == 'ADMIN'}"><span class="badge badge-neutral">Администратор</span></c:when>
                                    <c:otherwise><span class="user-role">Студент</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${account.blocked}"><span class="badge user-status-blocked"><span class="status-dot" aria-hidden="true"></span>Заблокирован</span></c:when>
                                    <c:otherwise><span class="badge"><span class="status-dot" aria-hidden="true"></span>Без блокировки</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td class="user-action-cell">
                                <c:choose>
                                    <c:when test="${account.role == 'ADMIN'}"><span class="user-protected">Блокировка недоступна</span></c:when>
                                    <c:when test="${account.blocked}">
                                        <form class="inline-form" method="post" action="${pageContext.request.contextPath}/admin/users">
                                            <input type="hidden" name="id" value="${account.id}">
                                            <button class="button-small button-secondary" type="submit" name="action" value="unblock">Разблокировать</button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <form class="inline-form" method="post" action="${pageContext.request.contextPath}/admin/users" onsubmit="return confirm('Заблокировать этого пользователя?');">
                                            <input type="hidden" name="id" value="${account.id}">
                                            <button class="button-small button-danger" type="submit" name="action" value="block">Заблокировать</button>
                                        </form>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
    <div class="users-panel-footer">
        <svg aria-hidden="true" width="15" height="15" viewBox="0 0 20 20" fill="none"><path d="m10 2 6 2.5V9c0 4-3 6.5-6 9-3-2.5-6-5-6-9V4.5L10 2Z" stroke="currentColor" stroke-width="1.3" stroke-linejoin="round"/><path d="m7 9 2 2 4-4" stroke="currentColor" stroke-width="1.3" stroke-linecap="round" stroke-linejoin="round"/></svg>
        Учётные записи администраторов защищены от блокировки.
    </div>
</section>

<jsp:include page="/WEB-INF/views/fragments/footer.jsp"/>
</body>
</html>
