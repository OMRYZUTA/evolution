package il.ac.mta.zuli.evolution.servlets;

import com.google.gson.Gson;
import il.ac.mta.zuli.evolution.DataManager;
import il.ac.mta.zuli.evolution.engine.EngineUtils;
import il.ac.mta.zuli.evolution.utils.ServletUtils;
import il.ac.mta.zuli.evolution.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "il.ac.mta.zuli.evolution.servlets.DashboardServlet", urlPatterns = "api/actions?action")
public class runAlgoServlet extends HttpServlet {
//    POST http://localhost:8080/server_Web_exploded/api/actions?action=start

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        Map<String, Object> mapForJSON = new HashMap<>();
//
//        try {
//            DataManager dataManager = ServletUtils.getDataManager(getServletContext());
//            List<String> usernames = dataManager.getUserNames();
//            List<TimetableSummary> timetableSummaries = dataManager.getTimetableSummaries();
//
//            //preparing the response (no need for DTOs because we already wrap the objects in JSON)
//            mapForJSON.put("users", usernames);
//            mapForJSON.put("timetables", timetableSummaries);
//        } catch (Throwable e) {
//            mapForJSON.put("error", e.getMessage());
//        } finally {
//            ServletUtils.sendJSONResponse(response, mapForJSON);
//        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String responseMessage = null;

        try {
            Gson gson = new Gson();
            Map<String, Object> requestMap = gson.fromJson(request.getReader(), new HashMap<String, Object>().getClass());
            String usernameFromSession = SessionUtils.getUsername(request);
            Map<String, Object> engineSettingsMap = (Map<String, Object>) requestMap.get("engineSettings");
            Map<String, Object> endPredicatesMap = (Map<String, Object>) requestMap.get("endPredicates");
            int generationStride = (int) requestMap.get("stride");
            DataManager dataManager = ServletUtils.getDataManager(getServletContext());
            dataManager.addAlgoRunToUser(usernameFromSession, engineSettingsMap, endPredicatesMap, generationStride);

            responseMessage = "OK";
        } catch (Throwable e) {
            responseMessage = EngineUtils.getToRootError(e);
        } finally {
            ServletUtils.sendJSONResponse(response, responseMessage);
        }
    }
}
