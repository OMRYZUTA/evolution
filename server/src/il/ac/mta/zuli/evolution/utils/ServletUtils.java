package il.ac.mta.zuli.evolution.utils;

import com.google.gson.Gson;
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
		String jsonResponse = gson.toJson(obj);

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

	public static int getIntParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);

		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException numberFormatException) {
			}
		}
		return Integer.MIN_VALUE;
	}

	//	public static String getBody(HttpServletRequest request) throws IOException {
//		String body = null;
//		StringBuilder stringBuilder = new StringBuilder();
//		BufferedReader bufferedReader = null;
//
//		try {
//			InputStream inputStream = request.getInputStream();
//			if (inputStream != null) {
//				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//				char[] charBuffer = new char[128];
//				int bytesRead = -1;
//				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
//					stringBuilder.append(charBuffer, 0, bytesRead);
//				}
//			} else {
//			}
//		} catch (IOException ex) {
//			throw ex;
//		} finally {
//			if (bufferedReader != null) {
//				try {
//					bufferedReader.close();
//				} catch (IOException ex) {
//					throw ex;
//				}
//			}
//		}
//
//		body = stringBuilder.toString();
//		return body;
//	}
}
