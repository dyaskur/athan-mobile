/**
 * 
 */
package com.athan.mobile.utils;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Messagebox;

/**
 * Messages utils
 * 
 * @author Saad BENBOUZID
 */
public class Messages {

	public static void error(String alertMsg) {
		try {
			Messagebox.show(alertMsg, Labels.getLabel("application.title"),
					Messagebox.OK, Messagebox.ERROR);
		} catch (Exception exc) {
			//
		}
	}

	private Messages() {
	}
}
