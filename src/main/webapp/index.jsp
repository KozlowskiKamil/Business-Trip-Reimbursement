<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reimbursement Claim</title>
</head>
<body>
<h1>Create a New Reimbursement Claim</h1>

<form action="reimbursement" method="post">
    <label for="tripDate">Trip Date:</label>
    <input type="date" id="tripDate" name="tripDate" required>
    <br>

    <label for="receiptType">Receipt Type:</label>
    <select id="receiptType" name="receiptType">
        <option value="taxi">Taxi</option>
        <option value="hotel">Hotel</option>
        <option value="plane">Plane Ticket</option>
        <option value="train">Train</option>
        <option value="other">Other</option>
    </select>
    <br>

    <label for="receiptAmount">Receipt Amount:</label>
    <input type="number" id="receiptAmount" name="receiptAmount" step="0.01" required
           max="${sessionScope.singleReceiptLimit != null ? sessionScope.singleReceiptLimit : 100.0}">
    <br>

    <button type="button" id="addReceiptButton">Add Receipt</button>
    <br>

    <div id="receiptList"></div>

    <label for="claimedTripDays">Claimed Trip Days:</label>
    <input type="number" id="claimedTripDays" name="claimedTripDays">
    <br>

    <label for="disableDays">Disable Days:</label>
    <input type="checkbox" id="disableDays" name="disableDays">
    <br>

    <label for="claimedMileage">Claimed Mileage (km):</label>
    <input type="number" id="claimedMileage" name="claimedMileage" step="0.1"
           max="${sessionScope.distanceLimit != null ? sessionScope.distanceLimit : 100.0}">

    <br>

    <button type="submit">Calculate Reimbursement</button>

</form>

<h2>Total Reimbursement Amount: <%= request.getAttribute("totalReimbursement") %> $</h2>

<hr>

<h3>Admin Page</h3>
<a href="/business_trip_reimbursement_war_exploded/admin">
    <button>Go to Admin Page</button>
</a>

<script>
    const addReceiptButton = document.getElementById('addReceiptButton');
    const receiptList = document.getElementById('receiptList');
    let receiptCounter = 0;

    addReceiptButton.addEventListener('click', () => {
        const receiptType = document.getElementById('receiptType').value;
        const receiptAmount = parseFloat(document.getElementById('receiptAmount').value);

        if (!isNaN(receiptAmount)) {
            const receiptItem = document.createElement('div');
            receiptItem.textContent = `${receiptType}: $${receiptAmount.toFixed(2)}`;
            receiptList.appendChild(receiptItem);

            const hiddenInput = document.createElement('input');
            hiddenInput.type = 'hidden';
            hiddenInput.name = `receipts[${receiptCounter}].type`;
            hiddenInput.value = receiptType;
            receiptList.appendChild(hiddenInput);

            const hiddenInput2 = document.createElement('input');
            hiddenInput2.type = 'hidden';
            hiddenInput2.name = `receipts[${receiptCounter}].amount`;
            hiddenInput2.value = receiptAmount;
            receiptList.appendChild(hiddenInput2);

            receiptCounter++;
        }
    });

    document.addEventListener('DOMContentLoaded', () => {
        const receiptInputs = document.querySelectorAll('input[name^="receipts["]');
        if (receiptInputs.length > 0) {
            receiptCounter = receiptInputs.length;
        }
    });
</script>
</body>
</html>
