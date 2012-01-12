/**
 * 
 */
package athan.web;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import athan.web.jdo.PMF;
import athan.web.jdo.RequestFound;
import athan.web.jdo.RequestNotFound;

/**
 * @author Saad BENBOUZID
 */
public class HttpPortal {

	private static final Logger log = Logger.getLogger(HttpPortal.class
			.getName());

	private static final String INFO_LOC = "INFOLOC";
	private static final String INFO_LANG = "INFOLANG";

	private static final String GEONAME_URL = "http://ws.geonames.org/search?q="
			+ INFO_LOC + "&maxRows=1&lang=" + INFO_LANG + "&style=full";

	/**
	 * Sends an HTTP GET request to a url
	 * 
	 * @param endpoint
	 *            - The URL of the server. (Example:
	 *            " http://www.yahoo.com/search")
	 * @param requestParameters
	 *            - all the request parameters (Example:
	 *            "param1=val1&param2=val2"). Note: This method will add the
	 *            question mark (?) to the request - DO NOT add it yourself
	 * @param requestNotFound
	 *            parameters originally sent by the caller to the WS
	 * 
	 * @return - The response from the end point
	 */
	public static Location sendGetRequest(String pInfoLoc, String pLang,
			RequestNotFound requestNotFound) throws LocationException {
		String endpoint = GEONAME_URL;

		endpoint = endpoint.replaceAll(INFO_LOC, pInfoLoc);
		endpoint = endpoint.replaceAll(INFO_LANG, pLang);

		InputStreamReader ins;

		Location resLoc = null;

		// Send a GET request to the servlet
		try {
			// Send data
			String urlStr = endpoint;

			log.info("External Geonames Http request : " + endpoint);

			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();
			HttpURLConnection connection = (HttpURLConnection) conn;
			connection.setDoOutput(false);
			connection.setDoInput(true);
			connection.setRequestMethod("GET");
			InputStream in = connection.getInputStream();
			ins = new InputStreamReader(in, "UTF-8");

			resLoc = getLocationValues(ins);

			in.close();
			connection.disconnect();

			writeObject(new RequestFound(resLoc));

		} catch (LocationException exc) {

			// Stores the request parameters originating from the exception
			// occured
			writeObject(requestNotFound);

			throw exc;
		} catch (Exception exc) {
			throw new LocationException(
					"An exception occured server side. Please contact administrator : saad.benbouzid@insalien.org");
		}

		return resLoc;
	}

	/**
	 * Writes object in datastore using GAE built-in JDO
	 * 
	 * @param resLoc
	 */
	private static void writeObject(Object obj) {
		// Writes object in datastore using GAE built-in JDO
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(obj);
		} finally {
			pm.close();
		}
	}

	private static Location getLocationValues(InputStreamReader pXmlContent)
			throws LocationException, Exception {
		Location loc = new Location();
		loc.setCoordinates(new Location.Coordinate());

		try {
			InputSource is = new InputSource(pXmlContent);
			is.setEncoding("UTF-8");

			DocumentBuilderFactory fabrique = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder constructeur = fabrique.newDocumentBuilder();
			Document document = constructeur.parse(is);

			Element root = document.getDocumentElement();

			// Get the first <geoname> node
			NodeList nlGeoName = root.getElementsByTagName("geoname");
			if (nlGeoName != null && nlGeoName.item(0) != null) {
				root = (Element) nlGeoName.item(0);
			} else {
				// No results found
				throw new LocationException(
						"NO LOCATION FOUND ! Please check the spelling. Hit also Help menu. Leave some fields BLANK if needed and make sure your inputs are correct.");
			}

			NodeList nlCityName = root.getElementsByTagName("name");
			if (nlCityName != null && nlCityName.item(0) != null) {
				loc.setCityName(nlCityName.item(0).getFirstChild()
						.getNodeValue());
			}
			NodeList nlRegionName = root.getElementsByTagName("adminName1");
			if (nlRegionName != null && nlRegionName.item(0) != null
					&& nlRegionName.item(0).getFirstChild() != null) {
				loc.setRegionName(nlRegionName.item(0).getFirstChild()
						.getNodeValue());
			}
			NodeList nlCountryName = root.getElementsByTagName("countryName");
			if (nlCountryName != null && nlCountryName.item(0) != null
					&& nlCountryName.item(0).getFirstChild() != null) {
				loc.setCountryName(nlCountryName.item(0).getFirstChild()
						.getNodeValue());
			}
			NodeList nlLatitude = root.getElementsByTagName("lat");
			if (nlLatitude != null && nlLatitude.item(0) != null) {
				String s_lat = nlLatitude.item(0).getFirstChild()
						.getNodeValue();
				if (s_lat != null) {
					loc.getCoordinates().setLat(Double.parseDouble(s_lat));
				}
			}
			NodeList nlLongitude = root.getElementsByTagName("lng");
			if (nlLongitude != null && nlLongitude.item(0) != null) {
				String s_lon = nlLongitude.item(0).getFirstChild()
						.getNodeValue();
				if (s_lon != null) {
					loc.getCoordinates().setLng(Double.parseDouble(s_lon));
				}
			}

			// log.info("Location HTTP RAW Response : " + xmlContent);
			log.info("Location HTTP PROCESSED Response : " + loc.toString());

		} catch (LocationException exc) {
			throw exc;
		}

		return loc;
	}

	/* remove leading whitespace */
	public static String ltrim(String source) {
		return source.replaceAll("^\\s+", "");
	}

	/* remove trailing whitespace */
	public static String rtrim(String source) {
		return source.replaceAll("\\s+$", "");
	}

	/* replace multiple whitespaces between words with single blank */
	public static String itrim(String source) {
		return source.replaceAll("\\b\\s{1,}\\b", "%20");
	}

	/* remove all superfluous whitespaces in source string */
	public static String trim(String source) {
		return itrim(ltrim(rtrim(source)));
	}

	public static String lrtrim(String source) {
		return ltrim(rtrim(source));
	}

	public static String lritrim(String source) {
		return ltrim(rtrim(itrim(source)));
	}
}
