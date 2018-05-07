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
    <title>Cranfield Information Retrieval</title>
</head>
<body>
    Cranfield Information Retrieval
<form action="index.jsp" method="post">
    <input type="text" name="searchTextbox" title="Search anything"/>
    <input type="submit" value="Search"/>
</form>
<table border="true">
    <TR>
        <TD>Word</TD>
        <TD>Weight</TD>
    </TR>
    <%
        IREngine irEngine = (IREngine)getServletConfig().getServletContext().getAttribute("engine");
        String searchString = request.getParameter("searchTextbox");
        IREngine.Query query = irEngine.new Query(searchString);
        List<Doc> relevantDocs = irEngine.search(query);
        for (String queryWord: query.wordWeights.keySet()) {
            out.println("<tr><td>" + queryWord + "</td>");
            out.println("<td>" + query.wordWeights.get(queryWord) + "</tr>");
        }
    %>
</table>
<%
    for (Doc doc: relevantDocs) {
        out.println("<a href='Cranfield/" + doc.doxIndex + ".txt'>" + doc.doxIndex + ".txt</a>");
        out.println("<br>Relevant value: " + doc.relevantValue + "</br>");
    }
%>
</body>
</html>
