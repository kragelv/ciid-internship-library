<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    boolean isEditMode = request.getAttribute("edit") != null && (boolean) request.getAttribute("edit");
%>

<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Книга: ${book.title()}</title>
     <jsp:include page="../shared/head-part.jsp"/>
    <script defer src="${pageContext.request.contextPath}/static/js/editMode.js"></script>
</head>
<body class="bg-gray-100">
    <jsp:include page="../shared/header.jsp"/>
    <div class="container mx-auto p-6">
        <div class="bg-white p-6 rounded-lg shadow-md">
            <h2 class="text-2xl font-semibold text-gray-700">Информация о книге</h2>

            <!-- Вид для просмотра книги -->
            <div id="viewMode" style="display: <%= isEditMode ? "none" : "block" %>;" class="mt-4">
                <p><strong>Название:</strong> ${book.title()}</p>
                <p><strong>Автор:</strong> ${book.author().firstName()} ${book.author().middleName()} ${book.author().lastName()}</p>
                <p><strong>Год выпуска:</strong> ${book.publishedYear()}</p>
                <p><strong>Кол-во копий:</strong> ${book.availableCopies()}</p>
                <p><strong>Жанры:</strong>
                    <c:forEach var="genre" items="${book.genres()}" varStatus="status" >
                        ${genre.name()}<c:if test="${!status.last}">, </c:if>
                    </c:forEach>
                </p>
                <div class="mt-6">
                    <button id="editButton" class="bg-yellow-500 text-white px-4 py-2 rounded hover:bg-yellow-600" onclick="toggleEdit()">Редактировать</button>
                </div>
            </div>

            <div id="editMode" style="display: <%= isEditMode ? "block" : "none" %>;" class="mt-4">
                <form id="bookForm" action="<c:url value="/actions/book/edit"/>" method="post">
                    <input type="hidden" name="id" value="${book.id()}" >
                    <div class="mb-4">
                        <label for="title" class="block text-gray-700">Название</label>
                        <input type="text" id="title" name="title" class="mt-2 p-2 border border-gray-300 rounded-lg w-full" value="${book.title()}" required>
                    </div>
                    <div class="mb-4">
                        <label for="author" class="block text-gray-700">Автор</label>
                        <select id="author" name="authorId" class="mt-2 p-2 border border-gray-300 rounded-lg w-full" required>
                            <c:forEach var="availableAuthor" items="${authors}">
                                <option value="${availableAuthor.id()}" 
                                    <c:if test="${availableAuthor.id() == book.author().id()}">selected</c:if>>
                                    ${availableAuthor.firstName()} ${availableAuthor.middleName()} ${availableAuthor.lastName()}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-4">
                        <label for="publishedYear" class="block text-gray-700">Год выпуска</label>
                        <input type="number" id="publishedYear" name="publishedYear" class="mt-2 p-2 border border-gray-300 rounded-lg w-full" value="${book.publishedYear()}">
                    </div>
                    <div class="mb-4">
                        <label for="availableCopies" class="block text-gray-700">Кол-во копий</label>
                        <input type="number" id="availableCopies" name="availableCopies" class="mt-2 p-2 border border-gray-300 rounded-lg w-full" value="${book.availableCopies()}">
                    </div>
                    <div class="mb-4">
                        <label for="genres" class="block text-gray-700">Жанры</label>
                        <select id="genres" name="genres" class="mt-2 p-2 border border-gray-300 rounded-lg w-full" multiple required>
                            <c:forEach var="genre" items="${genres}">
                                <option value="${genre.id()}" 
                                    <c:if test="${book.genres().contains(genre)}">selected</c:if>>
                                    ${genre.name()}
                                </option>
                            </c:forEach>
                        </select>
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
