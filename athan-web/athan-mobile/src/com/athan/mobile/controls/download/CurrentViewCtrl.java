/**
 * 
 */
package com.athan.mobile.controls.download;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;

import com.athan.mobile.utils.Messages;

/**
 * Current Controller
 * 
 * @author Saad BENBOUZID
 */
public class CurrentViewCtrl extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	public static final String MIME_TYPE_JAD = "text/vnd.sun.j2me.app-descriptor";

	public static final String MIME_TYPE_JAR = "application/java-archive";

	public static final String CURRENT_ATHAN_JAD = "/jar/current/Athan.jad";

	public static final String CURRENT_ATHAN_JAR = "/jar/current/Athan.jar";

	private static final String MIDLET_VERSION = "MIDlet-Version";

	private static final Logger LOG = Logger.getLogger(CurrentViewCtrl.class
			.getName());

	private Label lblCurrentVersion;
	private Label lblCurrentDate;

	private Label lblJadSize;
	private Label lblJarSize;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		displayFileProperties();

		displayVersionProperties();
	}

	/**
	 * Display version properties (version number, modification date,...)
	 */
	private void displayVersionProperties() {
		String versionNumber = StringUtils.EMPTY;
		String versionDate = StringUtils.EMPTY;
		try {
			// Gets version number
			File jadFile = new File(desktop.getWebApp()
					.getResource(CURRENT_ATHAN_JAD).getFile());

			versionNumber = getVersionNumber(jadFile);

			// Gets version date
			File jarFile = new File(desktop.getWebApp()
					.getResource(CURRENT_ATHAN_JAR).getFile());

			String format = Labels.getLabel("current.releaseDate.format");
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			versionDate = sdf.format(new Date(jarFile.lastModified()));

		} catch (Exception exc) {
			exc.printStackTrace();
			LOG.log(Level.SEVERE,
					"Erreur à la récupération des infos sur la version", exc);
		} finally {
			lblCurrentVersion.setValue(versionNumber);
			lblCurrentDate.setValue(versionDate);
		}
	}

	/**
	 * Returne version number from JAD file
	 * 
	 * @param jarFile
	 * @return version number if found, {@link StringUtils#EMTPY} otherwise
	 */
	private String getVersionNumber(File jarFile) {

		String ret = null;

		Scanner scanner = null;

		try {
			// Note that FileReader is used, not File, since File is not
			// Closeable
			scanner = new Scanner(new FileReader(jarFile));

			// first use a Scanner to get each line
			while (scanner.hasNextLine() && ret == null) {
				ret = processLine(scanner.nextLine());
			}
		} catch (FileNotFoundException fex) {
			fex.printStackTrace();
		} finally {
			// ensure the underlying stream is always closed
			// this only has any effect if the item passed to the Scanner
			// constructor implements Closeable (which it does in this case).
			scanner.close();
		}

		return ret == null ? StringUtils.EMPTY : ret;
	}

	/**
	 * Overridable method for processing lines in different ways.
	 * 
	 * <P>
	 * This simple default implementation expects simple name-value pairs,
	 * separated by an ':' sign. Examples of valid input :
	 * <tt>height : 167cm</tt> <tt>mass =  65kg</tt>
	 * <tt>disposition : "grumpy"</tt>
	 * <tt>this is the name : this is the value</tt>
	 */
	private String processLine(String aLine) {
		// use a second Scanner to parse the content of each line
		Scanner scanner = new Scanner(aLine);
		scanner.useDelimiter(":");
		if (scanner.hasNext()) {
			String name = scanner.next();
			String value = scanner.next();

			if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(value)) {
				name = StringUtils.trim(name);
				value = StringUtils.trim(value);

				if (StringUtils.equalsIgnoreCase(name, MIDLET_VERSION)) {
					return value;
				}
			}
		}

		// no need to call scanner.close(), since the source is a String
		return null;
	}

	/**
	 * Display file properties (file sizes,...)
	 */
	private void displayFileProperties() {
		// Fetches size of the files
		String jadSize = Labels.getLabel("current.fileSize.unknown");
		String jarSize = Labels.getLabel("current.fileSize.unknown");
		String unit = " " + Labels.getLabel("current.File.sizeUnit");
		try {
			File jadFile = new File(desktop.getWebApp()
					.getResource(CURRENT_ATHAN_JAD).getFile());
			File jarFile = new File(desktop.getWebApp()
					.getResource(CURRENT_ATHAN_JAR).getFile());
			jadSize = Long.toString(jadFile.length());
			jarSize = Long.toString(jarFile.length());
		} catch (Exception exc) {
			unit = StringUtils.EMPTY;
			exc.printStackTrace();
			LOG.log(Level.SEVERE,
					"Erreur à la récupération de la taille des fichiers", exc);
		} finally {
			lblJadSize.setValue(jadSize + unit);
			lblJarSize.setValue(jarSize + unit);
		}
	}

	public void onClick$btnJadFile() {
		try {
			Filedownload.save(CURRENT_ATHAN_JAD, MIME_TYPE_JAD);
		} catch (FileNotFoundException exc) {
			Messages.error(Labels.getLabel("current.fileDownload.error"));
			LOG.log(Level.SEVERE, "Erreur au téléchargement du fichier JAD",
					exc);
		} catch (Exception exc) {
			exc.printStackTrace();
			LOG.log(Level.SEVERE, "Erreur au téléchargement du fichier JAD",
					exc);
		}
	}

	public void onClick$btnJarFile() {
		try {
			Filedownload.save(CURRENT_ATHAN_JAR, MIME_TYPE_JAR);
		} catch (FileNotFoundException exc) {
			Messages.error(Labels.getLabel("current.fileDownload.error"));
			LOG.log(Level.SEVERE, "Erreur au téléchargement du fichier JAR",
					exc);
		} catch (Exception exc) {
			exc.printStackTrace();
			LOG.log(Level.SEVERE, "Erreur au téléchargement du fichier JAR",
					exc);
		}
	}

}
