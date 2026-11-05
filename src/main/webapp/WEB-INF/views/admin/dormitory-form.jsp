<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>
        <c:choose>
            <c:when test="${not empty dormitory}">Редактирование общежития</c:when>
            <c:otherwise>Добавление общежития</c:otherwise>
        </c:choose>
    </title>
</head>
<body>
<h1>
    <c:choose>
        <c:when test="${not empty dormitory}">Редактировать общежитие</c:when>
        <c:otherwise>Добавить общежитие</c:otherwise>
    </c:choose>
</h1>

<c:if test="${not empty errors.dormitory}">
    <p><c:out value="${errors.dormitory}"/></p>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/admin/dormitories">
    <c:choose>
        <c:when test="${not empty dormitory}">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="id" value="${dormitory.id}">
        </c:when>
        <c:otherwise>
            <input type="hidden" name="action" value="create">
        </c:otherwise>
    </c:choose>

    <p>
        <label for="university_id">Вуз</label>
        <select id="university_id" name="university_id" required>
            <c:forEach var="university" items="${universities}">
                <option value="${university.id}"
                        <c:if test="${university.id == dormitory.universityId}">selected</c:if>>
                    <c:out value="${university.name}"/>
                </option>
            </c:forEach>
        </select>
    </p>

    <p>
        <label for="name">Название</label>
        <input id="name" name="name" type="text" maxlength="100"
               value="<c:out value='${dormitory.name}'/>" required>
    </p>

    <p>
        <label for="address">Адрес</label>
        <input id="address" name="address" type="text" maxlength="100"
               value="<c:out value='${dormitory.address}'/>" required>
    </p>

    <button type="submit">Сохранить</button>
</form>

<p>
    <a href="${pageContext.request.contextPath}/admin/dormitories">Назад к списку</a>
</p>
</body>
</html>
