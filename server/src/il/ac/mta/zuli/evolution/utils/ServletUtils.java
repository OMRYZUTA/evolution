package il.ac.mta.zuli.evolution.utils;

import il.ac.mta.zuli.evolution.users.UserManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


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
