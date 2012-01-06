/**
 * 
 */
package athan.web;

import java.util.logging.Logger;

/**
 * @author Saad BENBOUZID
 */
public class LocationException extends Exception {

	private static final long serialVersionUID = 7046087855487936734L;
	private static final Logger log = Logger.getLogger(LocationException.class
			.getName());

	private String mMsg;

	public LocationException(String pMsg) {
		mMsg = pMsg;
		log.info("A custom exception occured :\n" + pMsg);
	}

	public String getMsg() {
		return mMsg;
	}
}
