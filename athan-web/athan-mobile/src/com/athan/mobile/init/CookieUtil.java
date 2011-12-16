/**
 * 
 */
package com.athan.mobile.init;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zkoss.zk.ui.Executions;

/**
 * G�n�rateur de cookie
 * 
 * @author Saad BENBOUZID
 */
public class CookieUtil {

	public static void setCookie(String name, String value) {
		((HttpServletResponse) Executions.getCurrent().getNativeResponse())
				.addCookie(new Cookie(name, value));
	}

	public static String getCookie(String name) {
		Cookie[] cookies = ((HttpServletRequest) Executions.getCurrent()
				.getNativeRequest()).getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}
