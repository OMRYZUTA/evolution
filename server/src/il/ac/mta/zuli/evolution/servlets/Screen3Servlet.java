package il.ac.mta.zuli.evolution.servlets;

import com.google.gson.Gson;
import il.ac.mta.zuli.evolution.DataManager;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import il.ac.mta.zuli.evolution.utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "il.ac.mta.zuli.evolution.servlets.Screen3Servlet", urlPatterns = "/api/timetable/details")
public class Screen3Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, Object> mapForJSON = new HashMap<>();

        // retrieve from request : timetableID
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(request.getReader(), new HashMap<String, Object>().getClass());
        double num = (double) map.get("timetableID");
        int ttID = (int) Math.ceil(num);

        try {
            DataManager dataManager = ServletUtils.getDataManager(getServletContext());
            TimeTable timeTable = dataManager.getTimetable(ttID);
            mapForJSON.put("timetable", timeTable);
        } catch (Throwable e) {
            mapForJSON.put("error", e.getMessage());
        } finally {
            ServletUtils.sendJSONResponse(response, mapForJSON);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

}
