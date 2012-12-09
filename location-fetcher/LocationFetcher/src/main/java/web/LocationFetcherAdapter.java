/**
 * 
 */
package athan.web;

import athan.web.jaxws.Geoname;
import athan.web.jaxws.GeonameResponse;

import java.util.logging.Logger;

/**
 * @author Saad BENBOUZID
 */
public class LocationFetcherAdapter {
	private LocationFetcher locFetcher = new LocationFetcher();

	private static final Logger log = Logger.getLogger(LocationFetcherServlet.class.getName());

	public GeonameResponse geoname(Geoname request) throws LocationException {

		log.info("Parameters :\n" + "City name : [" + request.getCityName() + "]\n" + "Region name : ["
				+ request.getRegionName() + "]\n" + "Country name : [" + request.getCountryName() + "]\n"
				+ "Language : [" + request.getLanguage() + "]\n");

		Location responseGeoname = locFetcher.geoname(request.getCityName(), request.getRegionName(),
				request.getCountryName(), request.getLanguage());
		GeonameResponse response = new GeonameResponse();
		response.setReturn(responseGeoname);

		return response;
	}
}
