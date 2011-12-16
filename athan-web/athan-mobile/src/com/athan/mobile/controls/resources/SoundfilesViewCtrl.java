/**
 * 
 */
package com.athan.mobile.controls.resources;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Html;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Vbox;

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
	private static final String CLASS_IMAGE_WAV = "tlb_wav";

	private static final String MIME_TYPE_MP3 = "audio/mpeg";
	private static final String MIME_TYPE_WAV = "audio/x-wav";

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
			sb.append("	<param name=\"flashvars\" value=\"mp3="
					+ enumSong.mp3Url() + "\" />");
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
					fileDownlad(enumSong.mp3Url(), MIME_TYPE_MP3);
				}
			});

			/* WAV button */
			Image imgWav = new Image();
			imgWav.setSclass(CLASS_IMAGE_WAV);
			imgWav.setParent(mainHbox);
			imgWav.addEventListener(Events.ON_CLICK, new EventListener() {

				@Override
				public void onEvent(Event event) throws Exception {
					fileDownlad(enumSong.wavUrl(), MIME_TYPE_WAV);
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
	 * File downloading handler
	 * 
	 * @param uri
	 * @param mime
	 */
	private void fileDownlad(String uri, String mime) {
		try {
			Filedownload.save(uri, mime);
		} catch (FileNotFoundException exc) {
			Messages.error(Labels.getLabel("sound.download.error"));
			LOG.log(Level.SEVERE, "Erreur au téléchargement du fichier son",
					exc);
		} catch (Exception exc) {
			exc.printStackTrace();
			LOG.log(Level.SEVERE, "Erreur au téléchargement du fichier son",
					exc);
		}
	}
}
