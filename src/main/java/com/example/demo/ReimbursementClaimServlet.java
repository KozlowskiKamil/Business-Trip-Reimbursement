package com.example.demo;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/reimbursement")
public class ReimbursementClaimServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LocalDate tripDate = LocalDate.parse(request.getParameter("tripDate"));
        List<Receipt> receipts = new ArrayList<>();

        // Pobieranie paragonów z formularza i dodawanie do listy receipts
        String[] receiptTypes = request.getParameterValues("receipts[].type");
        String[] receiptAmounts = request.getParameterValues("receipts[].amount");
        for (int i = 0; i < receiptTypes.length; i++) {
            String receiptType = receiptTypes[i];
            double receiptAmount = Double.parseDouble(receiptAmounts[i]);
            receipts.add(new Receipt(receiptType, receiptAmount));
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

        // Pobierz stawki z AdminConfigurationServlet lub z bazy danych
        double dailyAllowanceRate = 15.0;
        double mileageRate = 0.3;

        double totalReimbursement = ReimbursementCalculator.calculateTotalReimbursement(claim, dailyAllowanceRate, mileageRate);

        request.setAttribute("totalReimbursement", totalReimbursement);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Pobierz stawki z AdminConfigurationServlet lub z bazy danych
        double dailyAllowanceRate = 15.0;
        double mileageRate = 0.3;

        // Ustaw te wartości w atrybutach, aby były dostępne w formularzu JSP
        request.setAttribute("dailyAllowanceRate", dailyAllowanceRate);
        request.setAttribute("mileageRate", mileageRate);

        // Przekierowanie do strony z formularzem
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
