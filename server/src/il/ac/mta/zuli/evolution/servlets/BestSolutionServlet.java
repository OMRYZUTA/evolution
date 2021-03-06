package il.ac.mta.zuli.evolution.servlets;

import il.ac.mta.zuli.evolution.Constants;
import il.ac.mta.zuli.evolution.DataManager;
import il.ac.mta.zuli.evolution.dto.TimetableSolutionDTO;
import il.ac.mta.zuli.evolution.utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "il.ac.mta.zuli.evolution.servlets.BestSolutionServlet", urlPatterns = "/api/bestsolution")
public class BestSolutionServlet extends HttpServlet {

    //returns the best solution out of all the users solving this ttID
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, Object> mapForJSON = new HashMap<>();

        try {
            String timetableIDFromParameter = request.getParameter(Constants.TIMETABLE_ID);
            int ttID = Integer.parseInt(timetableIDFromParameter);
            DataManager dataManager = ServletUtils.getDataManager(getServletContext());
            TimetableSolutionDTO bestSolution = dataManager.getBestSolutionOfProblem(ttID);

            mapForJSON.put(Constants.DATA, bestSolution);
        } catch (Throwable e) {
            mapForJSON.put(Constants.ERROR, e.getMessage());
        } finally {
            ServletUtils.sendJSONResponse(response, mapForJSON);
        }
    }
}
