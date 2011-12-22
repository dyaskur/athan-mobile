/**
 * 
 */
package com.athan.mobile.controls.resources;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Html;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Vbox;

import com.athan.mobile.constants.AthanConstants;
import com.athan.mobile.controls.download.CurrentViewCtrl;
import com.athan.mobile.enums.EnumSong;
import com.athan.mobile.utils.Messages;

/**
 * Soundfiles Controller
 * 
 * @author Saad BENBOUZID
 */
public class SoundfilesViewCtrl extends GenericForwardComposer {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(CurrentViewCtrl.class
			.getName());

	private static final String SEPARATOR_SPACING = "5px";
	private static final String SONG_NAME_WIDTH = "120px";
	private static final String ZCLASS_SONG_NAME = "song_name";
	private static final String ZCLASS_SONG_NAME_CMPL = "song_name_cmpl";
	private static final String CLASS_IMAGE_MP3 = "tlb_mp3";

	private Vbox vbxMain;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		displayPlayers();
	}

	/**
	 * Displays players for each song
	 */
	private void displayPlayers() {
		for (final EnumSong enumSong : EnumSong.values()) {
			Hbox mainHbox = new Hbox();
			mainHbox.setStyle("position: relative; top: 110px;");

			/* Song name */
			Hbox labelHbox = new Hbox();
			labelHbox.setPack("left");
			labelHbox.setWidth(SONG_NAME_WIDTH);
			Label lblSong = new Label();
			lblSong.setValue(Labels.getLabel(enumSong.keyLabel()));
			lblSong.setZclass(ZCLASS_SONG_NAME);
			lblSong.setParent(labelHbox);
			if (!StringUtils.isEmpty(enumSong.labelCmpl())) {
				Label lblSongCmpl = new Label();
				lblSongCmpl.setValue(enumSong.labelCmpl());
				lblSongCmpl.setZclass(ZCLASS_SONG_NAME_CMPL);
				lblSongCmpl.setParent(labelHbox);
			}
			labelHbox.setParent(mainHbox);

			/* Song player */
			Html htmlPlayer = new Html();
			StringBuffer sb = new StringBuffer();
			sb.append("<object type=\"application/x-shockwave-flash\"");
			sb.append("	data=\"/swf/dewplayer.swf\" width=\"200\" height=\"20\"");
			sb.append("	name=\"dewplayer\">");
			sb.append("	<param name=\"wmode\" value=\"transparent\" />");
			sb.append("	<param name=\"movie\" value=\"/swf/dewplayer.swf\" />");
			sb.append("	<param name=\"flashvars\" value=\"mp3=" + "file://"
					+ AthanConstants.MP3_PATH + enumSong.mp3File() + "\" />");
			sb.append("</object>");
			htmlPlayer.setContent(sb.toString());
			htmlPlayer.setParent(mainHbox);

			/* MP3 button */
			Image imgMp3 = new Image();
			imgMp3.setSclass(CLASS_IMAGE_MP3);
			imgMp3.setParent(mainHbox);
			imgMp3.addEventListener(Events.ON_CLICK, new EventListener() {

				@Override
				public void onEvent(Event event) throws Exception {
					fileDownlad(enumSong.mp3File());
				}
			});

			/* Attaches line to container */
			mainHbox.setParent(vbxMain);

			/* Attaches a separator */
			Separator sep = new Separator();
			sep.setSpacing(SEPARATOR_SPACING);
			sep.setParent(vbxMain);
		}
	}

	/**
	 * Returns file url on classpath
	 * 
	 * @return
	 */
	private String getMp3FileUrl(String fileName) {

		try {
			String url = getClass().getResource(
					AthanConstants.MP3_PATH + fileName).getFile();
			return url;
		} catch (Exception exc) {
			LOG.log(Level.SEVERE, "Problème à la récupération du mp3 "
					+ fileName + " pour player", exc);
			return StringUtils.EMPTY;
		}
	}

	/**
	 * File downloading handler. <br>
	 * Can't handle wav files, as GAE doesn't allow to upload big files.
	 * 
	 * @param fileName
	 */
	private void fileDownlad(String fileName) {
		try {
			Executions.getCurrent().sendRedirect(
					AthanConstants.DOWNLOAD_SERVLET + "?"
							+ AthanConstants.DOWNLOAD_FILETYPE_PARAM + "="
							+ AthanConstants.DOWNLOAD_MP3 + "&"
							+ AthanConstants.DOWNLOAD_FILE_PARAM + "="
							+ fileName, null);
			LOG.log(Level.FINE, "Téléchargement du fichier JAD");

		} catch (Exception exc) {
			Messages.error(Labels.getLabel("soundfiles.download.error"));
			LOG.log(Level.SEVERE, "Erreur au téléchargement du fichier son",
					exc);
		}
	}
}
