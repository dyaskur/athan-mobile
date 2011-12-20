package com.athan.mobile.captcha;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class PostFormDataServlet
 */
public class PostFormDataServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

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

		response.setContentType("text/plain");
		String strResponse = "";
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
			Gson gson = new Gson();
			CaptchaForm captchaForm = (CaptchaForm) gson.fromJson(
					sb.toString(), CaptchaForm.class);

			if ((captchaForm == null)
					|| (captchaForm.getChallengeField() == null)
					|| (captchaForm.getResponseField() == null)) {
				throw new Exception(
						"Your words did not match. Please try submitting again.");
			}

			String remoteAddr = request.getRemoteAddr();
			ReCaptchaImpl reCaptcha = new ReCaptchaImpl();

			reCaptcha.setPrivateKey(getPrivateKey());

			ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(
					remoteAddr, captchaForm.getChallengeField(),
					captchaForm.getResponseField());

			if (!reCaptchaResponse.isValid()) {
				// RECAPTCHA VALIDATION FAILED
				throw new Exception(
						"Your words did not match. Please try submitting again.");
			}

			strResponse = "Your record has been accepted and you did a good job entering the two words. Thank you";
		} catch (Exception ex) {
			strResponse = "You record was not accepted. Reason : "
					+ ex.getMessage();
		}
		response.getWriter().println(strResponse);
	}

	/**
	 * Returns captcha private key
	 * 
	 * @return
	 */
	private String getPrivateKey() {

		try {
			InputStream is = getClass().getResourceAsStream(
					"/captcha/local.properties");

			Properties prop = new Properties();
			prop.load(is);

			return prop.getProperty("private");

		} catch (Exception exc) {
			log.log(Level.SEVERE, "Properties absent !", exc);
		}

		return null;
	}
}
