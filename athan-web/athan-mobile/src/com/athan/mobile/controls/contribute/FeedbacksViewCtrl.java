/**
 * 
 */
package com.athan.mobile.controls.contribute;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Textbox;

import com.athan.mobile.constants.AthanConstants;
import com.athan.mobile.validators.EmailValidator;

/**
 * Feedbacks Controller
 * 
 * @author Saad BENBOUZID
 */
public class FeedbacksViewCtrl extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(FeedbacksViewCtrl.class
			.getName());

	private Textbox txtName;
	// private Textbox txtFirstName;
	private Textbox txtLocation;
	private Textbox txtMobile;
	private Textbox txtEmail;
	private Textbox txtMessage;

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

		validateFields();

		Clients.evalJavaScript("submitFormData('"
				+ StringEscapeUtils.escapeJavaScript(Labels
						.getLabel("contribute.submitsuccess"))
				+ "','"
				+ StringEscapeUtils.escapeJavaScript(Labels
						.getLabel("contribute.submitcaptchaerror"))
				+ "','"
				+ StringEscapeUtils.escapeJavaScript(Labels
						.getLabel("contribute.submiterror")) + "');");
	}

	/**
	 * Checks for non authorized empty values in fields
	 */
	private void validateFields() {
		if (StringUtils.isEmpty(StringUtils.trim(txtName.getValue()))) {
			throw new WrongValueException(txtName,
					Labels.getLabel("contribute.noemptyvalue"));
		}
		if (StringUtils.isEmpty(StringUtils.trim(txtLocation.getValue()))) {
			throw new WrongValueException(txtLocation,
					Labels.getLabel("contribute.noemptyvalue"));
		}
		if (StringUtils.isEmpty(StringUtils.trim(txtMobile.getValue()))) {
			throw new WrongValueException(txtMobile,
					Labels.getLabel("contribute.noemptyvalue"));
		}
		if (!StringUtils.isEmpty(StringUtils.trim(txtEmail.getValue()))) {
			// CHecks only if not empty
			if (!(new EmailValidator().validate(txtEmail.getValue()))) {
				throw new WrongValueException(txtEmail,
						Labels.getLabel("contribute.noemptyvalue.email"));
			}
		}
		if (StringUtils.isEmpty(StringUtils.trim(txtMessage.getValue()))) {
			throw new WrongValueException(txtMessage,
					Labels.getLabel("contribute.noemptyvalue"));
		}
	}

	/**
	 * Returns captcha public key
	 * 
	 * @return
	 */
	private String getPublicKey() {

		try {
			InputStream is = getClass().getResourceAsStream(
					AthanConstants.APPLICATION_PROPERTIES_FILE);

			Properties prop = new Properties();
			prop.load(is);

			return prop.getProperty(AthanConstants.PUBLIC_PROPERTY);

		} catch (Exception exc) {
			log.log(Level.SEVERE, "Properties absent !", exc);
		}

		return null;
	}

}
