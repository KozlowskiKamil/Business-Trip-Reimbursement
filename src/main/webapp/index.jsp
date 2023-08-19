<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reimbursement Claim</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
</head>
<body>
<div class="container text-center">
    <h3>Create a New Reimbursement Claim</h3>
    <div class="row justify-content-center">
        <div class="col-12" style="max-width: 500px;">
            <form action="reimbursement" method="post">
                <label class="form-label" for="tripDate">Trip Date:</label>
                <input class="form-control mb-2" type="date" id="tripDate" name="tripDate" required>
                <br>
                <label class="form-label" for="receiptType2">Receipt Type2:</label>
                <select id="receiptType2" name="receiptType">
                    <option value="taxi">Taxi</option>
                    <option value="hotel">Hotel</option>
                    <option value="plane">Plane Ticket</option>
                    <option value="train">Train</option>
                    <option value="other">Other</option>
                </select>
                <label for="receiptTypeSelect">Choose Receipt Type:</label>
                <select class="form-control mb-3" id="receiptTypeSelect" name="receiptType">
                    <c:forEach items="${availableReceiptTypes}" var="receiptType">
                        <option value="${receiptType.name}">${receiptType.name}</option>
                    </c:forEach>
                </select>
                <br>
                <label class="form-label" for="receiptAmount">Receipt Amount:</label>
                <input class="form-control mb-2" type="number" id="receiptAmount" name="receiptAmount" step="0.01"
                       required
                       max="${sessionScope.singleReceiptLimit != null ? sessionScope.singleReceiptLimit : 100.0}">
                <button type="button" class="btn btn-primary mb-2" type="button" id="addReceiptButton">Add Receipt
                </button>
                <br>
                <div id="receiptList"></div>
                <label class="form-label mt-2" for="claimedTripDays">Claimed Trip Days:</label>
                <input class="form-control mb-2" type="number" id="claimedTripDays" name="claimedTripDays">
                <br>
                <label class="form-label" for="disableDays">A checkbox is available to disable on specific days:</label>
                <input class="form-check-input mt-0" type="checkbox" id="disableDays" name="disableDays">
                <br>
                <div id="disableDaysInputContainer" style="display: none;">
                    <label class="form-label" for="disabledDaysCount">Enter the number of days to subtract:</label>
                    <input class="form-control mb-2" type="number" id="disabledDaysCount" name="disabledDaysCount">
                </div>
                <label class="form-label mt-2" for="claimedMileage">Claimed Mileage (km):</label>
                <input class="form-control mb-2" type="number" id="claimedMileage" name="claimedMileage" step="0.1"
                       max="${sessionScope.distanceLimit != null ? sessionScope.distanceLimit : 100.0}">
                <button class="btn btn-primary mb-2" type="submit" id="calculateButton">Calculate Reimbursement</button>
                <br/>
            </form>
        </div>
    </div>
    <h4>Total Reimbursement Amount: <%= request.getAttribute("totalReimbursement") %> $</h4>
    <c:if test="${sessionScope.totalReimbursementLimit != null}">
        <h4>Total Reimbursement Limit: <%= session.getAttribute("totalReimbursementLimit") %> $</h4>
    </c:if>

    <hr>

    <h4>Admin Page</h4>
    <a href="${pageContext.request.contextPath}/admin">
        <button class="btn btn-outline-primary">Go to Admin Page</button>
    </a>
</div>
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
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4"
        crossorigin="anonymous"></script>
</body>
</html>