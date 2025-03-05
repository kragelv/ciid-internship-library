<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    boolean isEditMode = request.getAttribute("edit") != null && (boolean) request.getAttribute("edit");
%>

<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Жанр: ${genre.name()}</title>
    <jsp:include page="../shared/head-part.jsp"/>
    <script defer src="${pageContext.request.contextPath}/static/js/editMode.js"></script>
</head>
<body class="bg-gray-100">
    <jsp:include page="../shared/header.jsp"/>
    <div class="container mx-auto p-6">
        <div class="bg-white p-6 rounded-lg shadow-md">
            <h2 class="text-2xl font-semibold text-gray-700">Информация о жанре</h2>

            <!-- Вид для просмотра жанра -->
            <div id="viewMode" style="display: <%= isEditMode ? "none" : "block" %>;" class="mt-4">
                <p><strong>Название:</strong> ${genre.name()}</p>
                <div class="mt-6">
                    <button id="editButton" class="bg-yellow-500 text-white px-4 py-2 rounded hover:bg-yellow-600" onclick="toggleEdit()">Редактировать</button>
                </div>
            </div>

            <!-- Форма редактирования жанра -->
            <div id="editMode" style="display: <%= isEditMode ? "block" : "none" %>;" class="mt-4">
                <form id="genreForm" action="<c:url value="/actions/genre/edit"/>" method="post">
                    <input type="hidden" name="id" value="${genre.id()}" >
                    <div class="mb-4">
                        <label for="name" class="block text-gray-700">Название</label>
                        <input type="text" id="name" name="name" class="mt-2 p-2 border border-gray-300 rounded-lg w-full" value="${genre.name()}" required>
                    </div>
                    <div class="flex gap-4">
                        <button type="submit" class="bg-sky-400 text-white px-4 py-2 rounded hover:bg-sky-500">Сохранить</button>
                        <button type="button" id="cancelButton" class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-700" onclick="cancelEdit()">Отмена</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

</body>
</html>
