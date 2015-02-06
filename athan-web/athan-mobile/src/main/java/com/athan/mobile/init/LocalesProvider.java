/**
 * 
 */
package com.athan.mobile.init;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.zkoss.web.Attributes;

/**
 * Listener de request
 * 
 * @author Saad BENBOUZID
 */
public class LocalesProvider implements org.zkoss.zk.ui.util.RequestInterceptor {

	public static final String MY_LOCALE_COOKIE_NAME = "myLocale";

	public static final String LOCALE_EN = "en_EN";
	public static final String LOCALE_FR = "fr_FR";

	// private static final Logger log = Logger.getLogger(LocalesProvider.class
	// .getName());

	public void request(org.zkoss.zk.ui.Session sess, Object request,
			Object response) {

		// Récupération du cookie sur la langue précédente
		final Cookie[] cookies = ((HttpServletRequest) request).getCookies();

		if (cookies != null) {

			for (int j = cookies.length; --j >= 0;) {

				if (cookies[j].getName().equals(MY_LOCALE_COOKIE_NAME)) {

					// determine the locale
					String val = cookies[j].getValue();
					Locale locale = org.zkoss.util.Locales.getLocale(val);
					sess.setAttribute(Attributes.PREFERRED_LOCALE, locale);

					return;
				}
			}
		}
	}
}
