package athan.web.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "geoname", namespace = "http://web.athan/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "geoname", namespace = "http://web.athan/", propOrder = { "cityName", "countryName", "regionName",
		"language" })
public class Geoname {

	@XmlElement(name = "cityName", namespace = "")
	private String cityName;
	@XmlElement(name = "countryName", namespace = "")
	private String countryName;
	@XmlElement(name = "regionName", namespace = "")
	private String regionName;
	@XmlElement(name = "language", namespace = "")
	private String language;

	/**
	 * 
	 * @return
	 *         returns String
	 */
	public String getCityName() {
		return this.cityName;
	}

	/**
	 * 
	 * @param cityName
	 *            the value for the cityName property
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	/**
	 * 
	 * @return
	 *         returns String
	 */
	public String getCountryName() {
		return this.countryName;
	}

	/**
	 * 
	 * @param countryName
	 *            the value for the countryName property
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	/**
	 * 
	 * @return
	 *         returns String
	 */
	public String getRegionName() {
		return this.regionName;
	}

	/**
	 * 
	 * @param regionName
	 *            the value for the regionName property
	 */
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	/**
	 * 
	 * @return
	 *         returns String
	 */
	public String getLanguage() {
		return this.language;
	}

	/**
	 * 
	 * @param language
	 *            the value for the language property
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

}
