/**
 * 
 */
package athan.web;

import athan.web.jdo.RequestNotFound;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * @author Saad BENBOUZID
 */
@WebService
public class LocationFetcher {

	@WebMethod
	public Location geoname(String pCityName, String pRegionName, String pCountryName, String pLanguage)
			throws LocationException {

		String locParameters = "";
		if (pCityName != null && HttpPortal.lrtrim(pCityName).length() > 0) {
			locParameters += HttpPortal.lritrim(pCityName);
		}
		if (pRegionName != null && HttpPortal.lrtrim(pRegionName).length() > 0) {
			locParameters += "+" + HttpPortal.lritrim(pRegionName);
		}
		if (pCountryName != null && HttpPortal.lrtrim(pCountryName).length() > 0) {
			locParameters += "+" + HttpPortal.lritrim(pCountryName);
		}

		String langParameter = "en";
		if (pLanguage != null && HttpPortal.lrtrim(pLanguage).length() == 2) {
			langParameter = pLanguage;
		}

		// Stores the original parameters
		RequestNotFound requestNotFound = new RequestNotFound(pCityName, pRegionName, pCountryName, pLanguage);

		return HttpPortal.sendGetRequest(locParameters, langParameter, requestNotFound);

	}
}
