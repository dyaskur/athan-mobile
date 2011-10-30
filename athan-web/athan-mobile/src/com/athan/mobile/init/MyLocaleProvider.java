/**
 * 
 */
package com.athan.mobile.init;

import java.util.Locale;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.zkoss.web.Attributes;

/**
 * @author Saad BENBOUZID
 * 
 */
public class MyLocaleProvider implements
		org.zkoss.zk.ui.util.RequestInterceptor {

	private static final Logger log = Logger.getLogger(MyLocaleProvider.class
			.getName());

	public void request(org.zkoss.zk.ui.Session sess, Object request,
			Object response) {
		final Cookie[] cookies = ((HttpServletRequest) request).getCookies();
		log.info("ICI 1");
		if (cookies != null) {
			log.info("ICI 2");
			for (int j = cookies.length; --j >= 0;) {
				log.info("ICI 3");
				if (cookies[j].getName().equals("my.locale")) {
					log.info("ICI 4");
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
