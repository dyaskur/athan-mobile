package athan.web;

import athan.web.jaxws.Geoname;
import athan.web.jaxws.LocationExceptionBean;

import javax.xml.bind.JAXB;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SAAJResult;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.dom.DOMSource;
import java.util.Iterator;

/**
 * @author Saad BENBOUZID
 */
public class LocationFetcherSOAPHandler {

	private static final String NAMESPACE_URI = "http://web.athan/";
	private static final QName GEONAME_QNAME = new QName(NAMESPACE_URI, "geoname");

	private MessageFactory messageFactory;
	private LocationFetcherAdapter locFetcherAdapter;

	public LocationFetcherSOAPHandler() throws SOAPException {
		messageFactory = MessageFactory.newInstance();
		locFetcherAdapter = new LocationFetcherAdapter();
	}

	public SOAPMessage handleSOAPRequest(SOAPMessage request) throws SOAPException {
		SOAPBody soapBody = request.getSOAPBody();
		Iterator iterator = soapBody.getChildElements();
		Object responsePojo = null;
		while (iterator.hasNext()) {
			Object next = iterator.next();
			if (next instanceof SOAPElement) {
				SOAPElement soapElement = (SOAPElement) next;
				QName qname = soapElement.getElementQName();
				if (GEONAME_QNAME.equals(qname)) {
					responsePojo = handleGeonameRequest(soapElement);
					break;
				}
			}
		}
		SOAPMessage soapResponse = messageFactory.createMessage();
		soapBody = soapResponse.getSOAPBody();
		if (responsePojo != null) {
			JAXB.marshal(responsePojo, new SAAJResult(soapBody));
		} else {
			SOAPFault fault = soapBody.addFault();
			fault.setFaultString("Unrecognized SOAP request.");
		}
		return soapResponse;
	}

	private Object handleGeonameRequest(SOAPElement soapElement) {
		Geoname geonameRequest = JAXB.unmarshal(new DOMSource(soapElement), Geoname.class);
		try {
			return locFetcherAdapter.geoname(geonameRequest);
		} catch (LocationException exc) {
			LocationExceptionBean response = new LocationExceptionBean();
			response.setMessage(exc.getMsg());
			return response;
		}
	}
}
