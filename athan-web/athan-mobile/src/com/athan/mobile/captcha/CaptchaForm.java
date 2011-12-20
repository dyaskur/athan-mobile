/**
 * 
 */
package com.athan.mobile.captcha;

import java.io.Serializable;

/**
 * Class containing the fields exchanged between client and server side
 * 
 * @author Saad BENBOUZID
 */
public class CaptchaForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String firstname;
	private String location;
	private String mobile;
	private String message;
	private String email;
	private String challengeField;
	private String responseField;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getChallengeField() {
		return challengeField;
	}

	public void setChallengeField(String challengeField) {
		this.challengeField = challengeField;
	}

	public String getResponseField() {
		return responseField;
	}

	public void setResponseField(String responseField) {
		this.responseField = responseField;
	}

}
