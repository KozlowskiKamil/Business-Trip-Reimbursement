package pl.astek.trip;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
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
        double totalReimbursementLimit;

        if (session.getAttribute("dailyAllowanceRate") != null && session.getAttribute("mileageRate") != null && session.getAttribute("totalReimbursementLimit") != null) {
            dailyAllowanceRate = (Double) session.getAttribute("dailyAllowanceRate");
            mileageRate = (Double) session.getAttribute("mileageRate");
            totalReimbursementLimit = (Double) session.getAttribute("totalReimbursementLimit");
        } else {
            dailyAllowanceRate = 15.0;
            mileageRate = 0.3;
            totalReimbursementLimit = 2000.0;
        }

        double totalReimbursement = ReimbursementCalculator.calculateTotalReimbursement(claim, dailyAllowanceRate, mileageRate);
        request.setAttribute("totalReimbursement", totalReimbursement);
        session.setAttribute("totalReimbursement", totalReimbursement);
        session.setAttribute("totalReimbursementLimit", totalReimbursementLimit);
        request.setAttribute("totalReimbursementLimit", totalReimbursementLimit);
        saveReimbursementDataToJson(claim, totalReimbursement, tripDate.toString(),claimedTripDays, claimedMileage, receipts);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);

    }



    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        double dailyAllowanceRate;
        double mileageRate;
        double totalReimbursementLimit;

        if (session.getAttribute("dailyAllowanceRate") != null && session.getAttribute("mileageRate") != null && session.getAttribute("totalReimbursementLimit") != null) {
            dailyAllowanceRate = (Double) session.getAttribute("dailyAllowanceRate");
            mileageRate = (Double) session.getAttribute("mileageRate");
            totalReimbursementLimit = (Double) session.getAttribute("totalReimbursementLimit");
        } else {
            dailyAllowanceRate = 15.0;
            mileageRate = 0.3;
            totalReimbursementLimit = 2000.0;
        }

        request.setAttribute("dailyAllowanceRate", dailyAllowanceRate);
        request.setAttribute("mileageRate", mileageRate);
        session.setAttribute("totalReimbursementLimit", totalReimbursementLimit);
        request.setAttribute("totalReimbursementLimit", totalReimbursementLimit);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    private void saveReimbursementDataToJson(ReimbursementClaim claim, double totalReimbursement, String tripDate, int claimedTripDays, double claimedMileage, List<Receipt> receipts) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        ObjectNode jsonData = objectMapper.createObjectNode();
        jsonData.put("totalReimbursement", totalReimbursement);
        jsonData.put("tripDate", tripDate);
        jsonData.put("claimedTripDays", claimedTripDays);
        jsonData.put("claimedMileage", claimedMileage);
        jsonData.put("receipts", String.valueOf(receipts));
//        jsonData.put("disabledDaysCount", disabledDaysCount);


        try {
            File jsonFile = new File("C:\\Users\\Kamil\\Desktop\\Projects\\Business-Trip-Reimbursement\\trip.json");
            objectMapper.writeValue(jsonFile, jsonData);
            System.out.println("Dane zostały zapisane w pliku JSON.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Wystąpił błąd podczas zapisywania danych do pliku JSON.");
        }
    }

}