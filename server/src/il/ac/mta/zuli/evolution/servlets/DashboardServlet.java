package il.ac.mta.zuli.evolution.servlets;

import il.ac.mta.zuli.evolution.DataManager;
import il.ac.mta.zuli.evolution.engine.EngineUtils;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import il.ac.mta.zuli.evolution.engine.timetable.TimetableSummary;
import il.ac.mta.zuli.evolution.engine.xmlparser.XMLParser;
import il.ac.mta.zuli.evolution.utils.ServletUtils;
import il.ac.mta.zuli.evolution.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "il.ac.mta.zuli.evolution.servlets.DashboardServlet", urlPatterns = "/api/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, Object> mapForJSON = new HashMap<>();

        try {
            DataManager dataManager = ServletUtils.getDataManager(getServletContext());
            List<String> usernames = dataManager.getUserNames();
            List<TimetableSummary> timetableSummaries = dataManager.getTimetableSummaries();

            //preparing the response (no need for DTOs because we already wrap the objects in JSON)
            mapForJSON.put("users", usernames);
            mapForJSON.put("timetables", timetableSummaries);
        } catch (Throwable e) {
            mapForJSON.put("error", e.getMessage());
        } finally {
            ServletUtils.sendJSONResponse(response, mapForJSON);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //adding a new timetable to the dashboard

        String responseMessage = null;
        TimeTable timeTable = null;

        try {
            String usernameFromSession = SessionUtils.getUsername(request);

            //from file to Timetable (within Descriptor)
            InputStream inputStream = request.getInputStream();
            XMLParser xmlParser = new XMLParser();
            timeTable = xmlParser.unmarshall(inputStream);

            //adding the new timetable to the collection in DataManager
            DataManager dataManager = ServletUtils.getDataManager(getServletContext());
            dataManager.addTimetable(timeTable, usernameFromSession);

            responseMessage = "OK";
        } catch (JAXBException e) {
            responseMessage = "JAXB Exception";
        } catch (Throwable e) {
            responseMessage = EngineUtils.getToRootError(e);
        } finally {
            ServletUtils.sendJSONResponse(response, responseMessage);
        }
    }
}
