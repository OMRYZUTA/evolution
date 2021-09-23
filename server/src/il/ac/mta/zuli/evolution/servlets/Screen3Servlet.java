package il.ac.mta.zuli.evolution.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "il.ac.mta.zuli.evolution.servlets.Screen3Servlet", urlPatterns = "/api/screen3")
public class Screen3Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // header of request will include a parameter in header: <timetableID: integer>
        // retrieve from the session: user's unique-name
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

}
