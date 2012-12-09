package athan.web.jdo;

import athan.web.Location;
import com.google.appengine.api.datastore.Key;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Calendar;
import java.util.Date;

/**
 * Class used to store in datastore the parameters of a not found location
 * 
 * @author BENBOUZID
 */
@PersistenceCapable
public class RequestNotFound {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private String cityName;

	@Persistent
	private String regionName;

	@Persistent
	private String countryName;

	@Persistent
	private String language;

	@Persistent
	private Date creationDate;

	public RequestNotFound(String cityName, String regionName, String countryName, String language) {
		this.cityName = cityName;
		this.regionName = regionName;
		this.countryName = countryName;
		this.language = language;
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR, Location.getJdoHourGap(cal.getTime()));
		this.setCreationDate(cal.getTime());
	}

	public Key getKey() {
		return key;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

}
