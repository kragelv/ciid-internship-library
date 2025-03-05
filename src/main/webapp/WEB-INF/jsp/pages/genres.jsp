<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import ="java.util.List"%>

<% 
    boolean isFormVisible = session.getAttribute("errors") != null && !((List<?>) session.getAttribute("errors")).isEmpty();
%>

<!DOCTYPE html>
<html>
<head>
    <title>Жанры</title>
    <jsp:include page="../shared/head-part.jsp"/>
    <script defer src="${pageContext.request.contextPath}/static/js/genres.js"></script>
</head>
<body>
    <jsp:include page="../shared/header.jsp"/>
    <div class="max-w-9/10 mx-auto">
        <div id="genreForm" class="w-3/5 mx-auto p-6 rounded-lg shadow-md mb-6"  style="display: <%= isFormVisible ? "block" : "none" %>;">
            <h3 class="text-xl font-semibold text-gray-800 mb-8">Добавить Жанр</h3>
            <form action="<c:url value="/actions/genre/create"/>" method="post">
                <div class="mb-4">
                    <label for="genreName" class="block text-gray-700 font-medium">Название</label>
                    <input type="text" id="genreName" name="name" class="mt-2 p-2 border border-gray-300 rounded-lg w-full" required />
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
        <div class="p-6 rounded-lg shadow-lg mt-3">
            <div class="flex mb-4">
            <h2 class="text-2xl font-bold text-gray-800 mb-4">Жанры (${genres.size()})</h2>
                <button id="addButton" class="bg-blue-500 text-white font-semibold py-2 px-4 rounded-lg hover:bg-blue-700 transition duration-300 ease-in-out ms-auto"
                    onclick="showForm()"
                    style="display: <%= isFormVisible ? "none" : "inline-block" %>;">
                    Добавить
                </button>
            </div>
            <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
                <c:forEach var="genre" items="${requestScope.genres}">
                    <div class="bg-gray-100 p-2 rounded-lg shadow-md flex flex-col gap-1">
                        <a href="<c:url value="/genre?id=${genre.id()}"/>" class="block bg-gray-200 rounded-lg p-2">
                            <h3 class="text-lg font-semibold text-gray-800">${genre.name()}</h3>
                        </a>
                         <div class="flex align-items-baseline">
                            <div class="flex gap-1 ml-auto">
                                <a href="<c:url value="/genre?id=${genre.id()}&edit"/>" 
                                class="block bg-blue-500 text-white font-semibold py-1 px-2 rounded-md hover:bg-blue-700 transition duration-300 ease-in-out">
                                    ✏️
                                </a>
                           
                                <form action="<c:url value="/actions/genre/delete" />" method="post" 
                                    onsubmit="return confirm('Вы уверены, что хотите удалить жанр?');">
                                    <input type="hidden" name="id" value="${genre.id()}">
                                    <button type="submit" class="bg-red-500 text-white font-semibold py-1 px-2 rounded-md hover:bg-red-700 transition duration-300 ease-in-out">
                                    🗑️ 
                                    </button>
                                </form>
                            </div>  
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</body>
</html>

