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
    <link href="primary.css" rel="stylesheet">
    <link href="bootstrap.min.css" rel="stylesheet">
    <%--<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">--%>
    <title>Cranfield Information Retrieval</title>
</head>

<body class="container">
<h2 class="center">
    Cranfield Information Retrieval
</h2>

<form action="index.jsp" method="post" class="center">
    <label for="1">Type query: </label>
    <input type="text" name="searchTextbox" id="1" title="Search anything" class="irtextbox"/>
    <input type="submit" value="Search" class="irbutton"/>
</form>



<div class="row">
    <div class="col-sm-8">
        <%
            IREngine irEngine = (IREngine)getServletConfig().getServletContext().getAttribute("engine");
            String searchString = request.getParameter("searchTextbox");
            IREngine.Query query = irEngine.new Query(searchString);
            List<Doc> relevantDocs = irEngine.search(query);
            int i = 1;
            if (relevantDocs.size() > 0) {
                out.print("<p class=\"irp\">Result:</p>");
            }
            for (Doc doc: relevantDocs) {
                out.println("<a href='Cranfield/" + doc.doxIndex + ".txt'>" + doc.doxIndex + ".txt</a>");
                out.println("<br>Rank: " + (i++));
                out.println("<br>Relevant value: " + doc.relevantValue + "<br><br>");
            }
        %>

    </div>
    <div class="col-sm-4">
        <%--<div class="table-responsive">--%>
        <table class="table table-striped table-bordered">
            <%
                if (relevantDocs.size() > 0) {
                    out.print("<p class=\"irp\">Query word weights:</p>");
                    out.print("<tr><th>Word</th><th>Weight</th></tr>");
                }
                for (String queryWord: query.wordWeights.keySet()) {
                    out.println("<tr><td>" + queryWord + "</td>");
                    out.println("<td>" + query.wordWeights.get(queryWord) + "</tr>");
                }
            %>
        </table>
        <%--</div>--%>
    </div>
</div>
</body>
</html>
