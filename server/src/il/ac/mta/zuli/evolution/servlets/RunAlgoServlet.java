package il.ac.mta.zuli.evolution.servlets;

import com.google.gson.Gson;
import il.ac.mta.zuli.evolution.Constants;
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

@WebServlet(name = "il.ac.mta.zuli.evolution.servlets.RunAlgoServlet", urlPatterns = "/api/actions")
public class RunAlgoServlet extends HttpServlet {
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

        String actionType = request.getParameter(Constants.ALGO_ACTION);

        //    POST http://localhost:8080/server_Web_exploded/api/actions?action=start
        //TODO currently only handling Start
        try {
            String usernameFromSession = SessionUtils.getUsername(request);
            Gson gson = new Gson();
            Map<String, Object> requestMap = gson.fromJson(request.getReader(), new HashMap<String, Object>().getClass());

            int ttID = (int) Math.ceil((double) requestMap.get(Constants.TIMETABLE_ID)); //we know this an integer because we provided it
            Map<String, Object> engineSettingsMap = (HashMap<String, Object>) requestMap.get(Constants.ENGINE_SETTINGS);
            Map<String, Object> endPredicatesMap = (Map<String, Object>) requestMap.get(Constants.END_PREDICATES);
            Object generationStride = requestMap.get(Constants.STRIDE); //validating we received an int alter on

            DataManager dataManager = ServletUtils.getDataManager(getServletContext());
            dataManager.addAlgoRunToUser(
                    usernameFromSession,
                    ttID,
                    engineSettingsMap,
                    endPredicatesMap,
                    generationStride);

            responseMessage = "OK";
        } catch (Throwable e) {
            responseMessage = EngineUtils.getToRootError(e);
        } finally {
            ServletUtils.sendJSONResponse(response, responseMessage);
        }
    }
}
