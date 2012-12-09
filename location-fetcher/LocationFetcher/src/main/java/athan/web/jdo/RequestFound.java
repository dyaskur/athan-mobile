/**
 * 
 */
package athan.web.jdo;

import athan.web.Location;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import java.util.Calendar;
import java.util.Date;

/**
 * Class used to store in datastore the parameters of a found location
 * 
 * @author BENBOUZID
 */
@PersistenceCapable
public class RequestFound extends Location {

	@Persistent
	private Date creationDate;

	/**
	 * Constructor using a {@link Location}
	 * 
	 * @param loc
	 */
	public RequestFound(Location loc) {
		this.setCityName(loc.getCityName());
		this.setRegionName(loc.getRegionName());
		this.setCountryName(loc.getCountryName());
		this.setCoordinates(loc.getCoordinates());
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR, Location.getJdoHourGap(cal.getTime()));
		this.setCreationDate(cal.getTime());
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

}
