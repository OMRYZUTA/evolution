package il.ac.mta.zuli.evolution.servlets;

import il.ac.mta.zuli.evolution.Constants;
import il.ac.mta.zuli.evolution.DataManager;
import il.ac.mta.zuli.evolution.dto.StrideDataDTO;
import il.ac.mta.zuli.evolution.utils.ServletUtils;
import il.ac.mta.zuli.evolution.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "il.ac.mta.zuli.evolution.servlets.StrideDataServlet", urlPatterns = "/api/stridedata")
public class StrideDataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, Object> mapForJSON = new HashMap<>();

        try {
            String timetableIDFromParameter = request.getParameter(Constants.TIMETABLE_ID);
            int ttID = Integer.parseInt(timetableIDFromParameter);

            String usernameFromSession = SessionUtils.getUsername(request);

            DataManager dataManager = ServletUtils.getDataManager(getServletContext());
            List<StrideDataDTO> strideData = dataManager.getStrideData(usernameFromSession, ttID);

            mapForJSON.put(Constants.DATA, strideData);
        } catch (Throwable e) {
            mapForJSON.put(Constants.ERROR, e.getMessage());
        } finally {
            ServletUtils.sendJSONResponse(response, mapForJSON);
        }
    }
}

