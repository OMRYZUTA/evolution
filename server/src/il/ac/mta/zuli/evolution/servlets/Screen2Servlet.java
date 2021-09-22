package il.ac.mta.zuli.evolution.servlets;

import il.ac.mta.zuli.evolution.TimetableManager;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import il.ac.mta.zuli.evolution.engine.timetable.TimetableSummary;
import il.ac.mta.zuli.evolution.users.UserManager;
import il.ac.mta.zuli.evolution.utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "il.ac.mta.zuli.evolution.servlets.Screen2Servlet", urlPatterns = "/screen2")
public class Screen2Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        List<String> usernames = userManager.getUserNames();
        TimetableManager timetableManager = ServletUtils.getTimetableManager(getServletContext());
        //TODO get from timetableManager, and other managers? create here in servlet
        List<TimetableSummary> timetableSummaries = summarizeTimetables(userManager, timetableManager);

        //preparing the response (no need for DTOs because we already wrap the objects in JSON)
        Map<String, Object> mapForJSON = new HashMap<>();
        mapForJSON.put("users", usernames);
        mapForJSON.put("timetables", timetableSummaries);

        ServletUtils.sendJSONResponse(response, mapForJSON);
    }

    private List<TimetableSummary> summarizeTimetables(UserManager userManager, TimetableManager timetableManager) {
        List<TimetableSummary> newList = new ArrayList<>();
        List<TimeTable> timetables = timetableManager.getTimetables();

        for (TimeTable t : timetables) {
            newList.add(new TimetableSummary(t));
        }

        //TODO set maxFitness and how many users are solving
        return newList;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //TODO what to do when user picks an existing timetable or adds a new one?
    }
}
