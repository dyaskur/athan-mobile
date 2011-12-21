package com.athan.mobile.captcha.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import com.athan.mobile.captcha.CaptchaException;
import com.athan.mobile.captcha.CaptchaForm;
import com.athan.mobile.captcha.CaptchaResponse;
import com.athan.mobile.constants.AthanConstants;
import com.google.gson.Gson;

/**
 * Servlet implementation class PostFormDataServlet
 */
public class PostFormDataServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String JSON_CONTENT_TYPE = "text/json";

	private static final Logger log = Logger
			.getLogger(PostFormDataServlet.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PostFormDataServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType(JSON_CONTENT_TYPE);
		CaptchaResponse strResponse = new CaptchaResponse();
		Gson gson = new Gson();

		try {
			/*
			 * CAPTCHA CHECK
			 */
			// First validate the captcha, if not -- just get out of the loop
			StringBuilder sb = new StringBuilder();
			String s;
			while ((s = request.getReader().readLine()) != null) {
				sb.append(s);
			}

			CaptchaForm captchaForm = (CaptchaForm) gson.fromJson(
					sb.toString(), CaptchaForm.class);

			if ((captchaForm == null)
					|| (captchaForm.getChallengeField() == null)
					|| (captchaForm.getResponseField() == null)) {
				throw new CaptchaException(captchaForm.getCaptchaMessage());
			}

			String remoteAddr = request.getRemoteAddr();
			ReCaptchaImpl reCaptcha = new ReCaptchaImpl();

			reCaptcha.setPrivateKey(getPrivateKey());

			ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(
					remoteAddr, captchaForm.getChallengeField(),
					captchaForm.getResponseField());

			if (!reCaptchaResponse.isValid()) {
				// RECAPTCHA VALIDATION FAILED
				throw new CaptchaException(captchaForm.getCaptchaMessage());
			}

			// Builds the response message
			strResponse.setMessage(captchaForm.getSuccessMessage());
			strResponse.setCheckOK(true);

			// Sends email
			sendMail(captchaForm);

		} catch (CaptchaException ex) {
			// Builds the response message
			strResponse.setMessage(ex.getMessage());
			strResponse.setCheckOK(false);
		} catch (Exception ex) {
			// Builds the response message
			strResponse.setMessage(ex.getMessage());
			strResponse.setCheckOK(false);

			// Logs the unknown error
			log.log(Level.SEVERE, ex.getMessage(), ex);
		}

		// Returns response
		response.getWriter().print(gson.toJson(strResponse));
	}

	/**
	 * Send feedback as an email
	 */
	private void sendMail(CaptchaForm captchaForm) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		// Builds the body from data sent in the form
		String msgBody = String.format(AthanConstants.EMAIL_BODY,
				captchaForm.getName(), captchaForm.getFirstname(),
				captchaForm.getLocation(), captchaForm.getMobile(),
				captchaForm.getEmail(), captchaForm.getMessage());

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(AthanConstants.EMAIL_ADDRESS_FROM));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					AthanConstants.EMAIL_ADDRESS_TO));
			msg.setSubject(AthanConstants.EMAIL_SUBJECT);
			msg.setText(msgBody);
			Transport.send(msg);

		} catch (AddressException e) {
			log.log(Level.SEVERE, "AddressException", e);
		} catch (MessagingException e) {
			log.log(Level.SEVERE, "MessagingException", e);
		}
	}

	/**
	 * Returns captcha private key
	 * 
	 * @return
	 */
	private String getPrivateKey() {

		try {
			InputStream is = getClass().getResourceAsStream(
					AthanConstants.APPLICATION_PROPERTIES_FILE);

			Properties prop = new Properties();
			prop.load(is);

			return prop.getProperty(AthanConstants.PRIVATE_PROPERTY);

		} catch (Exception exc) {
			log.log(Level.SEVERE, "Properties absent !", exc);
		}

		return null;
	}
}
