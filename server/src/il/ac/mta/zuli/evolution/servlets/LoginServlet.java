package il.ac.mta.zuli.evolution.servlets;

import il.ac.mta.zuli.evolution.Constants;
import il.ac.mta.zuli.evolution.DataManager;
import il.ac.mta.zuli.evolution.User;
import il.ac.mta.zuli.evolution.utils.ServletUtils;
import il.ac.mta.zuli.evolution.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static il.ac.mta.zuli.evolution.utils.ServletUtils.getUserFromJson;

@WebServlet(name = "il.ac.mta.zuli.evolution.servlets.LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    private final String DASHBOARD_URL = "../../../evolution_web_app/src/pages/Dashboard";
    private final String SIGN_UP_URL = "../../../evolution_web_app/src/pages/signup/SignUp.js";

    //no doGet() implemented here

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String responseMessage = null;

        try {
            User newUser = getUserFromJson(request); //throws an exception , user can be invalid
            String usernameFromSession = SessionUtils.getUsername(request);

            if (usernameFromSession == null) {
                //user is not logged in yet
                DataManager dataManager = ServletUtils.getDataManager(getServletContext());
                synchronized (this) {
                    if (dataManager.doesUserExist(newUser.getUsername())) {
                        //TODO while(dataManager.doesUserExist(newUser.getUsername())) or IF?
                        responseMessage = Constants.USER_NAME_NOT_UNIQUE;
                    } else {
                        //add the new user to the users list
                        dataManager.addUser(newUser); //addUser is not a synchronized method (so there's no dead-lock)
                        //set the username in a session, so it will be available on each request
                        //the true parameter means that if a session object does not exist yet
                        //create a new one
                        request.getSession(true).setAttribute(Constants.USERNAME, newUser.getUsername());
                        responseMessage = Constants.SUCCESSFUL_LOGIN;
                    }
                }
            } else {
                // user is already logged in
                responseMessage = Constants.SUCCESSFUL_LOGIN;
            }
        } catch (IOException e) {
            responseMessage = Constants.USER_NAME_EMPTY;
        } finally {
            ServletUtils.sendJSONResponse(response, responseMessage);
        }
    }


    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
