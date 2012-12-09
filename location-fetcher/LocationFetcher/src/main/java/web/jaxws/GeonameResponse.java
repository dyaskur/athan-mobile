package athan.web.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "geonameResponse", namespace = "http://web.athan/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "geonameResponse", namespace = "http://web.athan/")
public class GeonameResponse {

	@XmlElement(name = "return", namespace = "")
	private athan.web.Location _return;

	/**
	 * 
	 * @return
	 *         returns Location
	 */
	public athan.web.Location getReturn() {
		return this._return;
	}

	/**
	 * 
	 * @param _return
	 *            the value for the _return property
	 */
	public void setReturn(athan.web.Location _return) {
		this._return = _return;
	}

}
