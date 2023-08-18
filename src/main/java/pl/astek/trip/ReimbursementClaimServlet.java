package pl.astek.trip;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/reimbursement")
public class ReimbursementClaimServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LocalDate tripDate = LocalDate.parse(request.getParameter("tripDate"));
        List<Receipt> receipts = new ArrayList<>();

        String[] receiptTypes = request.getParameterValues("receipts[].type");
        String[] receiptAmounts = request.getParameterValues("receipts[].amount");
        for (int i = 0; i < receiptTypes.length; i++) {
            String receiptType = receiptTypes[i];
            double receiptAmount = Double.parseDouble(receiptAmounts[i]);
            receipts.add(new Receipt(receiptType, receiptAmount));
            System.out.println("receipts = " + receipts.get(i).toString());
        }

        int claimedTripDays = Integer.parseInt(request.getParameter("claimedTripDays"));
        boolean disableDays = request.getParameter("disableDays") != null;
        double claimedMileage = Double.parseDouble(request.getParameter("claimedMileage"));

        ReimbursementClaim claim = new ReimbursementClaim();
        claim.setTripDate(String.valueOf(tripDate));
        claim.setReceipts(receipts);
        claim.setClaimedTripDays(claimedTripDays);
        claim.setDisableDays(disableDays);
        claim.setClaimedMileage(claimedMileage);

        HttpSession session = request.getSession();
        double dailyAllowanceRate;
        double mileageRate;

        if (session.getAttribute("dailyAllowanceRate") != null && session.getAttribute("mileageRate") != null) {
            dailyAllowanceRate = (Double) session.getAttribute("dailyAllowanceRate");
            mileageRate = (Double) session.getAttribute("mileageRate");
        } else {
            dailyAllowanceRate = 15.0;
            mileageRate = 0.3;
        }

        double totalReimbursement = ReimbursementCalculator.calculateTotalReimbursement(claim, dailyAllowanceRate, mileageRate);
        request.setAttribute("totalReimbursement", totalReimbursement);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        double dailyAllowanceRate;
        double mileageRate;

        if (session.getAttribute("dailyAllowanceRate") != null && session.getAttribute("mileageRate") != null) {
            dailyAllowanceRate = (Double) session.getAttribute("dailyAllowanceRate");
            mileageRate = (Double) session.getAttribute("mileageRate");
        } else {
            dailyAllowanceRate = 15.0;
            mileageRate = 0.3;
        }

        request.setAttribute("dailyAllowanceRate", dailyAllowanceRate);
        request.setAttribute("mileageRate", mileageRate);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

}