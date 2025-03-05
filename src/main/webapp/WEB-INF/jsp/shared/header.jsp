<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="io.kragelv.library.controller.util.NavLinkData" %>
<%@ page import ="java.util.List"%>
<%@ page import ="java.util.ArrayList"%>

<%
    List<NavLinkData> links = new ArrayList<>();
    links.add(new NavLinkData("/books", "Книги"));
    links.add(new NavLinkData("/genres", "Жанры"));
    
    request.setAttribute("navLinks", links);
%>
<header class="bg-blue-600 p-4">
    <div class="max-w-9/10 mx-auto flex gap-x-7 items-center">
        <c:forEach var="link" items="${navLinks}">
            <div>
                <a href="${pageContext.request.contextPath}${link.path()}" 
                   class="text-white text-lg font-semibold hover:text-blue-200 transition duration-300">
                    ${link.name()}
                </a>
            </div>
        </c:forEach>
    </div>
</header>