package il.ac.mta.zuli.evolution.servlets;

import com.google.gson.Gson;
import il.ac.mta.zuli.evolution.Constants;
import il.ac.mta.zuli.evolution.DataManager;
import il.ac.mta.zuli.evolution.dto.GenerationProgressDTO;
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

@WebServlet(name = "il.ac.mta.zuli.evolution.servlets.AlgoFlowServlet", urlPatterns = "/api/actions")
public class AlgoFlowServlet extends HttpServlet {
    DataManager dataManager;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String responseMessage = null;

        try {
            String usernameFromSession = SessionUtils.getUsername(request);
            Gson gson = new Gson();
            Map<String, Object> requestMap = gson.fromJson(request.getReader(), new HashMap<String, Object>().getClass());
            System.out.println("in AlgoFlowServlet, requestMap");
            System.out.println(requestMap);
            int ttID = (int) Math.ceil((double) requestMap.get(Constants.TIMETABLE_ID)); //we know this an integer because we provided it
            dataManager = ServletUtils.getDataManager(getServletContext());

            String actionType = request.getParameter(Constants.ALGO_ACTION).toUpperCase();
            System.out.println("in AlgoFlowServlet, param: " + actionType); //TODO delete later
            AlgorithmActions action = AlgorithmActions.valueOf(actionType);

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
                    throw new InvalidOperationException("Invalid algorithm action");
            }
        } catch (Throwable e) {
            responseMessage = EngineUtils.getToRootError(e);
        } finally {
            ServletUtils.sendJSONResponse(response, responseMessage);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //returns progressData (generationNum and BestScoreInGeneration)
        Map<String, Object> mapForJSON = new HashMap<>();

        try {
            String usernameFromSession = SessionUtils.getUsername(request);
            String timetableIDFromParameter = request.getParameter(Constants.TIMETABLE_ID);
            int ttID = Integer.parseInt(timetableIDFromParameter);
            DataManager dataManager = ServletUtils.getDataManager(getServletContext());
            GenerationProgressDTO progressDTO = dataManager.getProgressData(usernameFromSession, ttID);
            mapForJSON.put(Constants.DATA, progressDTO);
        } catch (Throwable e) {
            mapForJSON.put(Constants.ERROR, e.getMessage());
        } finally {
            ServletUtils.sendJSONResponse(response, mapForJSON);
        }
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
}
