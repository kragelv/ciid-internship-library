<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import ="java.util.List"%>

<% 
    boolean isFormVisible = session.getAttribute("errors") != null && !((List<?>) session.getAttribute("errors")).isEmpty();
%>

<!DOCTYPE html>
<html>
<head>
    <title>Книги</title>
    <jsp:include page="../shared/head-part.jsp"/>
    <script defer src="${pageContext.request.contextPath}/static/js/books.js"></script>
</head>
<body>
    <jsp:include page="../shared/header.jsp"/>
    <div class="max-w-9/10 mx-auto">
        <div id="bookForm" class="w-3/5 mx-auto p-6 rounded-lg shadow-md mb-6" style="display: <%= isFormVisible ? "block" : "none" %>;">
        <h3 class="text-xl font-semibold text-gray-800 mb-8">Добавить книгу</h3>
        <form action="<c:url value="/actions/book/create"/>" method="post">
            <div class="mb-4">
                <label for="title" class="block text-gray-700 font-medium">Название</label>
                <input type="text" id="title" name="title" class="mt-2 p-2 border border-gray-300 rounded-lg w-full" required />
            </div>

            <div class="mb-4">
                <label for="author" class="block text-gray-700 font-medium">Автор</label>
                <select id="author" name="authorId" class="mt-2 p-2 border border-gray-300 rounded-lg w-full" required min="0">
                    <c:forEach var="availableAuthor" items="${requestScope.authors}">
                        <option value="${availableAuthor.id()}">
                            ${availableAuthor.firstName()} ${availableAuthor.middleName()} ${availableAuthor.lastName()}
                            <c:if test="${availableAuthor.birthYear() != null}">
                                (${availableAuthor.birthYear()})
                            </c:if>
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="mb-4">
                <label for="publishedYear" class="block text-gray-700 font-medium">Год выпуска</label>
                <input type="number" id="publishedYear" name="publishedYear" class="mt-2 p-2 border border-gray-300 rounded-lg w-full" min="0" />
            </div>

            <div class="mb-4">
                <label for="availableCopies" class="block text-gray-700 font-medium">Количество копий</label>
                <input type="number" id="availableCopies" name="availableCopies" class="mt-2 p-2 border border-gray-300 rounded-lg w-full" required />
            </div>

            <div class="mb-4">
                <label for="genres" class="block text-gray-700 font-medium">Жанры</label>
                <select id="genres" name="genres" class="mt-2 p-2 border border-gray-300 rounded-lg w-full" multiple required>
                    <c:forEach var="availableGenre" items="${requestScope.genres}">
                        <option value="${availableGenre.id()}">${availableGenre.name()}</option>
                    </c:forEach>
                </select>
            </div>

            <c:if test="${not empty sessionScope.errors}">
                <ul style="color: red;">
                    <c:forEach var="error" items="${sessionScope.errors}">
                        <li>${error}</li>
                    </c:forEach>
                </ul>
                <% session.removeAttribute("errors"); %>
            </c:if>
            <div class="flex justify-end space-x-4">
                <button type="submit" class="bg-green-500 text-white font-semibold py-2 px-4 rounded-lg hover:bg-green-700 transition duration-300 ease-in-out">
                    Создать
                </button>
                <button type="button" class="bg-gray-500 text-white font-semibold py-2 px-4 rounded-lg hover:bg-gray-700 transition duration-300 ease-in-out" onclick="hideForm()">
                    Отмена
                </button>
            </div>
        </form>
    </div>
    <div class="bg-white p-6 rounded-lg shadow-lg mt-3">
        <div class="flex mb-4">
            <h2 class="text-2xl font-bold text-gray-800 mb-4">Книги (${books.size()})</h2>
                <button id="addButton" class="bg-blue-500 text-white font-semibold py-2 px-4 rounded-lg hover:bg-blue-700 transition duration-300 ease-in-out ms-auto" 
                    onclick="showForm()" 
                    style="display: <%= isFormVisible ? "none" : "inline-block" %>;">
                    Добавить
                </button>
            </div>
            <table class="w-full border-collapse border border-gray-200 rounded-lg overflow-hidden">
                <thead>
                    <tr class="bg-gray-200 text-gray-700 uppercase text-sm">
                        <th class="py-3 px-4 border border-gray-300">Автор</th>
                        <th class="py-3 px-4 border border-gray-300 w-[50%]">Название</th>
                        <th class="py-3 px-4 border border-gray-300">Год выпуска</th>
                        <th class="py-3 px-4 border border-gray-300">Кол-во копий</th>
                        <th class="py-3 px-4 border border-gray-300">Жанры</th>
                        <th class="py-3 px-4 border border-gray-300"></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="book" items="${requestScope.books}">
                        <tr class="border border-gray-300 bg-white hover:bg-gray-100 transition">
                            <td class="py-3 px-4 border border-gray-300">
                                ${book.author().firstName()} ${book.author().middleName()} ${book.author().lastName()}
                            </td>
                            <td class="py-3 px-4 border border-gray-300"><a href="<c:url value="/book?id=${book.id()}"/>">${book.title()}</a></td>
                            <td class="py-3 px-4 border border-gray-300">${book.publishedYear()}</td>
                            <td class="py-3 px-4 border border-gray-300">${book.availableCopies()}</td>
                            <td class="py-3 px-4 border border-gray-300">
                                <c:forEach var="genre" items="${book.genres()}" varStatus="status">
                                    ${genre.name()}<c:if test="${!status.last}">, </c:if>
                                </c:forEach>
                            </td>
                            <td class="py-3 px-4 border border-gray-300">
                                <div class="flex gap-1">
                                    <a href="<c:url value="/book?id=${book.id()}&edit"/>" 
                                    class="bg-blue-500 text-white font-semibold py-2 px-3 rounded-lg hover:bg-blue-700 transition duration-300 ease-in-out">
                                        ✏️
                                    </a>

                                    <form action="<c:url value="/actions/book/delete" />" method="post" 
                                        class="inline-block" onsubmit="return confirm('Вы уверены, что хотите удалить книгу?');">
                                        <input type="hidden" name="id" value="${book.id()}">
                                        <button type="submit" class="bg-red-500 text-white font-semibold py-2 px-3 rounded-lg hover:bg-red-700 transition duration-300 ease-in-out">
                                        🗑️ 
                                        </button>
                                    </form>
                                </div>
                                
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        
    </div>
</body>
</html>

