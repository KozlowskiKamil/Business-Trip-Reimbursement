<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Admin Configuration</title>
</head>
<body>
<h2>Admin Configuration</h2>
<form action="${pageContext.request.contextPath}/admin/configuration" method="post">
  <%--@declare id="dailyallowancerate"--%><%--@declare id="mileagerate"--%><%--@declare id="receipttypenames"--%><%--@declare id="singlereceiptlimit"--%><%--@declare id="totalreimbursementlimit"--%><%--@declare id="distancelimit"--%><label for="dailyAllowanceRate">Daily Allowance Rate ($/day):</label>
  <input type="number" step="0.01" name="dailyAllowanceRate" value="${adminConfig.dailyAllowanceRate}">
  <br>

  <label for="mileageRate">Mileage Rate ($/km):</label>
  <input type="number" step="0.01" name="mileageRate" value="${adminConfig.mileageRate}">
  <br>

  <label for="receiptTypeNames">Available Receipt Types (comma-separated):</label>
  <input type="text" name="receiptTypeNames" value="<c:forEach items='${adminConfig.availableReceiptTypes}' var='receiptType'>${receiptType.name},</c:forEach>">
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

  <input type="submit" value="Save Configuration">
</form>
</body>
</html>