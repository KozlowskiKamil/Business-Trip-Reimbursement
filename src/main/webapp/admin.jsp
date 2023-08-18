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
  <%--@declare id="receipttypenames"--%><label for="receiptTypeNames">Available Receipt Types (comma-separated):</label>
  <input type="text" name="receiptTypeNames">
  <br>
  <input type="submit" value="Save Receipt Types">
</form>

<hr>

<form action="${pageContext.request.contextPath}/admin" method="post">
  <%--@declare id="dailyallowancerate"--%><%--@declare id="mileagerate"--%><%--@declare id="singlereceiptlimit"--%><%--@declare id="totalreimbursementlimit"--%><%--@declare id="distancelimit"--%><label for="dailyAllowanceRate">Daily Allowance Rate ($/day):</label>
  <input type="number" step="0.01" name="dailyAllowanceRate" value="15.0">
  <br>

  <label for="mileageRate">Mileage Rate ($/km):</label>
  <input type="number" step="0.01" name="mileageRate" value="0.3">
  <br>

  <label for="singleReceiptLimit">Single Receipt Limit ($):</label>
  <input type="number" step="0.01" name="singleReceiptLimit" value="100.0">
  <br>

  <label for="totalReimbursementLimit">Total Reimbursement Limit ($):</label>
  <input type="number" step="0.01" name="totalReimbursementLimit" value="500.0">
  <br>

  <label for="distanceLimit">Distance Limit (km):</label>
  <input type="number" step="0.01" name="distanceLimit" value="100.0">
  <br>

  <input type="submit" value="Save Configuration">
</form>
</body>
</html>
