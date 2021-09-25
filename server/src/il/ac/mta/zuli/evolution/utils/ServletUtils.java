package il.ac.mta.zuli.evolution.utils;

import com.google.gson.Gson;
import il.ac.mta.zuli.evolution.Constants;
import il.ac.mta.zuli.evolution.DataManager;
import il.ac.mta.zuli.evolution.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


public class ServletUtils {
    /*
    Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
    the actual fetch of them is remained un-synchronized for performance POV
     */
    private static final Object dataManagerLock = new Object();

    public static void sendJSONResponse(HttpServletResponse response, Object obj)
            throws ServletException, IOException {
        response.setContentType("application/json");
        Gson gson = new Gson();
        String jsonResponse;
        try {
            jsonResponse = gson.toJson(obj);
        } catch (Throwable e) {
            HashMap<String,String> mapForJSON = new HashMap<>();
            mapForJSON.put(Constants.ERROR, e.getMessage());
            jsonResponse = gson.toJson(mapForJSON);
        }
        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }

    }

    public static User getUserFromJson(HttpServletRequest request) throws IOException {
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(request.getReader(), new HashMap<String, Object>().getClass());

        return new User((String) map.get("username"));
    }

    public static DataManager getDataManager(ServletContext servletContext) throws IOException {
        synchronized (dataManagerLock) {
            if (servletContext.getAttribute("dataManager") == null) {
                servletContext.setAttribute("dataManager", new DataManager());
            }
        }

        return (DataManager) servletContext.getAttribute("dataManager");
    }
}
