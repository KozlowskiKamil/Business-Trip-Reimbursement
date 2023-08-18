package pl.astek.trip;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin")
public class AdminConfigurationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        AdminConfiguration adminConfig = getAdminConfiguration();

        request.setAttribute("adminConfig", adminConfig);
        request.getRequestDispatcher("/admin.jsp").forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AdminConfiguration adminConfig = new AdminConfiguration();

        double dailyAllowanceRate = Double.parseDouble(request.getParameter("dailyAllowanceRate"));
        double mileageRate = Double.parseDouble(request.getParameter("mileageRate"));
        adminConfig.setDailyAllowanceRate(dailyAllowanceRate);
        adminConfig.setMileageRate(mileageRate);

        List<ReceiptType> availableReceiptTypes = new ArrayList<>();
        String[] receiptTypeNames = request.getParameterValues("receiptTypeNames");
        for (String receiptTypeName : receiptTypeNames) {
            availableReceiptTypes.add(new ReceiptType(receiptTypeName));
        }
        adminConfig.setAvailableReceiptTypes(availableReceiptTypes);

        double singleReceiptLimit = Double.parseDouble(request.getParameter("singleReceiptLimit"));
        double totalReimbursementLimit = Double.parseDouble(request.getParameter("totalReimbursementLimit"));
        double distanceLimit = Double.parseDouble(request.getParameter("distanceLimit"));
        adminConfig.setSingleReceiptLimit(singleReceiptLimit);
        adminConfig.setTotalReimbursementLimit(totalReimbursementLimit);
        adminConfig.setDistanceLimit(distanceLimit);
        response.sendRedirect(request.getContextPath() + "/admin.jsp");
    }

    private AdminConfiguration getAdminConfiguration() {
        AdminConfiguration adminConfig = new AdminConfiguration();
        adminConfig.setDailyAllowanceRate(15.0);
        adminConfig.setMileageRate(0.3);
        adminConfig.setAvailableReceiptTypes(new ArrayList<>());
        adminConfig.setSingleReceiptLimit(100.0);
        adminConfig.setTotalReimbursementLimit(500.0);
        adminConfig.setDistanceLimit(100.0);
        return adminConfig;
    }
}