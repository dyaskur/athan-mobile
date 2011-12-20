/**
 * 
 */
package com.athan.mobile.controls.contribute;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;

/**
 * Feedbacks Controller
 * 
 * @author Saad BENBOUZID
 */
public class FeedbacksViewCtrl extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(FeedbacksViewCtrl.class
			.getName());

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		// Initializes public key
		Clients.evalJavaScript("setPublicKey('" + getPublicKey() + "');");
	}

	/**
	 * Handles the submit data button
	 */
	public void onClick$btnSubmit() {
		Clients.evalJavaScript("submitFormData();");
	}

	/**
	 * Returns captcha public key
	 * 
	 * @return
	 */
	private String getPublicKey() {

		try {
			InputStream is = desktop.getWebApp().getResourceAsStream(
					"/captcha/local.properties");

			Properties prop = new Properties();
			prop.load(is);

			return prop.getProperty("public");

		} catch (Exception exc) {
			log.log(Level.SEVERE, "Properties absent !", exc);
		}

		return null;
	}

}
