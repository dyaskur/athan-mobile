package athan.web.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.1 in JDK 6
 * Generated source version: 2.1.1
 * 
 */
@XmlRootElement(name = "LocationException", namespace = "http://web.athan/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LocationException", namespace = "http://web.athan/")
public class LocationExceptionBean {

	private String message;

	/**
	 * 
	 * @return
	 *         returns String
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * 
	 * @param message
	 *            the value for the message property
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
