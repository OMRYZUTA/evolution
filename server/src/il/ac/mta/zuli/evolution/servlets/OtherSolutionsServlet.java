package il.ac.mta.zuli.evolution.servlets;

import il.ac.mta.zuli.evolution.Constants;
import il.ac.mta.zuli.evolution.DataManager;
import il.ac.mta.zuli.evolution.dto.OtherUserSolutionDTO;
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

@WebServlet(name = "il.ac.mta.zuli.evolution.servlets.OtherSolutionsServlet", urlPatterns = "/api/othersolutionsinfo")
public class OtherSolutionsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, Object> mapForJSON = new HashMap<>();
        String timetableIDFromParameter = request.getParameter(Constants.TIMETABLE_ID);

        try {
            int ttID = Integer.parseInt(timetableIDFromParameter);

            DataManager dataManager = ServletUtils.getDataManager(getServletContext());
            List<OtherUserSolutionDTO> otherSolutions = dataManager.getOtherSolutionsInfo(ttID);

            mapForJSON.put(Constants.DATA, otherSolutions);
        } catch (Throwable e) {
            mapForJSON.put(Constants.ERROR, e.getMessage());
        } finally {
            ServletUtils.sendJSONResponse(response, mapForJSON);
        }
    }

}
