<%@ page import="pl.astek.trip.ReceiptType" %>
<%@ page import="pl.astek.trip.AdminConfigurationServlet" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Configuration</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
</head>
<body>
<div class="container text-center">
    <h3>Admin Configuration</h3>
    <div class="row justify-content-center">
        <div class="col-12" style="max-width: 700px;">
            <form action="${pageContext.request.contextPath}/admin" method="post">
                <%--@declare id="dailyallowancerate"--%><%--@declare id="mileagerate"--%><%--@declare id="receipttypenames"--%><%--@declare id="singlereceiptlimit"--%><%--@declare id="totalreimbursementlimit"--%><%--@declare id="distancelimit"--%><label
                    for="dailyAllowanceRate">Daily Allowance Rate ($/day):</label>
                <input class="form-control mb-3 " type="number" step="0.01" name="dailyAllowanceRate"
                       value="${adminConfig.dailyAllowanceRate}">


                <label class="form-label" for="mileageRate">Mileage Rate ($/km):</label>
                <input class="form-control mb-3 " type="number" step="0.01" name="mileageRate"
                       value="${adminConfig.mileageRate}">


                <label class="form-label" for="receiptTypeNames">Edit Available Receipt Types (comma-separated):</label>
                <input class="form-control mb-3 " type="text" name="receiptTypeNames"
                       value="<%
           AdminConfigurationServlet adminConfig = (AdminConfigurationServlet) request.getSession().getAttribute("adminConfig");
           for (ReceiptType receiptType : adminConfig.getAvailableReceiptTypes()) {
               out.print(receiptType.getName() + ",");
           }
       %>"
                >
                <label class="form-label" for="newReceiptType">Add New Receipt Type:</label>
                <input class="form-control mb-3" type="text" id="newReceiptType" name="newReceiptType">

                <label class="form-label" for="singleReceiptLimit">Single Receipt Limit ($):</label>
                <input class="form-control mb-3 " type="number" step="0.01" name="singleReceiptLimit"
                       value="${adminConfig.singleReceiptLimit}">

                <label class="form-label" for="totalReimbursementLimit">Total Reimbursement Limit ($):</label>
                <input class="form-control mb-3 " type="number" step="0.01" name="totalReimbursementLimit"
                       value="${adminConfig.totalReimbursementLimit}">

                <label class="form-label" for="distanceLimit">Distance Limit (km):</label>
                <input class="form-control mb-3 " type="number" step="0.01" name="distanceLimit"
                       value="${adminConfig.distanceLimit}">

                <input class="btn btn-dark" type="submit" name="action" value="Save Configuration">
            </form>
        </div>
    </div>
    <hr>
    <h3>User Page</h3>
    <a href="${pageContext.request.contextPath}/reimbursement">
        <button class="btn btn-outline-dark">Go to User Page</button>
    </a>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4"
        crossorigin="anonymous"></script>
</body>
</html>