import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import pl.astek.trip.ReimbursementClaimServlet;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class ReimbursementClaimServletTest {

    @Test
    void testDoPostCalculatesTotalReimbursement() throws IOException, ServletException {
        ReimbursementClaimServlet servlet = new ReimbursementClaimServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);
        when(request.getParameter("tripDate")).thenReturn("2023-08-17");
        when(request.getParameterValues("receipts[].type")).thenReturn(new String[]{"taxi"});
        when(request.getParameterValues("receipts[].amount")).thenReturn(new String[]{"50"});
        when(request.getParameter("claimedTripDays")).thenReturn("5");
        when(request.getParameter("disableDays")).thenReturn("false");
        when(request.getParameter("claimedMileage")).thenReturn("100");
        servlet.doPost(request, response);
        verify(request).setAttribute(eq("totalReimbursement"), anyDouble());
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGetSetsRequestAttributesAndForwardsToIndexJSP() throws ServletException, IOException {
        ReimbursementClaimServlet servlet = new ReimbursementClaimServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);
        servlet.doGet(request, response);
        verify(request).setAttribute(eq("dailyAllowanceRate"), anyDouble());
        verify(request).setAttribute(eq("mileageRate"), anyDouble());
        verify(dispatcher).forward(request, response);
    }
}