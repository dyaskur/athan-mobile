/**
 * 
 */
package com.athan.mobile.captcha;

/**
 * Captcha exception
 * 
 * @author Saad BENBOUZID
 */
public class CaptchaException extends Exception {

	private static final long serialVersionUID = 1L;

	private String message;

	public CaptchaException() {
		super();
	}

	public CaptchaException(String message) {
		super(message);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
