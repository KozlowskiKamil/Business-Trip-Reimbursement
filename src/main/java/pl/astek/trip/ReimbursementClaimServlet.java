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
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/reimbursement")
public class ReimbursementClaimServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ReimbursementClaimServlet.class.getName());


    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("ReimbursementClaimServlet initialized");

        // Stwórz instancję AdminConfigurationServlet
        AdminConfigurationServlet adminConfig = new AdminConfigurationServlet();

        // Inicjalizuj listę availableReceiptTypes
        List<ReceiptType> availableReceiptTypes = new ArrayList<>();
        availableReceiptTypes.add(new ReceiptType("Taxi"));
        availableReceiptTypes.add(new ReceiptType("Train"));
        availableReceiptTypes.add(new ReceiptType("Boat"));

        // Tutaj możesz dodać inne typy, jeśli są potrzebne

        // Ustaw zainicjowaną listę w instancji AdminConfigurationServlet
        adminConfig.setAvailableReceiptTypes(availableReceiptTypes);

        // Przekaż listę do sesji, aby była dostępna w innych częściach aplikacji
        getServletContext().setAttribute("availableReceiptTypes", availableReceiptTypes);
        System.out.println("availableReceiptTypes: " + availableReceiptTypes);

    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.setLevel(Level.INFO);
        LocalDate tripDate = LocalDate.parse(request.getParameter("tripDate"));
        List<Receipt> receipts = new ArrayList<>();

        String[] receiptTypes = request.getParameterValues("receipts[].type");
        String[] receiptAmounts = request.getParameterValues("receipts[].amount");
        for (int i = 0; i < receiptTypes.length; i++) {
            String receiptType = receiptTypes[i];
            double receiptAmount = Double.parseDouble(receiptAmounts[i]);
            receipts.add(new Receipt(receiptType, receiptAmount));
            logger.info("receipts = " + receipts.get(i).toString());
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
        saveReimbursementDataToJson(claim, totalReimbursement, tripDate.toString(), claimedTripDays, claimedMileage, receipts);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);

    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.setLevel(Level.INFO);
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
        logger.setLevel(Level.INFO);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        ObjectNode jsonData = objectMapper.createObjectNode();
        jsonData.put("totalReimbursement", totalReimbursement);
        jsonData.put("tripDate", tripDate);
        jsonData.put("claimedTripDays", claimedTripDays);
        jsonData.put("claimedMileage", claimedMileage);
        jsonData.put("receipts", String.valueOf(receipts));

        try {
            File jsonFile = new File("trip.json");
            objectMapper.writeValue(jsonFile, jsonData);
            logger.info("The data was saved in a JSON file.");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("An error occurred while writing data to the JSON file.");
        }
    }
}