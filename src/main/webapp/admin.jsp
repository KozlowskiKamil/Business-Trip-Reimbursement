<%@ page import="pl.astek.trip.ReceiptType" %>
<%@ page import="pl.astek.trip.AdminConfigurationServlet" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Configuration</title>
</head>
<body>
<h2>Admin Configuration</h2>
<form action="${pageContext.request.contextPath}/admin" method="post">
    <%--@declare id="dailyallowancerate"--%><%--@declare id="mileagerate"--%><%--@declare id="receipttypenames"--%><%--@declare id="singlereceiptlimit"--%><%--@declare id="totalreimbursementlimit"--%><%--@declare id="distancelimit"--%><label
        for="dailyAllowanceRate">Daily Allowance Rate ($/day):</label>
    <input type="number" step="0.01" name="dailyAllowanceRate" value="${adminConfig.dailyAllowanceRate}">
    <br>

    <label for="mileageRate">Mileage Rate ($/km):</label>
    <input type="number" step="0.01" name="mileageRate" value="${adminConfig.mileageRate}">
    <br>

        <label for="receiptTypeNames">Available Receipt Types (comma-separated):</label>
        <input type="text" name="receiptTypeNames"
               value="<%
           AdminConfigurationServlet adminConfig = (AdminConfigurationServlet) request.getSession().getAttribute("adminConfig");
           for (ReceiptType receiptType : adminConfig.getAvailableReceiptTypes()) {
               out.print(receiptType.getName() + ",");
           }
       %>"
        >
        <br>

    <label for="singleReceiptLimit">Single Receipt Limit ($):</label>
    <input type="number" step="0.01" name="singleReceiptLimit" value="${adminConfig.singleReceiptLimit}">
    <br>

    <label for="totalReimbursementLimit">Total Reimbursement Limit ($):</label>
    <input type="number" step="0.01" name="totalReimbursementLimit" value="${adminConfig.totalReimbursementLimit}">
    <br>

    <label for="distanceLimit">Distance Limit (km):</label>
    <input type="number" step="0.01" name="distanceLimit" value="${adminConfig.distanceLimit}">
    <br>

    <input type="submit" name="action" value="Save Configuration">
</form>

<hr>

<form action="${pageContext.request.contextPath}/admin" method="post">
    <label for="receiptTypeNames">Edit Available Receipt Types (comma-separated):</label>
    <input type="text" name="receiptTypeNames" value="${adminConfig.getFormattedReceiptTypeNames()}">
    <br>
    <input type="submit" name="action" value="Save Receipt Types">
</form>

<hr>
<h3>User Page</h3>
<a href="/business_trip_reimbursement_war_exploded/reimbursement"><button>Go to User Page</button></a>
</body>
</html>
