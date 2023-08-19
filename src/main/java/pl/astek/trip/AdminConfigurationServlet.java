package pl.astek.trip;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet("/admin")
public class AdminConfigurationServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ReimbursementClaimServlet.class.getName());

    private double dailyAllowanceRate = 15.0;
    private double mileageRate = 0.3;
    private List<ReceiptType> availableReceiptTypes = new ArrayList<>();
    private double singleReceiptLimit = 100.0;
    private double totalReimbursementLimit = 2000.0;
    private double distanceLimit = 100.0;

    @Override
    public void init() throws ServletException {
        super.init();
        AdminConfigurationServlet adminConfig = this;
        if (availableReceiptTypes.isEmpty()) {
            availableReceiptTypes.add(new ReceiptType("Taxi"));
            availableReceiptTypes.add(new ReceiptType("Hotel"));
            availableReceiptTypes.add(new ReceiptType("Plane Ticket"));
            availableReceiptTypes.add(new ReceiptType("Train"));
            availableReceiptTypes.add(new ReceiptType("Other"));
            adminConfig.setAvailableReceiptTypes(availableReceiptTypes);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(true);
        AdminConfigurationServlet adminConfig = this;
        session.setAttribute("adminConfig", adminConfig);
        request.getRequestDispatcher("/admin.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.setLevel(Level.INFO);
        HttpSession session = request.getSession();
        AdminConfigurationServlet adminConfig = (AdminConfigurationServlet) session.getAttribute("adminConfig");

        String dailyAllowanceRateParam = request.getParameter("dailyAllowanceRate");
        double dailyAllowanceRate = 15.0;
        if (dailyAllowanceRateParam != null && !dailyAllowanceRateParam.isEmpty()) {
            dailyAllowanceRate = Double.parseDouble(dailyAllowanceRateParam);
        }
        double mileageRate = Double.parseDouble(request.getParameter("mileageRate"));
        double singleReceiptLimit = Double.parseDouble(request.getParameter("singleReceiptLimit"));
        double totalReimbursementLimit = Double.parseDouble(request.getParameter("totalReimbursementLimit"));
        double distanceLimit = Double.parseDouble(request.getParameter("distanceLimit"));
        adminConfig.setDailyAllowanceRate(dailyAllowanceRate);
        adminConfig.setMileageRate(mileageRate);
        adminConfig.setSingleReceiptLimit(singleReceiptLimit);
        adminConfig.setTotalReimbursementLimit(totalReimbursementLimit);
        adminConfig.setDistanceLimit(distanceLimit);

        session.setAttribute("dailyAllowanceRate", dailyAllowanceRate);
        session.setAttribute("mileageRate", mileageRate);
        session.setAttribute("singleReceiptLimit", singleReceiptLimit);
        session.setAttribute("totalReimbursementLimit", totalReimbursementLimit);
        session.setAttribute("distanceLimit", distanceLimit);
        logger.info("dailyAllowanceRate = " + dailyAllowanceRate);
        logger.info("mileageRate = " + mileageRate);
        logger.info("singleReceiptLimit = " + singleReceiptLimit);
        logger.info("totalReimbursementLimit = " + totalReimbursementLimit);
        logger.info("distanceLimit = " + distanceLimit);

        String receiptTypeNamesInput = request.getParameter("receiptTypeNames");
        if (receiptTypeNamesInput != null && !receiptTypeNamesInput.isEmpty()) {
            String[] receiptTypeNames = receiptTypeNamesInput.split(",");
            List<String> trimmedTypeNames = Arrays.stream(receiptTypeNames)
                    .map(String::trim)
                    .collect(Collectors.toList());

            // Sprawdź, czy każda nazwa typu istnieje na liście; jeśli nie, dodaj ją
            trimmedTypeNames.forEach(trimmedReceiptTypeName -> {
                if (!availableReceiptTypes.stream()
                        .anyMatch(existingType -> existingType.getName().equals(trimmedReceiptTypeName))) {
                    availableReceiptTypes.add(new ReceiptType(trimmedReceiptTypeName));
                }
            });
        }

        adminConfig.setAvailableReceiptTypes(availableReceiptTypes);

        session.setAttribute("adminConfig", adminConfig);

        response.sendRedirect(request.getContextPath() + "/admin.jsp");
    }

    public double getDailyAllowanceRate() {
        return dailyAllowanceRate;
    }

    public void setDailyAllowanceRate(double dailyAllowanceRate) {
        this.dailyAllowanceRate = dailyAllowanceRate;
    }

    public double getMileageRate() {
        return mileageRate;
    }

    public void setMileageRate(double mileageRate) {
        this.mileageRate = mileageRate;
    }

    public List<ReceiptType> getAvailableReceiptTypes() {
        return availableReceiptTypes;
    }

    public void setAvailableReceiptTypes(List<ReceiptType> availableReceiptTypes) {
        this.availableReceiptTypes = availableReceiptTypes;
    }

    public double getSingleReceiptLimit() {
        return singleReceiptLimit;
    }

    public void setSingleReceiptLimit(double singleReceiptLimit) {
        this.singleReceiptLimit = singleReceiptLimit;
    }

    public double getTotalReimbursementLimit() {
        return totalReimbursementLimit;
    }

    public void setTotalReimbursementLimit(double totalReimbursementLimit) {
        this.totalReimbursementLimit = totalReimbursementLimit;
    }

    public double getDistanceLimit() {
        return distanceLimit;
    }

    public void setDistanceLimit(double distanceLimit) {
        this.distanceLimit = distanceLimit;
    }

    @Override
    public String toString() {
        return "AdminConfigurationServlet{" +
                "dailyAllowanceRate=" + dailyAllowanceRate +
                ", mileageRate=" + mileageRate +
                ", availableReceiptTypes=" + availableReceiptTypes +
                ", singleReceiptLimit=" + singleReceiptLimit +
                ", totalReimbursementLimit=" + totalReimbursementLimit +
                ", distanceLimit=" + distanceLimit +
                '}';
    }

    public String getFormattedReceiptTypeNames() {
        StringBuilder formattedNames = new StringBuilder();
        for (ReceiptType receiptType : availableReceiptTypes) {
            formattedNames.append(receiptType.getName()).append(",");
        }
        return formattedNames.toString();
    }

}