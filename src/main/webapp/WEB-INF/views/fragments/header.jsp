<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<a class="skip-link" href="#main-content">Перейти к содержимому</a>
<header class="site-header">
    <div class="header-inner">
        <a class="brand" href="${pageContext.request.contextPath}${empty sessionScope.user ? '/signin' : '/profile'}">
            <span class="brand-symbol" aria-hidden="true">
                <svg viewBox="0 0 32 32" fill="none" width="26" height="26">
                    <path d="M5 14 16 5l11 9v12H5V14Z" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
                    <path d="M12 26v-9h8v9M12 12h8" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
                </svg>
            </span>
            <span>соседи<span class="brand-caption">ваше место. ваши люди.</span></span>
        </a>

        <nav class="main-nav" aria-label="Основная навигация">
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <a href="${pageContext.request.contextPath}/profile" aria-current="${param.navSection == 'profile' ? 'page' : 'false'}">Профиль</a>
                    <a href="${pageContext.request.contextPath}/matches" aria-current="${param.navSection == 'matches' ? 'page' : 'false'}">Найти соседей</a>
                    <a href="${pageContext.request.contextPath}/rooms" aria-current="${param.navSection == 'rooms' ? 'page' : 'false'}">Комнаты</a>
                    <a href="${pageContext.request.contextPath}/requests" aria-current="${param.navSection == 'requests' ? 'page' : 'false'}">Заявки</a>
                    <a href="${pageContext.request.contextPath}/questionnaire" aria-current="${param.navSection == 'questionnaire' ? 'page' : 'false'}">Анкета</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/signin" aria-current="${param.navSection == 'signin' ? 'page' : 'false'}">Вход</a>
                    <a href="${pageContext.request.contextPath}/signup" aria-current="${param.navSection == 'signup' ? 'page' : 'false'}">Регистрация</a>
                </c:otherwise>
            </c:choose>
        </nav>

        <c:if test="${not empty sessionScope.user}">
            <div class="account-nav">
                <span class="account-name"><c:out value="${sessionScope.user.firstName}"/></span>
                <form method="post" action="${pageContext.request.contextPath}/signout">
                    <button class="button-quiet" type="submit">Выйти</button>
                </form>
            </div>
        </c:if>
    </div>

    <c:if test="${sessionScope.user.role == 'ADMIN'}">
        <nav class="admin-nav" aria-label="Администрирование">
            <div class="admin-nav-inner">
                <span class="admin-label">Администратор</span>
                <a href="${pageContext.request.contextPath}/admin/users" aria-current="${param.navSection == 'admin-users' ? 'page' : 'false'}">Пользователи</a>
                <a href="${pageContext.request.contextPath}/admin/questions" aria-current="${param.navSection == 'admin-questions' ? 'page' : 'false'}">Вопросы</a>
                <a href="${pageContext.request.contextPath}/admin/universities" aria-current="${param.navSection == 'admin-universities' ? 'page' : 'false'}">Вузы</a>
                <a href="${pageContext.request.contextPath}/admin/dormitories" aria-current="${param.navSection == 'admin-dormitories' ? 'page' : 'false'}">Общежития</a>
                <a href="${pageContext.request.contextPath}/admin/reviews" aria-current="${param.navSection == 'admin-reviews' ? 'page' : 'false'}">Отзывы</a>
                <a href="${pageContext.request.contextPath}/admin/complaints" aria-current="${param.navSection == 'admin-complaints' ? 'page' : 'false'}">Жалобы</a>
            </div>
        </nav>
    </c:if>
</header>

<main id="main-content" class="app-main" tabindex="-1">
