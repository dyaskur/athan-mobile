/**
 * 
 */
package com.athan.mobile.captcha;

import java.io.Serializable;

/**
 * Response sent from server to client
 * 
 * @author Saad BENBOUZID
 */
public class CaptchaResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	public String message;
	public boolean checkOK;

	public CaptchaResponse() {
	}

	public CaptchaResponse(String message, boolean checkOK) {
		super();
		this.message = message;
		this.checkOK = checkOK;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isCheckOK() {
		return checkOK;
	}

	public void setCheckOK(boolean checkOK) {
		this.checkOK = checkOK;
	}

}
