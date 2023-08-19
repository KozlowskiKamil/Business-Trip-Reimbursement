package pl.astek.trip;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/getReceiptTypes")
public class GetReceiptTypesServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AdminConfigurationServlet adminConfig = (AdminConfigurationServlet) request.getSession().getAttribute("adminConfig");
        List<ReceiptType> receiptTypes = adminConfig.getAvailableReceiptTypes();
        ObjectMapper objectMapper = new ObjectMapper();
        String receiptTypesJson = objectMapper.writeValueAsString(receiptTypes);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(receiptTypesJson);
    }
}