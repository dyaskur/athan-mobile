/**
 * 
 */
package athan.web.jdo;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import athan.web.Location;

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
		this.setCreationDate(new Date());
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

}
