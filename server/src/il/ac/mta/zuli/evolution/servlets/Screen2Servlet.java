package il.ac.mta.zuli.evolution.servlets;

import il.ac.mta.zuli.evolution.DataManager;
import il.ac.mta.zuli.evolution.engine.timetable.TimetableSummary;
import il.ac.mta.zuli.evolution.utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "il.ac.mta.zuli.evolution.servlets.Screen2Servlet", urlPatterns = "/api/screen2")
public class Screen2Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DataManager dataManager = ServletUtils.getDataManager(getServletContext());
        List<String> usernames = dataManager.getUserNames();
        List<TimetableSummary> timetableSummaries = dataManager.getTimetableSummaries();

        //preparing the response (no need for DTOs because we already wrap the objects in JSON)
        Map<String, Object> mapForJSON = new HashMap<>();
        mapForJSON.put("users", usernames);
        mapForJSON.put("timetables", timetableSummaries);

        ServletUtils.sendJSONResponse(response, mapForJSON);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //TODO in UI, if user selected existing problem, screen3Servlet will handle it (and not this servlet)
        //If we reached post in this servlet, then it's only in order to add a new problem

        // header of request will include a parameters in header:
        // TimetableID: integer
        // and from the session: user's unique-name
        // Retrieve the parameters from the request:

        //1 get input-stream from body (the file)
        //2 generate Timetable (if valid file) (including uploadedBy user) (will need engine)
        //3 add timetable to timetables in dataManager

        //response: notify user in UI: successful or unsuccessful upload
    }

    private void generateTimetableFromXML() {
//TODO implement
    }
}
