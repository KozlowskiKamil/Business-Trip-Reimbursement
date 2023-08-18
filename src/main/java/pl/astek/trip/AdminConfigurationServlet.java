package pl.astek.trip;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet("/admin/configure")
public class AdminConfigurationServlet extends HttpServlet {
    private AdminConfiguration adminConfig;

}