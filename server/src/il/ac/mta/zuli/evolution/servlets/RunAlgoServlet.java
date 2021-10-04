package il.ac.mta.zuli.evolution.servlets;

import com.google.gson.Gson;
import il.ac.mta.zuli.evolution.Constants;
import il.ac.mta.zuli.evolution.DataManager;
import il.ac.mta.zuli.evolution.engine.EngineUtils;
import il.ac.mta.zuli.evolution.engine.exceptions.InvalidOperationException;
import il.ac.mta.zuli.evolution.utils.AlgorithmActions;
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
    DataManager dataManager;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String responseMessage = null;

        try {
            String usernameFromSession = SessionUtils.getUsername(request);
            Gson gson = new Gson();
            Map<String, Object> requestMap = gson.fromJson(request.getReader(), new HashMap<String, Object>().getClass());
            int ttID = (int) Math.ceil((double) requestMap.get(Constants.TIMETABLE_ID)); //we know this an integer because we provided it

            //start, stop, pause,resume
            String actionType = request.getParameter(Constants.ALGO_ACTION).toUpperCase();
            System.out.println("in runAlgoServlet, param: " + actionType);
            AlgorithmActions action = AlgorithmActions.valueOf(actionType);

            dataManager = ServletUtils.getDataManager(getServletContext());

            switch (action) {
                case START:
                    responseMessage = startAlgorithmRun(usernameFromSession, ttID, requestMap);
                    break;
                case PAUSE:
                    responseMessage = dataManager.pauseAlgorithmRunForUser(usernameFromSession, ttID);
                    break;
                case RESUME:
                    responseMessage = resumeAlgorithmRun(usernameFromSession, ttID, requestMap);
                    break;
                case STOP:

                    break;
                default:
                    throw new InvalidOperationException("Invlaid algorithm action");
            }
        } catch (Throwable e) {
            responseMessage = EngineUtils.getToRootError(e);
        } finally {
            ServletUtils.sendJSONResponse(response, responseMessage);
        }
    }

    private String resumeAlgorithmRun(String usernameFromSession, int ttID, Map<String, Object> requestMap) {
        Map<String, Object> engineSettingsMap = (HashMap<String, Object>) requestMap.get(Constants.ENGINE_SETTINGS);
        Map<String, Object> endPredicatesMap = (Map<String, Object>) requestMap.get(Constants.END_PREDICATES);
        Object generationStride = requestMap.get(Constants.STRIDE); //validating we received an int alter on


        dataManager.resumeAlgorithmRunForUser(
                usernameFromSession,
                ttID,
                engineSettingsMap,
                endPredicatesMap,
                generationStride);

        return "OK"; //TODO change response message
    }

    private String startAlgorithmRun(String usernameFromSession, int ttID, Map<String, Object> requestMap) {
        Map<String, Object> engineSettingsMap = (HashMap<String, Object>) requestMap.get(Constants.ENGINE_SETTINGS);
        Map<String, Object> endPredicatesMap = (Map<String, Object>) requestMap.get(Constants.END_PREDICATES);
        Object generationStride = requestMap.get(Constants.STRIDE); //validating we received an int alter on


        dataManager.startAlgorithmRunForUser(
                usernameFromSession,
                ttID,
                engineSettingsMap,
                endPredicatesMap,
                generationStride);

        return "OK"; //TODO change response message?
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    }

}
