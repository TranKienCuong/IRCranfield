<%@ page import="irpackage.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %><%--
  Created by IntelliJ IDEA.
  User: Kien Cuong
  Date: 5/3/2018
  Time: 9:23 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link href="primary.css" rel="stylesheet">
    <link href="bootstrap.min.css" rel="stylesheet">
    <%--<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">--%>
    <title>Cranfield Information Retrieval</title>
</head>

<body class="container">
<%
    IREngine irEngine = (IREngine)getServletConfig().getServletContext().getAttribute("engine");
    String searchString = request.getParameter("query");
    if (searchString == null)
        searchString = "";
    IREngine.Query query = (IREngine.Query) session.getAttribute("query");
    List<Doc> relevantDocs = (List<Doc>) session.getAttribute("relevantDocs");
    String s = (String) session.getAttribute("searchString");
    if (!searchString.equals(s) || query == null || relevantDocs == null) {
        query = irEngine.new Query(searchString);
        relevantDocs = irEngine.search(query);
        session.setAttribute("query", query);
        session.setAttribute("relevantDocs", relevantDocs);
        session.setAttribute("searchString", searchString);
    }

    final int resultsPerPage = 20;
    int pageNumber;
    try {
        pageNumber = Integer.parseInt(request.getParameter("page"));
    } catch (NumberFormatException ex) {
        pageNumber = 1;
    }
    int startResult = resultsPerPage * (pageNumber - 1);
    int numberOfResults = relevantDocs.size();
    int numberOfPages = (numberOfResults - 1) / resultsPerPage + 1;
%>

<h2 class="center">
    Cranfield Information Retrieval
</h2>

<form action="index.jsp" method="post" class="form-inline justify-content-center">
    <label for="query">Type query: </label>
    <input type="text" name="query" id="query" title="Search anything" class="form-control irtextbox" value="<%=searchString%>"/>
    <input type="submit" value="Search" class="form-control irbutton"/>
    <input type="hidden" name="page"/>
</form>

<%
    if (searchString.equals(""))
        return;
    if (startResult < 0 || startResult > numberOfResults) {
        out.print("Page not found!");
        return;
    }
%>

<%--Show previous, next and other page buttons--%>
<nav id="pages">
    <ul class="pagination">
        <%
            // previous and first page button
            if (pageNumber == 1) {
                out.print("<li class='page-item disabled'><a class='page-link' href='#'>&laquo;</a></li>");
                out.print("<li class='page-item disabled'><a class='page-link' href='#'>Previous</a></li>");
            }
            else {
                out.print("<li class='page-item'><a class='page-link' href='index.jsp?query=" + searchString + "&page=" + 1 + "'>&laquo;</a></li>");
                out.print("<li class='page-item'><a class='page-link' href='index.jsp?query=" + searchString + "&page=" + (pageNumber - 1) + "'>Previous</a></li>");
            }

            final int offset = 3;
            int start = pageNumber - offset;
            int end = pageNumber + offset;

            // some pages buttons before current page
            if (start > 1)
                out.print("<li class='page-item disabled'><a class='page-link' href='#'>...</a></li>");
            else
                start = 1;
            for (int i = start; i < pageNumber; i++)
                out.print("<li class='page-item'><a class='page-link' href='index.jsp?query=" + searchString + "&page=" + i + "'>" + i + "</a></li>");

            // current page button
            out.print("<li class='page-item active'><a class='page-link' href='index.jsp?query=" + searchString + "&page=" + pageNumber + "'>" + pageNumber + "</a></li>");

            // some pages buttons after current page
            if (end > numberOfPages)
                end = numberOfPages;
            for (int i = pageNumber + 1; i <= end; i++)
                out.print("<li class='page-item'><a class='page-link' href='index.jsp?query=" + searchString + "&page=" + i + "'>" + i + "</a></li>");
            if (pageNumber + offset < numberOfPages)
                out.print("<li class='page-item disabled'><a class='page-link' href='#'>...</a></li>");

            // next and last page button
            if (pageNumber == numberOfPages) {
                out.print("<li class='page-item disabled'><a class='page-link' href='#'>Next</a></li>");
                out.print("<li class='page-item disabled'><a class='page-link' href='#'>&raquo;</a></li>");
            }
            else {
                out.print("<li class='page-item'><a class='page-link' href='index.jsp?query=" + searchString + "&page=" + (pageNumber + 1) + "'>Next</a></li>");
                out.print("<li class='page-item'><a class='page-link' href='index.jsp?query=" + searchString + "&page=" + numberOfPages + "'>&raquo;</a></li>");
            }
        %>
    </ul>
</nav>

<div class="row">
    <%--Show results--%>
    <div class="col-sm-8">
        <%
            if (numberOfResults > 0)
                out.print("<p class='irp'>Found " + numberOfResults + " results:</p>");
            else {
                out.print("<p class='irp'>No results!</p>");
                return;
            }
            for (int i = startResult; i < startResult + resultsPerPage && i < numberOfResults; i++) {
                Doc doc = relevantDocs.get(i);
                out.println("<a href='Cranfield/" + doc.doxIndex + ".txt'>" + doc.doxIndex + ".txt</a>");
                out.println("<br>Rank: " + (i + 1));
                out.println("<br>Relevant value: " + doc.relevantValue + "<br><br>");
            }
        %>
    </div>

    <%--Show table of query word weights--%>
    <div class="col-sm-4">
        <div class="table-responsive">
            <table class="table table-striped table-bordered">
                <%
                    out.print("<p class='irp'>Query word weights:</p>");
                    out.print("<tr><th>Word</th><th>Weight</th></tr>");
                    for (String queryWord: query.wordWeights.keySet()) {
                        out.println("<tr><td>" + queryWord + "</td>");
                        out.println("<td>" + query.wordWeights.get(queryWord) + "</tr>");
                    }
                %>
            </table>
        </div>
    </div>
</div>
</body>
</html>
