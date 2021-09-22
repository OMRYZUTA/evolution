package il.ac.mta.zuli.evolution.utils;

import com.google.gson.Gson;
import il.ac.mta.zuli.evolution.users.User;
import il.ac.mta.zuli.evolution.users.UserManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class ServletUtils {

//	private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
//	private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";

	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */
	private static final Object userManagerLock = new Object();
	private static final Object chatManagerLock = new Object();

	public static UserManager getUserManager(ServletContext servletContext) {
		//UserManager is a singleton
		synchronized (userManagerLock) {
			if (servletContext.getAttribute("userManager") == null) {
				servletContext.setAttribute("userManager", new UserManager());
			}
		}

		return (UserManager) servletContext.getAttribute("userManager");
	}

	public static String getBody(HttpServletRequest request) throws IOException {
		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}

		body = stringBuilder.toString();
		return body;
	}

	public static User getUserFromJson(HttpServletRequest request) throws IOException {
		Gson g = new Gson();
		Map<String, Object> map =g.fromJson(request.getReader(), new HashMap<String,Object>().getClass());
		return new User((String)map.get("username"));
	}

//	public static ChatManager getChatManager(ServletContext servletContext) {
//		synchronized (chatManagerLock) {
//			if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
//				servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new ChatManager());
//			}
//		}
//		return (ChatManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);
//	}

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
}
