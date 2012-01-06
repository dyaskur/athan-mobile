package athan.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

@SuppressWarnings("serial")
public class LocationFetcherServlet extends HttpServlet {

	// public void doGet(HttpServletRequest req, HttpServletResponse resp)
	// throws IOException {
	// resp.setContentType("text/plain");
	// resp.getWriter().println("Hello, world");
	// }

	private static final Logger log = Logger
			.getLogger(LocationFetcherServlet.class.getName());

	static final String XML_HEADER = "text/xml;charset=\"UTF-8\"";
	static MessageFactory sMessageFactory;
	static LocationFetcherSOAPHandler sSoapHandler;

	static {
		try {
			sMessageFactory = MessageFactory.newInstance();
			sSoapHandler = new LocationFetcherSOAPHandler();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {

			log.info("POST Request");

			// Get all the headers from the HTTP request
			MimeHeaders headers = getHeaders(req);

			// Construct a SOAPMessage from the XML in the request body
			InputStream is = req.getInputStream();
			SOAPMessage soapRequest = sMessageFactory
					.createMessage(headers, is);

			// Handle soapReqest
			SOAPMessage soapResponse = sSoapHandler
					.handleSOAPRequest(soapRequest);

			// Write to HttpServeltResponse
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType(XML_HEADER);
			OutputStream os = resp.getOutputStream();
			soapResponse.writeTo(os);
			os.flush();
		} catch (SOAPException e) {
			throw new IOException("Exception while creating SOAP message.", e);
		}
	}

	static MimeHeaders getHeaders(HttpServletRequest req) {
		Enumeration headerNames = req.getHeaderNames();
		MimeHeaders headers = new MimeHeaders();
		while (headerNames.hasMoreElements()) {
			String headerName = (String) headerNames.nextElement();
			String headerValue = req.getHeader(headerName);
			StringTokenizer values = new StringTokenizer(headerValue, ",");
			while (values.hasMoreTokens()) {
				headers.addHeader(headerName, values.nextToken().trim());
			}
		}
		return headers;
	}
}
