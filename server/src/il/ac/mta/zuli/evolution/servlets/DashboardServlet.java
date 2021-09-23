package il.ac.mta.zuli.evolution.servlets;

import il.ac.mta.zuli.evolution.DataManager;
import il.ac.mta.zuli.evolution.engine.Descriptor;
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

        DataManager dataManager = ServletUtils.getDataManager(getServletContext());
        List<String> usernames = dataManager.getUserNames();
        List<TimetableSummary> timetableSummaries = dataManager.getTimetableSummaries();

        //preparing the response (no need for DTOs because we already wrap the objects in JSON)
        Map<String, Object> mapForJSON = new HashMap<>();
        mapForJSON.put("users", usernames);
        mapForJSON.put("timetables", timetableSummaries);

        ServletUtils.sendJSONResponse(response, mapForJSON);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //If we reached post in this servlet, then it's only in order to add a new problem
        String usernameFromSession = SessionUtils.getUsername(request);
        System.out.println(usernameFromSession);
        //todo get xml from request -convert to input stream
        //        //1 get input-stream from body (the file) and user from header
        //        //2 generate Timetable (if valid file) (including uploadedBy user) (will need engine)
        //        //3 add timetable to timetables in dataManager
        //
        //        //response: notify user in UI: successful upload (otherwise, an exception will cause an alert in UI?)
    }


    private Descriptor generateTimetableFromFile(InputStream fileToLoad) throws JAXBException {
        XMLParser xmlParser = new XMLParser();
        Descriptor descriptor = xmlParser.unmarshall(fileToLoad);
        //TODO update in UI if the file was successfully loaded or not

        return descriptor;
    }
}
