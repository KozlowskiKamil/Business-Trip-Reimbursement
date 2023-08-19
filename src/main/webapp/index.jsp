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

    <label for="disableDays">A checkbox is available to disable on specific days:</label>
    <input type="checkbox" id="disableDays" name="disableDays">
    <br>

    <div id="disableDaysInputContainer" style="display: none;">
        <label for="disabledDaysCount">Enter the number of days to subtract:</label>
        <input type="number" id="disabledDaysCount" name="disabledDaysCount">
    </div>

    <label for="claimedMileage">Claimed Mileage (km):</label>
    <input type="number" id="claimedMileage" name="claimedMileage" step="0.1"
           max="${sessionScope.distanceLimit != null ? sessionScope.distanceLimit : 100.0}">

    <br>

    <button type="submit" id="calculateButton">Calculate Reimbursement</button>

</form>

<h2>Total Reimbursement Amount: <%= request.getAttribute("totalReimbursement") %> $</h2>
<c:if test="${sessionScope.totalReimbursementLimit != null}">
    <h2>Total Reimbursement Limit: <%= session.getAttribute("totalReimbursementLimit") %> $</h2>
</c:if>

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
            receiptItem.textContent = receiptType + ': $' + receiptAmount.toFixed(2);
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

    function hasAtLeastOneReceipt() {
        const receiptInputs = document.querySelectorAll('input[name^="receipts["]');
        for (const input of receiptInputs) {
            if (input.value !== '') {
                return true;
            }
        }
        return false;
    }

    document.addEventListener('DOMContentLoaded', () => {
        const calculateButton = document.getElementById('calculateButton');

        calculateButton.addEventListener('click', (event) => {
            if (!hasAtLeastOneReceipt()) {
                event.preventDefault();
                alert('Please add at least one receipt before calculating reimbursement.');
            }
        });
    });

    const disableDaysCheckbox = document.getElementById('disableDays');

    const disableDaysInputContainer = document.getElementById('disableDaysInputContainer');

    disableDaysCheckbox.addEventListener('change', function () {
        if (this.checked) {
            disableDaysInputContainer.style.display = 'block';
        } else {
            disableDaysInputContainer.style.display = 'none';
        }
    });

    document.addEventListener('DOMContentLoaded', () => {
        const calculateButton = document.getElementById('calculateButton');

        calculateButton.addEventListener('click', (event) => {
            if (!hasAtLeastOneReceipt()) {
                event.preventDefault();
                alert('Please add at least one receipt before calculating reimbursement.');
            } else {
                const tripDate = document.getElementById('tripDate').value;
                const claimedTripDays = document.getElementById('claimedTripDays').value;
                const disableDays = document.getElementById('disableDays').checked;
                const disabledDaysCount = document.getElementById('disabledDaysCount').value;
                const claimedMileage = document.getElementById('claimedMileage').value;

                const receipts = [];
                const receiptElements = document.querySelectorAll('input[name^="receipts["]');
                receiptElements.forEach((element) => {
                    const receiptType = element.value;
                    const receiptAmount = parseFloat(element.nextElementSibling.value);
                    receipts.push({type: receiptType, amount: receiptAmount});
                });

                const reimbursementData = {
                    tripDate: tripDate,
                    claimedTripDays: claimedTripDays,
                    disableDays: disableDays,
                    disabledDaysCount: disabledDaysCount,
                    claimedMileage: claimedMileage,
                    receipts: receipts
                };

                fetch('/reimbursement', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(reimbursementData)
                })
                    .then(response => response.json())
                    .then(data => {
                        console.log('Data saved successfully:', data);
                    })
                    .catch(error => {
                        console.error('Error saving data:', error);
                    });
            }
        });
    });
</script>
</body>
</html>