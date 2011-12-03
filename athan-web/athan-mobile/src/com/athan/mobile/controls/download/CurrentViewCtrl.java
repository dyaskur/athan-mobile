/**
 * 
 */
package com.athan.mobile.controls.download;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;

/**
 * Current Controler
 * 
 * @author Saad BENBOUZID
 */
public class CurrentViewCtrl extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger(CurrentViewCtrl.class.getName());
	
	private Label lblJadSize;
	private Label lblJarSize;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		// Fetches size of the files
		String jadSize = Labels.getLabel("current.fileSize.unknown");
		String jarSize = Labels.getLabel("current.fileSize.unknown");
		String unit = " " + Labels.getLabel("current.File.sizeUnit");
		try {
			File jadFile = new File(desktop.getWebApp().getResource("/jar/nogps/Athan.jad")
				.getFile());
			File jarFile = new File(desktop.getWebApp().getResource("/jar/nogps/Athan.jar")
					.getFile());
			jadSize = Long.toString(jadFile.length());
			jarSize = Long.toString(jarFile.length());
		} catch(Exception exc) {
			unit = StringUtils.EMPTY;
			exc.printStackTrace();
			LOG.log(Level.SEVERE, "Erreur à la récupération de la taille des fichies", exc);
		} finally {
			lblJadSize.setValue(jadSize + unit);
			lblJarSize.setValue(jarSize + unit);
		}
	}

	public void onClick$btnJadFile() {
		try {
			Filedownload.save("/jar/nogps/Athan.jad",
					"text/vnd.sun.j2me.app-descriptor");
		} catch (FileNotFoundException exc) {
			Clients.alert(Labels.getLabel("current.fileDownload.error"),
					StringUtils.EMPTY, Messagebox.ERROR);
			LOG.log(Level.SEVERE, "Erreur au téléchargement du fichier JAD", exc);
		} catch (Exception exc) {
			exc.printStackTrace();
			LOG.log(Level.SEVERE, "Erreur au téléchargement du fichier JAD", exc);
		}
	}

	public void onClick$btnJarFile() {
		try {
			Filedownload.save("/jar/nogps/Athan.jar",
					"application/java-archive");
		} catch (FileNotFoundException exc) {
			Clients.alert(Labels.getLabel("current.fileDownload.error"),
					StringUtils.EMPTY, Messagebox.ERROR);
			LOG.log(Level.SEVERE, "Erreur au téléchargement du fichier JAR", exc);
		} catch (Exception exc) {
			exc.printStackTrace();
			LOG.log(Level.SEVERE, "Erreur au téléchargement du fichier JAR", exc);
		}
	}

}
