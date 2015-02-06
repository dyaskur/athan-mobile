/**
 * 
 */
package com.athan.mobile.constants;

/**
 * Constants
 * 
 * @author Saad BENBOUZID
 */
public class AthanConstants {

	public static final String APPLICATION_PROPERTIES_FILE = "/gae.properties";
	public static final String PRIVATE_PROPERTY = "private";
	public static final String PUBLIC_PROPERTY = "public";

	public static final String EMAIL_ADDRESS_FROM = "makavelikal@gmail.com";
	public static final String EMAIL_ADDRESS_TO = "makavelikal@gmail.com";
	public static final String EMAIL_SUBJECT = "Athan Mobile - Feedback";

	public static final String EMAIL_BODY = "From : %s %s\n\nIP Address : %s\n\nLocation : %s\n\nMobile : %s\n\nEmail : %s\n\nMessage : %s\n";

	public static final String MIME_TYPE_JAD = "text/vnd.sun.j2me.app-descriptor";
	public static final String MIME_TYPE_JAR = "application/java-archive";
	public static final String MIME_TYPE_MP3 = "audio/mpeg";

	public static final String CURRENT_ATHAN_JAD = "/jar/current/Athan.jad";
	public static final String CURRENT_ATHAN_JAR = "/jar/current/Athan.jar";

	public static final String MP3_PATH = "/sound/mp3/";

	public static final String DOWNLOAD_JAD = "JAD";
	public static final String DOWNLOAD_JAR = "JAR";
	public static final String DOWNLOAD_MP3 = "MP3";
	public static final String DOWNLOAD_SERVLET = "/download";
	public static final String DOWNLOAD_FILETYPE_PARAM = "type";
	public static final String DOWNLOAD_FILE_PARAM = "file";

	public static final String GA_JAR_DOWNLOAD = "_gaq.push(['_trackEvent', 'Files', 'Downloaded', 'Athan.jar']);";
	public static final String GA_JAD_DOWNLOAD = "_gaq.push(['_trackEvent', 'Files', 'Downloaded', 'Athan.jad']);";
	public static final String GA_MP3_DOWNLOAD = "_gaq.push(['_trackEvent', 'Files', 'Downloaded', '%s']);";

	private AthanConstants() {
	}

}
