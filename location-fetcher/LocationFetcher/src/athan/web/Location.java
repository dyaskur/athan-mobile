/**
 * 
 */
package athan.web;

/**
 * @author Saad BENBOUZID
 */
public class Location {

	private String mCityName;
	private String mRegionName;
	private String mCountryName;
	private Coordinate mCoordinates;

	public Location() {
	}

	public Location(String pCityName, String pRegionName, String pCountryName,
			Coordinate pCoordinates) {
		mCityName = pCityName;
		mRegionName = pRegionName;
		mCountryName = pCountryName;
		mCoordinates = pCoordinates;
	}

	public String toString() {
		String retour = "\n";

		retour += "CityName=[" + mCityName + "]\n";
		retour += "RegionName=[" + mRegionName + "]\n";
		retour += "CountryName=[" + mCountryName + "]\n";
		retour += "Coordinates=[" + mCoordinates.toString() + "]";

		return retour;
	}

	public String getCityName() {
		return mCityName;
	}

	public void setCityName(String pCityName) {
		mCityName = pCityName;
	}

	public String getRegionName() {
		return mRegionName;
	}

	public void setRegionName(String pRegionName) {
		mRegionName = pRegionName;
	}

	public String getCountryName() {
		return mCountryName;
	}

	public void setCountryName(String pCountryName) {
		mCountryName = pCountryName;
	}

	public Coordinate getCoordinates() {
		return mCoordinates;
	}

	public void setCoordinates(Coordinate pCoordinates) {
		mCoordinates = pCoordinates;
	}

}
