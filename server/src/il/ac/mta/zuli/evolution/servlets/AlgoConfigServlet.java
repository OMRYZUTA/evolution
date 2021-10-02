package il.ac.mta.zuli.evolution.servlets;

import il.ac.mta.zuli.evolution.Constants;
import il.ac.mta.zuli.evolution.DataManager;
import il.ac.mta.zuli.evolution.dto.AlgorithmConfigDTO;
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

@WebServlet(name = "il.ac.mta.zuli.evolution.servlets.AlgoConfigServlet", urlPatterns = "/api/algoconfig")
public class AlgoConfigServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, Object> mapForJSON = new HashMap<>();
        String timetableIDFromParameter = request.getParameter(Constants.TIMETABLE_ID);

        try {
            int ttID = Integer.parseInt(timetableIDFromParameter);
            DataManager dataManager = ServletUtils.getDataManager(getServletContext());
            String usernameFromSession = SessionUtils.getUsername(request);
            AlgorithmConfigDTO algoConfig = dataManager.getAlgoConfig(usernameFromSession, ttID);
            mapForJSON.put(Constants.DATA, algoConfig);
        } catch (Throwable e) {
            mapForJSON.put(Constants.ERROR, e.getMessage());
        } finally {
            ServletUtils.sendJSONResponse(response, mapForJSON);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

}
